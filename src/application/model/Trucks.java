package application.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "trucks")

public class Trucks {

	@DatabaseField(generatedId = true)
	int id;
	@DatabaseField(canBeNull = false)
	int licence_number;
	@DatabaseField(canBeNull = false)
	String brand;
	@DatabaseField(canBeNull = false)
	String model;
	@DatabaseField(canBeNull = false)
	int max_load;
	@DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
	int current_trailer_id;
	@DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
	int current_driver_id;

	public Trucks() {

	}

	public Trucks(int license_number, String brand, String model, int max_load, int current_trailer_id,
			int current_driver_id) {
		this.licence_number = license_number;
		this.brand = brand;
		this.model = model;
		this.max_load = max_load;
		this.current_trailer_id = current_trailer_id;
		this.current_driver_id = current_driver_id;
	}

}
