package application.view;


import application.Main;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Login_view_controller {

	@FXML
	private Button login_button;
	@FXML
	private TextField login_text_field;
	@FXML
	private PasswordField password_text_field;
	private Main main;
	
	@FXML
	public void onloginButtonClick() {
		
		main.showapplication();
		
	}
	
	public void setmainapp(Main main) {
		
		this.main = main;
		
	}
	
	
}
