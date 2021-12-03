package application.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "users")

public class Users {

	@DatabaseField(generatedId = true)
	Integer id;
	@DatabaseField(canBeNull = false)
	String role;
	@DatabaseField(canBeNull = false)
	String name;
	@DatabaseField(canBeNull = false)
	String surname;
	@DatabaseField(canBeNull = false)
	String username;
	@DatabaseField(canBeNull = false)
	String password_hash;
	@DatabaseField(canBeNull = false)
	String email;

	public Users() {

	}

	public Users(Users user) {

		this.id = user.id;
		this.role = user.role.toString();
		this.name = user.name;
		this.surname = user.surname;
		this.username = user.username;
		this.password_hash = null;
		this.email = user.email;

	}

	public Users(Role role, String name, String surname, String username, String password_hash, String email) {
		this.role = role.toString();
		this.name = name;
		this.surname = surname;
		this.username = username;
		this.password_hash = password_hash;
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Role getRole() {
		return Role.valueOf(role);
	}

	public void setRole(Role role) {
		this.role = role.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword_hash() {
		return password_hash;
	}

	public void setPassword_hash(String password_hash) {
		this.password_hash = password_hash;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public enum Role {
		administrator, user;
	}

}
