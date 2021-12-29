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
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.password4j.BCryptFunction;
import com.password4j.Hash;
import com.password4j.Password;
import com.password4j.types.BCrypt;

import application.Main;
import application.model.Users;

import java.util.Random;

public class DataGenerator {

	static List<String> names;
	static List<String> surnames;
	static Random random = new Random();
	static Main main;

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
				while ((line = br.readLine()) != null) {

					String[] columns = line.split(",");
					if (columns.length > 0) {
						names.add(columns[0]);
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
				while ((line = br.readLine()) != null) {

					String[] columns = line.split(",");
					if (columns.length > 0) {
						surnames.add(columns[0]);
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

	public static Date getRandomDate(Date startDate, Date endDate) {

		Long startTime = startDate.getTime();
		Long endTime = endDate.getTime();

		Long randomTime = startTime + (long) ((endTime - startTime) * (random.nextDouble()));
		return new Date(randomTime);
	}

	public static String getRandomUsername(String name, String surname) {

		return name.substring(0, 2) + surname.substring(0, 3);
	}

	public static String getRandomEmail(String name, String surname) {

		return name.substring(0, name.length() > 10 ? 10 : name.length() - 1) + "."
				+ surname.substring(0, surname.length() > 10 ? 10 : surname.length() - 1) + "@gmail.com";
	}

	public static void initialize(Main main) {

		DataGenerator.main = main;

	}

	// metoda do generowania

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

}
