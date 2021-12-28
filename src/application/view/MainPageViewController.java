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

public class MainPageViewController {

	@FXML
	private TableView inWarehouseTableView;
	@FXML
	private TableView scheduleTableView;
	@FXML
	private Label usernameLabel;
	@FXML
	private Label currentDateLabel;

	private Main main;

	private void updateTableView() {

		ScheduleController controller = main.getScheduleController();

		List<ScheduleIntermediate> schedules = ScheduleConverter.convert(controller.getSchedulesInWarehouse());
		inWarehouseTableView.getItems().clear();

		for (ScheduleIntermediate schedule : schedules) {

			inWarehouseTableView.getItems().add(schedule);
		}
		
		List<ScheduleIntermediate> scheduleToday = ScheduleConverter.convert(controller.getSchedulesToday());
		scheduleTableView.getItems().clear();

		for (ScheduleIntermediate schedule : scheduleToday) {

			scheduleTableView.getItems().add(schedule);
		}
	}

	public void setUp() {

		usernameLabel.setText(main.getCurrentUser().getName() + " " + main.getCurrentUser().getSurname());
		Date currentDate = new Date(System.currentTimeMillis());
		currentDateLabel.setText(Main.getDateFormat().format(currentDate));

		// tabela schedule
		// dodawanie kolumny id
		TableColumn<ScheduleIntermediate, Integer> column1 = new TableColumn("Id");
		column1.setCellValueFactory(new PropertyValueFactory("id"));
		scheduleTableView.getColumns().add(column1);

		// dodawanie kolumny planowana data
		TableColumn<ScheduleIntermediate, String> column2 = new TableColumn("Planowana data przyjazdu");
		column2.setCellValueFactory(new PropertyValueFactory("scheduled_date"));
		scheduleTableView.getColumns().add(column2);

		// dodawanie kolumny data przyjazdu
		TableColumn<ScheduleIntermediate, String> column3 = new TableColumn("Data przyjazdu");
		column3.setCellValueFactory(new PropertyValueFactory("arrival_date"));
		scheduleTableView.getColumns().add(column3);

		// dodawanie kolumny rodzaj
		TableColumn<ScheduleIntermediate, String> column5 = new TableColumn("Rodzaj");
		column5.setCellValueFactory(new PropertyValueFactory("type"));
		scheduleTableView.getColumns().add(column5);

		// dodawanie kolumny imie kierowcy
		TableColumn<ScheduleIntermediate, String> column6 = new TableColumn("Imiê kierowcy");
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

		// dodac kolumny marka itd
		// tabela warehouse

		// dodawanie kolumny id
		TableColumn<ScheduleIntermediate, Integer> column11 = new TableColumn("Id");
		column11.setCellValueFactory(new PropertyValueFactory("id"));
		inWarehouseTableView.getColumns().add(column11);

		// dodawanie kolumny planowana data
		TableColumn<ScheduleIntermediate, String> column22 = new TableColumn("Planowana data przyjazdu");
		column22.setCellValueFactory(new PropertyValueFactory("scheduled_date"));
		inWarehouseTableView.getColumns().add(column22);

		// dodawanie kolumny data przyjazdu
		TableColumn<ScheduleIntermediate, String> column33 = new TableColumn("Data przyjazdu");
		column33.setCellValueFactory(new PropertyValueFactory("arrival_date"));
		inWarehouseTableView.getColumns().add(column33);

		// dodawanie kolumny rodzaj
		TableColumn<ScheduleIntermediate, String> column55 = new TableColumn("Rodzaj");
		column55.setCellValueFactory(new PropertyValueFactory("type"));
		inWarehouseTableView.getColumns().add(column55);

		// dodawanie kolumny imie kierowcy
		TableColumn<ScheduleIntermediate, String> column66 = new TableColumn("Imiê kierowcy");
		column66.setCellValueFactory(new PropertyValueFactory("driverName"));
		inWarehouseTableView.getColumns().add(column66);

		// dodawanie kolumny nazwisko kierowcy
		TableColumn<ScheduleIntermediate, String> column77 = new TableColumn("Nazwisko kierowcy");
		column77.setCellValueFactory(new PropertyValueFactory("driverSurname"));
		inWarehouseTableView.getColumns().add(column77);

		// dodawanie kolumny nr rejestracyjny naczepy
		TableColumn<ScheduleIntermediate, String> column88 = new TableColumn("Nr rejestracyjny naczepy");
		column88.setCellValueFactory(new PropertyValueFactory("trailerNumber"));
		inWarehouseTableView.getColumns().add(column88);

		// dodawanie kolumny nr rejestracyjny auta
		TableColumn<ScheduleIntermediate, String> column99 = new TableColumn("Nr rejestracyjny auta");
		column99.setCellValueFactory(new PropertyValueFactory("truckLicenceNumber"));
		inWarehouseTableView.getColumns().add(column99);

		updateTableView();

	}

	public void setmainapp(Main main) {

		this.main = main;

	}

}
