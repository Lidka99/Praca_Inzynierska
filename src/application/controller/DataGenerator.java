package application.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;

import com.password4j.BCryptFunction;
import com.password4j.Hash;
import com.password4j.Password;
import com.password4j.types.BCrypt;

import application.Main;
import application.model.Drivers;
import application.model.Schedule;
import application.model.Trailers;
import application.model.Trucks;
import application.model.Users;

import java.util.Random;

public class DataGenerator {

	static List<String> names;
	static List<String> surnames;
	static List<String> licenceNumbers;
	static Random random = new Random();
	static Main main;

	public static Date addHoursToJavaUtilDate(Date date, int hours) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, hours);
		return calendar.getTime();
	}

	public static void loadNames(String filePath) {

		if (names == null) {
			names = new ArrayList<String>();
		}

		try {
			File file = new File(filePath);
			if (file.exists()) {
				FileInputStream fr = new FileInputStream(file); // reads the file
				InputStreamReader isr = new InputStreamReader(fr, StandardCharsets.ISO_8859_1);
				BufferedReader br = new BufferedReader(isr); // creates a buffering character input stream

				String line;
				line = br.readLine();
				if (line != null) {
					while ((line = br.readLine()) != null) {

						String[] columns = line.split(",");
						if (columns.length > 0) {
							names.add(columns[0]);
						}

					}
				}
				fr.close(); // closes the stream and release the resources
			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

	}

	public static void loadSurnames(String filePath) {

		if (surnames == null) {
			surnames = new ArrayList<String>();
		}

		try {
			File file = new File(filePath);
			if (file.exists()) {
				FileInputStream fr = new FileInputStream(file); // reads the file
				InputStreamReader isr = new InputStreamReader(fr, StandardCharsets.ISO_8859_1);
				BufferedReader br = new BufferedReader(isr); // creates a buffering character input stream

				String line;
				line = br.readLine();

				if (line != null) {
					while ((line = br.readLine()) != null) {

						String[] columns = line.split(",");
						if (columns.length > 0) {
							surnames.add(columns[0]);
						}

					}
				}
				fr.close(); // closes the stream and release the resources

			}

		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public static void loadLicenceNumbers(String filePath) {

		if (licenceNumbers == null) {
			licenceNumbers = new ArrayList<String>();
		}

		try {
			File file = new File(filePath);
			if (file.exists()) {
				FileInputStream fr = new FileInputStream(file); // reads the file
				InputStreamReader isr = new InputStreamReader(fr, StandardCharsets.ISO_8859_1);
				BufferedReader br = new BufferedReader(isr); // creates a buffering character input stream

				String line;

				while ((line = br.readLine()) != null) {

					String[] columns = line.split(",");
					if (columns.length > 0) {
						licenceNumbers.add(columns[0]);
					}

				}

				fr.close(); // closes the stream and release the resources

			}

		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public static String getRandomName() {

		return names.get(random.nextInt(names.size()));
	}

	public static String getRandomSurname() {

		return surnames.get(random.nextInt(surnames.size()));
	}

	public static String getRandomLicenceNumber() {

		return licenceNumbers.get(random.nextInt(licenceNumbers.size())) + getDrivingLicenceNumber();
	}

	public static Date getRandomDate(Date startDate, Date endDate) {

		Long startTime = startDate.getTime();
		Long endTime = endDate.getTime();

		Long randomTime = startTime + (long) ((endTime - startTime) * (random.nextDouble()));
		return new Date(randomTime);
	}

	public static Date getRandomDate(int startYear, int startMonth, int startDay, int endYear, int endMonth,
			int endDay) {

		return getRandomDate(new GregorianCalendar(startYear, startMonth, startDay).getTime(),
				new GregorianCalendar(endYear, endMonth, endDay).getTime());

	}

	public static String getRandomUsername(String name, String surname) {

		return name.substring(0, 2) + surname.substring(0, 3);
	}

	public static String getRandomEmail(String name, String surname) {

		return name.substring(0, name.length() > 10 ? 10 : name.length() - 1) + "."
				+ surname.substring(0, surname.length() > 10 ? 10 : surname.length() - 1) + "@gmail.com";
	}

	public static String getDrivingLicenceNumber() {

		String drivingLicenceNumber = "";
		for (int i = 0; i < 5; i++) {
			drivingLicenceNumber += new Integer(random.nextInt(10)).toString();
		}

		return drivingLicenceNumber;
	}

	public static int getRandomMaxLoad(int min, int max) {

		return min + random.nextInt(max - min);
	}

	public static Drivers getRandomDriver() {
		DriversController controller = main.getDriversController();
		List<Drivers> drivers = controller.getAllDrivers();
		return drivers.get(random.nextInt(drivers.size()));
	}

	public static Trailers getRandomTrailer(int maxLoad) {
		TrailersController controller = main.getTrailersController();
		List<Trailers> trailers = controller.getTrailersBelowMaxLoad(maxLoad);
		return trailers.get(random.nextInt(trailers.size()));
	}

	public static Trucks getRandomTruck() {
		TrucksController controller = main.getTrucksController();
		List<Trucks> trucks = controller.getAllTrucks();
		return trucks.get(random.nextInt(trucks.size()));
	}

	public static void initialize(Main main) {

		DataGenerator.main = main;

	}

	// metoda do generowania użytkowników

	public static void generateUsers(int count) {

		UsersController controller = main.getUsersController();

		for (int i = 0; i < count; i++) {

			String name = getRandomName();
			String surname = getRandomSurname();

			BCryptFunction myBcrypt = BCryptFunction.getInstance(BCrypt.Y, 11);

			Hash hash = Password.hash(name).with(myBcrypt);
			String passwordHash = hash.getResult();

			controller.create(Users.Role.user, name, surname, getRandomUsername(name, surname), passwordHash,
					getRandomEmail(name, surname));

		}

	}

	// metoda do generowania kierowców

	public static void generateDrivers(int count) {

		DriversController controller = main.getDriversController();

		for (int i = 0; i < count; i++) {

			controller.create(getRandomName(), getRandomSurname(), getDrivingLicenceNumber());

		}

	}

	// metoda do generowania naczep

	public static void generateTrailers(int count) {

		TrailersController controller = main.getTrailersController();

		String[] trailerTypes = new String[] { "Plandeka", "Firanka", "Mega" };

		for (int i = 0; i < count; i++) {

			controller.create(getRandomLicenceNumber(), trailerTypes[random.nextInt(trailerTypes.length)],
					getRandomMaxLoad(7, 30));

		}

	}

	// metoda do generowania ciężarówek

	public static void generateTrucks(int count) {

		TrucksController controller = main.getTrucksController();

		String[] trucksBrand = new String[] { "Volvo", "Scania", "Iveco", "Renault", "M.A.N" };

		String[] trucksModel = new String[] { "Lux", "Max", "Premium" };

		for (int i = 0; i < count; i++) {

			int maxLoad = getRandomMaxLoad(7, 39);

			controller.create(getRandomLicenceNumber(), trucksBrand[random.nextInt(trucksBrand.length)],
					trucksModel[random.nextInt(trucksModel.length)], maxLoad, getRandomDriver(),
					getRandomTrailer(maxLoad));

		}

	}

	// metoda do generowania planów przyjazdów

	public static void generateSchedules(int count) {

		ScheduleController controller = main.getScheduleController();

		for (int i = 0; i < count; i++) {

			Date scheduledDate = getRandomDate(2021, 10, 1, 2022, 3, 31);
			Date arrivalDate = null;
			Date departureDate = null;

			int scheduleOption = random.nextInt(3);

			if (scheduleOption > 0) {
				arrivalDate = getRandomDate(scheduledDate, addHoursToJavaUtilDate(scheduledDate, 12));
			}

			if (scheduleOption > 1) {
				departureDate = getRandomDate(arrivalDate, addHoursToJavaUtilDate(arrivalDate, 4));
			}

			Trucks truck = getRandomTruck();

			controller.create(scheduledDate, arrivalDate, departureDate,
					Schedule.Type.values()[random.nextInt(Schedule.Type.values().length)], truck.getDriver(), truck.getTrailer(), truck);

		}

	}

}
