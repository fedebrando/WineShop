
package model;

import java.io.Serializable;

/**
 * Class Service, which represent a supplier or a courier
 * @author Federico Brandini, Federico Putamorsi
 * @version 15.12.2022
 */
public class Service implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	/** The identifier of the service */
	private int id;
	
	/** The name who will do the service */
	private String name;
	
	/** The surname who will do the service */
	private String surname;
	
	/** The fiscal code who will do the service */
	private String fiscalCode;
	
	/** The telephone number who will do the service */
	private String telephone;

	/** The address who will do the service */
	private String companyAddress;

	/**
	 * Instantiates a service using passed parameters
	 * @param id The identifier of the service
	 * @param name The name who will do the service
	 * @param surname The surname who will do the service
	 * @param fiscalCode The fiscal code who will do the service
	 * @param telephone The telephone number who will do the service
	 * @param companyAddress The address who will do the service
	 */
	public Service(int id, String name, String surname, String fiscalCode, String telephone, String companyAddress)
	{
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.fiscalCode = fiscalCode;
		this.telephone = telephone;
		this.companyAddress = companyAddress;
	}

	/**
	 * @return The identifier of the service
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * @return The name who will do the service
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return The surname who will do the service
	 */
	public String getSurname()
	{
		return surname;
	}

	/**
	 * @return The fiscal code who will do the service
	 */
	public String getFiscalCode()
	{
		return fiscalCode;
	}

	/**
	 * @return The telephone number who will do the service
	 */
	public String getTelephone()
	{
		return telephone;
	}

	/**
	 * @return The address who will do the service
	 */
	public String getCompanyAddress()
	{
		return companyAddress;
	}

	@Override
	public String toString()
	{
		return "Service [id=" + id + ", name=" + name + ", surname=" + surname + ", fiscalCode=" + fiscalCode
				+ ", telephone=" + telephone + ", companyAddress=" + companyAddress + "]";
	}
}
