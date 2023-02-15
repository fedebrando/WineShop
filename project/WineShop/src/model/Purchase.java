package model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;

/**
 * Class Purchase, which represent a purchase (purchase order, purchase proposal, sales order)
 * @author Federico Brandini, Federico Putamorsi
 * @version 15.12.2022
 */
public class Purchase implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	/** Wines with their quantity */
	private HashMap<Integer, Integer> winesQuantity;
	
	/** The username whom do the purchase */
	private String username;
	
	/** Employee's digital signature */
	private String digitalSignature;
	
	/** The service's fiscal code*/
	private String fcService;
	
	/** The date when user does the purchase */
	private Timestamp date;
	
	/** Say if the purchase is a proposal or not */
	private boolean proposal;
	
	/** The total price */
	private float price;
	
	/** The date of the delivery */
	private Date deliveryDate;
	
	/** Says if purchase represent a real value state or not */
	private boolean isNull;

	/**
	 * 
	 * @param winesQuantity
	 * @param username
	 * @param digitalSignature
	 * @param fcService
	 * @param date
	 * @param proposal
	 * @param price
	 * @param deliveryDate
	 */
	public Purchase(HashMap<Integer, Integer> winesQuantity, String username, String digitalSignature, String fcService, Timestamp date, boolean proposal, float price, Date deliveryDate)
	{
		this.winesQuantity = winesQuantity;
		this.username = username;
		this.digitalSignature = digitalSignature;
		this.fcService = fcService;
		this.date = date;
		this.proposal = proposal;
		this.price = price;
		this.deliveryDate = deliveryDate;
		this.isNull = false;
	}
	
	public Purchase()
	{
		this.winesQuantity = new HashMap<Integer, Integer>();
		this.username = "corly";
		this.digitalSignature = null;
		this.fcService = null;
		this.date = null;
		this.proposal = true;
		this.price = (float) 12.7;
		this.deliveryDate = null;
		winesQuantity.put(3, 6);
		winesQuantity.put(5, 12);
		this.isNull = true;
	}

	/**
	 * 
	 * @return wines and wines quantity
	 */
	public HashMap<Integer, Integer> getWinesQuantity()
	{
		return winesQuantity;
	}

	/**
	 * set wines and wines quantity
	 * @param winesQuantity
	 */
	public void setWinesQuantity(HashMap<Integer, Integer> winesQuantity)
	{
		this.winesQuantity = winesQuantity;
	}

	/**
	 * 
	 * @return username of the user
	 */
	public String getUsername()
	{
		return username;
	}

	/**
	 * 
	 * @return digital dignature of the purchase
	 */
	public String getDigitalSignature()
	{
		return digitalSignature;
	}

	/**
	 * 
	 * @return the fc service of the purchase
	 */
	public String getFcService()
	{
		return fcService;
	}

	/**
	 * 
	 * @return the date of the purchase
	 */
	public Timestamp getDate()
	{
		return date;
	}
	
	/**
	 * Set the date of the purchase
	 * @param ts Timestamp to set
	 */
	public void setDate(Timestamp ts)
	{
		this.date = ts;
	}

	/**
	 * 
	 * @return the price of the purchase
	 */
	public float getPrice()
	{
		return price;
	}

	/**
	 * 
	 * @return Delivery date of the purchase
	 */
	public Date getDeliveryDate()
	{
		return deliveryDate;
	}
	
	/**
	 * 
	 * @return True if purchase is null, false otherwise
	 */
	public boolean isNull()
	{
		return isNull;
	}
	
	/**
	 * 
	 * @return True if it's purchase proposal, false otherwise
	 */
	public boolean isPurchaseProposal()
	{
		return this.proposal;
	}
	
	/**
	 * 
	 * @return True if it's signed purchase proposal, false otherwise
	 */
	public boolean isSignedPurchaseProposal()
	{
		return this.isPurchaseProposal() && this.digitalSignature != null;
	}
	
	/**
	 * 
	 * @return True if it's unisgned purchase proposal, false otherwise
	 */
	public boolean isUnsignedPurchaseProposal()
	{
		return this.isPurchaseProposal() && this.digitalSignature == null;
	}

	@Override
	public String toString()
	{
		return "Purchase [winesQuantity=" + winesQuantity + ", username=" + username + ", digitalSignature="
				+ digitalSignature + ", fcService=" + fcService + ", date=" + date + ", proposal=" + proposal
				+ ", price=" + price + ", deliveryDate=" + deliveryDate + "]";
	}
	
	
}
