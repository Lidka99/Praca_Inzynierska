package application.view;

import application.Main;
import application.model.Users;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

public class AdminPanel_view_controller {

	@FXML
	private ToggleButton usersToggleButton;
	private Main main;

	@FXML
	public void onUsersToggleButtonClick() {

		main.showAdminPageUsers();

	}

	public void setUp() {

		

	}

	public void setmainapp(Main main) {

		this.main = main;

	}

}
