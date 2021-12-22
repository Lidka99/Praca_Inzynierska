package application.view;

import java.sql.Driver;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
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
import application.controller.UsersController;
import application.model.Drivers;
import application.model.Schedule;
import application.model.Trailers;
import application.model.Trucks;
import application.model.Users;
import application.view.AdminPanelUsersViewController.EditingMode;
import application.view.AdminPanelUsersViewController.ValidatingStatus;
import application.view.intermediate.Converter;
import application.view.intermediate.ScheduleIntermediate;
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

	private ScheduleIntermediate selectedSchedule;

	private EditingMode editingMode = EditingMode.none;

	@FXML
	private Button addButton;
	@FXML
	private Button editButton;
	@FXML
	private Button deleteButton;

	private Main main;

	private ListChangeListener<ScheduleIntermediate> selectionListener;

	private List<Drivers> filteredDrivers;
	private List<Trucks> filteredTrucks;
	private List<Trailers> filteredTrailers;

	public void onDeleteButtonClick() {

		main.getScheduleController().delete(selectedSchedule.getId());
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
			Trailers trailer = filteredTrailers.get(trailerChoiceBox.getSelectionModel().getSelectedIndex());
			Trucks truck = filteredTrucks.get(truckChoiceBox.getSelectionModel().getSelectedIndex());

			// ValidatingStatus nameStatus = validate(name, 30, false);
			// ValidatingStatus surnameStatus = validate(surname, 30, false);
			// ValidatingStatus drivingLicenseStatus = validate(drivingLicense, 30, false);

			// if (nameStatus != ValidatingStatus.sukces || surnameStatus !=
			// ValidatingStatus.sukces
			// || drivingLicenseStatus != ValidatingStatus.sukces) {

			// return;
			// }

			main.getScheduleController().create(scheduledDate, type, driver, trailer, truck);

		}

			break;

		case editing: {

			LocalDate localDate = scheduledDateDatePicker.getValue();
			Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
			Date scheduledDate = Date.from(instant);

			Schedule.Type type = (Schedule.Type) typeChoiceBox.getValue();
			Drivers driver = filteredDrivers.get(driverChoiceBox.getSelectionModel().getSelectedIndex());
			Trailers trailer = filteredTrailers.get(trailerChoiceBox.getSelectionModel().getSelectedIndex());
			Trucks truck = filteredTrucks.get(truckChoiceBox.getSelectionModel().getSelectedIndex());

			/*
			 * ValidatingStatus nameStatus = validate(name, 30, false); ValidatingStatus
			 * surnameStatus = validate(surname, 30, false); ValidatingStatus
			 * drivingLicenseStatus = validate(drivingLicense, 10, false); // !!!!! obs�u�y�
			 * sprawdzanie // unikatowo�ci // w przypadku edycji !!!!!
			 * 
			 * if (nameStatus != ValidatingStatus.sukces || surnameStatus !=
			 * ValidatingStatus.sukces || drivingLicenseStatus != ValidatingStatus.sukces) {
			 * 
			 * return; }
			 */

			main.getScheduleController().update(selectedSchedule.getId(), scheduledDate, type, driver, trailer, truck);
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

		scheduledDateDatePicker.setDisable(!enabled);
		typeChoiceBox.setDisable(!enabled);
		driverChoiceBox.setDisable(!enabled);
		truckChoiceBox.setDisable(!enabled);
		trailerChoiceBox.setDisable(!enabled);

	}

	public void setUp() {

		// dodawanie kolumny id
		TableColumn<ScheduleIntermediate, Integer> column1 = new TableColumn("Id");
		column1.setCellValueFactory(new PropertyValueFactory("id"));
		scheduleTableView.getColumns().add(column1);

		// dodawanie kolumny planowana data
		TableColumn<ScheduleIntermediate, String> column2 = new TableColumn("Planowana data przyjazdu");
		column2.setCellValueFactory(new PropertyValueFactory("scheduled_date"));
		scheduleTableView.getColumns().add(column2);

		// dodawanie kolumny rodzaj
		TableColumn<ScheduleIntermediate, String> column5 = new TableColumn("Rodzaj");
		column5.setCellValueFactory(new PropertyValueFactory("type"));
		scheduleTableView.getColumns().add(column5);

		// dodawanie kolumny imie kierowcy
		TableColumn<ScheduleIntermediate, String> column6 = new TableColumn("Imi� kierowcy");
		column6.setCellValueFactory(new PropertyValueFactory("driverName"));
		scheduleTableView.getColumns().add(column6);

		// dodawanie kolumny nazwisko kierowcy
		TableColumn<ScheduleIntermediate, String> column7 = new TableColumn("Nazwisko kierowcy");
		column7.setCellValueFactory(new PropertyValueFactory("driverSurname"));
		scheduleTableView.getColumns().add(column7);

		// dodawanie kolumny nr rejestracyjny naczepy
		TableColumn<ScheduleIntermediate, String> column8 = new TableColumn("Nr rejestracyjny naczepy");
		column8.setCellValueFactory(new PropertyValueFactory("trailerNumber"));
		scheduleTableView.getColumns().add(column8);

		// dodawanie kolumny nr rejestracyjny auta
		TableColumn<ScheduleIntermediate, String> column9 = new TableColumn("Nr rejestracyjny auta");
		column9.setCellValueFactory(new PropertyValueFactory("truckLicenceNumber"));
		scheduleTableView.getColumns().add(column9);

		updateTableView();

		enableInputFields(false);

		updateFilteredLists();

		typeChoiceBox.getItems().setAll(Schedule.Type.values());

		// tworzymy tzw "nas�uchiwacz"

		if (selectionListener == null) {
			selectionListener = new ListChangeListener<ScheduleIntermediate>() {
				@Override
				public void onChanged(Change<? extends ScheduleIntermediate> change) {
					if (change.getList() != null && change.getList().size() > 0) {

						onSelectedSchedule(change.getList().get(0));

					}
				}

			};
		}
		enableTableView(true);
	}

	// zaczynamy lub przestajemy nas�uchiwac

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

	private void onSelectedSchedule(ScheduleIntermediate schedule) {

		selectedSchedule = schedule;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
		scheduledDateDatePicker.setValue(LocalDate.parse(schedule.getScheduled_date(), formatter));
		typeChoiceBox.setValue(Schedule.Type.valueOf(schedule.getType()));

		updateFilteredLists(false);

		int driverIndex;

		for (driverIndex = 0; driverIndex < filteredDrivers.size(); driverIndex++) {

			if (filteredDrivers.get(driverIndex).getId() == schedule.getDriver().getId())
				break;

		}
		driverChoiceBox.getSelectionModel().select(driverIndex);

		int truckIndex;

		for (truckIndex = 0; truckIndex < filteredTrucks.size(); truckIndex++) {

			if (filteredTrucks.get(truckIndex).getId() == schedule.getTruck().getId())
				break;

		}
		truckChoiceBox.getSelectionModel().select(truckIndex);

		int trailerIndex;

		for (trailerIndex = 0; trailerIndex < filteredTrailers.size(); trailerIndex++) {

			if (filteredTrailers.get(trailerIndex).getId() == schedule.getTrailer().getId())
				break;

		}
		trailerChoiceBox.getSelectionModel().select(trailerIndex);

	}

	private void clearScheduleFields() {

		scheduledDateDatePicker.setValue(null);
		typeChoiceBox.setValue(null);
		driverChoiceBox.setValue(null);
		truckChoiceBox.setValue(null);
		trailerChoiceBox.setValue(null);

	}

	private void updateTableView() {

		ScheduleController controller = main.getScheduleController();

		List<Schedule> allSchedules = controller.getAllSchedules();
		List<ScheduleIntermediate> schedules = Converter.convert(allSchedules);

		scheduleTableView.getItems().clear();

		for (ScheduleIntermediate schedule : schedules) {

			scheduleTableView.getItems().add(schedule);
		}
	}

	private void updateFilteredLists() {
		updateFilteredLists(true);
	}

	private void updateFilteredLists(boolean useFilters) {

		filteredDrivers = main.getDriversController().getAllDrivers();

		filteredTrucks = main.getTrucksController().getAllTrucks();

		filteredTrailers = main.getTrailersController().getAllTrailers();
		
		driverChoiceBox.getItems().clear();

		for (Drivers driver : filteredDrivers) {
			driverChoiceBox.getItems()
					.add(driver.getName() + " " + driver.getSurname() + " " + driver.getDriving_license());
		}
		
		truckChoiceBox.getItems().clear();

		for (Trucks truck : filteredTrucks) {
			truckChoiceBox.getItems().add(truck.getLicence_number() + " " + truck.getModel());
		}
		
		trailerChoiceBox.getItems().clear();

		for (Trailers trailer : filteredTrailers) {
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