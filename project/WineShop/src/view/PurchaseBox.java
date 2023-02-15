package view;

import java.util.HashMap;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Purchase;

/**
 * Class PurchaseBox IS-A Group which graphically represent a purchase
 * @author Federico Brandini, Federico Putamorsi
 * @version 14.02.2023
 */
public class PurchaseBox extends Group
{
	protected HBox hbRoot;
	private Label lblDate;
	private Label lblTotalPrice;
	
	/**
	 * Instantiates a purchase graphic box
	 * @param p The purchase to represent
	 */
	public PurchaseBox(Purchase p)
	{
		super();
		
		HashMap<Integer, Integer> winesQuantity = p.getWinesQuantity();
		VBox vbPrice = new VBox();
		
		this.hbRoot = new HBox();
		hbRoot.setPadding(new Insets(10, 10, 10, 10));
		lblDate = new Label(p.getDate().toString().split(" ")[0]);
		lblDate.setAlignment(Pos.BASELINE_CENTER);
		lblTotalPrice = new Label("€ " + String.format("%.2f", p.getPrice()));
		vbPrice.getChildren().addAll(new Label("Price"), lblTotalPrice);
		vbPrice.setAlignment(Pos.BASELINE_CENTER);
		lblDate.setPadding(new Insets(0, 10, 0, 0));
		vbPrice.setPadding(new Insets(0, 10, 0, 0));
		hbRoot.getChildren().addAll(lblDate, vbPrice);
		winesQuantity.forEach((idWine, quantity) -> {
			ImageView ivImage = new ImageView(getClass().getClassLoader().getResource("" + idWine + ".jpg").toString());
			Label lblCurrWine = new Label("x" + quantity);
			lblCurrWine.setPadding(new Insets(0, 10, 0, 0));
			ivImage.setFitHeight(70);
			ivImage.setPreserveRatio(true);
			hbRoot.getChildren().addAll(ivImage, lblCurrWine);
		});
		super.getChildren().add(hbRoot);
		hbRoot.getStyleClass().addAll("boldText", "roundRect");
	}
}
