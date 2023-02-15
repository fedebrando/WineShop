package view;

import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import org.controlsfx.control.CheckComboBox;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import model.Account;
import model.Purchase;
import model.PurchaseType;
import model.Report;
import model.User;
import model.Wine;

/**
 * Class AdministratorEmployeeController, which controls two different gui parts (admin and employee)
 * @author Federico Brandini, Federico Putamorsi
 * @version 14.02.2023
 */
public class AdministratorEmployeeController extends Controller implements Initializable
{
	private Timer timer;
	private Purchase purchaseProposal;
	@FXML
	private TabPane tpAdministratorEmployee;
	@FXML
	private Tab tabWines;
	@FXML
	private PasswordField pswCurrentPsw;
	@FXML
	private PasswordField pswNewPsw;
	@FXML
	private Button btnChangePsw;
	@FXML
	private TextField txtEmpName;
	@FXML
	private TextField txtEmpSurname;
	@FXML
	private TextField txtEmpFiscalCode;
	@FXML
	private TextField txtEmpTelephone;
	@FXML
	private TextField txtEmpAddress;
	@FXML
	private TextField txtEmpUsername;
	@FXML
	private TextField txtEmpPassword;
	@FXML
	private Tab tabEmpResetDelete;
	@FXML
	private TableView<User> tvEmployeesResetCancel;
	@FXML
	private TableColumn<User, String> colSurname;
	@FXML
	private TableColumn<User, String> colName;
	@FXML
	private TableColumn<User, String> colUsername;
	@FXML
	private TextField txtRDUsername;
	private ObservableList<User> employeesList = FXCollections.observableArrayList();
	@FXML
	private Tab tabWinesOnSale;
	@FXML
	private TextField txtWineName;
	@FXML
	private Spinner<Integer> spnWineYear;
	@FXML
	private TableView<Wine> tvWines;
	@FXML
	private TableColumn<Wine, String> colWName;
	@FXML
	private TableColumn<Wine, String> colWProducer;
	@FXML
	private TableColumn<Wine, String> colWOrigin;
	@FXML
	private TableColumn<Wine, Integer> colWYear;
	@FXML
	private TableColumn<Wine, String> colWNotes;
	@FXML
	private TableColumn<Wine, String> colWVines;
	@FXML
	private TableColumn<Wine, Float> colWPrice;
	@FXML
	private TableColumn<Wine, Integer> colWAvailableQuantity;
	private ObservableList<Wine> winesList = FXCollections.observableArrayList();
	@FXML
	private TableView<Purchase> tvSalesOrder;
	@FXML
	private Tab tabWinesSalesOrder;
	@FXML
	private DatePicker dateSOStartDate;
	@FXML
	private DatePicker dateSOEndDate;
	@FXML
	private TableColumn<Purchase, Date> colSODate;
	@FXML
	private TableColumn<Purchase, String> colSOUser;
	@FXML
	private TableColumn<Purchase, String> colSODigitalSignature;
	@FXML
	private TableColumn<Purchase, Float> colSOPrice;
	@FXML
	private TableColumn<Purchase, Date> colSODeliveryDate;
	@FXML
	private TableColumn<Purchase, String> colSOFCService;
	
	private ObservableList<Purchase> salesOrderList = FXCollections.observableArrayList();
	
	@FXML
	private TableView<Purchase> tvSalesOrderToSign;
	@FXML
	private Tab tabSalesOrderToSign;
	@FXML
	private DatePicker dateSOTSDeliveryDate;
	@FXML
	private TableColumn<Purchase, Date> colSOTSDate;
	@FXML
	private TableColumn<Purchase, String> colSOTSUser;
	@FXML
	private TableColumn<Purchase, Float> colSOTSPrice;
	@FXML
	private TableColumn<Purchase, String> colSOTSFCService;
	
	private ObservableList<Purchase> salesOrderToSignList = FXCollections.observableArrayList();
	@FXML
	private Button btnSOTS;
	
