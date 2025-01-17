package application.view;

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
import application.controller.ScheduleController;
import application.controller.UsersController;
import application.model.Drivers;
import application.model.Schedule;
import application.model.Trailers;
import application.model.Trucks;
import application.model.Users;
import application.model.Users.Role;
import application.view.intermediate.ScheduleConverter;
import application.view.intermediate.ScheduleIntermediate;
import application.view.intermediate.ScheduleIntermediateComparator;
import application.view.intermediate.TimeRaportEntryComparator;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.text.SimpleDateFormat;

public class ArrivalsDeparturesViewController {

	@FXML
	private TextField drivingLicenseTextField;
	@FXML
	private TextField truckLicenseNumberTextField;
	@FXML
	private TextField trailerLicenseNumberTextField;

	@FXML
	private Button letInButton;
	@FXML
	private Button letOutButton2;

	@FXML
	private TableView inWarehouseTableView;
	@FXML
	private TableView scheduledTableView;

	private ScheduleIntermediate selectedScheduleInWarehouse;
	private ScheduleIntermediate selectedScheduleInScheduled;

	private ListChangeListener<ScheduleIntermediate> selectionListenerInWarehouse;
	private ListChangeListener<ScheduleIntermediate> selectionListenerInScheduled;

	private Main main;

	public void onLetInButtonClick() {

		// nadpisywanie arrival date aktualna data

		if (selectedScheduleInScheduled != null) {
			Date currentDate = new Date(System.currentTimeMillis());
			main.getScheduleController().updateArrivalDate(selectedScheduleInScheduled.getId(), currentDate);
			selectedScheduleInScheduled = null;
			updateScheduledTableView();
		}

	}

	public void onLetOutButton2Click() {

		if (selectedScheduleInWarehouse != null) {
			Date currentDate = new Date(System.currentTimeMillis());
			main.getScheduleController().updateDepartureDate(selectedScheduleInWarehouse.getId(), currentDate);
			selectedScheduleInWarehouse = null;
			updateInWarehouseTableView();
		}

	}

	private void updateInWarehouseTableView() {

		ScheduleController controller = main.getScheduleController();

		List<ScheduleIntermediate> schedules = ScheduleConverter.convert(controller.getSchedulesInWarehouse());
		inWarehouseTableView.getItems().clear();
		
		Collections.sort(schedules, new ScheduleIntermediateComparator());

		for (ScheduleIntermediate schedule : schedules) {

			inWarehouseTableView.getItems().add(schedule);
		}
	}

	private void updateScheduledTableView() {

		ScheduleController controller = main.getScheduleController();

		List<ScheduleIntermediate> schedules = ScheduleConverter
				.convert(controller.getFilteredSchedulesForScheduled(drivingLicenseTextField.getText(),
						truckLicenseNumberTextField.getText(), trailerLicenseNumberTextField.getText()));
		scheduledTableView.getItems().clear();

		Collections.sort(schedules, new ScheduleIntermediateComparator());
		
		for (ScheduleIntermediate schedule : schedules) {

			scheduledTableView.getItems().add(schedule);
		}
	}

