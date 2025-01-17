package application.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;

import application.model.*;

public class TrucksController {
	private Dao<Trucks, Integer> trucksDao;

	private JdbcConnectionSource dataSourceConnection;

	public TrucksController(JdbcConnectionSource dataSourceConnection) {

		this.dataSourceConnection = dataSourceConnection;

		try {
			trucksDao = DaoManager.createDao(dataSourceConnection, Trucks.class);
			System.out.println(trucksDao.countOf());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Trucks getByTruckNumber(String trucksNumber) {
		for (Trucks truck : trucksDao) {

			if (trucksNumber.toLowerCase().equals(truck.getLicenceNumber().toLowerCase())) {
				return truck;
			}
		}

		return null;
	}

	public List<Trucks> getAllTrucks() {

		List<Trucks> allTrucks = new ArrayList<Trucks>();

		for (Trucks truck : trucksDao) {

			allTrucks.add(truck);
		}

		return allTrucks;
	}

	// aktualizowanie, edycja u admina

	public boolean update(int id, String drivingLicence, String brand, String model, Integer maxLoad, Drivers driver,
			Trailers trailer) {
		Trucks truck = getTruck(id);
		truck.setBrand(drivingLicence);
		truck.setBrand(brand);
		truck.setModel(model);
		truck.setMaxLoad(maxLoad);
		truck.setDriver(driver);
		truck.setTrailer(trailer);

		try {
			trucksDao.update(truck);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// tworzenie

	public boolean create(String licenceNumber, String brand, String model, Integer maxLoad, Drivers driver,
			Trailers trailer) {
		Trucks truck = new Trucks();
		truck.setLicenceNumber(licenceNumber);
		truck.setBrand(brand);
		truck.setModel(model);
		truck.setMaxLoad(maxLoad);
		truck.setDriver(driver);
		truck.setTrailer(trailer);

		try {
			trucksDao.create(truck);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// usuwanie

	public boolean delete(int id) {

		Trucks truck = getTruck(id);
		return delete(truck);

	}

	public boolean delete(Trucks truck) {

		if (truck != null) {

			try {
				trucksDao.delete(truck);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			return true;

		}
		return false;
	}

	public List<Trucks> searchTruckByLicenceNumber(String text) {

		List<Trucks> allTrucks = new ArrayList<Trucks>();

		for (Trucks truck : trucksDao) {

			if (truck.getLicenceNumber().toLowerCase().startsWith(text.toLowerCase()))

				allTrucks.add(new Trucks(truck));
		}

		return allTrucks;
	}

	// sprawdzanie czy istnieje ciezarowka o danym nr rejestracyjnym

	public boolean checkLicenceNumber(String licenseNumber, int ignoredId) {

		for (Trucks truck : trucksDao) {

			if (licenseNumber.equals(truck.getLicenceNumber()) && truck.getId() != ignoredId) {

				return true;
			}

		}

		return false;
	}

	public Trucks getTruck(int id) {

		for (Trucks truck : trucksDao) {

			if (truck.getId() == id) {

				return truck;
			}

		}

		return null;
	}
}
