package application.view;

import java.util.Optional;

import application.Main;
import application.model.Users;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Alert.AlertType;


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
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Ko�czenie pracy aplikacji");
		alert.setHeaderText("Masz zamiar si� wylogowa�");
		alert.setContentText("Czy na pewno chcesz to zrobi�?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			main.logOut();
		} else {

		}

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
