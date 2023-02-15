package view;

import java.net.URL;
import java.util.ResourceBundle;

import controller.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Account;
import model.User;

/**
 * Class CustomerSignInController, which controls customer sign-in interface
 * @author Federico Brandini, Federico Putamorsi
 * @version 14.02.2023
 */
public class CustomerSignInController extends Controller implements Initializable
{
	@FXML
	private TextField txtName;
	@FXML
	private TextField txtSurname;
	@FXML
	private TextField txtFiscalCode;
	@FXML
	private TextField txtTelephone;
	@FXML
	private TextField txtAddress;
	@FXML
	private TextField txtUsername;
	@FXML
	private PasswordField pswPassword;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		c = new Client();
	}
	
	@FXML
	protected void btnSignInHandler(final ActionEvent e)
	{
		String username = this.txtUsername.getText();
		User newCustomer = new User(this.txtName.getText(), this.txtSurname.getText(), this.txtFiscalCode.getText(), this.txtTelephone.getText(), 
				this.txtAddress.getText(), username, this.pswPassword.getText(), Account.CUSTOMER);
		if (c.signIn(newCustomer))
		{
			Utilities.messageDialog(AlertType.INFORMATION, "Outcome", "Success", "Customer " + username + " is correctly recorded.");
			Utilities.closeStage(e);
		}
		else
			Utilities.messageDialog(AlertType.ERROR, "Outcome", "Error", "Check your values.");
	}
}
