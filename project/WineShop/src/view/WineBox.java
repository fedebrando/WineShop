package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Wine;

/**
 * Class WineBox IS-A Group which graphically represent a wine to buy
 * @author Federico Brandini, Federico Putamorsi
 * @version 14.02.2023
 */
public class WineBox extends Group
{
	private VBox vbRoot;
	private HBox hbQuantity;
	private ImageView ipImage;
	private Label lblNameYear;
	private Label lblPrice;
	private Label lblQuantity;
	private ChoiceBox<Integer> cbQuantity;
	private ButtonValue<Wine> btnAction;
	
	/**
	 * Instantiates a wine box to buy that wine
	 * @param w The wine
	 * @param cart True if it is destined for a cart, false otherwise
	 * @param f The handler action of the button
	 */
	public WineBox(Wine w, boolean cart, EventHandler<ActionEvent> f)
	{
		super();
		vbRoot = new VBox();
		vbRoot.setPadding(new Insets(10, 10, 10, 10));
		vbRoot.getStyleClass().add("round");
		ipImage = new ImageView(getClass().getClassLoader().getResource("" + w.getId() + ".jpg").toString());
		ipImage.setFitHeight(100);
		ipImage.setPreserveRatio(true);
		lblNameYear = new Label(w.getName() + " (" + w.getYear() + ")");
		lblNameYear.getStyleClass().add("boldText");
		lblPrice = new Label("€ " + String.format("%.2f", w.getPrice()));
		lblPrice.getStyleClass().add("boldText");
		if (cart)
		{
			lblQuantity = new Label("Quantity: " + w.getAvailableQuantity());
			btnAction = new ButtonValue<Wine>("Remove", w);
			btnAction.setOnAction(f);
			btnAction.setAlignment(Pos.BASELINE_RIGHT);
			vbRoot.getChildren().addAll(ipImage, lblNameYear, lblPrice, lblQuantity, btnAction);
		}
		else
		{
			lblQuantity = new Label("Quantity ");
			lblQuantity.getStyleClass().add("normalText");
			cbQuantity = new ChoiceBox<Integer>();
			cbQuantity.setId("cb" + w.getId());
			for (int q = 1; q <= 5; q++)
				cbQuantity.getItems().add(q);
			for (int q = 6; q <= 18; q+=6)
				cbQuantity.getItems().add(q);
			cbQuantity.setValue(1);
			hbQuantity = new HBox();
			hbQuantity.getChildren().addAll(lblQuantity, cbQuantity);
			hbQuantity.setAlignment(Pos.BASELINE_CENTER);
			btnAction = new ButtonValue<Wine>("Add", w);
			btnAction.setOnAction(f);
			btnAction.setAlignment(Pos.BASELINE_RIGHT);
			vbRoot.getChildren().addAll(ipImage, lblNameYear, lblPrice, hbQuantity, btnAction);
		}
		super.getChildren().add(vbRoot);
		vbRoot.setAlignment(Pos.BASELINE_CENTER);
	}
}

