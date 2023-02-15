package view;

import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Class Utilities, which contains useful functions in all views
 * @author Federico Brandini, Federico Putamorsi
 * @version 14.02.2023
 */
public class Utilities
{
	/**
	 * Generate a blocking message dialog with passed value
	 * @param type The message type
	 * @param title The title of the message
	 * @param header The header of the message
	 * @param content The content of the message
	 * @return True if user press OK, false otherwise
	 */
	public static boolean messageDialog(AlertType type, String title, String header, String content)
	{
		Alert alert = new Alert(type);
		Optional<ButtonType> result;
		
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		result = alert.showAndWait();
		if (result.isEmpty())
			return false;
		
		return result.get() == ButtonType.OK;
	}
	
	/**
	 * Close the stage
	 * @param event The action event been thrown by an object of relative window
	 */
	public static void closeStage(final ActionEvent event)
	{
		Node source = (Node) event.getSource();
	    Stage stage = (Stage) source.getScene().getWindow();
	    stage.close();
	}
}
