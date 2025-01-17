package application.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "trucks")

public class Trucks {

	@DatabaseField(generatedId = true)
	Integer id;
	@DatabaseField(columnName = "licence_number" , canBeNull = false)
	String licenceNumber;
	@DatabaseField(canBeNull = false)
	String brand;
	@DatabaseField(canBeNull = false)
	String model;
	@DatabaseField(columnName = "max_load" ,canBeNull = false)
	int maxLoad;
	@DatabaseField(columnName = "current_trailer_id" ,canBeNull = false, foreign = true, foreignAutoRefresh = true)
	Trailers currentTrailer;
	@DatabaseField(columnName = "current_driver_id" ,canBeNull = false, foreign = true, foreignAutoRefresh = true)
	Drivers currentDriver;
	

	
	public Trucks() {
		
	}

	public Trucks(String licence_number, String brand, String model, int max_load, Trailers current_trailer_id,
			Drivers current_driver_id) {
		this.licenceNumber = licence_number;
		this.brand = brand;
		this.model = model;
		this.maxLoad = max_load;
		this.currentTrailer = current_trailer_id;
		this.currentDriver = current_driver_id;
	}
	
	public Trucks(Trucks truck) {
		this.id = truck.getId();
		this.licenceNumber = truck.getLicenceNumber();
		this.brand =  truck.getBrand();
		this.model =  truck.getModel();
		this.maxLoad =  truck.getMaxLoad();
		this.currentTrailer =  truck.getTrailer();
		this.currentDriver =  truck.getDriver();
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

	public int getMaxLoad() {
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
