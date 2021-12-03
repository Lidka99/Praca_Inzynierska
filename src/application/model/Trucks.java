package application.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "trucks")

public class Trucks {

	@DatabaseField(generatedId = true)
	Integer id;
	@DatabaseField(canBeNull = false)
	String licence_number;
	@DatabaseField(canBeNull = false)
	String brand;
	@DatabaseField(canBeNull = false)
	String model;
	@DatabaseField(canBeNull = false)
	int max_load;
	@DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
	Trailers current_trailer;
	@DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
	Drivers current_driver;

	public Trucks() {

	}

	public Trucks(String license_number, String brand, String model, int max_load, Trailers current_trailer_id,
			Drivers current_driver_id) {
		this.licence_number = license_number;
		this.brand = brand;
		this.model = model;
		this.max_load = max_load;
		this.current_trailer = current_trailer_id;
		this.current_driver = current_driver_id;
	}

}
