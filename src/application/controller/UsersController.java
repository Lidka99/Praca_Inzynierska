package application.controller;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;

import application.model.*;

public class UsersController {
	private Dao<Users, Integer> usersDao;

	private JdbcConnectionSource dataSourceConnection;

	public UsersController(JdbcConnectionSource dataSourceConnection) {

		this.dataSourceConnection = dataSourceConnection;

		try {
			usersDao = DaoManager.createDao(dataSourceConnection, Users.class);
			System.out.println(usersDao.countOf());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
