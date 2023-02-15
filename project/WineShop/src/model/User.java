
package model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class User, which represent information about a user
 * @author Federico Brandini, Federico Putamorsi
 * @version 15.12.2022
 */
public class User implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	/** The user's name */
	private String name;
	
	/** The user's surname */
	private String surname;
	
	/** User's fiscal code */
	private String fiscalCode;
	
	/** User's telephone number */
	private String telephone;

	/** User's address */
	private String address;
	
	/** The user's username */
	private String username;
	
	/** The password Hash of user */
	private String passwordHash;	
	
	/** Account type */
	private Account type;

	/**
	 * Instatiates a user using passed values
	 * @param name The user's name
	 * @param surname The user's surname
	 * @param fiscalCode User's fiscal code
	 * @param telephone User's telephone number
	 * @param address User's address
	 * @param username The user's username
	 * @param passwordHash The password Hash of user
	 * @param type Account type
	 */
	public User(String name, String surname, String fiscalCode, String telephone, String address, String username, String passwordHash, Account type)
	{
		super();
		this.name = name;
		this.surname = surname;
		this.fiscalCode = fiscalCode;
		this.telephone = telephone;
		this.address = address;
		this.username = username;
		this.passwordHash = passwordHash;
		this.type = type;
	}
	
	public User()
	{
		super();
		this.name = "ciccio";
		this.surname = "pocciccio";
		this.fiscalCode = "";
		this.telephone = "";
		this.address = "";
		this.username = "corly";
		this.passwordHash = DataAccess.SHA3_512("1234");
		this.type = Account.EMPLOYEE;
	}
	
	/**
	 * @return The user's name
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * @return The user's surname
	 */
	public String getSurname()
	{
		return surname;
	}
	
	/**
	 * @return User's fiscal code
	 */
	public String getFiscalCode()
	{
		return fiscalCode;
	}
	
	/**
	 * @return User's telephone number
	 */
	public String getTelephone()
	{
		return telephone;
	}
	
	/**
	 * @return User's address
	 */
	public String getAddress()
	{
		return address;
	}
	
	/**
	 * @return User's username
	 */
	public String getUsername()
	{
		return username;
	}
	
	/**
	 * @return User's password Hash
	 */
	public String getPasswordHash()
	{
		return passwordHash;
	}
	
	/**
	 * Set current password hash with received value
	 * @param passwordHash New password hash
	 */
	public void setPasswordHash(String passwordHash)
	{
		this.passwordHash = passwordHash;
	}
	
	/**
	 * @return Type of the account
	 */
	public Account getType()
	{
		return type;
	}

	@Override
	public String toString()
	{
		return "User [name=" + name + ", surname=" + surname + ", fiscalCode=" + fiscalCode + ", telephone=" + telephone
				+ ", address=" + address + ", username=" + username + ", passwordHash=" + passwordHash
				+ ", type=" + type + "]";
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(address, fiscalCode, name, passwordHash, surname, telephone, type, username);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(address, other.address) && Objects.equals(fiscalCode, other.fiscalCode)
				&& Objects.equals(name, other.name) && Objects.equals(passwordHash, other.passwordHash)
				&& Objects.equals(surname, other.surname) && Objects.equals(telephone, other.telephone)
				&& type == other.type && Objects.equals(username, other.username);
	}
}
