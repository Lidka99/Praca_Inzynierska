package application.view;

import application.Main;
import application.model.Users;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginViewController {

	@FXML
	private Button login_button;
	@FXML
	private TextField login_text_field;
	@FXML
	private PasswordField password_text_field;
	@FXML
	private Label error_label;
	private Main main;

	@FXML
	public void onloginButtonClick() {

		// wywolanie metody authenticate i zwr�cenie user
		Users user = main.getUsersController().authenticate(login_text_field.getText(), password_text_field.getText());

		login_text_field.clear();
		password_text_field.clear();

		if (user != null) {
			error_label.setVisible(false);
			main.setCurrentUser(user);
			main.showapplication();
		} else {
			System.out.println("B��dne dane");
			error_label.setVisible(true);

		}

	}
	
	

	public void setmainapp(Main main) {

		this.main = main;

	}

}
