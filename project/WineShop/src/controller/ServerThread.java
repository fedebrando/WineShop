
package controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

import model.Account;
import model.DataAccess;
import model.Purchase;
import model.PurchaseType;
import model.User;
import model.Wine;

/**
 * Class ServerThread, which represents a wineshop's server child that handle requests
 * @author Federico Brandini, Federico Putamorsi
 * @version 14.02.2023
 */
public class ServerThread implements Runnable
{
	/** Main Server */
	private Server srv;
	
	/** Server Socket */
	private Socket sock;
	
	/** Input stream for server's socket */
	private ObjectInputStream is;
	
	/** Output stream for server's socket */
	private ObjectOutputStream os;
	
	/** Account type of logged User */
	private Account type;
	
	/** Username of logged User */
	private String username;
	
	/**
	 * Instantiates a server child
	 * @param srv Main Server
	 * @param sock Main Socket Server
	 * @throws IOException If an error occured
	 */
	public ServerThread(final Server srv, final Socket sock) throws IOException
	{
		this.srv = srv;
		this.sock = sock;
		os = new ObjectOutputStream(new BufferedOutputStream(this.sock.getOutputStream()));
		os.flush();
		is = new ObjectInputStream(new BufferedInputStream(this.sock.getInputStream()));
		type = null;
		this.username = null;
	}
	
	@Override
	public void run()
	{
		String received;
		Object oReceived;
		boolean goOn = true;
		Outcome outcome = Outcome.SUCCESS;
		
		// probable sign in
		oReceived = receiveObject();
		if (!(oReceived instanceof String))
			return;
		received = (String) oReceived;
		
		if (received.equals("signIn"))
		{
			sendObject(addUser().getStrOutcome());
			return;
		}
		else if (!received.equals("login"))
			return;
		
		// login
		while (login() != Outcome.SUCCESS)
		{
			sendObject(Account.FOREIGN);
		}
		sendObject(this.type);
		
		// client's requests
		do
		{	
			oReceived = receiveObject();
			if (!(oReceived instanceof String))
			{
				goOn = false;
				continue;
			}
			received = (String)oReceived;
			
			outcome =
			switch (received)
			{
				case "logout" ->
				{
					goOn = false;
					yield Outcome.SUCCESS;
				}
				case "signIn" -> addUser();
				case "dequePurchaseProposal" -> getPurchaseProposal();
				case "enqueuePurchaseProposal" -> insertPurchaseProposal();
				case "changePsw" -> changePassword();
				case "filterWines" -> filterWines();
				case "winesUnderThreshold" -> winesUnderThreshold();
				case "promotions" -> promotions();
				case "addToCart" -> addToCart();
				case "removeFromCart" -> removeFromCart();
				case "cart" -> cart();
				case "filterCustomers" -> filterCustomers();
				case "employees" -> employees();
				case "userIsPresent" -> userIsPresent();
				case "resetEmployee" -> resetEmployeeAccount();
				case "deleteEmployee" -> deleteEmployeeAccount();
				case "createPurchase" -> createPurchase();
				case "deletePurchaseProposal" -> deletePurchaseProposal();
				case "filterSalesOrder" -> filterPurchase(PurchaseType.SALES_ORDER);
				case "filterPurchaseOrder" -> filterPurchase(PurchaseType.PURCHASE_ORDER);
				case "filterPurchaseProposal" -> filterPurchase(PurchaseType.PURCHASE_PROPOSAL);
				case "checkSalesOrder" -> checkSalesOrder();
				case "checkPurchaseProposal" -> checkPurchaseProposal();
				case "acceptRefusePurchase" -> acceptRefusePurchase();
				case "buy" -> buy();
				case "report" -> report();
				default -> Outcome.ERROR;
			};
			
			switch (outcome)
			{
				case SUCCESS:
					sendObject(Outcome.SUCCESS.getStrOutcome());
					break;
				case ERROR:
					sendObject(Outcome.ERROR.getStrOutcome());
					break;
				case INSUFFICIENT_PRIVILEGES:
					sendObject(Outcome.INSUFFICIENT_PRIVILEGES.getStrOutcome() + ": you'll be disconnected.");
					goOn = false; // illegal access attempt => bye
					break;
			}
		} while (goOn);
		
		// streams closure
		System.out.println("Connection terminated for " + sock.getInetAddress() + ":" + sock.getPort());
		closeStreams();
	}
	
