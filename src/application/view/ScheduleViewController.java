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
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class ScheduleViewController {

	@FXML
	private TableView scheduleTableView;

	@FXML
	private DatePicker datePicker;
	
	private Date selectedDate;

	private Main main;

	public void setUp() {

		// dodawanie kolumny id
		TableColumn<Schedule, Integer> column1 = new TableColumn("Id");
		column1.setCellValueFactory(new PropertyValueFactory("id"));
		scheduleTableView.getColumns().add(column1);

		// dodawanie kolumny data
		TableColumn<Schedule, Date> column2 = new TableColumn("Planowana data przyjazdu");
		column2.setCellValueFactory(new PropertyValueFactory("sheduled_date"));
		scheduleTableView.getColumns().add(column2);

		// dodawanie kolumny nazwisko
		TableColumn<Schedule, Date> column3 = new TableColumn("Data przyjazdu");
		column3.setCellValueFactory(new PropertyValueFactory("arrival_date"));
		scheduleTableView.getColumns().add(column3);

		// dodawanie kolumny username
		TableColumn<Schedule, Date> column4 = new TableColumn("Data wyjazdu");
		column4.setCellValueFactory(new PropertyValueFactory("departure_date"));
		scheduleTableView.getColumns().add(column4);

		// dodawanie kolumny email
		TableColumn<Schedule, String> column5 = new TableColumn("Rodzaj");
		column5.setCellValueFactory(new PropertyValueFactory("type"));
		scheduleTableView.getColumns().add(column5);

		// dodawanie kolumny rola
		TableColumn<Schedule, Trailers> column6 = new TableColumn("Id naczepy");
		column6.setCellValueFactory(new PropertyValueFactory("trailer"));
		scheduleTableView.getColumns().add(column6);

		// dodawanie kolumny rola
		TableColumn<Schedule, Drivers> column7 = new TableColumn("Id kierowcy");
		column7.setCellValueFactory(new PropertyValueFactory("driver"));
		scheduleTableView.getColumns().add(column7);

		// dodawanie kolumny rola
		TableColumn<Schedule, Trucks> column8 = new TableColumn("Id auta");
		column8.setCellValueFactory(new PropertyValueFactory("truck"));
		scheduleTableView.getColumns().add(column8);

		updateTableView();

		// tworzymy tzw "nasłuchiwacz"
		datePicker.valueProperty().addListener((observable, oldDate, newDate) -> {
			
			//aktualnie wybrana data
			ZoneId defaultZoneId = ZoneId.systemDefault();
			selectedDate = Date.from(newDate.atStartOfDay(defaultZoneId).toInstant());
			
			updateTableView();
		});

	}

	private void updateTableView() {

		ScheduleController controller = main.getScheduleController();

		List<Schedule> schedules = controller.getSchedulesByScheduledDate(selectedDate); 
		scheduleTableView.getItems().clear();

		for (Schedule schedule : schedules) {

			scheduleTableView.getItems().add(schedule);
		}
	}

	public void setmainapp(Main main) {

		this.main = main;

	}

}
