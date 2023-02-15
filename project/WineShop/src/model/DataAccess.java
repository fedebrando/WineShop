
package model;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class DataAccess, which supplies several type of method to retrieve data from the database wineshop (this class prevents SQL injection attacks)
 * @author Federico Brandini, Federico Putamorsi
 * @version 15.12.2022
 */
public class DataAccess
{
	public static final int ERROR = -1;
	private static final String DBURL = "jdbc:mysql://localhost:3306/wineshop?";
	private static final String ARGS = "createDatabaseIfNotExist=true&serverTimezone=UTC";
	private static final String LOGIN = "root";
	private static final String PASSWORD = "";
	public static final Date FAR = Date.valueOf("1800-01-01");
	
	private static Statement stmt;
	private static Connection conn;
	
	static
	{
		try
		{
			conn = DriverManager.getConnection(DBURL + ARGS, LOGIN, PASSWORD);
			stmt = conn.createStatement();
		}
		catch (SQLException e)
		{
			stmt = null;
			e.printStackTrace();
		}
	}
	
	public static String SHA3_512(String text)
	{
        MessageDigest crypt;
		try
		{
			crypt = MessageDigest.getInstance("SHA3-512");
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
			return null;
		}
        crypt.update(text.getBytes(StandardCharsets.UTF_8));

        byte[] bytes = crypt.digest();
        BigInteger bi = new BigInteger(1, bytes);
        String digest = String.format("%0" + (bytes.length << 1) + "x", bi);

        return digest;
	}
	
	/**
	 * Insert Wine w in DataBase
	 * @param w wine to insert
	 * @return True if the operation was successful, false otherwise
	 */
	public static boolean insertWine(Wine w)
	{
      	String insertSql = "INSERT INTO Wine(Name, Producer, Origin, Year, Notes, Vines, Price, AvailableQuantity) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
      	PreparedStatement pstmt;

      	try
      	{
          	pstmt = conn.prepareStatement(insertSql);

            pstmt.setString(1, w.getName());
            pstmt.setString(2, w.getProducer());
            pstmt.setString(3, w.getOrigin());
            pstmt.setString(4, Integer.toString(w.getYear()));
            pstmt.setString(5, w.getNotes());
            pstmt.setString(6, w.getVines());
            pstmt.setString(7, Double.toString(w.getPrice()));
            pstmt.setString(8, Integer.toString(w.getAvailableQuantity()));
            pstmt.addBatch();
            
            pstmt.executeBatch();
      	}
      	catch (SQLException e)
      	{
      		e.printStackTrace();
      		return false;
      	}
      	return true;
	}
	