	@FXML
	private CheckComboBox<String> ccbOrdersToSign;
	
	@FXML
	private TableView<Purchase> tvPurchaseOrder;
	@FXML
	private Tab tabWinesPurchaseOrder;
	@FXML
	private DatePicker datePOStartDate;
	@FXML
	private DatePicker datePOEndDate;
	@FXML
	private TableColumn<Purchase, Date> colPODate;
	@FXML
	private TableColumn<Purchase, String> colPOUser;
	@FXML
	private TableColumn<Purchase, String> colPODigitalSignature;
	@FXML
	private TableColumn<Purchase, Float> colPOPrice;
	@FXML
	private TableColumn<Purchase, Date> colPODeliveryDate;
	@FXML
	private TableColumn<Purchase, String> colPOFCService;
	
	private ObservableList<Purchase> purchaseOrderList = FXCollections.observableArrayList();
	
	@FXML
	private TableView<Purchase> tvPurchaseProposal;
	@FXML
	private Tab tabWinesPurchaseProposal;
	@FXML
	private DatePicker datePPStartDate;
	@FXML
	private DatePicker datePPEndDate;
	@FXML
	private TableColumn<Purchase, Date> colPPDate;
	@FXML
	private TableColumn<Purchase, String> colPPUser;
	@FXML
	private TableColumn<Purchase, String> colPPDigitalSignature;
	@FXML
	private TableColumn<Purchase, Float> colPPPrice;
	@FXML
	private TableColumn<Purchase, Date> colPPDeliveryDate;
	@FXML
	private TableColumn<Purchase, String> colPPFCService;
	
	private ObservableList<Purchase> purchaseProposalList = FXCollections.observableArrayList();
	
	@FXML
	private TableView<Purchase> tvRPurchaseProposal;
	@FXML
	private Tab tabCustomersRRP;
	@FXML
	private DatePicker dateRPPStartDate;
	@FXML
	private DatePicker dateRPPEndDate;
	@FXML
	private TableColumn<Purchase, Date> colRPPDate;
	@FXML
	private TableColumn<Purchase, String> colRPPUser;
	@FXML
	private TableColumn<Purchase, String> colRPPDigitalSignature;
	@FXML
	private TableColumn<Purchase, Float> colRPPPrice;
	@FXML
	private TableColumn<Purchase, Date> colRPPDeliveryDate;
	@FXML
	private TableColumn<Purchase, String> colRPPFCService;
	
	private ObservableList<Purchase> RpurchaseProposalList = FXCollections.observableArrayList();
	
	@FXML
	private TableView<User> tvCustomers;
	@FXML
	private Tab tabCustomersList;
	@FXML
	private TextField txtCustomersSurname;
	@FXML
	private TableColumn<User, String> colCUsername;
	@FXML
	private TableColumn<User, String> colCSurname;
	@FXML
	private TableColumn<User, String> colCName;
	@FXML
	private TableColumn<User, String> colCFiscalCode;
	@FXML
	private TableColumn<User, String> colCTelephone;
	@FXML
	private TableColumn<User, String> colCAddress;
	
	private ObservableList<User> customersList = FXCollections.observableArrayList();
	
	@FXML
	private Tab tabPaneService;
	
	@FXML
	private CheckComboBox<String> ccbWines;
	
	@FXML
	private ChoiceBox<Integer> cbQuantity;
	
	@FXML
	private Tab tabNotifications;
	@FXML
	private TableView<Wine> tvThreshold;
	@FXML
	private TableColumn<Wine, String> colTWine;
	@FXML
	private TableColumn<Wine, Integer> colTCurrentQuantity;
	private ObservableList<Wine> winesUnderThresholdList = FXCollections.observableArrayList();
	
	@FXML
	private Tab tabReport;
	@FXML
	private VBox vbAssesment;
	private ArrayList<ChoiceBox<Integer>> reportMarks;
	@FXML
	private DatePicker dateReportStart;
	@FXML
	private DatePicker dateReportEnd;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		// set purchase proposal to manage with unsignificant purchase
		this.purchaseProposal = new Purchase();
		
