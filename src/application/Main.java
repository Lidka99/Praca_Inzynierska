package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.activation.DataSource;

import com.j256.ormlite.jdbc.DataSourceConnectionSource;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.password4j.BCryptFunction;
import com.password4j.Hash;
import com.password4j.Password;
import com.password4j.types.BCrypt;

import application.controller.DriversController;
import application.controller.ScheduleController;
import application.controller.UsersController;
import application.model.Users;
import application.view.AdminPanel_view_controller;
import application.view.Application_view_controller;
import application.view.Login_view_controller;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class Main extends Application {

	private Stage primaryStage;
	private BorderPane applicationWindowRoot;
	private BorderPane adminPanelRoot;
	private Scene loginPanelScene;
	private Users currentUser;
	public JdbcConnectionSource dataBaseConnection;
	private static String DATABASE_URL = "jdbc:sqlserver://localhost:1433; databaseName=evidence; integratedSecurity=true";
	private UsersController usersController;

	// ��czenie z baz� danych
	@Override
	public void init() throws Exception {
		// TODO Auto-generated method stub
		super.init();
		try {
			dataBaseConnection = new JdbcConnectionSource(DATABASE_URL);
			System.out.println("Po��czono z baz� danych");

			usersController = new UsersController(dataBaseConnection);
			
			//dodowanie uzytkownikow

			// klasa algorytmu BCrypt
			// BCryptFunction myBcrypt = BCryptFunction.getInstance(BCrypt.Y, 11);

			// tworzenie hasha na podstawie Stringa
			// Hash hash = Password.hash("haslo123").with(myBcrypt);

			// usersController.Create("administrator", "Janusz", "Kowalczyk", "jko05",
			// hash.getResult(), "j.kowalczyk@gmail.com" );
			// System.out.println(Password.check("haslo123",
			// hash.getResult()).with(myBcrypt));

			// tworzenie hasha na podstawie Stringa
			// Hash hash = Password.hash("haslo124").with(myBcrypt);

			// usersController.Create("user", "Marek", "Stasiak", "mstas8",
			// hash.getResult(), "stasiak.m@gmail.com" );

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

	// g�owna metoda, tworzenie pierwszego okna (logowanie)
	@Override
	public void start(Stage primaryStage) {

		this.primaryStage = primaryStage;

		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/Login_view.fxml"));
			GridPane root = (GridPane) loader.load();
			Login_view_controller controller = loader.getController();

			controller.setmainapp(this);

			loginPanelScene = new Scene(root);
			primaryStage.setScene(loginPanelScene);
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public Users getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(Users currentUser) {
		this.currentUser = currentUser;
	}

	// wyloguj

	public void logOut() {

		primaryStage.setScene(loginPanelScene);
		primaryStage.show();

	}

	public void showapplication() {

		// otworzenie okna aplikacji

		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/Application_view.fxml"));
			applicationWindowRoot = (BorderPane) loader.load();

			Application_view_controller controller = loader.getController();

			controller.setmainapp(this);
			
			controller.setUp();

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
			Pane root1 = loader1.load();

			applicationWindowRoot.setCenter(root1);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void showRaportPage() {

		try {

			FXMLLoader loader1 = new FXMLLoader(Main.class.getResource("view/Raport_view.fxml"));
			Pane root1 = loader1.load();

			applicationWindowRoot.setCenter(root1);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void showAdminPage() {

		try {

			FXMLLoader loader1 = new FXMLLoader(Main.class.getResource("view/AdminPanel_view.fxml"));
			AnchorPane root = (AnchorPane) loader1.load();
			ObservableList<Node> rootChildren = root.getChildren();
			
			adminPanelRoot = (BorderPane) rootChildren.get(0);

			AdminPanel_view_controller controller = loader1.getController();

			controller.setmainapp(this);
			
			controller.setUp();
			
			applicationWindowRoot.setCenter(adminPanelRoot);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void showAdminPageUsers() {

		try {

			FXMLLoader loader1 = new FXMLLoader(Main.class.getResource("view/AdminPanelUsers_view.fxml"));
			Pane root1 = loader1.load();

			adminPanelRoot.setCenter(root1);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	

	public UsersController getUsersController() {
		return usersController;
	}

	public static void main(String[] args) {
		launch(args);
	}

}
