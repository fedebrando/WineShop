
package model;

/**
 * Enum Account, which represents different Account Type (eg. Administrator, Employee, Customer, Foreign)
 * @author Federico Brandini, Federico Putamorsi
 * @version 14.01.2023
 */
public enum Account
{
	CUSTOMER("Customer"),
	EMPLOYEE("Employee"),
	ADMINISTRATOR("Administrator"),
	FOREIGN("Foreign");
	
	private String strType;

	Account(String strType)
	{
		this.strType = strType;
	}
	
	/**
	 * @return The string type of the account
	 */
	public String getStrType()
	{
		return this.strType;
	}
}