	/**
	 * Increase List price of 1
	 * @return Number of update wines
	 */
	public static int increaseListPriceWines()
	{
		String strUpdate = "UPDATE Wine SET Price = Price + 1";
		try
		{
			return stmt.executeUpdate(strUpdate);
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return ERROR;
	}
	
	/**
	 * All wines
	 * @return ArrayList of all wines
	 */
	public static ArrayList<Wine> allWines()
	{
		return filterWines(null, null);
	}
	
	/**
	 * Filter Wines by name
	 * @param name Name to filter
	 * @return ArrayList of filtered wines
	 */
	public static ArrayList<Wine> filterWines(String name)
	{
		return filterWines(name, null);
	}
	
	/**
	 * Filter Wines by year
	 * @param year Year to filter
	 * @return ArrayList of filtered wines
	 */
	public static ArrayList<Wine> filterWines(Integer year)
	{
		return filterWines(null, year);
	}
	
	/**
	 * Filter Wines by name and by year
	 * @param name Name to filter
	 * @param year Year to filter
	 * @return ArrayList of filtered wines
	 */
	public static ArrayList<Wine> filterWines(String name, Integer year)
	{
		ArrayList<Wine> wineList = new ArrayList<Wine>();
		ResultSet rset = null;
		Wine w;
		PreparedStatement pstmt;
		int discriminant = (name != null ? 2 : 0) + (year != null ? 1 : 0); // 00, 01, 10, 11 (bit1->name, bit0->year)
		
		String query =
		switch (discriminant)
		{
			case 0 -> "SELECT * FROM Wine";
			case 1 -> "SELECT * FROM Wine WHERE Year = ?";
			case 2 -> "SELECT * FROM Wine WHERE Name LIKE ?";
			case 3 -> "SELECT * FROM Wine WHERE Name LIKE ? AND Year = ?";
			default -> "";
		};
		
		try
		{
			pstmt = conn.prepareStatement(query);
			
			switch (discriminant)
			{
				case 1:
					pstmt.setInt(1, year);
					break;
				case 2:
					pstmt.setString(1, name+"%");
					break;
				case 3:
					pstmt.setString(1, name+"%");
					pstmt.setInt(2, year);
					break;
			}

			rset = pstmt.executeQuery();
			while (rset.next())
			{
				w = new Wine(rset.getInt("ID"), rset.getString("Name"), rset.getString("Producer"), rset.getString("Origin"), rset.getInt("Year"), rset.getString("Notes"), rset.getString("Vines"), rset.getFloat("Price"), rset.getInt("AvailableQuantity"));
				wineList.add(w);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		
		return wineList;
	}
	
	/**
	 * Password Hash of selected username 
	 * @param username Username of the user
	 * @return Password Hash of User
	 */
	public static String passwordHashOf(String username)
	{
		String query = "SELECT PswHash FROM User WHERE Username = ?";
		ResultSet rset = null;
		String pswHash = null;
		PreparedStatement pstmt;
		
		try
		{
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, username);
			rset = pstmt.executeQuery();
			if (rset.next())
			{
				pswHash = rset.getString("PswHash");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return "";
		}
		return (pswHash == null ? "" : pswHash);
	}
	
	/**
	 * Account type of selected User
	 * @param username Username of the User
	 * @return Account Type of User
	 */
	public static Account accountTypeOf(String username)
	{
		String query = "SELECT Type FROM User WHERE Username = ?";
		ResultSet rset = null;
		String type = null;
		PreparedStatement pstmt;
		
		try
		{
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, username);
			rset = pstmt.executeQuery();
			if (rset.next())
			{
				type = rset.getString("Type");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		
		if (type == null)
			return null;
		return Account.valueOf(type.toUpperCase());
	}
	
	/**
	 * Delete selected wine
	 * @param id Id of wine
	 * @return True if the operation was successful, false otherwise
	 */
	public static boolean deleteWine(int id)
	{
		String query = "DELETE FROM Wine WHERE ID = ?";
		  
		try
		{
			PreparedStatement preparedStmt = conn.prepareStatement(query);
		    preparedStmt.setInt(1, id);
		    return preparedStmt.execute();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Insert User 
	 * @param u User to insert
	 * @return True if the operation was successful, false otherwise
	 */
	public static boolean insertUser(User u)
	{
		String query = "INSERT INTO User VALUES (?,?,?,?,?,?,?,?)";
		try
		{
			PreparedStatement preparedStmt = conn.prepareStatement(query);
		    preparedStmt.setString(1, u.getUsername());
		    preparedStmt.setString(2, u.getPasswordHash());
		    preparedStmt.setString(3, u.getType().getStrType());
		    preparedStmt.setString(4, u.getName());
		    preparedStmt.setString(5, u.getSurname());
		    preparedStmt.setString(6, u.getFiscalCode());
		    preparedStmt.setString(7, u.getTelephone());
		    preparedStmt.setString(8, u.getAddress());
		    preparedStmt.execute();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Verifies if User is present in db
	 * @param username Username of account
	 * @param currPswHash Password Hash of the account
	 * @return
	 */
	public static boolean authenticity(String username, String currPswHash)
	{
		String query = "SELECT * FROM User WHERE Username = ? AND PswHash = ?";
		ResultSet rset = null;
		PreparedStatement pstmt;
		
		try
		{
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, username);
			pstmt.setString(2, currPswHash);
			rset = pstmt.executeQuery();
			
			return rset.next();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Change Password Hash
	 * @param username Username of User
	 * @param pswHash Password Hash of User
	 * @return True if the operation was successful, false otherwise
	 */
	public static boolean changePswHash(String username, String pswHash)
	{
		String query = "UPDATE User SET PswHash = ? WHERE Username = ?";

		try
		{
			PreparedStatement preparedStmt = conn.prepareStatement(query);
		    preparedStmt.setString(1, pswHash);
		    preparedStmt.setString(2, username);
		    preparedStmt.execute();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Add wine/quantity to shopping cart
	 * @param username Username of User
	 * @param idWine Id of the wine
	 * @param quantity Quantity to add
	 * @return True if the operation was successful, false otherwise
	 */
	public static boolean addToCart(String username, int idWine, int quantity)
	{
		String query = "SELECT * FROM ShoppingCart WHERE Customer = ? AND IDWine = ?";
		ResultSet rset = null;
		PreparedStatement pstmt, pstmt2;
		
		try
		{
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, username);
			pstmt.setInt(2, idWine);
			rset = pstmt.executeQuery();
			
			if (rset.next())
			{
				query = "UPDATE ShoppingCart SET Quantity = Quantity + ? WHERE Customer = ? AND IDWine = ?";
				pstmt = conn.prepareStatement(query);
				pstmt.setInt(1, quantity);
				pstmt.setString(2, username);
				pstmt.setInt(3, idWine);
				pstmt.execute();
			}
			else
			{
				query = "INSERT INTO ShoppingCart VALUES (?,?,?)";
				pstmt2 = conn.prepareStatement(query);
				pstmt2.setString(1, username);
				pstmt2.setInt(2, idWine);
				pstmt2.setInt(3, quantity);
				pstmt2.execute();
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Serach a subset of customer by surname
	 * @param surname Surname of customer
	 * @return ArrayList of filter customers
	 */
	public static ArrayList<User> filterCustomers(String surname)
	{
		ResultSet rset = null;
		PreparedStatement pstmt;
		User u;
		ArrayList<User> customers = new ArrayList<User>();
		
		try
		{
			if (surname == null || surname == "")
				pstmt = conn.prepareStatement("SELECT * FROM User WHERE Type = '" + Account.CUSTOMER.getStrType() + "'");
			else
			{
				pstmt = conn.prepareStatement("SELECT * FROM User WHERE Type = '" + Account.CUSTOMER.getStrType() + "' AND Surname LIKE ?");
				pstmt.setString(1, surname+"%");
			}
			rset = pstmt.executeQuery();
			while (rset.next())
			{
				u = new User(rset.getString("Name"), rset.getString("Surname"), rset.getString("FiscalCode"), rset.getString("Telephone"), rset.getString("Address"), rset.getString("Username"), rset.getString("PswHash"), Account.CUSTOMER);
				customers.add(u);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return customers;
	}
	
	/**
	 * Delete Employee
	 * @param username Username of Employee
	 * @return True if the operation was successful, false otherwise
	 */
	public static boolean deleteEmployee(String username)
	{
		String query = "DELETE FROM User WHERE Type = '" + Account.EMPLOYEE.getStrType() + "' AND Username = ?";
		  
		try
		{
			PreparedStatement preparedStmt = conn.prepareStatement(query);
		    preparedStmt.setString(1, username);
		    preparedStmt.execute();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Price of selected wine
	 * @param idWine Id of wine
	 * @return the float price
	 */
	public static float priceOf(int idWine)
	{
		String query = "SELECT Price FROM Wine WHERE ID = ?";
		ResultSet rset = null;
		PreparedStatement pstmt;
		
		try
		{
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, idWine);
			rset = pstmt.executeQuery();
			
			if (rset.next())
				return rset.getFloat("Price");
			return -1;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * Type of Account
	 * @param username Username of the account
	 * @return the Account selected
	 */
	public static Account typeOf(String username)
	{
		String query = "SELECT Type FROM User WHERE Username = ?";
		ResultSet rset = null;
		PreparedStatement pstmt;
		
		try
		{
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, username);
			rset = pstmt.executeQuery();
			
			if (rset.next())
				return Account.valueOf(rset.getString("Type").toUpperCase());
			return Account.FOREIGN;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Get random FC service from db
	 * @param supplier true if supplier, false if courier
	 * @return random FC service
	 */
	public static String getRandomFcService(boolean supplier)
	{
		String query = "SELECT FiscalCode FROM Service WHERE Type = '" + (supplier ? "Supplier" : "Courier") + "'";
		ResultSet rset = null;
		PreparedStatement pstmt;
		
		try
		{
			pstmt = conn.prepareStatement(query);
			rset = pstmt.executeQuery();
			if (rset.next())
				return rset.getString("FiscalCode");
			return null;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Add Service
	 * @param ts Timestamp of the purchase
	 * @param supplier true if supplier, false if courier
	 * @return True if the operation was successful, false otherwise
	 */
	public static boolean addService(Timestamp ts, boolean supplier)
	{
		String query = "UPDATE Purchase SET FCService = ? WHERE Date = ?";

		try
		{
			PreparedStatement preparedStmt = conn.prepareStatement(query);
		    preparedStmt.setString(1, getRandomFcService(supplier));
		    preparedStmt.setString(2, ts.toString());
		    preparedStmt.execute();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Insert Purchase
	 * @param p Purchase to insert
	 * @return True if the operation was successful, false otherwise
	 */
	public static boolean insertPurchase(Purchase p)
	{
		String query = "INSERT INTO Purchase(User, DigitalSignature, IDWine, FCService, Date, Proposal, Quantity, Price, DeliveryDate) VALUES (?,?,?,?,?,?,?,?,?)";
		float totPrice = 0;
		int currQuantity;
		ArrayList<Integer> discounts = new ArrayList<Integer>();
		Timestamp now;
		
		for (Integer idWine : p.getWinesQuantity().keySet())
		{
			currQuantity = p.getWinesQuantity().get(idWine);
			totPrice += priceOf(idWine)*currQuantity;
			
			if (!isPurchaseOrder(p)) // sales order or purchase proposal (both made by a customer)
			{
				while (currQuantity >= 12)
				{
					if (discounts.indexOf(10) == -1)
						discounts.add(10);
					else
						discounts.add(3);
					currQuantity -= 12;
				}	
				while (currQuantity >= 6)
				{
					if (discounts.indexOf(5) == -1)
						discounts.add(5);
					else
						discounts.add(2);
					currQuantity -= 6;
				}	
			}
		}
		for (int discount : discounts)
			totPrice -= (discount/100.0) * totPrice;
		
		try
		{
			PreparedStatement preparedStmt = conn.prepareStatement(query);
		    preparedStmt.setString(1, p.getUsername());
		    if (isPurchaseOrder(p))
		    	preparedStmt.setString(2, p.getUsername());
		    else
		    	preparedStmt.setString(2, p.getDigitalSignature());
		    
		    preparedStmt.setString(4, p.getFcService());
	    	now = new Timestamp(System.currentTimeMillis());
	    	now.setNanos(0);
	    	p.setDate(now);
	    	preparedStmt.setString(5, now.toString());
		    preparedStmt.setInt(6, (p.isPurchaseProposal() ? 1 : 0));
		   
		    preparedStmt.setFloat(8, totPrice);
		    if (isPurchaseOrder(p))
		    	preparedStmt.setDate(9, new Date(now.getTime()));
		    else
		    	preparedStmt.setDate(9, p.getDeliveryDate());

		    p.getWinesQuantity().forEach((idWine, quantity) ->
		    {
		    	try
		    	{
					preparedStmt.setInt(3, idWine);
					preparedStmt.setInt(7, quantity);
					preparedStmt.execute();
				}
		    	catch (SQLException e)
		    	{
					e.printStackTrace();
				}
		    });
		    
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		
		if (isPurchaseOrder(p)) // purchase order
		{
			p.getWinesQuantity().forEach((idWine, quantity) -> {incrementAvailableQuantity(idWine, quantity);});
			addService(now, true);
		}
		return true;
	}
	
	/**
	 * Check if purchase is sales order
	 * @param p Purchase to check
	 * @return True if it is a sales order, false otherwise
	 */
	public static boolean isSalesOrder(Purchase p)
	{
		return typeOf(p.getUsername()) == Account.CUSTOMER && !p.isPurchaseProposal();
	}
	
	/**
	 * Check if purchase is signed sales order
	 * @param p Purchase to check
	 * @return True if it is a signed sales order, false otherwise
	 */
	public static boolean isSignedSalesOrder(Purchase p)
	{
		return isSalesOrder(p) && p.getDigitalSignature() != null;
	}
	
	/**
	 * Check if purchase is unsigned sales order
	 * @param p Purchase to check
	 * @return True if it is a unsigned sales order, false otherwise
	 */
	public static boolean isUnsignedSalesOrder(Purchase p)
	{
		return isSalesOrder(p) && p.getDigitalSignature() == null;
	}
	
	/**
	 * Check if purchase is purchase order
	 * @param p Purchase to check
	 * @return True if it is a purchase order, false otherwise
	 */
	public static boolean isPurchaseOrder(Purchase p)
	{
		return typeOf(p.getUsername()) == Account.EMPLOYEE;
	}
	
	/**
	 * Get purchase of a period and type
	 * @param type Type of purchase
	 * @param start Start date of period
	 * @param end End date of period
	 * @return ArrayList of filter Purchases
	 */
	public static ArrayList<Purchase> filterPurchase(PurchaseType type, Date start, Date end)
	{
		ArrayList<Purchase> purchases = new ArrayList<Purchase>();
		String query = "SELECT * FROM Purchase WHERE ? <= Date AND Date <= ? ";
		ResultSet rset = null;
		PreparedStatement pstmt;
		String currTs;
		HashMap<Integer, Integer> currWq = new HashMap<Integer, Integer>();
		Purchase currPurchase = null;
		String currUser;
		
		start = (start == null ? FAR : start);
		end = (end == null ? Date.valueOf(LocalDate.now()) : end);
		query +=
		switch (type)
		{
			case PURCHASE_PROPOSAL -> "AND Proposal";
			case PURCHASE_ORDER -> "AND NOT Proposal AND User IN (SELECT Username FROM User WHERE Type = '" + Account.EMPLOYEE.getStrType() + "')"; 
			case SALES_ORDER -> "AND NOT Proposal AND User IN (SELECT Username FROM User WHERE Type = '" + Account.CUSTOMER.getStrType() + "')";
			default -> "";
		};
		query += " ORDER BY User, Date";
		
		try
		{
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, (new Timestamp(start.getTime())).toString());
			end = Date.valueOf(end.toLocalDate().plusDays(1));
			pstmt.setString(2, (new Timestamp(end.getTime())).toString());
			rset = pstmt.executeQuery();
			currTs = "";
			currUser = "";
			while (rset.next())
			{
				if (!(rset.getString("User").equals(currUser) && rset.getString("Date").equals(currTs)))
				{
					if (currPurchase != null)
						purchases.add(currPurchase);
					currUser = rset.getString("User");
					currTs = rset.getString("Date");
					currPurchase = new Purchase(null, currUser, rset.getString("DigitalSignature"), rset.getString("FCService"), Timestamp.valueOf(currTs),
							rset.getBoolean("Proposal"), rset.getFloat("Price"), rset.getDate("DeliveryDate"));
					currWq = new HashMap<Integer, Integer>();
					currPurchase.setWinesQuantity(currWq);
				}
				currWq.put(rset.getInt("IDWine"), rset.getInt("Quantity"));
				if (rset.isLast())
					purchases.add(currPurchase);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return purchases;
	}
	
	/**
	 * Increment the aviable quantity of a wine
	 * @param idWine Id of wine
	 * @param quantity Quantity to increment
	 * @return True if the operation was successful, false otherwise
	 */
	public static boolean incrementAvailableQuantity(int idWine, int quantity)
	{
		String query = "UPDATE Wine SET AvailableQuantity = AvailableQuantity + (?) WHERE ID = ?";

		try
		{
			PreparedStatement preparedStmt = conn.prepareStatement(query);
		    preparedStmt.setInt(1, quantity);
		    preparedStmt.setInt(2, idWine);
		    preparedStmt.execute();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Add Digital Signature to purchase
	 * @param ts TimeStamp of purchase
	 * @param digitalSignature Digital Signature to sign
	 * @return True if the operation was successful, false otherwise
	 */
	public static boolean addDigitalSignature(Timestamp ts, String digitalSignature)
	{
		String query = "UPDATE Purchase SET DigitalSignature = ? WHERE Date = ?";

		try
		{
			PreparedStatement preparedStmt = conn.prepareStatement(query);
		    preparedStmt.setString(1, digitalSignature);
		    preparedStmt.setString(2, ts.toString());
		    preparedStmt.execute();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Add Delivery Date to a purchase
	 * @param ts TimeStamp of purchase
	 * @param deliveryDate Delivery date of purchase
	 * @return True if the operation was successful, false otherwise
	 */
	public static boolean addDeliveryDate(Timestamp ts, Date deliveryDate)
	{
		String query = "UPDATE Purchase SET DeliveryDate = ? WHERE Date = ?";

		try
		{
			PreparedStatement preparedStmt = conn.prepareStatement(query);
		    preparedStmt.setDate(1, deliveryDate);
		    preparedStmt.setString(2, ts.toString());
		    preparedStmt.execute();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Delete a purchase
	 * @param ts Timestamp of a purchase
	 * @return True if the operation was successful, false otherwise
	 */
	public static boolean deletePurchase(Timestamp ts)
	{
		String query = "DELETE FROM Purchase WHERE Date = ?";
		  
		try
		{
			PreparedStatement preparedStmt = conn.prepareStatement(query);
		    preparedStmt.setString(1, ts.toString());
		    preparedStmt.execute();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Delete selected Purchase Proposal
	 * @param ts Timestamp of the purchase
	 * @return True if the operation was successful, false otherwise
	 */
	public static boolean deletePurchaseProposal(Timestamp ts)
	{
		String query = "DELETE FROM Purchase WHERE Proposal AND Date = ?";

		try
		{
			PreparedStatement preparedStmt = conn.prepareStatement(query);
		    preparedStmt.setString(1, ts.toString());
		    preparedStmt.execute();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Accept or Refuse the arrived purchase
	 * @param ts Timestamp of the purchase
	 * @param accept True if accept, false otherwise
	 * @return True if the operation was successful, false otherwise
	 */
	public static boolean acceptRefusePurchase(Timestamp ts, boolean accept)
	{
		if (accept)
		{
			String query = "UPDATE Purchase SET Proposal = 0 WHERE Date = ?";

			try
			{
				PreparedStatement preparedStmt = conn.prepareStatement(query);
			    preparedStmt.setString(1, ts.toString());
			    preparedStmt.execute();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				return false;
			}
			return true;
		}
		return deletePurchase(ts);
	}
	
	/**
	 * Wines and quantity in Shopping Cart
	 * @param customerUsername Username of customer
	 * @return HashMap <wine,quantity>
	 */
	public static HashMap<Integer, Integer> shoppingCart(String customerUsername)
	{
		String query = "SELECT IDWine, Quantity FROM ShoppingCart WHERE Customer = ?";
		ResultSet rset = null;
		PreparedStatement pstmt;
		HashMap<Integer, Integer> winesQuantity = new HashMap<Integer, Integer>();
		
		try
		{
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, customerUsername);
			rset = pstmt.executeQuery();
			
			while (rset.next())
				winesQuantity.put(rset.getInt("IDWine"), rset.getInt("Quantity"));
			
			return winesQuantity;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Delete item from shopping cart
	 * @param customerUsername Username of customer
	 * @param idWine Id of wine
	 * @return True if the operation was successful, false otherwise
	 */
	public static boolean deleteItemCart(String customerUsername, int idWine)
	{
		String query = "DELETE FROM ShoppingCart WHERE Customer = ? AND IDWine = ?";
		  
		try
		{
			PreparedStatement preparedStmt = conn.prepareStatement(query);
		    preparedStmt.setString(1, customerUsername);
		    preparedStmt.setInt(2, idWine);
		    preparedStmt.execute();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Buy product from cart
	 * @param customerUsername Username of customer
	 * @return True if the operation was successful, false otherwise
	 */
	public static boolean buy(String customerUsername)
	{
		HashMap<Integer, Integer> winesQuantity = shoppingCart(customerUsername);
		Purchase p;
		ArrayList<Integer> idToRemove = new ArrayList<Integer>();
		
		p = new Purchase(winesQuantity, customerUsername, null, null, null, false, 0, null);
		winesQuantity.forEach((idWine, quantity) -> {winesQuantity.put(idWine, (incrementAvailableQuantity(idWine, -quantity) ? quantity : 0));});
		winesQuantity.forEach((idWine, quantity) -> {if (quantity > 0) deleteItemCart(customerUsername, idWine);});
		
		for (Integer idWine : winesQuantity.keySet())
			if (winesQuantity.get(idWine) == 0)
				idToRemove.add(idWine);
		for (Integer id : idToRemove)
			winesQuantity.remove(id);
		
		return insertPurchase(p);
	}
	
	/**
	 * All Employee
	 * @return ArrayList of all employees
	 */
	public static ArrayList<User> allEmployees()
	{
		ResultSet rset = null;
		PreparedStatement pstmt;
		User u;
		ArrayList<User> employees = new ArrayList<User>();
		
		try
		{
			pstmt = conn.prepareStatement("SELECT * FROM User WHERE Type = ?");
			pstmt.setString(1, Account.EMPLOYEE.getStrType());

			rset = pstmt.executeQuery();
			while (rset.next())
			{
				u = new User(rset.getString("Name"), rset.getString("Surname"), rset.getString("FiscalCode"), rset.getString("Telephone"), rset.getString("Address"), rset.getString("Username"), rset.getString("PswHash"), Account.CUSTOMER);
				employees.add(u);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return employees;
	}
	
	/**
	 * Check if User is present
	 * @param type Type of User
	 * @param username Username of User
	 * @return True if user is present, false otherwise
	 */
	public static boolean userIsPresent(Account type, String username)
	{
		String query = "SELECT * FROM User WHERE Type = ? AND Username = ?";
		ResultSet rset = null;
		PreparedStatement pstmt;
		
		try
		{
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, type.getStrType());
			pstmt.setString(2, username);
			rset = pstmt.executeQuery();
			
			if (rset.next())
				return true;
			return false;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Wines in the shopping cart
	 * @param customer, Username of customer
	 * @return ArrayList of wines in cart
	 */
	public static ArrayList<Wine> cart(String customer)
	{
		ArrayList<Wine> wines = new ArrayList<Wine>();
		String query = "SELECT W.ID, W.Name, W.Producer, W.Origin, W.Year, W.Notes, W.Vines, W.Price, S.Quantity FROM ShoppingCart S, Wine W WHERE S.IDWine = W.ID AND S.Customer = ?";
		ResultSet rset = null;
		PreparedStatement pstmt;
		
		try
		{
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, customer);
			rset = pstmt.executeQuery();
			
			while (rset.next())
				wines.add(new Wine(rset.getInt("ID"), rset.getString("Name"), rset.getString("Producer"), rset.getString("Origin"), rset.getInt("Year"), rset.getString("Notes"), rset.getString("Vines"), rset.getFloat("Price"), rset.getInt("Quantity")));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return wines;
	}
	
	/**
	 * Get Purchase from timestamp
	 * @param ts Timestamp of wine
	 * @return purchase with specified timestamp
	 */
	public static Purchase getPurchaseFromTimestamp(Timestamp ts)
	{
		ArrayList<Purchase> purchases = filterPurchase(PurchaseType.ALL, null, null);
		
		for (Purchase p : purchases)
			if ((p.getDate()).equals(ts))
				return p;
		return new Purchase();
	}
	
	/**
	 * Compute threshold from price
	 * @param price Price of wine
	 * @return threshold
	 */
	private static int threshold(float price)
	{
		return (int)(1400.0/price) + 1; // price = 20 => threshold = 70
	}

	/**
	 * Wines with their threshold
	 * @return HashMap<wine,threshold>
	 */
	public static HashMap<Integer, Integer> quantityThresholds()
	{
		HashMap<Integer, Integer> qt = new HashMap<Integer, Integer>();
		String query = "SELECT ID, Price FROM Wine";
		ResultSet rset = null;
		PreparedStatement pstmt;
		
		try
		{
			pstmt = conn.prepareStatement(query);
			rset = pstmt.executeQuery();
			
			while (rset.next())
				qt.put(rset.getInt("ID"), threshold(rset.getFloat("Price")));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return qt;
	}
	
	/**
	 * Total income in a period
	 * @param start Start date of the period
	 * @param end End date of the period
	 * @return Total income
	 */
	private static float income(Date start, Date end)
	{
		ArrayList<Purchase> salesOrder = filterPurchase(PurchaseType.SALES_ORDER, start, end);
		float income = 0;
		
		for (Purchase so : salesOrder)
			income += so.getPrice();
		return income;
	}
	
	/**
	 * Total expenses in a period
	 * @param start Start date of the period
	 * @param end End date of the period
	 * @return Total expenses
	 */
	private static float expenses(Date start, Date end)
	{
		ArrayList<Purchase> purchaseOrders = filterPurchase(PurchaseType.PURCHASE_ORDER, start, end);
		float expenses = 0;
		
		for (Purchase po : purchaseOrders)
			expenses += po.getPrice();
		return expenses;
	}
	
	/**
	 * Total sold bottles in a period
	 * @param start Start date of the period
	 * @param end End date of the period
	 * @return Total sold bottles
	 */
	private static int soldBottles(Date start, Date end)
	{
		ArrayList<Purchase> salesOrder = filterPurchase(PurchaseType.SALES_ORDER, start, end);
		int soldBottles = 0;
		HashMap<Integer, Integer> currWinesQuantity;
		
		for (Purchase so : salesOrder)
		{
			currWinesQuantity = so.getWinesQuantity();
			for (Integer idWine : currWinesQuantity.keySet())
				soldBottles += currWinesQuantity.get(idWine);
		}
		return soldBottles;
	}
	
	/**
	 * Number of available bottles
	 * @return Number of available bottles
	 */
	private static int availableBottles()
	{
		String query = "SELECT SUM(AvailableQuantity) AS AvailableBottles FROM Wine";
		ResultSet rset = null;
		PreparedStatement pstmt;
		
		try
		{
			pstmt = conn.prepareStatement(query);
			rset = pstmt.executeQuery();
			
			if (!rset.next())
				return -1;
			return rset.getInt("AvailableBottles");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * List of Sold wines in a period
	 * @param start Start date of the period
	 * @param end End date of the period
	 * @return HashMap<wine,qunatity>
	 */
	private static HashMap<Integer, Integer> soldWines(Date start, Date end)
	{
		HashMap<Integer, Integer> soldWines = new HashMap<Integer, Integer>();
		String query = "SELECT IDWine, COUNT(*) AS SoldWines FROM Purchase WHERE ? <= Date AND Date <= ? "
				+ "AND NOT Proposal AND User IN (SELECT Username FROM User WHERE Type = '" + Account.CUSTOMER.getStrType() + "') GROUP BY User, Date";
		ResultSet rset = null;
		PreparedStatement pstmt;
		
		try
		{
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, (new Timestamp(start.getTime())).toString());
			pstmt.setString(2, (new Timestamp(end.getTime())).toString());
			rset = pstmt.executeQuery();
			
			while (rset.next())
				soldWines.put(rset.getInt("IDWine"), rset.getInt("SoldWines"));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return soldWines;
	}
	
	/**
	 * Generate Report in a period
	 * @param start Start date of the period
	 * @param end End date of the period
	 * @return Report
	 */
	public static Report report(Date start, Date end)
	{
		return new Report(income(start, end), expenses(start, end), soldBottles(start, end), availableBottles(), soldWines(start, end));
	}
}
