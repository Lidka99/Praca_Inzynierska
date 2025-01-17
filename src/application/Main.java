package application;

import java.io.IOException;
import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.activation.DataSource;
import javax.swing.text.DateFormatter;

import com.j256.ormlite.jdbc.DataSourceConnectionSource;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.password4j.BCryptFunction;
import com.password4j.Hash;
import com.password4j.Password;
import com.password4j.types.BCrypt;

import application.controller.DataGenerator;
import application.controller.DriversController;
import application.controller.ScheduleController;
import application.controller.TrailersController;
import application.controller.TrucksController;
import application.controller.UsersController;
import application.model.Users;
import application.view.AdminPanelDriversViewController;
import application.view.AdminPanelSchedulesViewController;
import application.view.AdminPanelTrailersViewController;
import application.view.AdminPanelTrucksViewController;
import application.view.AdminPanelUsersViewController;
import application.view.AdminPanelViewController;
import application.view.ApplicationViewController;
import application.view.ArrivalsDeparturesViewController;
import application.view.LoginViewController;
import application.view.MainPageViewController;
import application.view.RaportViewController;
import application.view.ScheduleViewController;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class Main extends Application {

	private static DateFormat dateFormat;
	private static DateFormat dateTimeFormat;
	private static DateFormat timeFormat;
	private static DateFormat hourFormat;
	private static DateFormat minuteFormat;

	private Stage primaryStage;

	private BorderPane applicationWindowRoot;
	private BorderPane adminPanelRoot;
	private Scene loginPanelScene;
	private Users currentUser;

	public JdbcConnectionSource dataBaseConnection;
	private static String DATABASE_URL = "jdbc:sqlserver://localhost:1433; databaseName=evidence; integratedSecurity=true";
	private UsersController usersController;
	private DriversController driversController;
	private ScheduleController scheduleController;
	private TrucksController trucksController;
	private TrailersController trailersController;

	// ��czenie z baz� danych
	@Override
	public void init() throws Exception {
		// TODO Auto-generated method stub
		super.init();

		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		timeFormat = new SimpleDateFormat("HH:mm");
		hourFormat = new SimpleDateFormat("HH");
		minuteFormat = new SimpleDateFormat("mm");

		try {
			dataBaseConnection = new JdbcConnectionSource(DATABASE_URL);
			System.out.println("Po��czono z baz� danych");

			usersController = new UsersController(dataBaseConnection);
			driversController = new DriversController(dataBaseConnection);
			trucksController = new TrucksController(dataBaseConnection);
			trailersController = new TrailersController(dataBaseConnection);
			scheduleController = new ScheduleController(dataBaseConnection);

			// ODKOMENTUJ ABY WYGENEROWA� DANE TESTOWE
			//generateTestData();

		} catch (SQLException e) {
			System.err.println("Brak mo�liwo�ci po��czenia z baz� danych");
			e.printStackTrace();
		}
	}

	public void generateTestData() {

		///// KOD DO GENEROWANIA
		// dodowanie uzytkownikow

		// klasa algorytmu BCrypt
		BCryptFunction myBcrypt = BCryptFunction.getInstance(BCrypt.Y, 11);

		// tworzenie hasha na podstawie Stringa
		Hash hash = Password.hash("haslo123").with(myBcrypt);

		usersController.create(Users.Role.administrator, "Janusz", "Kowalczyk", "jko05", hash.getResult(),
				"j.kowalczyk@gmail.com");
		System.out.println(Password.check("haslo123", hash.getResult()).with(myBcrypt));

		// tworzenie hasha na podstawie Stringa
		Hash hash1 = Password.hash("haslo124").with(myBcrypt);

		usersController.create(Users.Role.user, "Marek", "Stasiak", "mstas8", hash1.getResult(), "stasiak.m@gmail.com");

		DataGenerator.initialize(this);

		DataGenerator.loadNames(".\\do generacji\\imionazenskie.csv");
		DataGenerator.loadNames(".\\do generacji\\imionameskie.csv");
		DataGenerator.loadSurnames(".\\do generacji\\nazwiskazenskie.csv");
		DataGenerator.loadSurnames(".\\do generacji\\nazwiskameskie.csv");
		DataGenerator.loadLicenceNumbers(".\\do generacji\\rejestracje.csv");

		DataGenerator.generateData(20, 40, 40, 40, 500);

		// sprawdzanie poprawnosci z generowaniem dat
		// Date date = DataGenerator.getRandomDate(2021, 10, 1, 2022, 3, 31);
		// System.out.println(date);

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
		this.primaryStage.setTitle("Aplikacja kontroluj�ca przyjazdy i wyjazdy floty transportowej");

		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/Login_view.fxml"));
			Pane root = loader.load();
			LoginViewController controller = loader.getController();

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

			ApplicationViewController controller = loader.getController();

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

			MainPageViewController controller = loader1.getController();

			controller.setmainapp(this);

			controller.setUp();

			applicationWindowRoot.setCenter(root1);
			applicationWindowRoot.setLeft(null);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void showSchedulePage() {

		try {

			FXMLLoader loader1 = new FXMLLoader(Main.class.getResource("view/Schedule_view.fxml"));
			Pane root1 = loader1.load();

			ScheduleViewController controller = loader1.getController();

			controller.setmainapp(this);

			controller.setUp();

			applicationWindowRoot.setCenter(root1);
			applicationWindowRoot.setLeft(null);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void showArrivalsDeparturesPage() {

		try {

			FXMLLoader loader1 = new FXMLLoader(Main.class.getResource("view/ArrivalsDeparture_view.fxml"));
			TabPane root1 = loader1.load();

			ArrivalsDeparturesViewController controller = loader1.getController();

			controller.setmainapp(this);

			controller.setUp();

			applicationWindowRoot.setCenter(root1);
			applicationWindowRoot.setLeft(null);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void showRaportPage() {

		try {

			FXMLLoader loader1 = new FXMLLoader(Main.class.getResource("view/Raport_view.fxml"));
			TabPane root1 = loader1.load();

			RaportViewController controller = loader1.getController();

			controller.setmainapp(this);

			controller.setUp();

			applicationWindowRoot.setCenter(root1);
			applicationWindowRoot.setLeft(null);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void showAdminPage() {

		try {

			FXMLLoader loader1 = new FXMLLoader(Main.class.getResource("view/AdminPanel_view.fxml"));
			Pane root1 = loader1.load();

			AdminPanelViewController controller = loader1.getController();

			controller.setmainapp(this);

			controller.setUp();

			applicationWindowRoot.setLeft(root1);
			applicationWindowRoot.setCenter(null);
			showAdminPageUsers();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void showAdminPageUsers() {

		try {

			FXMLLoader loader1 = new FXMLLoader(Main.class.getResource("view/AdminPanelUsers_view.fxml"));
			Pane root1 = loader1.load();

			AdminPanelUsersViewController controller = loader1.getController();

			controller.setmainapp(this);

			controller.setUp();

			applicationWindowRoot.setCenter(root1);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void showAdminPageDrivers() {

		try {

			FXMLLoader loader1 = new FXMLLoader(Main.class.getResource("view/AdminPanelDrivers_view.fxml"));
			Pane root1 = loader1.load();

			AdminPanelDriversViewController controller = loader1.getController();

			controller.setmainapp(this);

			controller.setUp();

			applicationWindowRoot.setCenter(root1);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void showAdminPageTrucks() {

		try {

			FXMLLoader loader1 = new FXMLLoader(Main.class.getResource("view/AdminPanelTrucks_view.fxml"));
			Pane root1 = loader1.load();

			AdminPanelTrucksViewController controller = loader1.getController();

			controller.setmainapp(this);

			controller.setUp();

			applicationWindowRoot.setCenter(root1);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void showAdminPageTrailers() {

		try {

			FXMLLoader loader1 = new FXMLLoader(Main.class.getResource("view/AdminPanelTrailers_view.fxml"));
			Pane root1 = loader1.load();

			AdminPanelTrailersViewController controller = loader1.getController();

			controller.setmainapp(this);

			controller.setUp();

			applicationWindowRoot.setCenter(root1);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void showAdminPageSchedule() {

		try {

			FXMLLoader loader1 = new FXMLLoader(Main.class.getResource("view/AdminPanelSchedule_view.fxml"));
			Pane root1 = loader1.load();

			AdminPanelSchedulesViewController controller = loader1.getController();

			controller.setmainapp(this);

			controller.setUp();

			applicationWindowRoot.setCenter(root1);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public UsersController getUsersController() {
		return usersController;
	}

	public DriversController getDriversController() {
		return driversController;
	}

	public ScheduleController getScheduleController() {
		return scheduleController;
	}

	public TrucksController getTrucksController() {
		return trucksController;
	}

	public TrailersController getTrailersController() {
		return trailersController;
	}

	public static DateFormat getDateFormat() {
		return dateFormat;
	}

	public static DateFormat getDateTimeFormat() {
		return dateTimeFormat;
	}

	public static DateFormat getTimeFormat() {
		return timeFormat;
	}

	public static DateFormat getHourFormat() {
		return hourFormat;
	}

	public static DateFormat getMinuteFormat() {
		return minuteFormat;
	}

	public static void main(String[] args) {
		launch(args);
	}

}
