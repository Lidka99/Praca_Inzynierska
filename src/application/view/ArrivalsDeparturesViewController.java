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
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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
	private TextField scheduledDateTextField;
	@FXML
	private TextField typeTextField;
	@FXML
	private TextField nameSurnameTextField;
	@FXML
	private TextField brandTextField;
	@FXML
	private TextField trailerTypeTextField;
	@FXML
	private AnchorPane infoPanel;
	@FXML
	private Button letInButton;
	@FXML
	private Button letOutButton;
	@FXML
	private Button letOutButton2;
	@FXML
	private TableView inWarehouseTableView;

	private Schedule selectedSchedule;
	private ScheduleIntermediate selectedScheduleIntermediate;

	private ListChangeListener<ScheduleIntermediate> selectionListener;

	private Main main;

	public void onSearchButtonClick() {

		selectedSchedule = null;

		String driverLicenseNumber = drivingLicenseTextField.getText().trim();
		Drivers driver = main.getDriversController().getByDriverLicense(driverLicenseNumber);

		String truckLicenseNumber = truckLicenseNumberTextField.getText().trim();
		Trucks truck = main.getTrucksController().getByTruckNumber(truckLicenseNumber);

		String trailerLicenseNumber = trailerLicenseNumberTextField.getText().trim();
		Trailers trailer = main.getTrailersController().getByTrailerNumber(trailerLicenseNumber);

		if (driver != null && truck != null && trailer != null) {

			List<Schedule> schedules = main.getScheduleController().getSchedulesByData(driver, trailer, truck);

			for (Schedule schedule : schedules) {

				if (schedule.getDeparture_date() == null) {
					if (selectedSchedule == null) {
						selectedSchedule = schedule;
					} else if (selectedSchedule.getScheduled_date().compareTo(schedule.getScheduled_date()) > 0) {
						selectedSchedule = schedule;
					}
				}

			}

			// tworzenie alertu
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Brak wyników");
			alert.setHeaderText("Nie znaleziono danych w bazie!");
			alert.setContentText("Wpisz dane jeszcze raz albo skontaktuj siê z administratorem.");

			alert.showAndWait();
		}

		updateInfoPanel();

	}

	private void updateInfoPanel() {

		if (selectedSchedule != null) {
			infoPanel.setVisible(true);
			letInButton.setVisible(selectedSchedule.getArrival_date() == null);
			letOutButton.setVisible(
					selectedSchedule.getArrival_date() != null && selectedSchedule.getDeparture_date() == null);
			scheduledDateTextField.setText(selectedSchedule.getScheduled_date().toString());
			nameSurnameTextField
					.setText(selectedSchedule.getDriver().getName() + ' ' + selectedSchedule.getDriver().getSurname());
			typeTextField.setText(selectedSchedule.getType().toString());
			brandTextField.setText(selectedSchedule.getTruck().getBrand());
			trailerTypeTextField.setText(selectedSchedule.getTrailer().getTrailerType());

		} else {
			infoPanel.setVisible(false);
		}

	}

	public void onLetInButtonClick() {

		// nadpisywanie arrival date aktualna data

		if (selectedSchedule != null) {
			Date currentDate = new Date(System.currentTimeMillis());
			main.getScheduleController().updateArrivalDate(selectedSchedule.getId(), currentDate);
			selectedSchedule = main.getScheduleController().getSchedule(selectedSchedule.getId());
			updateInfoPanel();
		}

	}

	public void onLetOutButtonClick() {

		if (selectedSchedule != null) {
			Date currentDate = new Date(System.currentTimeMillis());
			main.getScheduleController().updateDepartureDate(selectedSchedule.getId(), currentDate);
			selectedSchedule = main.getScheduleController().getSchedule(selectedSchedule.getId());
			updateInfoPanel();
		}

	}

	public void onLetOutButton2Click() {

		if (selectedScheduleIntermediate != null) {
			Date currentDate = new Date(System.currentTimeMillis());
			main.getScheduleController().updateDepartureDate(selectedScheduleIntermediate.getId(), currentDate);
			selectedScheduleIntermediate = null;
			updateTableView();
		}

	}

	private void updateTableView() {

		ScheduleController controller = main.getScheduleController();

		List<ScheduleIntermediate> schedules = ScheduleConverter.convert(controller.getSchedulesInWarehouse());
		inWarehouseTableView.getItems().clear();

		for (ScheduleIntermediate schedule : schedules) {

			inWarehouseTableView.getItems().add(schedule);
		}
	}

	public void setUp() {

		// dodawanie kolumny id
		TableColumn<ScheduleIntermediate, Integer> column1 = new TableColumn("Id");
		column1.setCellValueFactory(new PropertyValueFactory("id"));
		inWarehouseTableView.getColumns().add(column1);

		// dodawanie kolumny planowana data
		TableColumn<ScheduleIntermediate, String> column2 = new TableColumn("Planowana data przyjazdu");
		column2.setCellValueFactory(new PropertyValueFactory("scheduled_date"));
		inWarehouseTableView.getColumns().add(column2);

		// dodawanie kolumny data przyjazdu
		TableColumn<ScheduleIntermediate, String> column3 = new TableColumn("Data przyjazdu");
		column3.setCellValueFactory(new PropertyValueFactory("arrival_date"));
		inWarehouseTableView.getColumns().add(column3);

		// dodawanie kolumny rodzaj
		TableColumn<ScheduleIntermediate, String> column5 = new TableColumn("Rodzaj");
		column5.setCellValueFactory(new PropertyValueFactory("type"));
		inWarehouseTableView.getColumns().add(column5);

		// dodawanie kolumny imie kierowcy
		TableColumn<ScheduleIntermediate, String> column6 = new TableColumn("Imiê kierowcy");
		column6.setCellValueFactory(new PropertyValueFactory("driverName"));
		inWarehouseTableView.getColumns().add(column6);

		// dodawanie kolumny nazwisko kierowcy
		TableColumn<ScheduleIntermediate, String> column7 = new TableColumn("Nazwisko kierowcy");
		column7.setCellValueFactory(new PropertyValueFactory("driverSurname"));
		inWarehouseTableView.getColumns().add(column7);

		// dodawanie kolumny nr rejestracyjny naczepy
		TableColumn<ScheduleIntermediate, String> column8 = new TableColumn("Nr rejestracyjny naczepy");
		column8.setCellValueFactory(new PropertyValueFactory("trailerNumber"));
		inWarehouseTableView.getColumns().add(column8);

		// dodawanie kolumny nr rejestracyjny auta
		TableColumn<ScheduleIntermediate, String> column9 = new TableColumn("Nr rejestracyjny auta");
		column9.setCellValueFactory(new PropertyValueFactory("truckLicenceNumber"));
		inWarehouseTableView.getColumns().add(column9);

		// dodac ko

		updateTableView();
		
		//nas³uchiwacz

		if (selectionListener == null) {
			selectionListener = new ListChangeListener<ScheduleIntermediate>() {
				@Override
				public void onChanged(Change<? extends ScheduleIntermediate> change) {
					if (change.getList() != null && change.getList().size() > 0) {

						onSelectedSchedule(change.getList().get(0));

					}
				}

			};
			inWarehouseTableView.getSelectionModel().getSelectedItems().addListener(selectionListener);
		}
		

		infoPanel.setVisible(false);

	}

	private void onSelectedSchedule(ScheduleIntermediate schedule) {

		selectedScheduleIntermediate = schedule;

	}

	public void setmainapp(Main main) {

		this.main = main;

	}

}
