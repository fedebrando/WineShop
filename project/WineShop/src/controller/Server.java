
package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import model.DataAccess;
import model.Purchase;
import model.PurchaseType;

/**
 * Class Server, the Main Server of the application
 * @author Federico Brandini, Federico Putamorsi
 * @version 10.01.2023
 */
public class Server
{
	private static final int MAXPOOL = 100;
	private static final long IDLETIME = 5000;
	
	/** Server's socket */
	private ServerSocket srvSock;
	
	/** Server pool */
	private ThreadPoolExecutor pool;
	
	/** Queue with current Purchase Proposal to manage */
	private ArrayDeque<Purchase> purchaseProposal;
	
	/** Map with wines treshold */
	private HashMap<Integer, Integer> winesThreshold;
	
	public Server() throws IOException
	{
		ArrayList<Purchase> purchases;
		
		this.srvSock = new ServerSocket(5670);
		this.purchaseProposal = new ArrayDeque<Purchase>();
		purchases = DataAccess.filterPurchase(PurchaseType.PURCHASE_PROPOSAL, DataAccess.FAR, Date.valueOf(LocalDate.now()));
		purchases.removeIf(p -> {return p.isSignedPurchaseProposal();});
		this.purchaseProposal.addAll(purchases);
		this.winesThreshold = DataAccess.quantityThresholds(); 
	}

	public void start()
	{
		this.pool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), MAXPOOL, IDLETIME, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		boolean error = false;
		Socket s;
		
		do
		{
			try
			{
				System.out.println("Listening...");
				s = srvSock.accept();
				System.out.println("Connection from " + s.getInetAddress() + ":" + s.getPort());
				this.pool.execute(new ServerThread(this, s));
			}
			catch (Exception e)
			{
				error = true;
			}
		} while (!error);
		
		this.pool.shutdown();
	}
	
	public void stop()
	{
		try
		{
			this.srvSock.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Enqueue a new Purchase Proposal
	 * @param p Purchase to enqueue
	 * @return True if the operation was successful, false otherwise
	 */
	public boolean enqueuePurchaseProposal(Purchase p)
	{
		return this.purchaseProposal.add(p);
	}
	
	/**
	 * Dequeue a Purchase Proposal
	 * @return purchase removed
	 */
	public Purchase dequeuePurchaseProposal()
	{
		if (this.purchaseProposal.isEmpty())
			return null;
		return this.purchaseProposal.removeFirst();
	}
	
	/**
	 * Get the threshold
	 * @param idWine Id of wine
	 * @return Threshold of selected wine
	 */
	public int getThresholdFromIDWine(int idWine)
	{
		return this.winesThreshold.get(idWine);
	}
	
	public static void main(String[] args) throws IOException
	{
		new Server().start();
	}
}
