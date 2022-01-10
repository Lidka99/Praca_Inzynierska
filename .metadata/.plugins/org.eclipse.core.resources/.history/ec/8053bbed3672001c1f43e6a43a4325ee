package application.view;

import java.sql.Driver;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.FastDateParser;

import com.password4j.BCryptFunction;
import com.password4j.Hash;
import com.password4j.Password;
import com.password4j.types.BCrypt;

import application.Main;
import application.controller.DriversController;
import application.controller.ScheduleController;
import application.controller.TrailersController;
import application.controller.TrucksController;
import application.controller.UsersController;
import application.model.Drivers;
import application.model.Schedule;
import application.model.Trailers;
import application.model.Trucks;
import application.model.Users;
import application.view.AdminPanelUsersViewController.EditingMode;
import application.view.AdminPanelUsersViewController.ValidatingStatus;
import application.view.intermediate.ScheduleIntermediate;
import application.view.intermediate.TrucksConverter;
import application.view.intermediate.TrucksIntermediate;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminPanelTrucksViewController {

	@FXML
	private TableView trucksTableView;

	@FXML
	private TextField licenceNumberInputField;
	@FXML
	private TextField brandInputField;
	@FXML
	private TextField modelInputField;
	@FXML
	private TextField maxLoadInputField;

	@FXML
	private ChoiceBox driverChoiceBox;

	@FXML
	private ChoiceBox trailerChoiceBox;

	@FXML
	private TextField searchingInputField;

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

	private Main main;

	private ListChangeListener<TrucksIntermediate> selectionListener;

	private List<Drivers> filteredDrivers;
	private List<Trailers> filteredTrailers;

	private TrucksIntermediate selectedTruck;

	public void onDeleteButtonClick() {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Usuniêcie ciê¿arówki");
		alert.setHeaderText("Masz zamiar usun¹æ ciêzarówkê");
		alert.setContentText("Czy na pewno chcesz to zrobiæ?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			main.getTrucksController().delete(selectedTruck.getId());
			updateTableView();
			clearTruckFields();
		} else {

		}
	}

	public void onCreateButtonCick() {

		clearTruckFields();
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

			String licenceNumber = licenceNumberInputField.getText();
			String brand = brandInputField.getText();
			String model = modelInputField.getText();
			String maxLoad = maxLoadInputField.getText(); // tutaj nie wiem jak zrobic intem

			Drivers driver = filteredDrivers.get(driverChoiceBox.getSelectionModel().getSelectedIndex());
			Trailers trailer = filteredTrailers.get(trailerChoiceBox.getSelectionModel().getSelectedIndex());

			main.getTrucksController().create(licenceNumber, brand, model, Integer.parseInt(maxLoad), driver, trailer);

		}

			break;

		case editing: {

			String licenceNumber = licenceNumberInputField.getText();
			String brand = brandInputField.getText();
			String model = modelInputField.getText();
			String maxLoad = maxLoadInputField.getText(); // tutaj nie wiem jak zrobic intem

			Drivers driver = filteredDrivers.get(driverChoiceBox.getSelectionModel().getSelectedIndex());
			Trailers trailer = filteredTrailers.get(trailerChoiceBox.getSelectionModel().getSelectedIndex());

			main.getTrucksController().create(licenceNumber, brand, model, Integer.parseInt(maxLoad), driver, trailer);
		}

			break;

		default:
			break;
		}

		clearTruckFields();
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

	public void onCancelButtonCick() {

		clearTruckFields();
		updateTableView();
		enableTableView(true);
		enableEditButttons(true);
		editingMode = EditingMode.none;
		enableInputFields(false);
		
		editButton.setDisable(true);
		deleteButton.setDisable(true);
		cancelButton.setDisable(true);
		saveButton.setDisable(true);

	}

	public void enableInputFields(boolean enabled) {

		licenceNumberInputField.setDisable(!enabled);
		brandInputField.setDisable(!enabled);
		modelInputField.setDisable(!enabled);
		maxLoadInputField.setDisable(!enabled);
		driverChoiceBox.setDisable(!enabled);
		trailerChoiceBox.setDisable(!enabled);

	}

	public void setUp() {
		
		editButton.setDisable(true);
		deleteButton.setDisable(true);
		cancelButton.setDisable(true);
		saveButton.setDisable(true);

		// dodawanie kolumny id
		TableColumn<TrucksIntermediate, Integer> column1 = new TableColumn("Id");
		column1.setCellValueFactory(new PropertyValueFactory("id"));
		trucksTableView.getColumns().add(column1);

		// dodawanie kolumny marka
		TableColumn<TrucksIntermediate, String> column2 = new TableColumn("Marka");
		column2.setCellValueFactory(new PropertyValueFactory("brand"));
		trucksTableView.getColumns().add(column2);

		// dodawanie kolumny model
		TableColumn<TrucksIntermediate, String> column3 = new TableColumn("Model");
		column3.setCellValueFactory(new PropertyValueFactory("model"));
		trucksTableView.getColumns().add(column3);

		// dodawanie kolumny nr rejestracyjny ciê¿arówki
		TableColumn<TrucksIntermediate, String> column4 = new TableColumn("Nr rejestracyjny ciê¿arówki");
		column4.setCellValueFactory(new PropertyValueFactory("licenceNumber"));
		trucksTableView.getColumns().add(column4);

		// dodawanie kolumny maksymalna ³adownoœæ
		TableColumn<TrucksIntermediate, Integer> column5 = new TableColumn("Maksymalna ³adownoœæ");
		column5.setCellValueFactory(new PropertyValueFactory("maxLoad"));
		trucksTableView.getColumns().add(column5);

		// dodawanie kolumny nr rejestracyjny naczepy
		TableColumn<TrucksIntermediate, String> column6 = new TableColumn("Nr rejestracyjny naczepy");
		column6.setCellValueFactory(new PropertyValueFactory("trailerNumber"));
		trucksTableView.getColumns().add(column6);

		// dodawanie kolumny imie kierowcy
		TableColumn<TrucksIntermediate, String> column7 = new TableColumn("Imiê");
		column7.setCellValueFactory(new PropertyValueFactory("driverName"));
		trucksTableView.getColumns().add(column7);

		// dodawanie kolumny nazwisko kierowcy
		TableColumn<TrucksIntermediate, String> column8 = new TableColumn("Nazwisko");
		column8.setCellValueFactory(new PropertyValueFactory("driverSurname"));
		trucksTableView.getColumns().add(column8);

		updateTableView();

		enableInputFields(false);

		updateFilteredLists();

		// nas³uchiwacz do filtrowania

		searchingInputField.textProperty().addListener((observable, oldValue, newValue) -> {
			onSearchTextEdit(newValue);
		});

		// tworzymy tzw "nas³uchiwacz"

		if (selectionListener == null) {
			selectionListener = new ListChangeListener<TrucksIntermediate>() {
				@Override
				public void onChanged(Change<? extends TrucksIntermediate> change) {
					if (change.getList() != null && change.getList().size() > 0) {

						onSelectedTruck(change.getList().get(0));
					}
				}

			};
		}
		enableTableView(true);
	}

	// zaczynamy lub przestajemy nas³uchiwac

	private void enableTableView(boolean enabled) {

		if (enabled) {
			trucksTableView.getSelectionModel().getSelectedItems().addListener(selectionListener);
		} else {
			trucksTableView.getSelectionModel().getSelectedItems().removeListener(selectionListener);
		}

	}

	private void enableEditButttons(boolean enabled) {

		addButton.setDisable(!enabled);
		editButton.setDisable(!enabled);
		deleteButton.setDisable(!enabled);

	}

	private void onSelectedTruck(TrucksIntermediate truck) {

		selectedTruck = truck;

		licenceNumberInputField.setText(selectedTruck.getLicenceNumber());
		brandInputField.setText(selectedTruck.getBrand());
		modelInputField.setText(selectedTruck.getModel());
		maxLoadInputField.setText(selectedTruck.getMaxLoad().toString());

		updateFilteredLists(false);

		int driverIndex;

		for (driverIndex = 0; driverIndex < filteredDrivers.size(); driverIndex++) {

			if (filteredDrivers.get(driverIndex).getId() == truck.getDriver().getId())
				break;

		}

		driverChoiceBox.getSelectionModel().select(driverIndex);

		int trailerIndex;

		for (trailerIndex = 0; trailerIndex < filteredTrailers.size(); trailerIndex++) {

			if (filteredTrailers.get(trailerIndex).getId() == truck.getTrailer().getId())
				break;

		}
		trailerChoiceBox.getSelectionModel().select(trailerIndex);

		editButton.setDisable(truck == null);
		
		editButton.setDisable(false);
		deleteButton.setDisable(false);
	}

	private void clearTruckFields() {

		licenceNumberInputField.setText(null);
		brandInputField.setText(null);
		modelInputField.setText(null);
		maxLoadInputField.setText(null);
		driverChoiceBox.setValue(null);
		trailerChoiceBox.setValue(null);
		editButton.setDisable(true);

	}

	private void updateTableView() {

		TrucksController controller = main.getTrucksController();

		List<Trucks> AllTrucks = controller.searchTruckByLicenceNumber(searchingInputField.getText());
		List<TrucksIntermediate> Trucks = TrucksConverter.convert(AllTrucks);

		trucksTableView.getItems().clear();

		for (TrucksIntermediate truck : Trucks) {

			trucksTableView.getItems().add(truck);
		}
	}

	private void updateFilteredLists() {
		updateFilteredLists(true);
	}

	private void updateFilteredLists(boolean useFilters) {

		filteredDrivers = main.getDriversController().getAllDrivers();

		filteredTrailers = main.getTrailersController().getAllTrailers();

		driverChoiceBox.getItems().clear();

		for (Drivers driver : filteredDrivers) {
			driverChoiceBox.getItems()
					.add(driver.getName() + " " + driver.getSurname() + " " + driver.getDriving_license());
		}

		trailerChoiceBox.getItems().clear();

		for (Trailers trailer : filteredTrailers) {
			trailerChoiceBox.getItems().add(trailer.getTrailerNumber() + " " + trailer.getTrailerType());
		}

	}
	
	public void onSearchTextEdit(String text) {

		updateTableView();

	}

	public void setmainapp(Main main) {

		this.main = main;

	}

	public enum EditingMode {
		adding, editing, none
	}

}
