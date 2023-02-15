package model;

/**
 * Enum PurchaseType, which represents different Purchase Type
 * @author Federico Brandini, Federico Putamorsi
 * @version 14.01.2023
 */

public enum PurchaseType
{
	SALES_ORDER("salesOrder"),
	PURCHASE_PROPOSAL("purchaseProposal"),
	PURCHASE_ORDER("purchaseOrder"),
	ALL("");

	private String strPurchase;
	
	PurchaseType(String strPurchase)
	{
		this.strPurchase = strPurchase;
	}
	
	/**
	 * @return The string description of the purchase type
	 */
	public String getStrPurchaseType()
	{
		return this.strPurchase;
	}
}
