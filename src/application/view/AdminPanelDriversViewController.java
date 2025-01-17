package application.view;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;

import com.password4j.BCryptFunction;
import com.password4j.Hash;
import com.password4j.Password;
import com.password4j.types.BCrypt;

import application.Main;
import application.controller.DriversController;
import application.controller.UsersController;
import application.model.Drivers;
import application.model.Users;
import application.view.AdminPanelUsersViewController.EditingMode;
import application.view.AdminPanelUsersViewController.ValidatingStatus;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Alert.AlertType;
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

	private Drivers selectedDriver;

	private EditingMode editingMode = EditingMode.none;

	@FXML
	private Button addButton;
	@FXML
	private Button editButton;
	@FXML
	private Button deleteButton;
	@FXML
	private Button saveButton;
	@FXML
	private Button cancelButton;
	@FXML
	private TextField searchingInputField;

	private Main main;

	private ListChangeListener<Drivers> selectionListener;

	public void onDeleteButtonClick() {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Usuni�cie kierowcy");
		alert.setHeaderText("Masz zamiar usun�� kierowc�");
		alert.setContentText("Czy na pewno chcesz to zrobi�?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			main.getDriversController().delete(selectedDriver);
			updateTableView();
			clearDriverFields();
		} else {

		}

	}

	public void onCreateButtonCick() {

		clearDriverFields();
		enableTableView(false);
		enableEditButttons(false);
		editingMode = EditingMode.adding;
		enableInputFields(true);

		cancelButton.setDisable(false);
		saveButton.setDisable(false);

	}

	public void onEditButtonCick() {

		enableTableView(false);
		enableEditButttons(false);
		editingMode = EditingMode.editing;
		enableInputFields(true);

		cancelButton.setDisable(false);
		saveButton.setDisable(false);

	}

	public void onSaveButtonCick() {

		switch (editingMode) {
		case adding: {

			String name = nameInputField.getText();
			String surname = surnameInputField.getText();
			String drivingLicense = driving_licenseInputField.getText();

			ValidatingStatus nameStatus = validate(name, 30, false);
			ValidatingStatus surnameStatus = validate(surname, 30, false);
			ValidatingStatus drivingLicenseStatus = validate(drivingLicense, 30, false);

			if (nameStatus != ValidatingStatus.sukces || surnameStatus != ValidatingStatus.sukces
					|| drivingLicenseStatus != ValidatingStatus.sukces) {

				return;
			}

			main.getDriversController().create(name, surname, drivingLicense);
		}

			break;

		case editing: {

			String name = nameInputField.getText();
			String surname = surnameInputField.getText();
			String drivingLicense = driving_licenseInputField.getText();

			ValidatingStatus nameStatus = validate(name, 30, false);
			ValidatingStatus surnameStatus = validate(surname, 30, false);
			ValidatingStatus drivingLicenseStatus = validate(drivingLicense, 10, false); // !!!!! obs�u�y� sprawdzanie
																							// unikatowo�ci
			// w przypadku edycji !!!!!

			if (nameStatus != ValidatingStatus.sukces || surnameStatus != ValidatingStatus.sukces
					|| drivingLicenseStatus != ValidatingStatus.sukces) {

				return;
			}

			main.getDriversController().update(selectedDriver.getId(), name, surname, drivingLicense);

		}
			break;

		default:
			break;
		}

		clearDriverFields();
		updateTableView();
		enableTableView(true);
		enableEditButttons(true);
		enableInputFields(false);
		editingMode = EditingMode.none;

		editButton.setDisable(true);
		deleteButton.setDisable(true);
		cancelButton.setDisable(true);
		saveButton.setDisable(true);

	}

	private ValidatingStatus validate(String text, int maxLength, boolean checkDrivingLicenseUniqueness) {

		if (text == null || text.length() <= 0)
			return ValidatingStatus.pustePole;

		if (text.length() > maxLength)
			return ValidatingStatus.tekstZaDlugi;

		if (checkDrivingLicenseUniqueness && main.getDriversController().checkDrivingLicense(text))
			return ValidatingStatus.wartoscNieunikatowa;

		return ValidatingStatus.sukces;

	}

	public void onCancelButtonCick() {

		clearDriverFields();
		updateTableView();
		enableTableView(true);
		enableEditButttons(true);
		editingMode = EditingMode.none;
		enableInputFields(false);

	}

	public void enableInputFields(boolean enabled) {

		nameInputField.setDisable(!enabled);
		surnameInputField.setDisable(!enabled);
		driving_licenseInputField.setDisable(!enabled);

		editButton.setDisable(true);
		deleteButton.setDisable(true);
		cancelButton.setDisable(true);
		saveButton.setDisable(true);

	}

	public void setUp() {

		editButton.setDisable(true);
		deleteButton.setDisable(true);
		cancelButton.setDisable(true);
		saveButton.setDisable(true);

		// dodawanie kolumny id
		TableColumn<Drivers, Integer> column1 = new TableColumn("Id");
		column1.setCellValueFactory(new PropertyValueFactory("id"));
		driversTableView.getColumns().add(column1);

		// dodawanie kolumny imie
		TableColumn<Drivers, String> column2 = new TableColumn("Imi�");
		column2.setCellValueFactory(new PropertyValueFactory("name"));
		driversTableView.getColumns().add(column2);

		// dodawanie kolumny nazwisko
		TableColumn<Drivers, String> column3 = new TableColumn("Nazwisko");
		column3.setCellValueFactory(new PropertyValueFactory("surname"));
		driversTableView.getColumns().add(column3);

		// dodawanie kolumny numer prawa jazdy
		TableColumn<Drivers, String> column4 = new TableColumn("Numer prawa jazdy");
		column4.setCellValueFactory(new PropertyValueFactory("driving_license"));
		driversTableView.getColumns().add(column4);

		updateTableView();

		enableInputFields(false);

		// nas�uchiwacz do filtrowania

		searchingInputField.textProperty().addListener((observable, oldValue, newValue) -> {
			onSearchTextEdit(newValue);
		});

		// tworzymy tzw "nas�uchiwacz"

		if (selectionListener == null) {
			selectionListener = new ListChangeListener<Drivers>() {
				@Override
				public void onChanged(Change<? extends Drivers> change) {
					if (change.getList() != null && change.getList().size() > 0) {

						onSelectedDriver(change.getList().get(0));

					}
				}

			};
		}
		enableTableView(true);
	}

	public void onSearchTextEdit(String text) {

		updateTableView();

	}

	// zaczynamy lub przestajemy nas�uchiwac

	private void enableTableView(boolean enabled) {

		if (enabled) {
			driversTableView.getSelectionModel().getSelectedItems().addListener(selectionListener);
		} else {
			driversTableView.getSelectionModel().getSelectedItems().removeListener(selectionListener);
		}

	}

	private void enableEditButttons(boolean enabled) {

		addButton.setDisable(!enabled);
		editButton.setDisable(!enabled);
		deleteButton.setDisable(!enabled);

	}

	private void onSelectedDriver(Drivers driver) {

		selectedDriver = driver;
		nameInputField.setText(selectedDriver.getName());
		surnameInputField.setText(selectedDriver.getSurname());
		driving_licenseInputField.setText(selectedDriver.getDriving_license());
		editButton.setDisable(driver == null);

		editButton.setDisable(false);
		deleteButton.setDisable(false);

	}

	private void clearDriverFields() {

		nameInputField.setText(null);
		surnameInputField.setText(null);
		driving_licenseInputField.setText(null);

		editButton.setDisable(true);

	}

	private void updateTableView() {

		DriversController controller = main.getDriversController();

		List<Drivers> AllDrivers = controller.searchDriversbySurname(searchingInputField.getText());

		driversTableView.getItems().clear();

		for (Drivers driver : AllDrivers) {

			driversTableView.getItems().add(driver);
		}
	}

	public void setmainapp(Main main) {

		this.main = main;

	}

	public enum EditingMode {
		adding, editing, none
	}

	public enum ValidatingStatus {
		sukces, pustePole, tekstZaDlugi, wartoscNieunikatowa

	}

}
