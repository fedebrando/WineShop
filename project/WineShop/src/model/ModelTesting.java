
package model;

import java.util.ArrayList;

public class ModelTesting
{
	public static void main(String[] args)
	{
		// select
		ArrayList<Wine> wines = DataAccess.allWines();
		
		for (Wine w : wines)
			System.out.println(w);
		
		// update
		System.out.println("Vini modificati: " + DataAccess.increaseListPriceWines());
		
		wines = DataAccess.allWines();
		
		for (Wine w : wines)
			System.out.println(w);
		
		// insert
		Wine w = new Wine(0, "Ciccio", "Ciccio", "Ciccio", 1000, "Ciccio", "Ciccio", 1.00f, 1);
		System.out.println(DataAccess.insertWine(w) ? "Ok" : "Oops...");
		
		wines = DataAccess.allWines();
		
		for (Wine w1 : wines)
			System.out.println(w1);
		
		// delete
		System.out.println(DataAccess.deleteWine(11) ? "DEL OK" : "DEL NO");
		wines = DataAccess.allWines();
		
		for (Wine w1 : wines)
			System.out.println(w1);
	}
}
