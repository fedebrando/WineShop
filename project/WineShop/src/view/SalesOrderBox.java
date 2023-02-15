package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.Purchase;

/**
 * Class SalesOrderBox IS-A PurchaseBox which graphically represent a sales order
 * @author Federico Brandini, Federico Putamorsi
 * @version 14.02.2023
 */
public class SalesOrderBox extends PurchaseBox
{
	private Label lblDeliveryDate;
	private Label lblCourierCode;
	
	/**
	 * Instantiates a sales order graphic box
	 * @param p The sales order to represent
	 */
	public SalesOrderBox(Purchase p)
	{
		super(p);
		VBox vbDeliveryDate = new VBox(), vbCourierCode = new VBox();
		lblDeliveryDate = new Label((p.getDeliveryDate() != null ? p.getDeliveryDate().toString() : "-"));
		vbDeliveryDate.getChildren().addAll(new Label("Delivery date"), lblDeliveryDate);
		vbDeliveryDate.setAlignment(Pos.BASELINE_CENTER);
		lblCourierCode = new Label((p.getFcService() != null ? p.getFcService() : "-"));
		vbCourierCode.getChildren().addAll(new Label("Courier code"), lblCourierCode);
		vbCourierCode.setAlignment(Pos.BASELINE_CENTER);
		vbDeliveryDate.setPadding(new Insets(0, 10, 0, 0));
		vbCourierCode.setPadding(new Insets(0, 10, 0, 0));
		super.hbRoot.getChildren().addAll(vbDeliveryDate, vbCourierCode);
	}
}
