package application.view;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import application.Main;
import application.model.Schedule;
import application.view.intermediate.Converter;
import application.view.intermediate.RaportGenerator;
import application.view.intermediate.ScheduleIntermediate;
import application.view.intermediate.TimeRaportEntry;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class RaportViewController {

	@FXML
	private Button generateButton;
	@FXML
	private Button exportCSV;
	@FXML
	private DatePicker startDatePicker;
	@FXML
	private DatePicker endDatePicker;
	@FXML
	private TableView raportTableView;
	@FXML
	private LineChart raportChart;

	private Main main;

	List<TimeRaportEntry> raport;

	public void onGenerateButtonCLick() {

		LocalDate localDate = startDatePicker.getValue();
		Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
		Date startDate = Date.from(instant);

		LocalDate localDate2 = endDatePicker.getValue();
		Instant instant2 = Instant.from(localDate2.atStartOfDay(ZoneId.systemDefault()));
		Date endDate = Date.from(instant2);

		List<Schedule> schedules = main.getScheduleController().getSchedulesWithArrivalBetween(startDate, endDate);

		List<ScheduleIntermediate> schedulesIntermediate = Converter.convert(schedules);

		raport = RaportGenerator.generateTimeRaport(schedulesIntermediate);
		
		updateTableView();

	}

	public void onExportCSVButtonCLick() {

	}

	public void updateTableView() {
		
		if(raport != null) {
			
			raportTableView.getItems().clear();

			for (TimeRaportEntry raportEntry : raport) {

				raportTableView.getItems().add(raportEntry);
			}
		}

	}

	public void setUp() {

		// dodawanie kolumny data
		TableColumn<TimeRaportEntry, String> column1 = new TableColumn("Data");
		column1.setCellValueFactory(new PropertyValueFactory("dateString"));
		raportTableView.getColumns().add(column1);

		// dodawanie kolumny max
		TableColumn<TimeRaportEntry, Float> column2 = new TableColumn("Maksymlny czas pobytu");
		column2.setCellValueFactory(new PropertyValueFactory("maxTime"));
		raportTableView.getColumns().add(column2);


		// dodawanie kolumny min
		TableColumn<TimeRaportEntry, Float> column3 = new TableColumn("Minimalny czas pobytu");
		column3.setCellValueFactory(new PropertyValueFactory("minTime"));
		raportTableView.getColumns().add(column3);

		// dodawanie kolumny srednia
		TableColumn<TimeRaportEntry, Float> column4 = new TableColumn("Œredni czas pobytu");
		column4.setCellValueFactory(new PropertyValueFactory("avgTime"));
		raportTableView.getColumns().add(column4);


		updateTableView();

	}

	public void setmainapp(Main main) {

		this.main = main;

	}

}
