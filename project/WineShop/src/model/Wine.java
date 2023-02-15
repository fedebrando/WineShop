
package model;

import java.io.Serializable;

/**
 * Class Wine, which represent a kind of wine
 * @author Federico Brandini, Federico Putamorsi
 * @version 15.12.2022
 */
public class Wine implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	/** The identifier of the wine */
	private int id;
	
	/** The wine's name */
	private String name;
	
	/** The producer of wine */
	private String producer;
	
	/** The origin of wine */
	private String origin;
	
	/** Bottling year */
	private int year;
	
	/** Other information */
	private String notes;
	
	/** The production vineyard */
	private String vines;
	
	/** List price */
	private float price;
	
	/** Bottles available quantity */
	private int availableQuantity;

	/**
	 * Instantiates a kind of wine
	 * @param id The identifier of the wine
	 * @param name The wine's name
	 * @param producer The producer of wine
	 * @param origin The origin of wine
	 * @param year Bottling year
	 * @param notes Other information
	 * @param vines The production vineyard
	 * @param price List price
	 * @param availableQuantity Bottles available quantity
	 */
	public Wine(int id, String name, String producer, String origin, int year, String notes, String vines, float price, int availableQuantity)
	{
		this.id = id;
		this.name = name;
		this.producer = producer;
		this.origin = origin;
		this.year = year;
		this.notes = notes;
		this.vines = vines;
		this.price = price;
		this.availableQuantity = availableQuantity;
	}

	/**
	 * @return The identifier of the wine
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * @return The wine's name
	 */
	public String getName() 
	{
		return name;
	}

	/**
	 * @return The producer of wine
	 */
	public String getProducer()
	{
		return producer;
	}

	/**
	 * @return The origin of wine
	 */
	public String getOrigin()
	{
		return origin;
	}

	/**
	 * @return Bottling year
	 */
	public int getYear()
	{
		return year;
	}

	/**
	 * @return Other information about wine
	 */
	public String getNotes()
	{
		return notes;
	}

	/**
	 * @return The production vineyard
	 */
	public String getVines()
	{
		return vines;
	}

	/**
	 * @return The price of the wine
	 */
	public float getPrice()
	{
		return price;
	}

	/**
	 * @return Bottles available quantity
	 */
	public int getAvailableQuantity()
	{
		return availableQuantity;
	}

	@Override
	public String toString()
	{
		return "Wine [id=" + id + ", name=" + name + ", producer=" + producer + ", origin=" + origin + ", year=" + year
				+ ", notes=" + notes + ", vines=" + vines + ", price=" + price + ", availableQuantity="
				+ availableQuantity + "]";
	}
}
