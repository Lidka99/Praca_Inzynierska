package application.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.password4j.BCryptFunction;
import com.password4j.Hash;
import com.password4j.Password;
import com.password4j.types.BCrypt;

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

	// tworzenie użytkowników

	public boolean create(Users.Role role, String name, String surname, String username, String password_hash,
			String email) {
		Users newUser = new Users(role, name, surname, username, password_hash, email);
		try {
			usersDao.create(newUser);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// usuwanie użytkowników

	public boolean delete(int id) {

		Users user = getUser(id);
		return delete(user);

	}

	public boolean delete(Users user) {

		if (user != null) {

			try {
				usersDao.delete(user);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			return true;

		}
		return false;
	}

	public Users getUser(int id) {

		for (Users user : usersDao) {

			if (id == user.getId()) {

				return user;
			}
		}
		return null;
	}

	public List<Users> getAllUsers() {

		List<Users> allUsers = new ArrayList<Users>();

		for (Users user : usersDao) {

			allUsers.add(new Users(user));
		}

		return allUsers;
	}

	public Users authenticate(String login, String password) {

		for (Users user : usersDao) {

			if (login.equals(user.getUsername())) {
				BCryptFunction myBcrypt = BCryptFunction.getInstance(BCrypt.Y, 11);

				if (myBcrypt.check(password, user.getPassword_hash())) {
					return user;
				}
			}

		}

		return null;
	}

}