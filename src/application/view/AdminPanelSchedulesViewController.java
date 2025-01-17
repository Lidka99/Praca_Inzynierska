package application.view;

import java.sql.Driver;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
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
import application.controller.UsersController;
import application.model.Drivers;
import application.model.Schedule;
import application.model.Trailers;
import application.model.Trucks;
import application.model.Users;
import application.view.AdminPanelUsersViewController.EditingMode;
import application.view.AdminPanelUsersViewController.ValidatingStatus;
import application.view.intermediate.ScheduleConverter;
import application.view.intermediate.ScheduleIntermediate;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.converter.LocalTimeStringConverter;

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

	@FXML
	private Spinner hourSpinner;

	@FXML
	private Spinner minuteSpinner;

	private ScheduleIntermediate selectedSchedule;

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
	private DatePicker filterDateDatePicker;
	private Date selectedDate;

	private Main main;

	private ListChangeListener<ScheduleIntermediate> selectionListener;

	private List<Drivers> filteredDrivers;
	private List<Trucks> filteredTrucks;
	private List<Trailers> filteredTrailers;

	public void onDeleteButtonClick() {

		if (selectedSchedule != null) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Usuni�cie transportu");
			alert.setHeaderText("Masz zamiar usun�� transport");
			alert.setContentText("Czy na pewno chcesz to zrobi�?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				main.getScheduleController().delete(selectedSchedule.getId());
				updateTableView();
				clearScheduleFields();

			} else {

			}

		}
	}

	public void onCreateButtonCick() {

		clearScheduleFields();
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

			LocalDate localDate = scheduledDateDatePicker.getValue();
			Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
			Date scheduledDate = Date.from(instant);

			LocalTime time = (LocalTime) hourSpinner.getValue();
			long msHour = (long) time.getHour() * 60 * 60 * 1000;
			long msMinute = (long) time.getMinute() * 60 * 1000;
			scheduledDate = new Date(scheduledDate.getTime() + msHour + msMinute);

			Schedule.Type type = (Schedule.Type) typeChoiceBox.getValue();
			Drivers driver = filteredDrivers.get(driverChoiceBox.getSelectionModel().getSelectedIndex());
			Trailers trailer = filteredTrailers.get(trailerChoiceBox.getSelectionModel().getSelectedIndex());
			Trucks truck = filteredTrucks.get(truckChoiceBox.getSelectionModel().getSelectedIndex());

			if (validate(scheduledDate, driver, truck, trailer) == false) {
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("B��d zapisu");
				alert.setHeaderText("Taki zestaw figuruje ju� w danym dniu");
				
				alert.show();
				return;
			}

			main.getScheduleController().create(scheduledDate, type, driver, trailer, truck);

		}

			break;

		case editing: {

			LocalDate localDate = scheduledDateDatePicker.getValue();
			Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
			Date scheduledDate = Date.from(instant);

			LocalTime time = (LocalTime) hourSpinner.getValue();
			long msHour = (long) time.getHour() * 60 * 60 * 1000;
			long msMinute = (long) time.getMinute() * 60 * 1000;
			scheduledDate = new Date(scheduledDate.getTime() + msHour + msMinute);

			Schedule.Type type = (Schedule.Type) typeChoiceBox.getValue();
			Drivers driver = filteredDrivers.get(driverChoiceBox.getSelectionModel().getSelectedIndex());
			Trailers trailer = filteredTrailers.get(trailerChoiceBox.getSelectionModel().getSelectedIndex());
			Trucks truck = filteredTrucks.get(truckChoiceBox.getSelectionModel().getSelectedIndex());

		//	if (validate(scheduledDate, driver, truck, trailer)) {
				
				//Alert alert = new Alert(AlertType.INFORMATION);
				//alert.setTitle("B��d zapisu");
				//alert.setHeaderText("Taki zestaw figuruje ju� w danym dniu");
				
				//alert.show();
				//return;
			//}

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

		editButton.setDisable(true);
		deleteButton.setDisable(true);
		cancelButton.setDisable(true);
		saveButton.setDisable(true);

	}

	private boolean validate(Date scheduledDate, Drivers driver, Trucks truck, Trailers trailer) {

		List<Schedule> schedules = main.getScheduleController().getSchedulesByData(driver, trailer, truck);
		Date scheduledDateWithZeroTime = null;

		try {
			scheduledDateWithZeroTime = Main.getDateFormat().parse(Main.getDateFormat().format(scheduledDate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (scheduledDateWithZeroTime != null) {
			for (Schedule schedule : schedules) {

				Date dateWithZeroTime = null;

				try {
					dateWithZeroTime = Main.getDateFormat()
							.parse(Main.getDateFormat().format(schedule.getScheduled_date()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (dateWithZeroTime != null && dateWithZeroTime.equals(scheduledDateWithZeroTime)) {

					return false;
				}

			}

		}

		return true;
	}

	public void onCancelButtonCick() {

		clearScheduleFields();
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

		scheduledDateDatePicker.setDisable(!enabled);
		typeChoiceBox.setDisable(!enabled);
		driverChoiceBox.setDisable(!enabled);
		truckChoiceBox.setDisable(!enabled);
		trailerChoiceBox.setDisable(!enabled);
		hourSpinner.setDisable(!enabled);

	}

	public void setUp() {

		editButton.setDisable(true);
		deleteButton.setDisable(true);
		cancelButton.setDisable(true);
		saveButton.setDisable(true);

		// dodawanie kolumny id
		TableColumn<ScheduleIntermediate, Integer> column1 = new TableColumn("Id");
		column1.setCellValueFactory(new PropertyValueFactory("id"));
		scheduleTableView.getColumns().add(column1);

		// dodawanie kolumny planowana data
		TableColumn<ScheduleIntermediate, String> column2 = new TableColumn("Planowana data przyjazdu");
		column2.setCellValueFactory(new PropertyValueFactory("scheduled_date"));
		scheduleTableView.getColumns().add(column2);

		// dodawanie kolumny data przyjazdu
		TableColumn<ScheduleIntermediate, String> column22 = new TableColumn("Data przyjazdu");
		column22.setCellValueFactory(new PropertyValueFactory("arrival_date"));
		scheduleTableView.getColumns().add(column22);

		// dodawanie kolumny data wyjazdu
		TableColumn<ScheduleIntermediate, String> column222 = new TableColumn("Data wyjazdu");
		column222.setCellValueFactory(new PropertyValueFactory("departure_date"));
		scheduleTableView.getColumns().add(column222);

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

		// tworzymy tzw "nas�uchiwacz" do filtrowania po dacie

		filterDateDatePicker.valueProperty().addListener((observable, oldDate, newDate) -> {

			// aktualnie wybrana data
			if (newDate != null) {

				ZoneId defaultZoneId = ZoneId.systemDefault();
				selectedDate = Date.from(newDate.atStartOfDay(defaultZoneId).toInstant());

			}

			else {
				selectedDate = null;
			}
			updateTableView();
		});

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
		// Value factory spinner hours.
		SpinnerValueFactory<LocalTime> valueFactory = new SpinnerValueFactory() {

			{
				setConverter(new LocalTimeStringConverter(FormatStyle.MEDIUM));
			}

			@Override
			public void decrement(int steps) {
				if (getValue() == null) {

					LocalTime time = LocalTime.now();
					setValue(time.withMinute((time.getMinute() / 15) * 15).withSecond(0));
				}

				else {
					LocalTime time = (LocalTime) getValue();
					time = time.withMinute((time.getMinute() / 15) * 15);
					time = time.withSecond(0);

					setValue(time.minusMinutes(steps * 15));
				}
			}

			@Override
			public void increment(int steps) {
				if (this.getValue() == null) {
					LocalTime time = LocalTime.now();
					setValue(time.withMinute((time.getMinute() / 15) * 15).withSecond(0));
				} else {
					LocalTime time = (LocalTime) getValue();
					time = time.withMinute((time.getMinute() / 15) * 15);
					time = time.withSecond(0);

					setValue(time.plusMinutes(steps * 15));
				}
			}
		};

		hourSpinner.setValueFactory(valueFactory);

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
		try {
			Date scheduledTime = Main.getTimeFormat()
					.parse(Main.getTimeFormat().format(Main.getDateTimeFormat().parse(schedule.getScheduled_date())));
			// int hours = (int) (scheduledTime.getTime() / 1000 / 60 / 60)+1;
			// int minutes = (int) (scheduledTime.getTime() / 1000 / 60) % 60;
			// hourSpinner.getValueFactory().setValue(hours);
			// minuteSpinner.getValueFactory().setValue(minutes);

			LocalTime time = LocalDateTime.ofInstant(scheduledTime.toInstant(), ZoneId.systemDefault()).toLocalTime();

			hourSpinner.getValueFactory().setValue(time);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		typeChoiceBox.setValue(Schedule.Type.valueOf(schedule.getType()));

		editButton.setDisable(schedule == null);
		editButton.setDisable(false);
		deleteButton.setDisable(false);

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

		editButton.setDisable(true);

	}

	private void updateTableView() {

		ScheduleController controller = main.getScheduleController();

		List<Schedule> allSchedules = null;
		if (selectedDate != null) {
			allSchedules = controller.getSchedulesByScheduledDate(selectedDate);

		} else {
			allSchedules = controller.getAllSchedules();

		}

		List<ScheduleIntermediate> schedules = ScheduleConverter.convert(allSchedules);

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
			truckChoiceBox.getItems().add(truck.getLicenceNumber() + " " + truck.getModel());
		}

		trailerChoiceBox.getItems().clear();

		for (Trailers trailer : filteredTrailers) {
			trailerChoiceBox.getItems().add(trailer.getTrailerNumber() + " " + trailer.getTrailerType());
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
