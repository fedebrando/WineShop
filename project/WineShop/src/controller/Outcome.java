package controller;

/**
 * Enum Outcome, which represents different Outcome ( Success, Error, Insufficient_Privileges)
 * @author Federico Brandini, Federico Putamorsi
 * @version 14.01.2023
 */
public enum Outcome
{
	SUCCESS("success"),
	ERROR("error"),
	INSUFFICIENT_PRIVILEGES("insufficient privileges");

	private String strOutcome;
	
	Outcome(String strOutcome)
	{
		this.strOutcome = strOutcome;
	}
	
	/**
	 * @return The string description of the outcome
	 */
	public String getStrOutcome()
	{
		return this.strOutcome;
	}
}
