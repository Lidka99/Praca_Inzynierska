package application.view;

import application.Main;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Application_view_controller {

	@FXML
	private Button main_page_button;
	@FXML
	private Button raport_button;
	
	private Main main;

	@FXML
	public void onMainPageButtonClick() {

		main.showMainPage();

	}
	
	@FXML
	public void onRaportButtonClick() {

		main.showRaportPage();

	}

	public void setmainapp(Main main) {

		this.main = main;

	}

}
