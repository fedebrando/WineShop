package view;

import java.net.URL;
import java.sql.Timestamp;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Purchase;
import model.PurchaseType;
import model.Wine;

/**
 * Class CustomerController, which controls a customer view
 * @author Federico Brandini, Federico Putamorsi
 * @version 14.02.2023
 */
public class CustomerController extends Controller implements Initializable
{
	@FXML
	private GridPane paneWines;
	@FXML
	private Tab tabWines;
	@FXML
	private PasswordField pswCurrentPsw;
	@FXML
	private PasswordField pswNewPsw;
	@FXML
	private Button btnChangePsw;
	@FXML
	private Label lblPromotions;
	@FXML
	private TextField txtWineName;
	@FXML
	private Spinner<Integer> spnWineYear;
	@FXML
	private Tab tabCart;
	@FXML
	private ImageView ivCart;
	@FXML
	private GridPane paneCart;
	@FXML
	private Label lblProductsNumber;
	@FXML
	private Label lblTotalPrice;
	@FXML
	private Button btnBuy;
	@FXML
	private Tab tabSalesOrder;
	@FXML
	private VBox vbSalesOrder;
	@FXML
	private Tab tabPurchaseProposal;
	@FXML
	private VBox vbPurchaseProposal;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		// wines
		this.tabWines.setOnSelectionChanged(e -> {
			updateWineBoxes();
			updatePromotions();
		});
		this.spnWineYear.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(Year.now().getValue() - 200, Year.now().getValue()));
		this.spnWineYear.getValueFactory().setValue(Year.now().getValue() - 4);
		
		// cart
		this.ivCart.setImage(new Image(getClass().getClassLoader().getResource("cart.png").toString()));
		this.tabCart.setOnSelectionChanged(e -> {
			updateCart();
		});
		
		// sales order
		this.tabSalesOrder.setOnSelectionChanged(e -> {
			updateSalesOrder();
		});
		
		// purchase proposal
		this.tabPurchaseProposal.setOnSelectionChanged(e -> {
			updatePurchaseProposal();
		});

		this.chkConsiderHandler(null);
	}
	
	private void updateWineBoxes()
	{
		ArrayList<Wine> wines = this.c.wines(this.txtWineName.getText(), (this.spnWineYear.isDisable() ? null : this.spnWineYear.getValue()));
		int numRows = paneWines.getRowCount();
		int numCols = paneWines.getColumnCount();
		paneWines.gridLinesVisibleProperty();
		
		paneWines.getChildren().clear();
		for (int i = 0; i < wines.size() && i < numRows*numCols; i++)
			paneWines.add(new WineBox(wines.get(i), false, e -> {btnAddHandler(e);}), i % numCols, i / numCols);
		paneWines.setMinSize(400, 200);
	}
	
	private void updatePromotions()
	{
		this.lblPromotions.setText(c.getPromotions());
	}
	
	private void updateSalesOrder()
	{
		ArrayList<Purchase> soList = this.c.purchases(PurchaseType.SALES_ORDER, null, null);
		
		this.vbSalesOrder.getChildren().clear();
		for (Purchase so : soList)
			this.vbSalesOrder.getChildren().add(new SalesOrderBox(so));
	}
	
	private void updatePurchaseProposal()
	{
		ArrayList<Purchase> ppList = this.c.purchases(PurchaseType.PURCHASE_PROPOSAL, null, null);
		
		this.vbPurchaseProposal.getChildren().clear();
		for (Purchase pp : ppList)
			this.vbPurchaseProposal.getChildren().add(new PurchaseProposalBox(pp, e -> {btnPPAcceptHandler(e);}, e -> {btnPPRefuseHandler(e);}));
	}
	
	private void updateCart()
	{
		ArrayList<Wine> wines = this.c.cart();
		int numRows = paneCart.getRowCount();
		int numCols = paneCart.getColumnCount();
		int numProduct = 0;
		float totalPrice = 0;
		paneCart.gridLinesVisibleProperty();
		
		paneCart.getChildren().clear();
		for (int i = 0; i < wines.size() && i < numRows*numCols; i++)
		{
			paneCart.add(new WineBox(wines.get(i), true, e -> {btnRemoveHandler(e);}), i % numCols, i / numCols);
			numProduct += wines.get(i).getAvailableQuantity();
			totalPrice += wines.get(i).getAvailableQuantity() * wines.get(i).getPrice();
		}
		paneCart.setMinSize(400, 200);
		
		// update 'buy' section
		lblTotalPrice.setText("€ " + String.format("%.2f", totalPrice));
		lblProductsNumber.setText(String.valueOf(numProduct));
		
		if (numProduct == 0)
			this.btnBuy.setDisable(true);
		else
			this.btnBuy.setDisable(false);
	}
	
	@FXML
	protected void btnChangePswHandler(final ActionEvent e)
	{
		String outcomeTxt;
		AlertType atype;
		
		if (c.changePassword(c.getUsername(), pswCurrentPsw.getText(), pswNewPsw.getText()))
		{
			outcomeTxt = "The password is changed correctly.";
			atype = AlertType.INFORMATION;
		}
		else
		{
			outcomeTxt = "Error: current password is wrong.";
			atype = AlertType.ERROR;
		}
		Utilities.messageDialog(atype, "Outcome", "Outcome", outcomeTxt);
		this.pswCurrentPsw.setText("");
		this.pswNewPsw.setText("");
	}
	
	@FXML
	protected void chkConsiderHandler(final ActionEvent e)
	{
		this.spnWineYear.setDisable(!this.spnWineYear.isDisable());
	}
	
	@FXML
	protected void btnSearchWinesHandler(final ActionEvent e)
	{
		this.updateWineBoxes();
	}
	
	@SuppressWarnings("unchecked")
	@FXML
	protected void btnRemoveHandler(final ActionEvent e)
	{
		ButtonValue<Wine> btn = (ButtonValue<Wine>) e.getSource();
		this.c.removeFromCart(btn.getValue().getId());
		updateCart();
	}
	
	@SuppressWarnings("unchecked")
	@FXML
	protected void btnAddHandler(final ActionEvent e)
	{
		ButtonValue<Wine> btn = (ButtonValue<Wine>) e.getSource();
		Wine w = btn.getValue();
		this.c.addToCart(w.getId(), ((ChoiceBox<Integer>)((HBox) btn.getParent().getChildrenUnmodifiable().get(3)).getChildren().get(1)).getValue());
		updateCart();
	}
	
	@SuppressWarnings("unchecked")
	@FXML
	protected void btnBuyHandler(final ActionEvent e)
	{
		boolean outcome;
		
		if (!Utilities.messageDialog(AlertType.CONFIRMATION, "Confirm purchase", "Do you want to proceed with the purchase?", "If you confirm, the purchase wille be executed."))
			return;
		outcome = this.c.buy();
		if (outcome)
			Utilities.messageDialog(AlertType.INFORMATION, "Outcome", "Success", "The products were bought.");
		else
			Utilities.messageDialog(AlertType.ERROR, "Outcome", "Error", "An error was occured.");
		updateCart();
		if (!this.paneCart.getChildren().isEmpty())
		{
			if (Utilities.messageDialog(AlertType.CONFIRMATION, "Request", "Purchase proposal", "Wines remain in the shopping cart are not available with their quantity.\nDo you want to make a purchase proposal?"))
			{
				HashMap<Integer, Integer> winesQuantity = new HashMap<Integer, Integer>();
				Purchase pp = new Purchase(winesQuantity, this.c.getUsername(), null, null, null, true, 0, null);
				ObservableList<Node> wineInCart = (ObservableList<Node>) this.paneCart.getChildren();
				Wine w;
				
				for (Node n : wineInCart)
				{
					w = ((ButtonValue<Wine>)(((VBox)((WineBox)n).getChildren().get(0)).getChildren().get(4))).getValue();
					winesQuantity.put(w.getId(), w.getAvailableQuantity());
				}
				if (this.c.createPurchase(pp))
				{
					for (Integer idWine : winesQuantity.keySet())
						c.removeFromCart(idWine);
					Utilities.messageDialog(AlertType.INFORMATION, "Success", null, "The purchase proposal is correctly sent.");
				}
				else
					Utilities.messageDialog(AlertType.ERROR, "Error", null, "An error was occured.");
				updateCart();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void btnPPAcceptHandler(final ActionEvent e)
	{
		Purchase pp = ((ButtonValue<Purchase>)e.getSource()).getValue();
		Purchase so = new Purchase(pp.getWinesQuantity(), this.c.getUsername(), null, null, null, false, 0, null);
		
		this.c.createPurchase(so);
		this.c.deletePurchaseProposal(pp.getDate());
		this.updatePurchaseProposal();
	}
	
	@SuppressWarnings("unchecked")
	protected void btnPPRefuseHandler(final ActionEvent e)
	{
		Timestamp ts = ((ButtonValue<Timestamp>)e.getSource()).getValue();
		
		this.c.deletePurchaseProposal(ts);
		this.updatePurchaseProposal();
	}
}
