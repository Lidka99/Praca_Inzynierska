package view;

import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Login_panel {

	public void start(Stage primaryStage) {
		try {
	
			// creating label username
			Text username = new Text("Username");

			// creating label password
			Text password = new Text("Password");

			// Creating Text Filed for username
			TextField username_field = new TextField();

			// Creating Text Filed for password
			PasswordField password_field = new PasswordField();

			// Creating Button Log_in
			Button log_in = new Button("Log in");
			log_in.setPrefSize(150, 50);
			

			// Creating the mouse event handler
			EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {

					Application_panel application = new Application_panel();
					application.start(primaryStage);

				}
			};
			// Registering the event filter
			log_in.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);

			// Creating a Grid Pane
			GridPane gridPane = new GridPane();

			// Setting size for the pane
			gridPane.setMinSize(400, 200);

			// Setting the Grid alignment
			gridPane.setAlignment(Pos.CENTER);

			// Arranging all the nodes in the grid
			
			gridPane.add(username, 0, 0);
			gridPane.add(username_field, 1, 0);
			gridPane.add(password, 0, 1);
			gridPane.add(password_field, 1, 1);
			gridPane.add(log_in, 1, 2);

			Scene scene = new Scene(gridPane, 400, 400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
