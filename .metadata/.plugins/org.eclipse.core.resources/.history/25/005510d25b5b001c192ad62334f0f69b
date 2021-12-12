package application.view;

import java.util.List;

import application.Main;
import application.controller.DriversController;
import application.controller.UsersController;
import application.model.Drivers;
import application.model.Users;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminPanelDriversViewController {

	@FXML
	private TableView driversTableView;

	@FXML
	private TextField nameInputField;
	@FXML
	private TextField surnameInputField;
	@FXML
	private TextField driving_licenseInputField;
	
	private Main main;

	public void setUp() {

		// dodawanie kolumny id
		TableColumn<Drivers, Integer> column1 = new TableColumn("Id");
		column1.setCellValueFactory(new PropertyValueFactory("id"));
		driversTableView.getColumns().add(column1);

		// dodawanie kolumny imie
		TableColumn<Drivers, String> column2 = new TableColumn("Imiê");
		column2.setCellValueFactory(new PropertyValueFactory("name"));
		driversTableView.getColumns().add(column2);

		// dodawanie kolumny nazwisko
		TableColumn<Drivers, String> column3 = new TableColumn("Nazwisko");
		column3.setCellValueFactory(new PropertyValueFactory("surname"));
		driversTableView.getColumns().add(column3);

		// dodawanie kolumny nr prawa jazdy 
		TableColumn<Drivers, String> column4 = new TableColumn("Nr prawa jazdy");
		column4.setCellValueFactory(new PropertyValueFactory("username"));
		driversTableView.getColumns().add(column4);

		
		DriversController controller = main.getDriversController();

		List<Drivers> AllDrivers = controller.getAllDrivers();

		for (Drivers driver : AllDrivers) {

			driversTableView.getItems().add(driver);

		}

		nameInputField.setEditable(false);
		surnameInputField.setEditable(false);
		driving_licenseInputField.setEditable(false);
		

		// zaczynamy nas³uchiwaæ zdarzenie wywo³ywane przy zmianie elementu

		driversTableView.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<Drivers>() {
			@Override
			public void onChanged(Change<? extends Drivers> change) {
				if (change.getList() != null && change.getList().size() > 0) {

					Drivers selectedDriver = change.getList().get(0);
					nameInputField.setText(selectedDriver.getName());
					surnameInputField.setText(selectedDriver.getSurname());
					driving_licenseInputField.setText(selectedDriver.getDriving_license());
					
				}
			}

		});
		
		
	}

	public void setmainapp(Main main) {

		this.main = main;

	}

}
