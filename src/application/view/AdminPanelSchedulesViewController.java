package application.view;

import java.sql.Driver;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
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
import application.model.Schedule;
import application.model.Trailers;
import application.model.Trucks;
import application.model.Users;
import application.view.AdminPanelUsersViewController.EditingMode;
import application.view.AdminPanelUsersViewController.ValidatingStatus;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminPanelSchedulesViewController {

	@FXML
	private TableView scheduleTableView;
	
	@FXML
	private DatePicker scheduledDateDatePicker;
	
	@FXML
	private ChoiceBox typeChoiceBox;
	
	@FXML
	private ChoiceBox driverChoiceBox;
	
	@FXML
	private ChoiceBox truckChoiceBox;

	@FXML
	private ChoiceBox trailerChoiceBox;
	
	

	private Schedule selectedSchedule;
	
	private EditingMode editingMode = EditingMode.none;

	@FXML
	private Button addButton;
	@FXML
	private Button editButton;
	@FXML
	private Button deleteButton;

	private Main main;

	private ListChangeListener<Schedule> selectionListener;
	
	private List <Drivers> filteredDrivers;
	private List <Trucks> filteredTrucks;
	private List <Trailers> filteredTrailers;
	

	public void onDeleteButtonClick() {

		main.getScheduleController().delete(selectedSchedule);
		updateTableView();
		clearScheduleFields();

	}

	public void onCreateButtonCick() {

		clearScheduleFields();
		enableTableView(false);
		enableEditButttons(false);
		editingMode = EditingMode.adding;
		enableInputFields(true);

	}

	public void onEditButtonCick() {

		enableTableView(false);
		enableEditButttons(false);
		editingMode = EditingMode.editing;
		enableInputFields(true);

	}

	public void onSaveButtonCick() {

		switch (editingMode) {
		case adding: {

			LocalDate localDate = scheduledDateDatePicker.getValue();
			Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
			Date scheduledDate = Date.from(instant);
			
			Schedule.Type type = (Schedule.Type) typeChoiceBox.getValue();
			Drivers driver = filteredDrivers.get(driverChoiceBox.getSelectionModel().getSelectedIndex());

			//ValidatingStatus nameStatus = validate(name, 30, false);
			//ValidatingStatus surnameStatus = validate(surname, 30, false);
			//ValidatingStatus drivingLicenseStatus = validate(drivingLicense, 30, false);

			/*if (nameStatus != ValidatingStatus.sukces || surnameStatus != ValidatingStatus.sukces
					|| drivingLicenseStatus != ValidatingStatus.sukces) {

				return;
			}
			*/
		}
		
		break;

		case editing: {

			String name = nameInputField.getText();
			String surname = surnameInputField.getText();
			String drivingLicense = driving_licenseInputField.getText();

			ValidatingStatus nameStatus = validate(name, 30, false);
			ValidatingStatus surnameStatus = validate(surname, 30, false);
			ValidatingStatus drivingLicenseStatus = validate(drivingLicense, 10, false); // !!!!! obsłużyć sprawdzanie
																							// unikatowości
			// w przypadku edycji !!!!!

			if (nameStatus != ValidatingStatus.sukces || surnameStatus != ValidatingStatus.sukces
					|| drivingLicenseStatus != ValidatingStatus.sukces) {

				return;
			}
		}
		break;

		default:
			break;
		}

		clearScheduleFields();
		updateTableView();
		enableTableView(true);
		enableEditButttons(true);
		enableInputFields(false);
		editingMode = EditingMode.none;

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

		clearScheduleFields();
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
		
	}

	public void setUp() {

		// dodawanie kolumny id
		TableColumn<Drivers, Integer> column1 = new TableColumn("Id");
		column1.setCellValueFactory(new PropertyValueFactory("id"));
		scheduleTableView.getColumns().add(column1);

		// dodawanie kolumny imie
		TableColumn<Drivers, String> column2 = new TableColumn("Imię");
		column2.setCellValueFactory(new PropertyValueFactory("name"));
		scheduleTableView.getColumns().add(column2);

		// dodawanie kolumny nazwisko
		TableColumn<Drivers, String> column3 = new TableColumn("Nazwisko");
		column3.setCellValueFactory(new PropertyValueFactory("surname"));
		scheduleTableView.getColumns().add(column3);

		// dodawanie kolumny numer prawa jazdy
		TableColumn<Drivers, String> column4 = new TableColumn("Numer prawa jazdy");
		column4.setCellValueFactory(new PropertyValueFactory("driving_license"));
		scheduleTableView.getColumns().add(column4);

	
		updateTableView();

		enableInputFields(false);
		
		updateFilteredLists();
		
		typeChoiceBox.getItems().setAll(Schedule.Type.values());



		// tworzymy tzw "nasłuchiwacz"

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

	// zaczynamy lub przestajemy nasłuchiwac

	private void enableTableView(boolean enabled) {

		if (enabled) {
			scheduleTableView.getSelectionModel().getSelectedItems().addListener(selectionListener);
		} else {
			scheduleTableView.getSelectionModel().getSelectedItems().removeListener(selectionListener);
		}

	}

	private void enableEditButttons(boolean enabled) {

		addButton.setDisable(!enabled);
		editButton.setDisable(!enabled);
		deleteButton.setDisable(!enabled);

	}

	private void onSelectedDriver(Drivers driver) {

		selectedSchedule = driver;
		nameInputField.setText(selectedSchedule.getName());
		surnameInputField.setText(selectedSchedule.getSurname());
		driving_licenseInputField.setText(selectedSchedule.getDriving_license());
		

	}

	private void clearScheduleFields() {

		nameInputField.setText(null);
		surnameInputField.setText(null);
		driving_licenseInputField.setText(null);
		
	}

	private void updateTableView() {

		DriversController controller = main.getDriversController();

		List<Drivers> AllDrivers = controller.getAllDrivers();

		scheduleTableView.getItems().clear();

		for (Drivers driver : AllDrivers) {

			scheduleTableView.getItems().add(driver);
		}
	}
	
	
	private void updateFilteredLists() {
		
		filteredDrivers = main.getDriversController().getAllDrivers();
		
		filteredTrucks = main.getTrucksController().getAllTrucks();
		
		filteredTrailers = main.getTrailersController().getAllTrailers();
		
		for(Drivers driver : filteredDrivers) {
			driverChoiceBox.getItems().add(driver.getName() + " " + driver.getSurname() + " " + driver.getDriving_license());
		}
		
		for(Trucks truck : filteredTrucks) {
			truckChoiceBox.getItems().add(truck.getLicence_number() + " " + truck.getModel());
		}
		
		for(Trailers trailer : filteredTrailers) {
			trailerChoiceBox.getItems().add(trailer.getTrailer_number() + " " + trailer.getTrailer_type());
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
