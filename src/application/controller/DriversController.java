package application.controller;

import application.model.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.*;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.password4j.BCryptFunction;
import com.password4j.types.BCrypt;

public class DriversController {

	private Dao<Drivers, Integer> driversDao;

	private JdbcConnectionSource dataSourceConnection;

	public DriversController(JdbcConnectionSource dataSourceConnection) {

		this.dataSourceConnection = dataSourceConnection;

		try {
			driversDao = DaoManager.createDao(dataSourceConnection, Drivers.class);
			System.out.println(driversDao.countOf());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// tworzenie kierowcow

	public boolean create(String name, String surname, String drivingLicense) {
		Drivers newDriver = new Drivers(name, surname, drivingLicense);
		try {
			driversDao.create(newDriver);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// edycja kierowcow

	public boolean update(int id, String name, String surname, String drivingLicense) {

		Drivers driver = getDrivers(id);
		driver.setName(name);
		driver.setSurname(surname);
		driver.setDriving_license(drivingLicense);

		try {
			driversDao.update(driver);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private Drivers getDrivers(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	// usuwanie u¿ytkowników

	public boolean delete(int id) {

		Drivers driver = getDrivers(id);
		return delete(driver);

	}

	public boolean delete(Drivers driver) {

		if (driver != null) {

			try {
				driversDao.delete(driver);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			return true;

		}
		return false;
	}

	public Drivers getDriver(int id) {

		for (Drivers driver : driversDao) {

			if (id == driver.getId()) {

				return driver;
			}
		}
		return null;
	}

	public List<Drivers> getAllDrivers() {

		List<Drivers> allDrivers = new ArrayList<Drivers>();

		for (Drivers driver : driversDao) {

			allDrivers.add(new Drivers(driver));
		}

		return allDrivers;
	}

	// sprawdzanie czy istnieje kierowca o danym nr prawa jazdy

	public boolean checkDrivingLicense(String drivingLicense) {

		for (Drivers driver : driversDao) {

			if (drivingLicense.equals(driver.getDriving_license())) {

				return true;
			}

		}

		return false;
	}

	public Drivers getByDriverLicense(String driverLicenseNumber) {
		for (Drivers driver : driversDao) {

			if (driverLicenseNumber.toLowerCase().equals(driver.getDriving_license().toLowerCase())) {
				return driver;
			}
		}

		return null;
	}
}
