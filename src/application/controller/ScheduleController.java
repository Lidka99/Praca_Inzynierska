package application.controller;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;

import application.Main;
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

	public List<Schedule> getSchedulesToday() {

		List<Schedule> allSchedules = new ArrayList<Schedule>();

		Date currentDate = new Date(System.currentTimeMillis());

		

		Date todayWithZeroTime = null;

		try {
			todayWithZeroTime = Main.getDateFormat().parse(Main.getDateFormat().format(currentDate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (todayWithZeroTime != null) {

			for (Schedule schedule : scheduleDao) {

				Date scheduledDate = null;

				try {
					scheduledDate = Main.getDateFormat().parse(Main.getDateFormat().format(schedule.getScheduled_date()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (scheduledDate != null && scheduledDate.equals(todayWithZeroTime)) {
					allSchedules.add(schedule);
				}

			}
		}

		return allSchedules;

	}

	public List<Schedule> getSchedulesWithArrivalBetween(Date startDate, Date endDate) {

		List<Schedule> allSchedules = new ArrayList<Schedule>();

		
		for (Schedule schedule : scheduleDao) {

			if (schedule.getArrival_date() != null) {

				Date arrivalDate = null;

				try {
					arrivalDate = Main.getDateFormat().parse(Main.getDateFormat().format(schedule.getArrival_date()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (arrivalDate != null && schedule.getDeparture_date() != null) {
					if (startDate.getTime() <= arrivalDate.getTime() && endDate.getTime() >= arrivalDate.getTime()) {
						allSchedules.add(schedule);
					}
				}

			}
		}

		return allSchedules;
	}

	public List<Schedule> getSchedulesInWarehouse() {

		List<Schedule> allSchedules = new ArrayList<Schedule>();

		for (Schedule schedule : scheduleDao) {
			if (schedule.getDeparture_date() == null && schedule.getArrival_date() != null) {
				allSchedules.add(schedule);
			}

		}

		return allSchedules;

	}

	public List<Schedule> getSchedulesByData(Drivers driver, Trailers trailer, Trucks truck) {

		List<Schedule> allSchedules = new ArrayList<Schedule>();

		for (Schedule schedule : scheduleDao) {
			if (schedule.getDriver().getId() == driver.getId() && schedule.getTrailer().getId() == trailer.getId()
					&& schedule.getTruck().getId() == truck.getId()) {
				allSchedules.add(schedule);
			}

		}

		return allSchedules;

	}

	public Schedule getSchedule(int id) {

		for (Schedule schedule : scheduleDao) {

			if (id == schedule.getId()) {

				return schedule;
			}
		}
		return null;
	}

	// metoda aktualizujaca aktualna date przyjazdu

	public boolean updateArrivalDate(int id, Date arrivalDate) {
		Schedule schedule = getSchedule(id);
		schedule.setArrival_date(arrivalDate);

		try {
			scheduleDao.update(schedule);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// metoda aktualizujaca date wyjazdu

	public boolean updateDepartureDate(int id, Date departureDate) {
		Schedule schedule = getSchedule(id);
		schedule.setDeparture_date(departureDate);

		try {
			scheduleDao.update(schedule);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
