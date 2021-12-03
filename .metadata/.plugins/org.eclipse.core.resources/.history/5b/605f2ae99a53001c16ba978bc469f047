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

	public Drivers(Drivers driver) {
		
		this.name = driver.name;
		this.surname = driver.surname;
		this.driving_license = driver.driving_license;

	}
	
	public Drivers(String name, String surname, String driving_license) {
		this.name = name;
		this.surname = surname;
		this.driving_license = driving_license;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getDriving_license() {
		return driving_license;
	}

	public void setDriving_license(String driving_license) {
		this.driving_license = driving_license;
	}
	
	
}
