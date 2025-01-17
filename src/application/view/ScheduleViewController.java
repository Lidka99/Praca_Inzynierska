package application.view;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import application.view.intermediate.TimeRaportEntry;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class ScheduleViewController {

	@FXML
	private TableView scheduleTableView;

	@FXML
	private DatePicker datePicker;

	private Date selectedDate;

	@FXML
	private Button exportCSV;

	private Main main;

	List<ScheduleIntermediate> schedules = new ArrayList<ScheduleIntermediate>();

	public void setUp() {

		// dodawanie kolumny id
//		TableColumn<ScheduleIntermediate, Integer> column1 = new TableColumn("Id");
//		column1.setCellValueFactory(new PropertyValueFactory("id"));
//		scheduleTableView.getColumns().add(column1);

		// dodawanie kolumny planowana data
		TableColumn<ScheduleIntermediate, String> column2 = new TableColumn("Planowana data przyjazdu");
		column2.setCellValueFactory(new PropertyValueFactory("scheduled_date"));
		scheduleTableView.getColumns().add(column2);

		// dodawanie kolumny data przyjazdu
		TableColumn<ScheduleIntermediate, String> column3 = new TableColumn("Data przyjazdu");
		column3.setCellValueFactory(new PropertyValueFactory("arrival_date"));
		scheduleTableView.getColumns().add(column3);

		// dodawanie kolumny data wyjazdu
		TableColumn<ScheduleIntermediate, String> column4 = new TableColumn("Data wyjazdu");
		column4.setCellValueFactory(new PropertyValueFactory("departure_date"));
		scheduleTableView.getColumns().add(column4);

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

		// dodawanie kolumny prawo jazdy
		TableColumn<ScheduleIntermediate, String> column71 = new TableColumn("Nr prawa jazdy");
		column71.setCellValueFactory(new PropertyValueFactory("driverLicenceNumber"));
		scheduleTableView.getColumns().add(column71);

		// dodawanie kolumny nr rejestracyjny auta
		TableColumn<ScheduleIntermediate, String> column9 = new TableColumn("Nr rejestracyjny auta");
		column9.setCellValueFactory(new PropertyValueFactory("truckLicenceNumber"));
		scheduleTableView.getColumns().add(column9);

		// dodawanie kolumny nr rejestracyjny naczepy
		TableColumn<ScheduleIntermediate, String> column8 = new TableColumn("Nr rejestracyjny naczepy");
		column8.setCellValueFactory(new PropertyValueFactory("trailerNumber"));
		scheduleTableView.getColumns().add(column8);

		scheduleTableView.setPlaceholder(new Label ("Brak wpis�w w harmonogramie"));
		updateTableView();

		// tworzymy tzw "nas�uchiwacz"
		datePicker.valueProperty().addListener((observable, oldDate, newDate) -> {

			// aktualnie wybrana data
			if (newDate != null) {

				ZoneId defaultZoneId = ZoneId.systemDefault();
				selectedDate = Date.from(newDate.atStartOfDay(defaultZoneId).toInstant());

			}
			updateTableView();
		});

	}

	// eksport CSV

	public void onExportCSVButtonCLick() {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Eksportuj do CSV");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("CSV", "*.csv"));

		File file = fileChooser.showSaveDialog(main.getPrimaryStage());

		// zapisywanie pliku
		try {
			FileWriter writer = new FileWriter(file);

			writer.write(ScheduleIntermediate.getCSVHeader() + "\n");

			if( schedules != null) {
			for (ScheduleIntermediate schedule : schedules) {

				writer.write(schedule.toCSV() + "\n");
			}

			}
			writer.close();

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Zapisywanie pliku");
			alert.setHeaderText(null);
			alert.setContentText("Zapisano");

			alert.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Zapisywanie pliku");
			alert.setHeaderText(null);
			alert.setContentText("B��d w zapisie");

			alert.showAndWait();
		}

	}

	private void updateTableView() {

		ScheduleController controller = main.getScheduleController();

		List<ScheduleIntermediate> schedules = ScheduleConverter
				.convert(selectedDate != null ? controller.getSchedulesByScheduledDate(selectedDate)
						: controller.getSchedulesToday());

		this.schedules = schedules;

		if (selectedDate == null) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
			datePicker.setValue(
					LocalDate.parse(Main.getDateTimeFormat().format(new Date(System.currentTimeMillis())), formatter));
		}
		scheduleTableView.getItems().clear();

		Collections.sort(schedules, new ScheduleIntermediateComparator());
		for (ScheduleIntermediate schedule : schedules) {

			scheduleTableView.getItems().add(schedule);
		}
	}

	public void setmainapp(Main main) {

		this.main = main;

	}

}
