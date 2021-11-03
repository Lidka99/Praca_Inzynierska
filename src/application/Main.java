package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import view.Application_panel;
import view.Login_panel;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		
		Login_panel login = new Login_panel();
		login.start(primaryStage);
		
	
	}
	public static void main(String[] args) {
		launch(args);
	}
}
