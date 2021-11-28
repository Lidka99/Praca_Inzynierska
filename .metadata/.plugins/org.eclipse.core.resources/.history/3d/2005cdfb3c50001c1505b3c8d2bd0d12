package application.view;

import java.util.List;

import com.password4j.BCryptFunction;
import com.password4j.Hash;
import com.password4j.Password;
import com.password4j.types.BCrypt;

import application.Main;
import application.controller.UsersController;
import application.model.Users;
import application.model.Users.Role;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminPanelUsersViewController {

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
	@FXML
	private TextField passwordInputField;
	@FXML
	private Button addButton;
	@FXML
	private Button editButton;
	@FXML
	private Button deleteButton;

	private Users selectedUser;

	private EditingMode editingMode = EditingMode.none;

	private Main main;

	private ListChangeListener<Users> selectionListener;

	public void onDeleteButtonClick() {

		main.getUsersController().delete(selectedUser);
		updateTableView();
		clearUserFields();

	}

	public void onCreateButtonCick() {
		clearUserFields();
		enableTableView(false);
		enableEditButttons(false);
		editingMode = EditingMode.adding;
	}

	public void onSaveButtonCick() {

		switch (editingMode) {
		case adding: {
			Users.Role role = Role.valueOf(roleInputField.getText());
			String name = nameInputField.getText();
			String surname = surnameInputField.getText();
			String username = usernameInputField.getText();
			String password = passwordInputField.getText();
			String email = emailInputField.getText();
			
			//klasa algorytmu BCrypt
			BCryptFunction myBcrypt = BCryptFunction.getInstance(BCrypt.Y, 11);

			
			Hash hash = Password.hash(password).with(myBcrypt);
			
					main.getUsersController().create(role, name, surname, username, hash.getResult(), email);
		}	
			break;
			
		case editing: {
		}
			break;

		default:
			break;
		}
		
		clearUserFields();
		enableTableView(true);
		enableEditButttons(true);
		editingMode = EditingMode.none;
	}

	public void setUp() {

		// dodawanie kolumny id
		TableColumn<Users, Integer> column1 = new TableColumn("Id");
		column1.setCellValueFactory(new PropertyValueFactory("id"));
		usersTableView.getColumns().add(column1);

		// dodawanie kolumny imie
		TableColumn<Users, String> column2 = new TableColumn("Imiê");
		column2.setCellValueFactory(new PropertyValueFactory("name"));
		usersTableView.getColumns().add(column2);

		// dodawanie kolumny nazwisko
		TableColumn<Users, String> column3 = new TableColumn("Nazwisko");
		column3.setCellValueFactory(new PropertyValueFactory("surname"));
		usersTableView.getColumns().add(column3);

		// dodawanie kolumny username
		TableColumn<Users, String> column4 = new TableColumn("Nazwa u¿ytkownika");
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

		updateTableView();

		nameInputField.setEditable(false);
		surnameInputField.setEditable(false);
		usernameInputField.setEditable(false);
		emailInputField.setEditable(false);
		roleInputField.setEditable(false);

		// tworzymy tzw "nas³uchiwacz"

		if (selectionListener == null) {
			selectionListener = new ListChangeListener<Users>() {
				@Override
				public void onChanged(Change<? extends Users> change) {
					if (change.getList() != null && change.getList().size() > 0) {

						onSelectedUser(change.getList().get(0));

					}
				}

			};
		}
		enableTableView(true);
	}

	// zaczynamy lub przestajemy nas³uchiwac

	private void enableTableView(boolean enabled) {

		if (enabled) {
			usersTableView.getSelectionModel().getSelectedItems().addListener(selectionListener);
		} else {
			usersTableView.getSelectionModel().getSelectedItems().removeListener(selectionListener);
		}

	}

	private void enableEditButttons(boolean enabled) {

		addButton.setDisable(!enabled);
		editButton.setDisable(!enabled);
		deleteButton.setDisable(!enabled);

	}

	private void onSelectedUser(Users user) {

		selectedUser = user;
		nameInputField.setText(selectedUser.getName());
		surnameInputField.setText(selectedUser.getSurname());
		usernameInputField.setText(selectedUser.getUsername());
		emailInputField.setText(selectedUser.getEmail());
		roleInputField.setText(selectedUser.getRole().toString());

	}

	private void clearUserFields() {

		nameInputField.setText(null);
		surnameInputField.setText(null);
		usernameInputField.setText(null);
		emailInputField.setText(null);
		roleInputField.setText(null);

	}

	private void updateTableView() {

		UsersController controller = main.getUsersController();

		List<Users> AllUsers = controller.getAllUsers();

		usersTableView.getItems().clear();

		for (Users user : AllUsers) {

			usersTableView.getItems().add(user);
		}
	}

	public void setmainapp(Main main) {

		this.main = main;

	}

	public enum EditingMode {
		adding, editing, none
	}
}
