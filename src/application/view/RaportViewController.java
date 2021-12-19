package application.view;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

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
	@FXML
	private NumberAxis numberAxis;
	@FXML
	private CategoryAxis categoryAxis;

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
		updateLineChart();
		
		exportCSV.setDisable(raport == null || raport.isEmpty());

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
			
			writer.write(TimeRaportEntry.getCSVHeader() + "\n");

			for (TimeRaportEntry entry : raport) {
				
				writer.write(entry.toCSV() + "\n");
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
			alert.setContentText("B³¹d w zapisie");

			alert.showAndWait();
		}

	}

	public void updateTableView() {

		if (raport != null) {

			raportTableView.getItems().clear();

			for (TimeRaportEntry raportEntry : raport) {

				raportTableView.getItems().add(raportEntry);
			}
		}

	}

	public void updateLineChart() {

		if (raport != null) {

			raportChart.getData().clear();

			categoryAxis.getCategories().clear();

			Series<String, Number> seriesMax = new Series<String, Number>();
			seriesMax.setName("Maksymalny czas pobytu (min)");

			Series<String, Number> seriesMin = new Series<String, Number>();
			seriesMin.setName("Minimalny czas pobytu (min)");

			Series<String, Number> seriesAvg = new Series<String, Number>();
			seriesAvg.setName("Œredni czas pobytu (min)");

			for (TimeRaportEntry raportEntry : raport) {
				if (!categoryAxis.getCategories().contains(raportEntry.getDateString())) {
					categoryAxis.getCategories().add(raportEntry.getDateString());
				}
				seriesMax.getData()
						.add(new Data<String, Number>(raportEntry.getDateString(), raportEntry.getMaxTimeMinutes()));
				seriesMin.getData()
						.add(new Data<String, Number>(raportEntry.getDateString(), raportEntry.getMinTimeMinutes()));
				seriesAvg.getData()
						.add(new Data<String, Number>(raportEntry.getDateString(), raportEntry.getAvgTimeMinutes()));

			}

			raportChart.getData().add(seriesMin);
			raportChart.getData().add(seriesMax);
			raportChart.getData().add(seriesAvg);

		}

	}

	public void setUp() {

		// dodawanie kolumny data
		TableColumn<TimeRaportEntry, String> column1 = new TableColumn("Data");
		column1.setCellValueFactory(new PropertyValueFactory("dateString"));
		raportTableView.getColumns().add(column1);

		// dodawanie kolumny max
		TableColumn<TimeRaportEntry, Float> column2 = new TableColumn("Maksymalny czas pobytu");
		column2.setCellValueFactory(new PropertyValueFactory("maxTimeMinutes"));
		raportTableView.getColumns().add(column2);

		// dodawanie kolumny min
		TableColumn<TimeRaportEntry, Float> column3 = new TableColumn("Minimalny czas pobytu");
		column3.setCellValueFactory(new PropertyValueFactory("minTimeMinutes"));
		raportTableView.getColumns().add(column3);

		// dodawanie kolumny srednia
		TableColumn<TimeRaportEntry, Float> column4 = new TableColumn("Œredni czas pobytu");
		column4.setCellValueFactory(new PropertyValueFactory("avgTimeMinutes"));
		raportTableView.getColumns().add(column4);

		updateTableView();
		updateLineChart();
		
		exportCSV.setDisable(raport == null || raport.isEmpty());

	}

	public void setmainapp(Main main) {

		this.main = main;

	}

}