	public void setUp() {

		// do wypuszczania

		// dodawanie kolumny id
//		TableColumn<ScheduleIntermediate, Integer> column1 = new TableColumn("Id");
//		column1.setCellValueFactory(new PropertyValueFactory("id"));
//		inWarehouseTableView.getColumns().add(column1);

		// dodawanie kolumny planowana data
		TableColumn<ScheduleIntermediate, String> column2 = new TableColumn("Planowana data przyjazdu");
		column2.setCellValueFactory(new PropertyValueFactory("scheduled_date"));
		inWarehouseTableView.getColumns().add(column2);

		// dodawanie kolumny data przyjazdu
		TableColumn<ScheduleIntermediate, String> column33 = new TableColumn("Data przyjazdu");
		column33.setCellValueFactory(new PropertyValueFactory("arrival_date"));
		inWarehouseTableView.getColumns().add(column33);

		// dodawanie kolumny rodzaj
		TableColumn<ScheduleIntermediate, String> column5 = new TableColumn("Rodzaj");
		column5.setCellValueFactory(new PropertyValueFactory("type"));
		inWarehouseTableView.getColumns().add(column5);

		// dodawanie kolumny imie kierowcy
		TableColumn<ScheduleIntermediate, String> column6 = new TableColumn("Imię kierowcy");
		column6.setCellValueFactory(new PropertyValueFactory("driverName"));
		inWarehouseTableView.getColumns().add(column6);

		// dodawanie kolumny nazwisko kierowcy
		TableColumn<ScheduleIntermediate, String> column7 = new TableColumn("Nazwisko kierowcy");
		column7.setCellValueFactory(new PropertyValueFactory("driverSurname"));
		inWarehouseTableView.getColumns().add(column7);

		// dodawanie kolumny prawo jazdy
		TableColumn<ScheduleIntermediate, String> column71 = new TableColumn("Nr prawa jazdy");
		column71.setCellValueFactory(new PropertyValueFactory("driverLicenceNumber"));
		inWarehouseTableView.getColumns().add(column71);

		// dodawanie kolumny nr rejestracyjny naczepy
		TableColumn<ScheduleIntermediate, String> column8 = new TableColumn("Nr rejestracyjny naczepy");
		column8.setCellValueFactory(new PropertyValueFactory("trailerNumber"));
		inWarehouseTableView.getColumns().add(column8);

		// dodawanie kolumny nr rejestracyjny auta
		TableColumn<ScheduleIntermediate, String> column9 = new TableColumn("Nr rejestracyjny auta");
		column9.setCellValueFactory(new PropertyValueFactory("truckLicenceNumber"));
		inWarehouseTableView.getColumns().add(column9);

		// do wpuszczania

		// dodawanie kolumny id
//		TableColumn<ScheduleIntermediate, Integer> column11 = new TableColumn("Id");
//		column11.setCellValueFactory(new PropertyValueFactory("id"));
//		scheduledTableView.getColumns().add(column11);

		// dodawanie kolumny planowana data
		TableColumn<ScheduleIntermediate, String> column22 = new TableColumn("Planowana data przyjazdu");
		column22.setCellValueFactory(new PropertyValueFactory("scheduled_date"));
		scheduledTableView.getColumns().add(column22);

		// dodawanie kolumny rodzaj
		TableColumn<ScheduleIntermediate, String> column55 = new TableColumn("Rodzaj");
		column55.setCellValueFactory(new PropertyValueFactory("type"));
		scheduledTableView.getColumns().add(column55);

		// dodawanie kolumny imie kierowcy
		TableColumn<ScheduleIntermediate, String> column66 = new TableColumn("Imię kierowcy");
		column66.setCellValueFactory(new PropertyValueFactory("driverName"));
		scheduledTableView.getColumns().add(column66);

		// dodawanie kolumny nazwisko kierowcy
		TableColumn<ScheduleIntermediate, String> column77 = new TableColumn("Nazwisko kierowcy");
		column77.setCellValueFactory(new PropertyValueFactory("driverSurname"));
		scheduledTableView.getColumns().add(column77);

		// dodawanie kolumny prawo jazdy
		TableColumn<ScheduleIntermediate, String> column771 = new TableColumn("Nr prawa jazdy");
		column771.setCellValueFactory(new PropertyValueFactory("driverLicenceNumber"));
		scheduledTableView.getColumns().add(column771);

		// dodawanie kolumny nr rejestracyjny naczepy
		TableColumn<ScheduleIntermediate, String> column88 = new TableColumn("Nr rejestracyjny naczepy");
		column88.setCellValueFactory(new PropertyValueFactory("trailerNumber"));
		scheduledTableView.getColumns().add(column88);

		// dodawanie kolumny nr rejestracyjny auta
		TableColumn<ScheduleIntermediate, String> column99 = new TableColumn("Nr rejestracyjny auta");
		column99.setCellValueFactory(new PropertyValueFactory("truckLicenceNumber"));
		scheduledTableView.getColumns().add(column99);


		inWarehouseTableView.setPlaceholder(new Label ("Brak pojazdów aktualnie przebywających na terenie magazynu"));
		updateInWarehouseTableView();
		scheduledTableView.setPlaceholder(new Label ("Nie znaleziono danych"));
		updateScheduledTableView();

		// nasłuchiwacz

		if (selectionListenerInWarehouse == null) {
			selectionListenerInWarehouse = new ListChangeListener<ScheduleIntermediate>() {
				@Override
				public void onChanged(Change<? extends ScheduleIntermediate> change) {
					if (change.getList() != null && change.getList().size() > 0) {

						onSelectedScheduleInWarehouse(change.getList().get(0));

					}
				}

			};
			inWarehouseTableView.getSelectionModel().getSelectedItems().addListener(selectionListenerInWarehouse);
		}

		// nasłuchiwacz2

		if (selectionListenerInScheduled == null) {
			selectionListenerInScheduled = new ListChangeListener<ScheduleIntermediate>() {
				@Override
				public void onChanged(Change<? extends ScheduleIntermediate> change) {
					if (change.getList() != null && change.getList().size() > 0) {

						onSelectedScheduleInScheduled(change.getList().get(0));

					}
				}

			};
			scheduledTableView.getSelectionModel().getSelectedItems().addListener(selectionListenerInScheduled);
		}

		// nasłuchiwacz do filtrowania1

		drivingLicenseTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			onSearchTextEdit(newValue);
		});

		// nasłuchiwacz do filtrowania2

		truckLicenseNumberTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			onSearchTextEdit(newValue);
		});

		// nasłuchiwacz do filtrowania3

		trailerLicenseNumberTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			onSearchTextEdit(newValue);
		});

	}

	private void onSearchTextEdit(String newValue) {
		updateScheduledTableView();
	}

	private void onSelectedScheduleInWarehouse(ScheduleIntermediate schedule) {

		selectedScheduleInWarehouse = schedule;

	}

	private void onSelectedScheduleInScheduled(ScheduleIntermediate schedule) {

		selectedScheduleInScheduled = schedule;

	}

	public void setmainapp(Main main) {

		this.main = main;

	}

}
