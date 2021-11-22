package application.controller;

import java.sql.SQLException;

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

}