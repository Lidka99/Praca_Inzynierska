package application.view;

import application.Main;
import application.model.Users;
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
		
		//wywolanie metody authenticate i zwr�cenie user
		Users user = main.getUsersController().authenticate(login_text_field.getText(), password_text_field.getText());
		
		if (user != null) {

		main.showapplication();
		}
		else {
			System.out.println("B��dne dane");
		}
		

	}

	public void setmainapp(Main main) {

		this.main = main;

	}

}
