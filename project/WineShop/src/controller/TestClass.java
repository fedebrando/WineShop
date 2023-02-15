package controller;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Account;
import model.Purchase;
import model.User;
import model.Wine;

class TestClass
{
	Client c;
	
	@BeforeEach
	void initTests() throws Exception
	{
		c = new Client();
		c.login("aFedeBrando", "ciccio");
	}
	
	@Test
	final void testLogin()
	{	
		assertEquals(c.login("aFedeBrando", "ciccio"), Account.ADMINISTRATOR);
		c.logout();
	}

	@Test
	final void testLogout()
	{
		assertTrue(c.logout());
	}

	@Test
	final void testChangePassword()
	{
		c.login("aFedeBrando", "ciccio");
		assertTrue(c.changePassword("aFedeBrando", "ciccio", "ciccio"));
		c.logout();
	}

	@Test
	final void testWines()
	{
		assertTrue(c.wines(null, null) instanceof ArrayList<Wine>);
		c.logout();
	}

	@Test
	final void testGetPromotions()
	{
		assertTrue(c.getPromotions() != null);
		c.logout();
	}

	@Test
	final void testAddToCart()
	{
		assertTrue(c.addToCart(1, 1));
		c.logout();
	}

	@Test
	final void testCustomers() 
	{
		assertTrue(c.customers(null) != null);
		c.logout();
	}

	@Test
	final void testResetEmployee() 
	{
		User u = new User();
		c.signIn(u);
		assertTrue(c.resetEmployee(u.getUsername()));
		c.logout();
	}

	@Test
	final void testDeleteEmployee() 
	{
		User u = new User();
		c.signIn(u);
		assertTrue(c.deleteEmployee(u.getUsername()));
		c.logout();
	}

	@Test
	final void testCreatePurchase() 
	{
		Purchase p = new Purchase();
		 assertTrue(c.createPurchase(p));
		 c.logout();
	}

	@Test
	final void testBuy() 
	{
		assertTrue(c.buy());
		c.logout();
	}

	@Test
	final void testGetPurchaseProposal() 
	{
		assertTrue(c.getPurchaseProposal() instanceof Purchase);
		c.logout();
	}

	@Test
	final void testSendPurchaseProposal() 
	{
		Purchase p = new Purchase();
		assertTrue(c.sendPurchaseProposal(p));
		c.logout();
	}

	@Test
	final void testEmployees() 
	{
		c.login("aFedeBrando", "ciccio");
		assertTrue(c.employees() != null);
		c.logout();
	}

	@Test
	final void testUserIsPresent() 
	{
		c.login("aFedeBrando", "ciccio");
		assertTrue(c.userIsPresent(Account.ADMINISTRATOR, "aFedeBrando"));
		c.logout();
	}

	@Test
	final void testCart() 
	{
		assertTrue(c.cart() instanceof ArrayList<Wine>);
		c.logout();
	}

	@Test
	final void testRemoveFromCart()
	{
		c.addToCart(1, 1);
		assertTrue(c.removeFromCart(1));
		c.logout();
	}

	@Test
	final void testDeletePurchaseProposal() 
	{
		Timestamp ts = Timestamp.valueOf("1800-02-02");
		assertTrue(c.deletePurchaseProposal(ts));
		c.logout();
	}
}
