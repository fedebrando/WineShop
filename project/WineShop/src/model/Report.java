package model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Class Report, which represent a monthly report of an employee
 * @author Federico Brandini, Federico Putamorsi
 * @version 15.01.2022
 */
public class Report implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	/** The income of the period */
	private float income;
	
	/** The expenses of the period */
	private float expenses;
	
	/** The total sold bottles in the period */
	private int soldBottle;
	
	/** The available bottle */
	private int availableBottle;
	
	/** The list of sold wines */
	private HashMap<Integer, Integer> soldWine;
	
	
	/**
	 * 
	 * @param income
	 * @param expenses
	 * @param soldBottle
	 * @param availableBottle
	 * @param soldWine
	 */
	public Report(float income, float expenses, int soldBottle, int availableBottle,
			HashMap<Integer, Integer> soldWine)
	{
		super();
		this.income = income;
		this.expenses = expenses;
		this.soldBottle = soldBottle;
		this.availableBottle = availableBottle;
		this.soldWine = soldWine;
	}

	/**
	 * 
	 * @return The income of the period
	 */
	public float getIncome()
	{
		return income;
	}

	/**
	 * 
	 * @return The expenses of the period
	 */
	public float getExpenses()
	{
		return expenses;
	}

	/**
	 * 
	 * @return The total sold bottles in the period 
	 */
	public int getSoldBottle()
	{
		return soldBottle;
	}
	
	/**
	 * 
	 * @return The available bottle
	 */
	public int getAvailableBottle()
	{
		return availableBottle;
	}

	/**
	 * 
	 * @return The list of sold wines
	 */
	public HashMap<Integer, Integer> getSoldWine()
	{
		return soldWine;
	}

	@Override
	public String toString()
	{
		return "Report [income=" + income + ", expenses=" + expenses + ", soldBottle=" + soldBottle
				+ ", availableBottle=" + availableBottle + ", soldWine=" + soldWine + "]";
	}
}
