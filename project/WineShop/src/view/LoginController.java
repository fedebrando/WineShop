package view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import controller.Client;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Account;
import model.Purchase;

/**
 * Class LoginController, which control the login interface
 * @author Federico Brandini, Federico Putamorsi
 * @version 14.02.2023
 */
public class LoginController implements Initializable
{	
	private Client c;
	private Controller controller;
	@FXML
	private Label lblErrorLogin;
	@FXML
	private TextField txtUsername;
	@FXML
	private PasswordField pswPassword;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
	}
	
	@FXML
	protected void btnEnterHandler(final ActionEvent e)
	{
		this.c = new Client();
		Account type = c.login(txtUsername.getText(), pswPassword.getText());
		
		if (type == Account.FOREIGN)
			lblErrorLogin.setText("Error: username or password are incorrect.");
		else
		{
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("w" + (type == Account.CUSTOMER ? Account.CUSTOMER.getStrType() : "AdministratorEmployee") + ".fxml"));
			
			Parent parent;
			try
			{
				parent = fxmlLoader.load();
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
				return;
			}
			parent.getStylesheets().add(getClass().getClassLoader().getResource("style.css").toExternalForm());
			
			Scene scene = new Scene(parent);
			Stage stage = new Stage();
			if (type == Account.CUSTOMER)
				controller = fxmlLoader.<CustomerController>getController();
			else
				controller = fxmlLoader.<AdministratorEmployeeController>getController();
			controller.setClient(this.c);
			controller.setAtype(type);
	        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	            @Override
	            public void handle(WindowEvent e)
	            {
	            	if (controller instanceof AdministratorEmployeeController)
	            	{
		            	Purchase mpp = ((AdministratorEmployeeController)controller).getPurchaseProposal();
		            	if (!mpp.isNull())
		            		c.sendPurchaseProposal(mpp);
	            	}
	            	c.logout();
	            	Platform.exit();
					System.exit(0);
	            }
	        });
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setScene(scene);
			stage.getIcons().add(new Image(getClass().getClassLoader().getResource("icon.png").toString()));
			Utilities.closeStage(e);
			stage.showAndWait();
		}
	}
	
	@FXML
	protected void btnSignInHandler(final ActionEvent e)
	{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("wCustomerSignIn.fxml"));
		
		Parent parent;
		try
		{
			parent = fxmlLoader.load();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
			return;
		}
		parent.getStylesheets().add(getClass().getClassLoader().getResource("style.css").toExternalForm());
		
		Scene scene = new Scene(parent);
		Stage stage = new Stage();       
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(scene);
		stage.getIcons().add(new Image(getClass().getClassLoader().getResource("icon.png").toString()));
		stage.showAndWait();
	}
}