	/**
	 * Send an object to client
	 * @param oSend The object to send
	 * @return True if the sending was successful, false otherwise
	 */
	private boolean sendObject(Object oSend)
	{
		try
		{	
			os.writeObject(oSend);
			os.flush();
			os.reset();
		}
		catch (IOException e)
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Awaits object arrival
	 * @return Received object
	 */
	private Object receiveObject()
	{
		Object oReceive;
		try
		{				
			oReceive = is.readObject();
		}
		catch (IOException | ClassNotFoundException e)
		{
			oReceive = null;
		}
		return oReceive;
	}
	
	/**
	 * Close socket communication
	 * @return True if the operation was successful, false otherwise
	 */
	private boolean closeStreams()
	{
		try
		{
			os.close();
			is.close();	
		}
		catch (IOException e)
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Executes Login procedure
	 * @return The Outcome
	 */
	private Outcome login()
	{
		Object oReceived;
		
		String username, password, pswHash;
		
		oReceived = receiveObject();
		if (oReceived instanceof String)
			username = (String)oReceived;
		else
			return Outcome.ERROR;
			
		oReceived = receiveObject();
		if (oReceived instanceof String)
			password = (String)oReceived;
		else
			return Outcome.ERROR;
		
		pswHash = DataAccess.passwordHashOf(username);
		if (pswHash.equals(DataAccess.SHA3_512(password)))
			this.type = DataAccess.accountTypeOf(username);
		else
			return Outcome.ERROR;
		this.username = username;
		
		return Outcome.SUCCESS;
	}
	
	/**
	 * Executes the add User procedure
	 * @return The Outcome
	 */
	private Outcome addUser()
	{
		User u = (User) receiveObject();
		u.setPasswordHash(DataAccess.SHA3_512(u.getPasswordHash()));
		
		return DataAccess.insertUser(u) ? Outcome.SUCCESS : Outcome.ERROR;
	}
	
	/**
	 * Executes the change password procedure
	 * @return The Outcome
	 */
	private Outcome changePassword()
	{
		String username, pswHash, currPswHash;
		
		username = (String) receiveObject();
		currPswHash = DataAccess.SHA3_512((String) receiveObject());
		pswHash = DataAccess.SHA3_512((String) receiveObject());
		
		if (!DataAccess.authenticity(username, currPswHash))
			return Outcome.ERROR;
		
		return DataAccess.changePswHash(username, pswHash) ? Outcome.SUCCESS : Outcome.ERROR;
	}
	
	/**
	 * Executes the reset Employee procedure
	 * @return The Outcome
	 */
	private Outcome resetEmployeeAccount()
	{
		String username = (String) receiveObject();
		
		return DataAccess.changePswHash(username, DataAccess.SHA3_512("changeMe")) ? Outcome.SUCCESS : Outcome.ERROR;
	}
	
	/**
	 * Executes the filter wines procedure
	 * @return The Outcome
	 */
	private Outcome filterWines()
	{
		String param, name;
		int year;
		
		param = (String) receiveObject();
		
		ArrayList<Wine> wines =
		switch (param)
		{
			case "all" ->
			{
				yield DataAccess.allWines();
			}
			case "name" ->
			{
				name = (String) receiveObject();
				yield DataAccess.filterWines(name);
			}
			case "year" ->
			{
				year = (int) receiveObject();
				yield DataAccess.filterWines(year);
			}
			case "nameYear" ->
			{
				name = (String) receiveObject();
				year = (int) receiveObject();
				yield DataAccess.filterWines(name, year);
			}
			default -> null;
		};
		
		if (wines == null)
			wines = new ArrayList<Wine>();
		sendObject(wines);
		
		return wines == null ? Outcome.ERROR : Outcome.SUCCESS;
	}
	
	/**
	 * Executes the promotions procedure
	 * @return The Outcome
	 */
	private Outcome promotions()
	{
		return sendObject("1 cassa da 6 bottiglie -> 5% di sconto (2% per le successive)\n"
				+ "1 cassa da 12 bottiglie -> 10% di sconto (3% per le successive)") ? Outcome.SUCCESS : Outcome.ERROR;
	}
	
	/**
	 * Executes the add to cart procedure
	 * @return The Outcome
	 */
	private Outcome addToCart()
	{
		int idWine = (int) receiveObject();
		int quantity = (int) receiveObject();
		
		return DataAccess.addToCart(this.username, idWine, quantity) ? Outcome.SUCCESS : Outcome.ERROR;
	}
	
	/**
	 * Executes the filter Customers procedure
	 * @return The Outcome
	 */
	private Outcome filterCustomers()
	{
		String surname = (String) receiveObject();
		ArrayList<User> customers = DataAccess.filterCustomers(surname);
		
		return sendObject(customers) ? Outcome.SUCCESS : Outcome.ERROR;
	}
	
	/**
	 * Executes the delete Employee procedure
	 * @return The Outcome
	 */
	private Outcome deleteEmployeeAccount()
	{
		return DataAccess.deleteEmployee((String) receiveObject()) ? Outcome.SUCCESS : Outcome.ERROR;
	}
	
	/**
	 * Executes the create purchase procedure
	 * @return The Outcome
	 */
	private Outcome createPurchase()
	{	
		Purchase p = (Purchase) receiveObject();
		
		if (DataAccess.insertPurchase(p))
		{
			if (p.isPurchaseProposal())
			{
				p = DataAccess.getPurchaseFromTimestamp(p.getDate());
				if (!p.isNull())
					this.srv.enqueuePurchaseProposal(p);
			}
			return Outcome.SUCCESS;
		}
		return Outcome.ERROR;
	}
	
	/**
	 * Executes the filter purchase procedure
	 * @return The Outcome
	 */
	private Outcome filterPurchase(PurchaseType type)
	{
		Date start, end;
		
		start = (Date) receiveObject();
		end = (Date) receiveObject();
		
		return sendObject(DataAccess.filterPurchase(type, start, end)) ? Outcome.SUCCESS : Outcome.ERROR;
	}
	
	/**
	 * Executes the check sales order procedure
	 * @return The Outcome
	 */
	private Outcome checkSalesOrder()
	{
		Timestamp ts = (Timestamp) receiveObject();
		Date deliveryDate = (Date) receiveObject();
		
		return (DataAccess.addDigitalSignature(ts, this.username) && DataAccess.addDeliveryDate(ts, deliveryDate)
				&& DataAccess.addService(ts, false)) ? Outcome.SUCCESS : Outcome.ERROR;
	}
	
	/**
	 * Executes the accept or refuse purchase procedure
	 * @return The Outcome
	 */
	private Outcome acceptRefusePurchase()
	{
		Timestamp ts = (Timestamp) receiveObject();
		boolean accept = (boolean) receiveObject();
		
		return DataAccess.acceptRefusePurchase(ts, accept) ? Outcome.SUCCESS : Outcome.ERROR;
	}
	
	/**
	 * Executes the buy procedure
	 * @return The Outcome
	 */
	private Outcome buy()
	{	
		return DataAccess.buy(this.username) ? Outcome.SUCCESS : Outcome.ERROR;
	}
	
	/**
	 * Executes the get Purchase Proposal procedure
	 * @return The Outcome
	 */
	private Outcome getPurchaseProposal()
	{
		Purchase p = this.srv.dequeuePurchaseProposal();
		
		return sendObject(p == null ? (new Purchase()) : p) ? Outcome.SUCCESS : Outcome.ERROR;
	}
	
	/**
	 * Executes the insert Purchase Proposal procedure
	 * @return The Outcome
	 */
	private Outcome insertPurchaseProposal()
	{
		return this.srv.enqueuePurchaseProposal((Purchase) receiveObject()) ? Outcome.SUCCESS : Outcome.ERROR;
	}
	
	/**
	 * Executes the employees procedure
	 * @return The Outcome
	 */
	private Outcome employees()
	{	
		return sendObject(DataAccess.allEmployees()) ? Outcome.SUCCESS : Outcome.ERROR;
	}
	
	/**
	 * Executes the user is present procedure
	 * @return The Outcome
	 */
	private Outcome userIsPresent()
	{
		Account type = (Account) receiveObject();
		String username = (String) receiveObject();
		
		return DataAccess.userIsPresent(type, username) ? Outcome.SUCCESS : Outcome.ERROR;
	}
	
	/**
	 * Executes the cart procedure
	 * @return The Outcome
	 */
	private Outcome cart()
	{
		return sendObject(DataAccess.cart(this.username)) ? Outcome.SUCCESS : Outcome.ERROR;
	}
	
	/**
	 * Executes the remove from cart procedure
	 * @return The Outcome
	 */
	private Outcome removeFromCart()
	{
		int idWine = (int) receiveObject();
		
		return DataAccess.deleteItemCart(this.username, idWine) ? Outcome.SUCCESS : Outcome.ERROR;
	}
	
	/**
	 * Executes the delete purchase proposal procedure
	 * @return The Outcome
	 */
	private Outcome deletePurchaseProposal()
	{
		return DataAccess.deletePurchaseProposal((Timestamp) receiveObject()) ? Outcome.SUCCESS : Outcome.ERROR;
	}
	
	/**
	 * Executes the check purchase proposal procedure
	 * @return The Outcome
	 */
	private Outcome checkPurchaseProposal()
	{
		Timestamp ts = (Timestamp) receiveObject();
		
		return DataAccess.addDigitalSignature(ts, this.username) ? Outcome.SUCCESS : Outcome.ERROR;
	}
	
	/**
	 * Executes the wines under threshold procedure
	 * @return The Outcome
	 */
	private Outcome winesUnderThreshold()
	{
		ArrayList<Wine> wines = DataAccess.allWines();
		ArrayList<Wine> winesUnderThreshold = new ArrayList<Wine>();
		
		for (Wine w : wines)
			if (w.getAvailableQuantity() < this.srv.getThresholdFromIDWine(w.getId()))
				winesUnderThreshold.add(w);
		
		return sendObject(winesUnderThreshold) ? Outcome.SUCCESS : Outcome.ERROR;
	}
	
	/**
	 * Executes the report procedure
	 * @return The Outcome
	 */
	private Outcome report()
	{
		Date start = (Date) receiveObject();
		Date end = (Date) receiveObject();
		
		return sendObject(DataAccess.report(start, end)) ? Outcome.SUCCESS : Outcome.ERROR;
	}
}
