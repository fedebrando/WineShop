
module WineShop
{
	requires transitive javafx.controls;
	requires javafx.fxml;
	requires transitive javafx.graphics;
	requires transitive java.sql;
	requires org.controlsfx.controls;
	requires itext;
	requires org.junit.jupiter.api;
	opens model to javafx.fxml;
	opens view to javafx.fxml;
	exports model;
	exports view;
}