package application.view;

import application.Main;
import application.model.Users;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;


public class ApplicationViewController {

	
	@FXML
	private ToggleButton main_page_button;
	@FXML
	private ToggleButton schedule_button;
	@FXML
	private ToggleButton arrivalsDeparturesButton;
	@FXML
	private ToggleButton raport_button;
	@FXML
	private ToggleButton admin_panel_button;
	@FXML
	private ToggleButton log_out_button;

	private Main main;

	@FXML
	public void onMainPageButtonClick() {

		main.showMainPage();

	}
	
	@FXML
	public void onScheduleButtonClick() {

		main.showSchedulePage();

	}
	
	@FXML
	public void onArrivalsDeparturesButtonClick() {

		main.showArrivalsDeparturesPage();

	}
	
	

	@FXML
	public void onRaportButtonClick() {

		main.showRaportPage();

	}

	@FXML
	public void onAdminPanelButtonClick() {
		
		main.showAdminPage();

	}

	@FXML
	public void onLogOutButtonClick() {

		main.logOut();

	}

	public void setUp() {

		Users user = main.getCurrentUser();
		if (user.getRole() == Users.Role.administrator) {

			admin_panel_button.setVisible(true);
		} else
			admin_panel_button.setVisible(false);

	}

	public void setmainapp(Main main) {

		this.main = main;

	}

}
