package application.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "dbo.drivers")
public class Drivers {

	@DatabaseField(generatedId = true)
	public int id;
	@DatabaseField(canBeNull = false)
	public String name;
	@DatabaseField(canBeNull = false)
	public String surname;
	@DatabaseField(canBeNull = false)
	public String driving_license;

	public Drivers() {

	}
	
	public Drivers(String name, String surname, String driving_license) {
		this.name = name;
		this.surname = surname;
		this.driving_license = driving_license;
	}
}