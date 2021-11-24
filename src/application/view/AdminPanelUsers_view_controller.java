package application.view;

import java.util.List;

import application.Main;
import application.controller.UsersController;
import application.model.Users;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminPanelUsers_view_controller {

	@FXML
	private TableView usersTableView;

	@FXML
	private TextField nameInputField;
	@FXML
	private TextField surnameInputField;
	@FXML
	private TextField usernameInputField;
	@FXML
	private TextField emailInputField;
	@FXML
	private TextField roleInputField;

	private Main main;

	public void setUp() {

		// dodawanie kolumny id
		TableColumn<Users, Integer> column1 = new TableColumn("Id");
		column1.setCellValueFactory(new PropertyValueFactory("id"));
		usersTableView.getColumns().add(column1);

		// dodawanie kolumny imie
		TableColumn<Users, String> column2 = new TableColumn("Imi�");
		column2.setCellValueFactory(new PropertyValueFactory("name"));
		usersTableView.getColumns().add(column2);

		// dodawanie kolumny nazwisko
		TableColumn<Users, String> column3 = new TableColumn("Nazwisko");
		column3.setCellValueFactory(new PropertyValueFactory("surname"));
		usersTableView.getColumns().add(column3);

		// dodawanie kolumny username
		TableColumn<Users, String> column4 = new TableColumn("Nazwa u�ytkownika");
		column4.setCellValueFactory(new PropertyValueFactory("username"));
		usersTableView.getColumns().add(column4);

		// dodawanie kolumny email
		TableColumn<Users, String> column5 = new TableColumn("Adres email");
		column5.setCellValueFactory(new PropertyValueFactory("email"));
		usersTableView.getColumns().add(column5);

		// dodawanie kolumny rola
		TableColumn<Users, String> column6 = new TableColumn("Rola");
		column6.setCellValueFactory(new PropertyValueFactory("role"));
		usersTableView.getColumns().add(column6);

		UsersController controller = main.getUsersController();

		List<Users> AllUsers = controller.getAllUsers();

		for (Users user : AllUsers) {

			usersTableView.getItems().add(user);

		}

		nameInputField.setEditable(false);
		surnameInputField.setEditable(false);
		usernameInputField.setEditable(false);
		emailInputField.setEditable(false);
		roleInputField.setEditable(false);

		// zaczynamy nas�uchiwa� zdarzenie wywo�ywane przy zmianie elementu

		usersTableView.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<Users>() {
			@Override
			public void onChanged(Change<? extends Users> change) {
				if (change.getList() != null && change.getList().size() > 0) {

					Users selectedUser = change.getList().get(0);
					nameInputField.setText(selectedUser.getName());
					surnameInputField.setText(selectedUser.getSurname());
					usernameInputField.setText(selectedUser.getUsername());
					emailInputField.setText(selectedUser.getEmail());
					roleInputField.setText(selectedUser.getRole().toString());

				}
			}

		});
		
		//usersTableView.setSelectionModel(null);

	}

	public void setmainapp(Main main) {

		this.main = main;

	}

}
