package application.controller;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;

import application.model.*;

public class TrailersController {
	
	private Dao<Trailers, Integer> trailersDao;

	private JdbcConnectionSource dataSourceConnection;

	public TrailersController(JdbcConnectionSource dataSourceConnection) {

		this.dataSourceConnection = dataSourceConnection;

		try {
			trailersDao = DaoManager.createDao(dataSourceConnection, Trailers.class);
			System.out.println(trailersDao.countOf());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
