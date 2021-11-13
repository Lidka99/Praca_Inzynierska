package application.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "users")

public class Users {
	
	@DatabaseField(generatedId = true)
	int id;
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
	
	public Users(String role, String name, String surname, String username, String password_hash, String email) {
		this.role = role;
		this.name = name;
		this.surname = surname;
		this.username = username;
		this.password_hash = password_hash;
		this.email = email;
	}

}
