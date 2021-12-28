package application.view.intermediate;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import application.model.Drivers;
import application.model.Trailers;
import application.model.Trucks;



public class TrucksIntermediate {


	Integer id;

	String licenceNumber;

	String brand;
	
	String model;
	
	int maxLoad;
	
	Trailers currentTrailer;
	
	Drivers currentDriver;
	
	String driverName;
	String driverSurname;
	String trailerNumber;
	
	public TrucksIntermediate(Trucks truck) {
		this.id = truck.getId();
		this.licenceNumber = truck.getLicenceNumber();
		this.brand =  truck.getBrand();
		this.model =  truck.getModel();
		this.maxLoad =  truck.getMaxLoad();
		this.currentTrailer =  truck.getTrailer();
		this.currentDriver =  truck.getDriver();
		
		
		if (currentDriver != null) {
			this.driverName = currentDriver.getName();
			this.driverSurname = currentDriver.getSurname();
			}

		if (currentTrailer != null)
			this.trailerNumber = currentTrailer.getTrailerNumber();
	}
	
	
	public Trailers getCurrentTrailer() {
		return currentTrailer;
	}


	public void setCurrentTrailer(Trailers currentTrailer) {
		this.currentTrailer = currentTrailer;
	}


	public Drivers getCurrentDriver() {
		return currentDriver;
	}


	public void setCurrentDriver(Drivers currentDriver) {
		this.currentDriver = currentDriver;
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


	public String getTrailerNumber() {
		return trailerNumber;
	}


	public void setTrailerNumber(String trailerNumber) {
		this.trailerNumber = trailerNumber;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLicenceNumber() {
		return licenceNumber;
	}

	public void setLicenceNumber(String licence_number) {
		this.licenceNumber = licence_number;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Integer getMaxLoad() {
		return maxLoad;
	}

	public void setMaxLoad(int max_load) {
		this.maxLoad = max_load;
	}

	public Trailers getTrailer() {
		return currentTrailer;
	}

	public void setTrailer(Trailers current_trailer) {
		this.currentTrailer = current_trailer;
	}

	public Drivers getDriver() {
		return currentDriver;
	}

	public void setDriver(Drivers current_driver) {
		this.currentDriver = current_driver;
	}
	

}
