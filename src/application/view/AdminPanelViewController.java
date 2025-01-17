package application.view;

import application.Main;
import application.model.Users;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

public class AdminPanelViewController {

	@FXML
	private ToggleButton usersToggleButton;
	@FXML
	private ToggleButton driversToggleButton;
	@FXML
	private ToggleButton trailersToggleButton;
	@FXML
	private ToggleButton trucksToggleButton;
	private Main main;

	@FXML
	public void onUsersToggleButtonClick() {

		main.showAdminPageUsers();

	}
	
	@FXML
	public void onDriversToggleButtonClick() {

		main.showAdminPageDrivers();

	}
	
	@FXML
	public void onTrailersToggleButtonClick() {

		main.showAdminPageTrailers();

	}
	
	@FXML
	public void onTrucksToggleButtonClick() {

		main.showAdminPageTrucks();

	}
	
	@FXML
	public void onSchedulesToggleButtonClick() {

		main.showAdminPageSchedule();

	}


	public void setUp() {

		

	}

	public void setmainapp(Main main) {

		this.main = main;

	}

}
