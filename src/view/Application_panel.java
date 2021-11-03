package view;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Application_panel {

	public void start(Stage primaryStage) {
		try {
			BorderPane border = new BorderPane();
			HBox hbox = new HBox();
			border.setTop(hbox);
			hbox.setPadding(new Insets(15, 12, 15, 12));
			hbox.setSpacing(10);
			hbox.setStyle("-fx-background-color: #96c5ef;");

			VBox vbox = new VBox();
			border.setLeft(vbox);

			Scene scene = new Scene(border, 1200, 800);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
			
			Button buttonHome = new Button("Strona g��wna");
			buttonHome.setPrefSize(150, 50);
			
			Button buttonPlan = new Button("Plan przyjazd�w");
			buttonPlan.setPrefSize(150, 50);
			buttonPlan.setStyle("-fx-border-color: #0f78d5; -fx-border-width: 2px;");
			
			Button buttonIn = new Button("Wpuszczanie");
			buttonIn.setPrefSize(150, 50);
			
			Button buttonOut = new Button("Wypuszczanie");
			buttonOut.setPrefSize(150, 50);

			Button buttonRaport = new Button("Raport");
			buttonRaport.setPrefSize(150, 50);

			Button buttonChange = new Button("Zmie� dane");
			buttonChange.setPrefSize(150, 50);
			
			Button buttonAdd = new Button("Dodaj u�ytkownika");
			buttonAdd.setPrefSize(160, 50);
			
			Button buttonLogout = new Button("Wyloguj si�");
			buttonLogout.setPrefSize(160, 50);
			
			
			hbox.getChildren().addAll(buttonHome, buttonPlan, buttonIn, buttonOut, buttonRaport, buttonChange, buttonAdd, buttonLogout);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
