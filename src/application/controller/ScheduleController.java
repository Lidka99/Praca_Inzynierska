package application.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

	public List<Schedule> getAllSchedules() {

		List<Schedule> allSchedules = new ArrayList<Schedule>();

		for (Schedule schedule : scheduleDao) {
			allSchedules.add(schedule);
		}

		return allSchedules;

	}
	
	public List<Schedule> getSchedulesByScheduledDate(Date scheduled_date) {

		List<Schedule> allSchedules = new ArrayList<Schedule>();

		for (Schedule schedule : scheduleDao) {
			if (schedule.getScheduled_date().equals(scheduled_date)) { 
				allSchedules.add(schedule);
			}
			
		}

		return allSchedules;

	}
	
	public List<Schedule> getSchedulesByArrivalDate(Date arrival_date) {

		List<Schedule> allSchedules = new ArrayList<Schedule>();

		for (Schedule schedule : scheduleDao) {
			if (schedule.getArrival_date().equals(arrival_date)) {
				allSchedules.add(schedule);
			}
			
		}

		return allSchedules;

	}
	
	public List<Schedule> getSchedulesbyDepartureDate(Date departure_date) {

		List<Schedule> allSchedules = new ArrayList<Schedule>();

		for (Schedule schedule : scheduleDao) {
			if (schedule.getDeparture_date().equals(departure_date)) {
				allSchedules.add(schedule);
			}
			
		}

		return allSchedules;

	}

}
