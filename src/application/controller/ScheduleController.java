package application.controller;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;

import application.model.*;

public class ScheduleController {
	
	private Dao<Schedule, Integer> scheduleDao;

	private JdbcConnectionSource dataSourceConnection;

	public ScheduleController(JdbcConnectionSource dataSourceConnection) {

		this.dataSourceConnection = dataSourceConnection;

		try {
			scheduleDao = DaoManager.createDao(dataSourceConnection, Schedule.class);
			System.out.println(scheduleDao.countOf());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
