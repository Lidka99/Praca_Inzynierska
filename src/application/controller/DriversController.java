package application.controller;

import application.model.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.*;
import com.j256.ormlite.jdbc.JdbcConnectionSource;

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
	
	//
	public boolean Create(String name, String surname, String driving_license) {
		Drivers newDriver = new Drivers(name, surname, driving_license);
		try {
			driversDao.create(newDriver);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
public List<Drivers> getAllDrivers(){
		
		List<Drivers> allDrivers = new ArrayList<Drivers>();
		
		for (Drivers driver : driversDao) {
			
			allDrivers.add(new Drivers(driver));
		}
		
		
		return allDrivers;
	}
}
