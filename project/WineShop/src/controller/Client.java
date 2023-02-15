
package controller;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

import model.Account;
import model.Purchase;
import model.PurchaseType;
import model.Report;
import model.User;
import model.Wine;

/**
 * Class Client, which represents a wineshop's client (eg. Administrator, Employee, Customer)
 * @author Federico Brandini, Federico Putamorsi
 * @version 14.01.2023
 */
public class Client
{
	/** Server port */
	private static final int SPORT = 5670;
	
	/** Server host */
	private static final String SHOST = "localhost";
	
	/** Client's socket */
	private Socket client;
	
	/** Output stream for client's socket */
	private ObjectOutputStream os;
	
	/** Input stream for client's socket */
	private ObjectInputStream is;
	
	/** Username of logged user */
	private String username;

	/**
	 * Instantiates new wineshop client
	 */
	public Client()
	{
		try
		{
			username = null;
			client = new Socket(SHOST, SPORT);
			os = new ObjectOutputStream(client.getOutputStream());
			os.flush();	// to avoid is (the object) forward block
			is = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Send an object to server
	 * @param oSend The object to send
	 * @return True if the sending was successful, false otherwise
	 */
	public boolean sendObject(Object oSend)
	{
		try
		{	
			os.writeObject(oSend);
			os.flush();
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
	public Object receiveObject()
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
	 * Record new user in WineShop system
	 * @param u The new user
	 * @return True if the operation was successful, false otherwise
	 */
	public boolean signIn(User u)
	{
		sendObject("signIn");
		sendObject(u);
		
		return receiveObject().equals("success");
	}
	
	/**
	 * Executes a user's login
	 * @param username The username of the account
	 * @param password The password of the account
	 * @return The Account type of logged user
	 */
	public Account login(String username, String password)
	{	
		sendObject("login");
		sendObject(username);
		sendObject(password);
		Account type = (Account) receiveObject();
		
		if (type != Account.FOREIGN)
			this.username = username;
  		return type;
	}
	
	/**
	 * @return Username of logged user
	 */
	public String getUsername()
	{
		return this.username;
	}
	
	/**
	 * Executes the logout for current session
	 */
	public boolean logout()
	{
		sendObject("logout");
		
		return receiveObject().equals("success");
	}
	
	/**
	 * Change the current password with another one
	 * @param username Username of the account to modify
	 * @param currentPassword Current password of that account
	 * @param newPassword The new password
	 * @return True if the operation was successful, false otherwise
	 */
	public boolean changePassword(String username, String currentPassword, String newPassword)
	{
		sendObject("changePsw");
		sendObject(username);
		sendObject(currentPassword);
		sendObject(newPassword);
		
		return receiveObject().equals("success");
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * Search a subset of wines in the WineShop
	 * @param name The initials of the wines' name (empty string for all wines)
	 * @param year The product year
	 * @return Recorded wines which respect the constraints
	 */
	public ArrayList<Wine> wines(String name, Integer year)
	{
		Object oReceived;
		int discriminant = ((name != null && name != "") ? 2 : 0) + (year != null ? 1 : 0); // 00, 01, 10, 11 (bit1->name, bit0->year)
		
		sendObject("filterWines");
		switch (discriminant)
		{
			case 0:
				sendObject("all");
				break;
			case 1:
				sendObject("year");
				sendObject(year);
				break;
			case 2:
				sendObject("name");
				sendObject(name);
				break;
			case 3:
				sendObject("nameYear");
				sendObject(name);
				sendObject(year);
				break;
		};

		oReceived = receiveObject();
		receiveObject();
		return oReceived instanceof ArrayList ? (ArrayList<Wine>)oReceived : (new ArrayList<Wine>());
	}
	
	/**
	 * @return The promotions string
	 */
	public String getPromotions()
	{
		sendObject("promotions");
		String proms = (String) receiveObject();
		receiveObject();
		
		return proms;
	}
	
	/**
	 * Add wine to the shopping cart
	 * @param idWine The id of the wine to add
	 * @param quantity The quantity of that wine
	 * @return True if the operation was successful, false otherwise
	 */
	public boolean addToCart(int idWine, int quantity)
	{
		sendObject("addToCart");
		sendObject(idWine);
		sendObject(quantity);
		
		return receiveObject().equals("success");
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * Search a subset of customers
	 * @param surname The initials of surname
	 * @return List of customers, who respect the constraints
	 */
	public ArrayList<User> customers(String surname)
	{
		Object oReceived;
		
		sendObject("filterCustomers");
		sendObject((surname == null ? "" : surname));
		oReceived = receiveObject();
		receiveObject();
		
		return oReceived instanceof ArrayList ? (ArrayList<User>)oReceived : (new ArrayList<User>());
	}
	
	/**
	 * Change the password with 'changeMe'
	 * @param username The username of the account to reset
	 * @return True if the operation was successful, false otherwise
	 */
	public boolean resetEmployee(String username)
	{
		sendObject("resetEmployee");
		sendObject(username);
		
		return receiveObject().equals("success");
	}
	
	/**
	 * Delete employee
	 * @param username The username of the account to delete
	 * @return True if the operation was successful, false otherwise
	 */
	public boolean deleteEmployee(String username)
	{
		sendObject("deleteEmployee");
		sendObject(username);
		
		return receiveObject().equals("success");
	}
	
	/**
	 * Create a general Purchase
	 * @param p Purchase to create
	 * @return True if the operation was successful, false otherwise
	 */
	public boolean createPurchase(Purchase p)
	{
		sendObject("createPurchase");
		sendObject(p);
		
		return receiveObject().equals("success");
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * Search a subset of purchases
	 * @param type type of purchase
	 * @param start start date for search
	 * @param end end date for search
	 * @return ArrayList of purchases
	 */
	public ArrayList<Purchase> purchases(PurchaseType type, Date start, Date end)
	{	
		Object oReceived;
		
		String cmd =
		switch (type)
		{
			case SALES_ORDER -> "filterSalesOrder";
			case PURCHASE_ORDER -> "filterPurchaseOrder";
			case PURCHASE_PROPOSAL -> "filterPurchaseProposal";
			default -> null;
		};
		if (cmd == null)
			return new ArrayList<Purchase>();
		
		sendObject(cmd);
		sendObject(start);
		sendObject(end);
		oReceived = receiveObject();
		receiveObject();
		
		return oReceived instanceof ArrayList ? (ArrayList<Purchase>)oReceived : (new ArrayList<Purchase>());
	}
	
	/**
	 * Check a SalesOrder, make digital signature, delivery date, add a service 
	 * @param ts the timestamp of the sales order
	 * @param deliveryDate delivey date to add
	 * @return True if the operation was successful, false otherwise
	 */
	public boolean checkSalesOrder(Timestamp ts, Date deliveryDate)
	{
		sendObject("checkSalesOrder");
		sendObject(ts);
		sendObject(deliveryDate);
		      
		return receiveObject().equals("success");
	}
	
	/**
	 * Check a Purchase Proposal: add Digital Signature
	 * @param ts Timestamp of the purchase proposal
	 * @return True if the operation was successful, false otherwise
	 */
	public boolean checkPurchaseProposal(Timestamp ts)
	{
		sendObject("checkPurchaseProposal");
		sendObject(ts);
		      
		return receiveObject().equals("success");
	}
	
	/**
	 * Accept or Refuse a Purchase
	 * @param ts Timestamp of the purchase
	 * @param accept True if accept, false otherwise
	 * @return True if the operation was successful, false otherwise
	 */
	public boolean acceptRefusePurchase(Timestamp ts, boolean accept)
	{
		sendObject("acceptRefusePurchase");
		sendObject(ts);
		sendObject(accept);
		
		return receiveObject().equals("success");
	}
	
	/**
	 * Buy products in shopping cart
	 * @return True if the operation was successful, false otherwise
	 */
	public boolean buy()
	{
		sendObject("buy");
		
		return receiveObject().equals("success");
	}
	
	/**
	 * Get the purchase proposal that just arrived to serve
	 * @return Purchase proposal
	 */
	public Purchase getPurchaseProposal()
	{
		Purchase p;
		
		sendObject("dequePurchaseProposal");
		p = (Purchase) receiveObject();
		receiveObject();
		
		return p;
	}
	
	/**
	 * Send the proposal that will be served
	 * @param p Purchase to send
	 * @return True if the operation was successful, false otherwise
	 */
	public boolean sendPurchaseProposal(Purchase p)
	{
		sendObject("enqueuePurchaseProposal");
		sendObject(p);
		
		return receiveObject().equals("success");
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * Search all of employees
	 * @return ArrayList of employees
	 */
	public ArrayList<User> employees()
	{
		Object oReceived;
		
		sendObject("employees");
		oReceived = receiveObject();
		receiveObject();
		
		return oReceived instanceof ArrayList ? (ArrayList<User>)oReceived : (new ArrayList<User>());
	}
	
	/**
	 * Check if the user is present
	 * @param type type of the user
	 * @param username username of the user
	 * @return True if user is present, false otherwise
	 */
	public boolean userIsPresent(Account type, String username)
	{
		sendObject("userIsPresent");
		sendObject(type);
		sendObject(username);
		
		return receiveObject().equals("success");
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * Search wines in shopping cart
	 * @return ArrayList of wines added to cart
	 */
	public ArrayList<Wine> cart()
	{
		Object oReceived;
		
		sendObject("cart");
		oReceived = receiveObject();
		receiveObject();
		
		return oReceived instanceof ArrayList ? (ArrayList<Wine>)oReceived : (new ArrayList<Wine>());
	}
	
	/**
	 * Remove wine from cart
	 * @param idWine Id of the wine to remove
	 * @return True if the operation was successful, false otherwise
	 */
	public boolean removeFromCart(int idWine)
	{
		sendObject("removeFromCart");
		sendObject(idWine);
		
		return receiveObject().equals("success");
	}
	
	/**
	 * Delete selected Purchase Proposal
	 * @param ts Timestamp of selected purchase to delete
	 * @return True if the operation was successful, false otherwise
	 */
	public boolean deletePurchaseProposal(Timestamp ts)
	{
		sendObject("deletePurchaseProposal");
		sendObject(ts);
		
		return receiveObject().equals("success");
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * Indicates which wines have low quantities in the database
	 * @return ArrayList of wines with low quantities
	 */
	public ArrayList<Wine> winesUnderThreshold()
	{
		Object oReceived;
		
		sendObject("winesUnderThreshold");
		oReceived = receiveObject();
		receiveObject();
		
		return oReceived instanceof ArrayList ? (ArrayList<Wine>)oReceived : (new ArrayList<Wine>());
	}
	
	/**
	 * Generate a Report from indicated period
	 * @param start Start date of the period
	 * @param end End date of the period
	 * @return Report
	 */
	public Report report(Date start, Date end)
	{
		Report rep;
		
		sendObject("report");
		sendObject(start);
		sendObject(end);
		rep = (Report) receiveObject();
		receiveObject();
		
		return rep;
	}
  	
//  	public static void main(String[] args)
//  	{
//  		Client c = new Client();
//  		ArrayList<Wine> wineList;
//  		ArrayList<User> customers;
//  		ArrayList<Purchase> purchases;
//  		Account type;
//  		Purchase currP;
//  		
//  		if ((type = c.login("eFedePuta", "ciccio")) != Account.FOREIGN)
//  		{
//  			System.out.println("Login avvenuto.");
//  			
//  			// EMPLOYEE purchase proposal
//  			if (type == Account.EMPLOYEE)
//  			{
//  				currP = c.getPurchaseProposal();
//  				System.out.println(c.sendPurchaseProposal(currP));
//  			}
//  			
////  			// promotions
////  			System.out.println(c.getPromotions());
////  			
////  			// wines
////  			wineList = c.wines(null, null);
////  			for (Wine w : wineList)
////  				System.out.println(w);
//  			
// 			// delete employee
//  			//System.out.println(c.deleteEmployee("corly"));
//  			 
//  	  		// sign in
//  	  		User u = new User();
//  	  		System.out.println(c.signIn(u));
////  	  		
////  	  		// change password
////  	  		System.out.println(c.changePassword("corly", "1234", "1"));
////  	  		
////  	  		// add to cart
////  	  		System.out.println(c.addToCart(4, 6));
////  	  		
////  	  		// username
////  	  		customers = c.customers("poc");
////  			for (User u1 : customers)
////  				System.out.println(u1);
////  			
////  			// reset employee
////  			System.out.println(c.resetEmployee("corly"));
////  			
//  			// create purchase proposal
//  			System.out.println(c.createPurchase(new Purchase()));
////  			
////  			// create purchase order
////  			System.out.println(c.createPurchase(new Purchase()));
////  			
////  			// create sales order
////  			System.out.println(c.createPurchase(new Purchase()));
////  			
//  			// sales order
//  			purchases = c.purchases(PurchaseType.SALES_ORDER, Date.valueOf("2000-01-01"), Date.valueOf(LocalDate.now()));
//  			System.out.println("Sales Order");
//  			for (Purchase p : purchases)
//  				System.out.println(p);
//  			
//  			// purchase order
//  			purchases = c.purchases(PurchaseType.PURCHASE_ORDER, Date.valueOf("2000-01-01"), Date.valueOf(LocalDate.now()));
//  			System.out.println("Purchase Order");
//  			for (Purchase p : purchases)
//  				System.out.println(p);
//  			
//  			// purchase proposal
//  			purchases = c.purchases(PurchaseType.PURCHASE_PROPOSAL, Date.valueOf("2000-01-01"), Date.valueOf(LocalDate.now()));
//  			System.out.println("Purchase Proposal");
//  			for (Purchase p : purchases)
//  				System.out.println(p);
////  			
////  			// check sales order
////  			System.out.println(c.checkSalesOrder(Timestamp.valueOf("2023-02-02 17:07:49"), Date.valueOf(LocalDate.now())));
////  			
////  			// accept/refuse
////  			System.out.println(c.acceptRefusePurchase(Timestamp.valueOf("2023-02-02 17:40:52"), false));
////  			
////  			// buy
////  			System.out.println(c.buy());
//  			
//  			// logout
//  			if (c.logout())
//  				System.out.println("Logout avvenuto.");
//  			else
//  				System.out.println("Logout NON avvenuto.");
//  		}
//  		else
//  			System.out.println("Errore.");
//  	}
}
