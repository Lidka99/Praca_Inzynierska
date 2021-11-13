package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.activation.DataSource;

import com.j256.ormlite.jdbc.DataSourceConnectionSource;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;

import application.controller.DriversController;
import application.controller.ScheduleController;
import application.view.Application_view_controller;
import application.view.Login_view_controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class Main extends Application {

	private Stage primaryStage;
	private BorderPane applicationWindowRoot;
	public JdbcConnectionSource  dataBaseConnection;
	private static String DATABASE_URL = "jdbc:sqlserver://localhost:1433; databaseName=evidence; integratedSecurity=true";

	// ��czenie z baz� danych
	@Override
	public void init() throws Exception {
		// TODO Auto-generated method stub
		super.init();
		try {
			dataBaseConnection = new JdbcConnectionSource(DATABASE_URL);
			System.out.println("Po��czono z baz� danych");
			
			DriversController controller =  new DriversController(dataBaseConnection);
			
			controller.Create("Andrzej", "Nowak", "45gt5e5");
			
			
		} catch (SQLException e) {
			System.err.println("Brak mo�liwo�ci po��czenia z baz� danych");
			e.printStackTrace();
		}
	}
	
	// Zamykanie po��czenia

	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
		super.stop();
		try {
			if (dataBaseConnection != null)
				dataBaseConnection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// g�owna metoda, tworzenie pierwszego okna
	@Override
	public void start(Stage primaryStage) {

		this.primaryStage = primaryStage;

		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/Login_view.fxml"));
			GridPane root = (GridPane) loader.load();
			Login_view_controller controller = loader.getController();

			controller.setmainapp(this);

			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	


	public void showapplication() {

		// otworzenie okna aplikacji

		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/Application_view.fxml"));
			applicationWindowRoot = (BorderPane) loader.load();

			Application_view_controller controller = loader.getController();

			controller.setmainapp(this);

			showMainPage();

			Scene scene = new Scene(applicationWindowRoot);
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// otworzenie strony g��wnej w oknie aplikacji

	public void showMainPage() {

		try {

			FXMLLoader loader1 = new FXMLLoader(Main.class.getResource("view/MainPage_view.fxml"));
			BorderPane root1 = (BorderPane) loader1.load();

			applicationWindowRoot.setCenter(root1);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void showRaportPage() {

		try {

			FXMLLoader loader1 = new FXMLLoader(Main.class.getResource("view/Raport_view.fxml"));
			BorderPane root1 = (BorderPane) loader1.load();

			applicationWindowRoot.setCenter(root1);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}

}
