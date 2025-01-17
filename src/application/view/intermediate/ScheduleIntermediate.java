package application.view.intermediate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.j256.ormlite.field.DatabaseField;

import application.Main;
import application.model.Drivers;
import application.model.Schedule;
import application.model.Trailers;
import application.model.Trucks;

public class ScheduleIntermediate {

	Integer id;
	
	Date scheduleDateAsDate;

	String scheduled_date;

	String arrival_date;

	String departure_date;

	String type;

	Trailers trailer;

	Drivers driver;

	Trucks truck;
	
	String driverName;
	
	String driverSurname;
	String driverLicenceNumber;
	
	String trailerNumber;
	
	String truckLicenceNumber;
	
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getScheduled_date() {
		return scheduled_date;
	}

	public void setScheduled_date(String scheduled_date) {
		this.scheduled_date = scheduled_date;
	}

	public String getArrival_date() {
		return arrival_date;
	}

	public void setArrival_date(String arrival_date) {
		this.arrival_date = arrival_date;
	}

	public String getDeparture_date() {
		return departure_date;
	}

	public void setDeparture_date(String departure_date) {
		this.departure_date = departure_date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Trailers getTrailer() {
		return trailer;
	}

	public void setTrailer(Trailers trailer) {
		this.trailer = trailer;
	}

	public Drivers getDriver() {
		return driver;
	}

	public void setDriver(Drivers driver) {
		this.driver = driver;
	}

	public Trucks getTruck() {
		return truck;
	}

	public void setTruck(Trucks truck) {
		this.truck = truck;
	}
	
	

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getDriverSurname() {
		return driverSurname;
	}

	public void setDriverSurname(String driverSurname) {
		this.driverSurname = driverSurname;
	}
	
	

	public String getDriverLicenceNumber() {
		return driverLicenceNumber;
	}

	public void setDriverLicenceNumber(String driverLicenceNumber) {
		this.driverLicenceNumber = driverLicenceNumber;
	}

	public String getTrailerNumber() {
		return trailerNumber;
	}

	public void setTrailerNumber(String trailerNumber) {
		this.trailerNumber = trailerNumber;
	}

	public String getTruckLicenceNumber() {
		return truckLicenceNumber;
	}

	public void setTruckLicenceNumber(String truckLicenceNumber) {
		this.truckLicenceNumber = truckLicenceNumber;
	}

	public ScheduleIntermediate(Schedule schedule) {
		super();
		
		
		
		this.id = schedule.getId();

		Date scheduledDate = schedule.getScheduled_date();
		scheduleDateAsDate = scheduledDate;
		if (scheduledDate != null) {
			
			
			this.scheduled_date = Main.getDateTimeFormat().format(scheduledDate);
		}

		Date arrivalDate = schedule.getArrival_date();
		if (arrivalDate != null) {
			this.arrival_date = Main.getDateTimeFormat().format(arrivalDate);
		}

		Date departureDate = schedule.getDeparture_date();
		if (departureDate != null) {
			this.departure_date = Main.getDateTimeFormat().format(departureDate);
		}
		
		this.type = schedule.getType().toString();
		this.trailer = schedule.getTrailer();
		this.driver = schedule.getDriver();
		this.truck = schedule.getTruck();
		
		if (driver != null) {
		this.driverName = driver.getName();
		this.driverSurname = driver.getSurname();
		this.driverLicenceNumber = driver.getDriving_license();
		}
		
		if (trailer != null)
		this.trailerNumber = trailer.getTrailerNumber();
		
		if (truck != null)
		this.truckLicenceNumber = truck.getLicenceNumber();
	}
	
	public static String getCSVHeader() {
		return "Data, Data przyjazdu, Data wyjazdu, Rodzaj, Imi� kierowcy, Nazwisko kierowcy, Nr prawa jazdy, Nr rejestracyjny auta, Nr rejestracyjny naczepy";
	}
	
	public String toCSV() {
		return scheduled_date + "," + arrival_date + "," + departure_date + "," + type + "," + driverName + "," + driverSurname + "," + driverLicenceNumber + "," + truckLicenceNumber + "," + trailerNumber;
	}

	

}