		// employees table
		colSurname.setCellValueFactory(new PropertyValueFactory<User, String>("surname"));
	    colName.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
	    colUsername.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
	    tvEmployeesResetCancel.setItems(employeesList);
		this.tabEmpResetDelete.setOnSelectionChanged(e -> {
			updateTableEmployeesRD();
		});
		
		// wines table
		this.spnWineYear.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(Year.now().getValue() - 200, Year.now().getValue()));
		this.spnWineYear.getValueFactory().setValue(Year.now().getValue() - 4);
		colWName.setCellValueFactory(new PropertyValueFactory<Wine, String>("name"));
		colWProducer.setCellValueFactory(new PropertyValueFactory<Wine, String>("producer"));
		colWOrigin.setCellValueFactory(new PropertyValueFactory<Wine, String>("origin"));
		colWYear.setCellValueFactory(new PropertyValueFactory<Wine, Integer>("year"));
		colWNotes.setCellValueFactory(new PropertyValueFactory<Wine, String>("notes"));
		colWVines.setCellValueFactory(new PropertyValueFactory<Wine, String>("vines"));
		colWPrice.setCellValueFactory(new PropertyValueFactory<Wine, Float>("price"));
		colWAvailableQuantity.setCellValueFactory(new PropertyValueFactory<Wine, Integer>("availableQuantity"));
		this.tvWines.setItems(this.winesList);
		this.tabWinesOnSale.setOnSelectionChanged(e -> {
			updateTableWines();
		});
		colWPrice.setCellFactory(c -> new TableCell<>() {
		    @Override
		    protected void updateItem(Float balance, boolean empty)
		    {
		        super.updateItem(balance, empty);
		        if (balance == null || empty)
		        {
		            setText(null);
		        }
		        else
		        {
		            setText("€ " + String.format("%.2f", balance.doubleValue()));
		        }
		    }
		});
		
		// Sales Order table
		this.dateSOStartDate.setValue(LocalDate.now());
		this.dateSOEndDate.setValue(LocalDate.now());
		this.colSODate.setCellValueFactory(new PropertyValueFactory<Purchase, Date>("date"));
		this.colSOUser.setCellValueFactory(new PropertyValueFactory<Purchase, String>("username"));
		this.colSODigitalSignature.setCellValueFactory(new PropertyValueFactory<Purchase, String>("digitalSignature"));
		this.colSOPrice.setCellValueFactory(new PropertyValueFactory<Purchase, Float>("price"));
		this.colSODeliveryDate.setCellValueFactory(new PropertyValueFactory<Purchase, Date>("deliveryDate"));
		this.colSOFCService.setCellValueFactory(new PropertyValueFactory<Purchase, String>("fcService"));
		this.tvSalesOrder.setItems(salesOrderList);
		this.tabWinesSalesOrder.setOnSelectionChanged(e -> {
			updateTableSalesOrder();
		});
		colSOPrice.setCellFactory(c -> new TableCell<>() {
		    @Override
		    protected void updateItem(Float balance, boolean empty)
		    {
		        super.updateItem(balance, empty);
		        if (balance == null || empty)
		        {
		            setText(null);
		        }
		        else
		        {
		            setText("€ " + String.format("%.2f", balance.doubleValue()));
		        }
		    }
		});
		
		// Purchase Order table
		this.datePOStartDate.setValue(LocalDate.now());
		this.datePOEndDate.setValue(LocalDate.now());
		this.colPODate.setCellValueFactory(new PropertyValueFactory<Purchase, Date>("date"));
		this.colPOUser.setCellValueFactory(new PropertyValueFactory<Purchase, String>("username"));
		this.colPODigitalSignature.setCellValueFactory(new PropertyValueFactory<Purchase, String>("digitalSignature"));
		this.colPOPrice.setCellValueFactory(new PropertyValueFactory<Purchase, Float>("price"));
		this.colPODeliveryDate.setCellValueFactory(new PropertyValueFactory<Purchase, Date>("deliveryDate"));
		this.colPOFCService.setCellValueFactory(new PropertyValueFactory<Purchase, String>("fcService"));
		this.tvPurchaseOrder.setItems(purchaseOrderList);
		this.tabWinesPurchaseOrder.setOnSelectionChanged(e -> {
			updateTablePurchaseOrder();
		});
		colPOPrice.setCellFactory(c -> new TableCell<>() {
		    @Override
		    protected void updateItem(Float balance, boolean empty)
		    {
		        super.updateItem(balance, empty);
		        if (balance == null || empty)
		        {
		            setText(null);
		        }
		        else
		        {
		            setText("€ " + String.format("%.2f", balance.doubleValue()));
		        }
		    }
		});
		
		// Purchase Proposal table
		this.datePPStartDate.setValue(LocalDate.now());
		this.datePPEndDate.setValue(LocalDate.now());
		this.colPPDate.setCellValueFactory(new PropertyValueFactory<Purchase, Date>("date"));
		this.colPPUser.setCellValueFactory(new PropertyValueFactory<Purchase, String>("username"));
		this.colPPDigitalSignature.setCellValueFactory(new PropertyValueFactory<Purchase, String>("digitalSignature"));
		this.colPPPrice.setCellValueFactory(new PropertyValueFactory<Purchase, Float>("price"));
		this.colPPDeliveryDate.setCellValueFactory(new PropertyValueFactory<Purchase, Date>("deliveryDate"));
		this.colPPFCService.setCellValueFactory(new PropertyValueFactory<Purchase, String>("fcService"));
		this.tvPurchaseProposal.setItems(purchaseProposalList);
		this.tabWinesPurchaseProposal.setOnSelectionChanged(e -> {
			updateTablePurchaseProposal();
		});
		colPPPrice.setCellFactory(c -> new TableCell<>() {
		    @Override
		    protected void updateItem(Float balance, boolean empty)
		    {
		        super.updateItem(balance, empty);
		        if (balance == null || empty)
		        {
		            setText(null);
		        }
		        else
		        {
		            setText("€ " + String.format("%.2f", balance.doubleValue()));
		        }
		    }
		});
		
		// Received Purchase Proposal table
		this.colRPPDate.setCellValueFactory(new PropertyValueFactory<Purchase, Date>("date"));
		this.colRPPUser.setCellValueFactory(new PropertyValueFactory<Purchase, String>("username"));
		this.colRPPDigitalSignature.setCellValueFactory(new PropertyValueFactory<Purchase, String>("digitalSignature"));
		this.colRPPPrice.setCellValueFactory(new PropertyValueFactory<Purchase, Float>("price"));
		this.colRPPDeliveryDate.setCellValueFactory(new PropertyValueFactory<Purchase, Date>("deliveryDate"));
		this.colRPPFCService.setCellValueFactory(new PropertyValueFactory<Purchase, String>("fcService"));
		this.tvRPurchaseProposal.setItems(RpurchaseProposalList);
		this.tabCustomersRRP.setOnSelectionChanged(e -> {
			updateTableRPurchaseProposal();
		});
		colRPPPrice.setCellFactory(c -> new TableCell<>() {
		    @Override
		    protected void updateItem(Float balance, boolean empty)
		    {
		        super.updateItem(balance, empty);
		        if (balance == null || empty)
		        {
		            setText(null);
		        }
		        else
		        {
		            setText("€ " + String.format("%.2f", balance.doubleValue()));
		        }
		    }
		});
		

		// Customers table
		this.colCUsername.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
		this.colCSurname.setCellValueFactory(new PropertyValueFactory<User, String>("surname"));
		this.colCName.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
		this.colCFiscalCode.setCellValueFactory(new PropertyValueFactory<User, String>("fiscalCode"));
		this.colCTelephone.setCellValueFactory(new PropertyValueFactory<User, String>("telephone"));
		this.colCAddress.setCellValueFactory(new PropertyValueFactory<User, String>("address"));
		this.tvCustomers.setItems(customersList);
		this.tabCustomersList.setOnSelectionChanged(e -> {
			updateTableCustomers();
		});
		
		// init purchase order
		this.tabPaneService.setOnSelectionChanged(e -> {
			updateccbWines();
			updatecbQuantity();
		});
		
		// Sales Order To Sign table
		this.dateSOTSDeliveryDate.setValue(LocalDate.now());
		this.colSOTSDate.setCellValueFactory(new PropertyValueFactory<Purchase, Date>("date"));
		this.colSOTSUser.setCellValueFactory(new PropertyValueFactory<Purchase, String>("username"));
		this.colSOTSPrice.setCellValueFactory(new PropertyValueFactory<Purchase, Float>("price"));
		this.colSOTSFCService.setCellValueFactory(new PropertyValueFactory<Purchase, String>("fcService"));
		this.tvSalesOrderToSign.setItems(salesOrderToSignList);
		this.tabSalesOrderToSign.setOnSelectionChanged(e -> {
			updateTableSalesOrderToSign();
		});
		colSOTSPrice.setCellFactory(c -> new TableCell<>() {
		    @Override
		    protected void updateItem(Float balance, boolean empty)
		    {
		        super.updateItem(balance, empty);
		        if (balance == null || empty)
		        {
		            setText(null);
		        }
		        else
		        {
		            setText("€ " + String.format("%.2f", balance.doubleValue()));
		        }
		    }
		});
		
		// Thresholds table
		this.colTWine.setCellValueFactory(new PropertyValueFactory<Wine, String>("name"));
		this.colTCurrentQuantity.setCellValueFactory(new PropertyValueFactory<Wine, Integer>("availableQuantity"));
		this.tvThreshold.setItems(this.winesUnderThresholdList);
		this.tabNotifications.setOnSelectionChanged(e -> {
			updateTableThreshold();
		});
		
		// timer for employees
	    timer = new Timer();
	    timer.schedule(new TimerTask() {
	        @Override
	        public void run()
	        {
	        	changePurchaseProposal();
	        }
	    },1000*5,1000*60*5);
	    
	    // report
	    this.dateReportEnd.setValue(LocalDate.now());
	    this.dateReportStart.setValue(LocalDate.now().minusDays(30));
	    reportMarks = new ArrayList<ChoiceBox<Integer>>();
		this.tabReport.setOnSelectionChanged(e -> {
			updateTableEmployeesMarks();
		});
		
		this.chkConsiderHandler(null);
	}
	
	private void updateTableEmployeesMarks()
	{
		ArrayList<User> employees = this.c.employees();
		
		this.reportMarks.clear();
		this.vbAssesment.getChildren().clear();
		for (User emp : employees)
			this.vbAssesment.getChildren().add(new EmployeeAssesmentBox(emp, this.reportMarks));
	}
	
	private void updateTableThreshold()
	{
		ArrayList<Wine> winesUnderThreshold = this.c.winesUnderThreshold();
		
		this.winesUnderThresholdList.clear();
		this.winesUnderThresholdList.addAll(winesUnderThreshold);
	}
	
	private void updateccbOrdersToSign(ArrayList<Purchase> ordersToSign)
	{	
		this.ccbOrdersToSign.getItems().clear();
		for (Purchase p : ordersToSign)
			this.ccbOrdersToSign.getItems().add(p.getUsername() + "[" + p.getDate() + "]");
		this.btnSOTS.setDisable(this.ccbOrdersToSign.getItems().isEmpty());
	}
	
	private Account getatype()
	{
		return this.atype;
	}
	
	private void updateTableEmployeesRD()
	{
		ArrayList<User> employees = this.c.employees();
		
		this.employeesList.clear();
		this.employeesList.addAll(employees);
	}
	
	private void updateTableWines()
	{
		ArrayList<Wine> wines = this.c.wines(this.txtWineName.getText(), (this.spnWineYear.isDisable() ? null : this.spnWineYear.getValue()));
		
		this.winesList.clear();
		this.winesList.addAll(wines);
	}
	
	private void updateTableSalesOrder()
	{
		ArrayList<Purchase> purchases = this.c.purchases(PurchaseType.SALES_ORDER, Date.valueOf(this.dateSOStartDate.getValue()), Date.valueOf(this.dateSOEndDate.getValue()));
		
		this.salesOrderList.clear();
		this.salesOrderList.addAll(purchases);
	}
	
	private void updateTableSalesOrderToSign()
	{
		ArrayList<Purchase> purchases = this.c.purchases(PurchaseType.SALES_ORDER, null, null);
		
		purchases.removeIf(p -> {return p.getDigitalSignature() != null;});
		this.salesOrderToSignList.clear();
		this.salesOrderToSignList.addAll(purchases);
		updateccbOrdersToSign(purchases);
	}
	
	private void updateTablePurchaseOrder()
	{
		ArrayList<Purchase> purchases = this.c.purchases(PurchaseType.PURCHASE_ORDER, Date.valueOf(this.datePOStartDate.getValue()), Date.valueOf(this.datePOEndDate.getValue()));
		
		this.purchaseOrderList.clear();
		this.purchaseOrderList.addAll(purchases);
	}
	
	private void updateTableRPurchaseProposal()
	{
		if (!this.RpurchaseProposalList.isEmpty())
			return;
		this.purchaseProposal = this.c.getPurchaseProposal();
		if (!this.purchaseProposal.isNull())
			this.RpurchaseProposalList.add(this.purchaseProposal);
	}
	
	private void updateTablePurchaseProposal()
	{
		ArrayList<Purchase> purchases = this.c.purchases(PurchaseType.PURCHASE_PROPOSAL, Date.valueOf(this.datePPStartDate.getValue()), Date.valueOf(this.datePPEndDate.getValue()));
		
		this.purchaseProposalList.clear();
		this.purchaseProposalList.addAll(purchases);
	}
	
	private void updateTableCustomers()
	{
		ArrayList<User> customers = this.c.customers(this.txtCustomersSurname.getText());
		
		this.customersList.clear();
		this.customersList.addAll(customers);
	}
	
	private void updateccbWines()
	{
		ArrayList<Wine> wines = this.c.wines("", null);
		
		this.ccbWines.getItems().clear();
		for (Wine w : wines)
			this.ccbWines.getItems().add("[" + w.getId() + "]" + w.getName());
	}
	
	private void updatecbQuantity()
	{
		this.cbQuantity.getItems().clear();
		this.cbQuantity.getItems().add(6);
		this.cbQuantity.getItems().add(12);
		this.cbQuantity.getItems().add(18);
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
	protected void btnAddEmployeeHandler(final ActionEvent e)
	{
		User u = new User(this.txtEmpName.getText(), this.txtEmpSurname.getText(), this.txtEmpFiscalCode.getText(), 
				this.txtEmpTelephone.getText(), this.txtEmpAddress.getText(), this.txtEmpUsername.getText(),
				this.txtEmpPassword.getText(), Account.EMPLOYEE);
		String outcomeTxt;
		AlertType atype;
		
		if (this.c.signIn(u))
		{
			outcomeTxt = "The employee is correctly recorded.";
			atype = AlertType.INFORMATION;
		}
		else
		{
			outcomeTxt = "Error: check values!";
			atype = AlertType.ERROR;
		}
		Utilities.messageDialog(atype, "Outcome", "Outcome", outcomeTxt);
	}
	
	@FXML
	protected void btnSearchWinesHandler(final ActionEvent e)
	{
		this.updateTableWines();
	}
	
	@FXML
	protected void btnSalesOrderListHandler(final ActionEvent e)
	{
		this.updateTableSalesOrder();
	}
	
	@FXML
	protected void btnPurchaseOrderListHandler(final ActionEvent e)
	{
		this.updateTablePurchaseOrder();
	}
	
	@FXML
	protected void btnPurchaseProposalListHandler(final ActionEvent e)
	{
		this.updateTablePurchaseProposal();
	}
	
	@FXML
	protected void btnSearchCustomersHandler(final ActionEvent e)
	{
		this.updateTableCustomers();
	}
	
	@FXML
	protected void btnResetDeleteHandler(final ActionEvent e)
	{
		Button btn = (Button) e.getSource();
		String txtButton = btn.getText().toLowerCase();
		String username = txtRDUsername.getText();
		boolean outcome;
		
		if (!Utilities.messageDialog(AlertType.CONFIRMATION, "Confirm " + txtButton, "Attention!", "Are you sure to " + txtButton + " that employee?"))
			return;
		
		if (c.userIsPresent(Account.EMPLOYEE, username))
		{
			if (txtButton.equals("reset"))
				outcome = c.resetEmployee(username);
			else
			{
				outcome = c.deleteEmployee(username);
				if (outcome)
					this.updateTableEmployeesRD();
			}
		}
		else
			outcome = false;
		
		Utilities.messageDialog((outcome ? AlertType.INFORMATION : AlertType.ERROR), "Outcome", "Outcome", (outcome ? "Success." : "An error was occured. Check the username."));
	}
	
	@FXML
	protected void chkConsiderHandler(final ActionEvent e)
	{
		this.spnWineYear.setDisable(!this.spnWineYear.isDisable());
	}
	
	@FXML
	protected void btnSendPurchaseOrderHandler(final ActionEvent e)
	{
		HashMap<Integer, Integer> winesQuantity = new HashMap<Integer, Integer>();
		Integer quantity = cbQuantity.getValue();
		ObservableList<String> wines = ccbWines.getCheckModel().getCheckedItems();
		
		for (String w : wines)
			winesQuantity.put(Integer.parseInt(w.substring(w.indexOf('[')+1, w.indexOf(']'))), quantity);
		
		Purchase p = new Purchase(winesQuantity, this.c.getUsername(), null, null, null, false, 0, null);
		boolean outcome = this.c.createPurchase(p);
		
		Utilities.messageDialog((outcome ? AlertType.INFORMATION : AlertType.ERROR), "Outcome", "Outcome", (outcome ? "Success." : "An error was occured."));
	}
	
	@FXML
	protected void btnSOTSHandler(final ActionEvent e)
	{
		ObservableList<String> ots = this.ccbOrdersToSign.getCheckModel().getCheckedItems();
		Date deliveryDate = Date.valueOf(this.dateSOTSDeliveryDate.getValue());
		boolean outcome = true;
		
		for (String o : ots)
			outcome = outcome && this.c.checkSalesOrder(Timestamp.valueOf(o.substring(o.indexOf('[')+1, o.indexOf(']'))), deliveryDate);			
		this.updateTableSalesOrderToSign();
		Utilities.messageDialog((outcome ? AlertType.INFORMATION : AlertType.ERROR), "Outcome", "Outcome", (outcome ? "Success." : "An error was occured."));
	}
	
	@FXML
	protected void btnCreatePOHandler(final ActionEvent e)
	{
		Purchase rp = this.purchaseProposal;
		
		if (rp.isNull())
		{
			this.RpurchaseProposalList.clear();
			Utilities.messageDialog(AlertType.WARNING, "Warning", "No purchase", "There are no purchase proposals to manage.");
			return;
		}
		
		Purchase p = new Purchase(rp.getWinesQuantity(), this.c.getUsername(), null, null, null, false, 0, null);
		boolean outcome = this.c.createPurchase(p);
		if (outcome)
		{
			this.c.checkPurchaseProposal(this.purchaseProposal.getDate());
			this.RpurchaseProposalList.clear();
			this.updateTableRPurchaseProposal();
		}
		Utilities.messageDialog((outcome ? AlertType.INFORMATION : AlertType.ERROR), "Outcome", "Outcome", (outcome ? "Success." : "An error was occured."));
	}
	
	@FXML
	protected void btnPrintReportHandler(final ActionEvent e)
	{
		Report report = this.c.report(Date.valueOf(this.dateReportStart.getValue()), Date.valueOf(this.dateReportEnd.getValue()));
		String strAssesments = "", strSoldWines = "";
		HashMap<Integer, Integer> hmSoldWines;
		
		try
		{
			// Make PDF report
			Document doc = new Document();
			PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("report.pdf"));
			
			doc.open();
			
			Font fBigTitle = FontFactory.getFont(FontFactory.COURIER, 18, Font.BOLD);
			Font fSmallTitle = FontFactory.getFont(FontFactory.COURIER, 14, Font.BOLD);
			Font fParagraph = FontFactory.getFont(FontFactory.COURIER, 11, Font.NORMAL);
			
			Paragraph bigTitle = new Paragraph("WineShop Report", fBigTitle);
			bigTitle.setAlignment(Element.ALIGN_CENTER);
			doc.add(bigTitle);
			doc.add(new Paragraph("Period", fSmallTitle));
			doc.add(new Paragraph("From:\t" + this.dateReportStart.getValue().toString(), fParagraph));
			doc.add(new Paragraph("To:\t" + this.dateReportEnd.getValue().toString(), fParagraph));
			doc.add(new Paragraph(" "));
			doc.add(new Paragraph("Business", fSmallTitle));
			doc.add(new Paragraph("Income:\t€ " + String.format("%.2f", report.getIncome()), fParagraph));
			doc.add(new Paragraph("Expenses:\t€ " + String.format("%.2f", report.getExpenses()), fParagraph));
			doc.add(new Paragraph("Sold bottles:\t" + report.getSoldBottle(), fParagraph));
			doc.add(new Paragraph("Available bottles:\t" + report.getAvailableBottle(), fParagraph));
			hmSoldWines = report.getSoldWine();
			for (Integer idWine : hmSoldWines.keySet())
				strSoldWines += "[wine: " + idWine + ", quantity: " + hmSoldWines.get(idWine) + "] ";
			doc.add(new Paragraph("Sold wines:\t" + strSoldWines, fParagraph));
			doc.add(new Paragraph(" "));
			doc.add(new Paragraph("Employees assesments", fSmallTitle));
			for (ChoiceBox<Integer> cbAssesment : this.reportMarks)
				strAssesments += cbAssesment.getId() + ": " + cbAssesment.getValue() + "/5\n";
			doc.add(new Paragraph(strAssesments, fParagraph));
		
			doc.close();
			writer.close();
		}
		catch (Exception e1)
		{
			Utilities.messageDialog(AlertType.ERROR, "Error", null, e1.toString());
			return;
		}
		Utilities.messageDialog(AlertType.INFORMATION, "Success", null, "The report file was created.");
	}
	
	private void changePurchaseProposal()
	{
		Purchase newPP = this.c.getPurchaseProposal();
		
		this.RpurchaseProposalList.clear();
		if (!this.purchaseProposal.isNull())
			this.c.sendPurchaseProposal(this.purchaseProposal);
		this.purchaseProposal = newPP;
		if (!this.purchaseProposal.isNull())
			this.RpurchaseProposalList.add(this.purchaseProposal);
	}
	
	/**
	 * Current Purchase Proposal to manage
	 * @return Current Purchase Proposal to manage
	 */
	public Purchase getPurchaseProposal()
	{
		return this.purchaseProposal;
	}
	
	@Override
	public void setAtype(Account atype)
	{
		super.setAtype(atype);
		if (getatype() == Account.ADMINISTRATOR)
		{
			this.timer.cancel();
			this.tpAdministratorEmployee.getTabs().remove(2);
			this.tpAdministratorEmployee.getTabs().remove(2);
			this.tpAdministratorEmployee.getTabs().remove(2);
		}
		else // employee
		{
			this.tpAdministratorEmployee.getTabs().remove(1);
			this.tpAdministratorEmployee.getTabs().remove(4);
		}
		this.updateTableWines();
	}
}
