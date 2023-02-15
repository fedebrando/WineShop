package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Class WindowApplication, from which the client application starts
 * @author Federico Brandini, Federico Putamorsi
 * @version 14.02.2023
 */
public class WindowApplication extends Application
{
	private static final String FXMLFILE = "wLogin.fxml";
	
	@Override
	public void start(final Stage stage) throws Exception
	{
		Parent root = FXMLLoader.load(getClass().getResource(FXMLFILE));
	
	    stage.setScene(new Scene(root));
	    stage.setResizable(false);
	    stage.getIcons().add(new Image(getClass().getClassLoader().getResource("icon.png").toString()));
	    root.getStylesheets().add(getClass().getClassLoader().getResource("style.css").toExternalForm());
	    stage.show();
	}

	public static void main(final String[] args)
	{
		Application.launch(WindowApplication.class, args);
	}
}
