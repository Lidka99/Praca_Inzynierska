package application.view;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

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
	private ChoiceBox roleChoiceBox;
	@FXML
	private TextField passwordInputField;
	@FXML
	private Button addButton;
	@FXML
	private Button editButton;
	@FXML
	private Button deleteButton;
	@FXML
	private Button saveButton;
	@FXML
	private Button cancelButton;
	@FXML
	private HBox passwordHbox;
	@FXML
	private TextField searchingInputField;

	private Users selectedUser;

	private EditingMode editingMode = EditingMode.none;

	private Main main;

	private ListChangeListener<Users> selectionListener;

	public void onDeleteButtonClick() {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Usuni�cie u�ytkownika");
		alert.setHeaderText("Masz zamiar usun�� u�ytkownika");
		alert.setContentText("Czy na pewno chcesz to zrobi�?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			main.getUsersController().delete(selectedUser);
			updateTableView();
			clearUserFields();
		} else {

		}

	}

	public void onCreateButtonCick() {

		clearUserFields();
		passwordHbox.setVisible(true);
		enableTableView(false);
		enableEditButttons(false);
		editingMode = EditingMode.adding;
		enableInputFields(true);
		
		cancelButton.setDisable(false);
		saveButton.setDisable(false);

	}

	public void onEditButtonCick() {

		passwordHbox.setVisible(true);
		enableTableView(false);
		enableEditButttons(false);
		editingMode = EditingMode.editing;
		enableInputFields(true);
		
		cancelButton.setDisable(false);
		saveButton.setDisable(false);


	}

	public void onGenerateButtonClick() {

		String upperCaseLetters = RandomStringUtils.random(2, 65, 90, true, true);
		String lowerCaseLetters = RandomStringUtils.random(2, 97, 122, true, true);
		String numbers = RandomStringUtils.randomNumeric(2);
		String specialChar = RandomStringUtils.random(2, 33, 47, false, false);
		String totalChars = RandomStringUtils.randomAlphanumeric(2);
		String combinedChars = upperCaseLetters.concat(lowerCaseLetters).concat(numbers).concat(specialChar)
				.concat(totalChars);
		List<Character> pwdChars = combinedChars.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
		Collections.shuffle(pwdChars);

		String password = pwdChars.stream().collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
				.toString();

		passwordInputField.setText(password);

	}

	public void onSaveButtonCick() {

		switch (editingMode) {
		case adding: {

			String name = nameInputField.getText();
			String surname = surnameInputField.getText();
			String username = usernameInputField.getText();
			String password = passwordInputField.getText();
			String email = emailInputField.getText();

			ValidatingStatus nameStatus = validate(name, 30, false);
			ValidatingStatus surnameStatus = validate(surname, 30, false);
			ValidatingStatus usernameStatus = validate(username, 10, true);
			ValidatingStatus passwordStatus = validate(password, 100, false);
			ValidatingStatus emailStatus = validate(email, 30, false);

			if (nameStatus != ValidatingStatus.sukces || surnameStatus != ValidatingStatus.sukces
					|| usernameStatus != ValidatingStatus.sukces || passwordStatus != ValidatingStatus.sukces
					|| emailStatus != ValidatingStatus.sukces) {

				return;
			}

			Users.Role role = (Users.Role) roleChoiceBox.getValue();

			// klasa algorytmu BCrypt
			BCryptFunction myBcrypt = BCryptFunction.getInstance(BCrypt.Y, 11);

			Hash hash = Password.hash(password).with(myBcrypt);

			main.getUsersController().create(role, name, surname, username, hash.getResult(), email);
		}
			break;

		case editing: {

			String name = nameInputField.getText();
			String surname = surnameInputField.getText();
			String username = usernameInputField.getText();
			String password = passwordInputField.getText();
			String email = emailInputField.getText();

			ValidatingStatus nameStatus = validate(name, 30, false);
			ValidatingStatus surnameStatus = validate(surname, 30, false);
			ValidatingStatus usernameStatus = validate(username, 10, true);
			// ValidatingStatus passwordStatus = validate(password, 100, false);
			ValidatingStatus emailStatus = validate(email, 30, false);

			if (nameStatus != ValidatingStatus.sukces || surnameStatus != ValidatingStatus.sukces
					|| usernameStatus != ValidatingStatus.sukces || emailStatus != ValidatingStatus.sukces) {

				return;
			}

			Users.Role role = (Users.Role) roleChoiceBox.getValue();

			String passwordHash = null;

			if (password != null && !password.isEmpty()) {

				// klasa algorytmu BCrypt
				BCryptFunction myBcrypt = BCryptFunction.getInstance(BCrypt.Y, 11);

				Hash hash = Password.hash(password).with(myBcrypt);
				passwordHash = hash.getResult();

			}

			main.getUsersController().update(selectedUser.getId(), role, name, surname, username, passwordHash, email);

		}
			break;

		default:
			break;
		}

		clearUserFields();
		passwordHbox.setVisible(false);
		updateTableView();
		enableTableView(true);
		enableEditButttons(true);
		enableInputFields(false);
		editingMode = EditingMode.none;
		
		editButton.setDisable(true);
		deleteButton.setDisable(true);
		cancelButton.setDisable(true);
		saveButton.setDisable(true);


	}

	private ValidatingStatus validate(String text, int maxLength, boolean checkUsernameUniqueness) {

		if (text == null || text.length() <= 0)
			return ValidatingStatus.pustePole;

		if (text.length() > maxLength)
			return ValidatingStatus.tekstZaDlugi;

		if (checkUsernameUniqueness
				&& main.getUsersController().checkUsername(text, selectedUser != null ? selectedUser.getId() : -1))
			return ValidatingStatus.wartoscNieunikatowa;

		return ValidatingStatus.sukces;

	}

	public void onCancelButtonCick() {

		clearUserFields();
		passwordHbox.setVisible(false);
		updateTableView();
		enableTableView(true);
		enableEditButttons(true);
		editingMode = EditingMode.none;
		enableInputFields(false);
		
		editButton.setDisable(true);
		deleteButton.setDisable(true);
		cancelButton.setDisable(true);
		saveButton.setDisable(true);

	}

	public void enableInputFields(boolean enabled) {

		nameInputField.setDisable(!enabled);
		surnameInputField.setDisable(!enabled);
		usernameInputField.setDisable(!enabled);
		emailInputField.setDisable(!enabled);
		roleChoiceBox.setDisable(!enabled);

	}

	public void setUp() {
		
		editButton.setDisable(true);
		deleteButton.setDisable(true);
		cancelButton.setDisable(true);
		saveButton.setDisable(true);
		

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

		updateTableView();

		enableInputFields(false);

		passwordHbox.setVisible(false);

		// uzupe�nienie warto�ci w combo boxie

		roleChoiceBox.getItems().setAll(Users.Role.values());

		clearUserFields();

		// nas�uchiwacz do filtrowania

		searchingInputField.textProperty().addListener((observable, oldValue, newValue) -> {
			onSearchTextEdit(newValue);
		});

		// tworzymy tzw "nas�uchiwacz"

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

	public void onSearchTextEdit(String text) {
		
		updateTableView();

	}

	// zaczynamy lub przestajemy nas�uchiwac

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
		roleChoiceBox.getSelectionModel().select(selectedUser.getRole().ordinal());
		editButton.setDisable(user == null);
		
		editButton.setDisable(false);
		deleteButton.setDisable(false);
		

	}

	private void clearUserFields() {

		nameInputField.setText(null);
		surnameInputField.setText(null);
		usernameInputField.setText(null);
		emailInputField.setText(null);
		roleChoiceBox.getSelectionModel().select(0);
		passwordInputField.setText(null);
		editButton.setDisable(true);

	}

	private void updateTableView() {

		UsersController controller = main.getUsersController();

		List<Users> AllUsers = controller.searchUsersbySurname(searchingInputField.getText());

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

	public enum ValidatingStatus {
		sukces, pustePole, tekstZaDlugi, wartoscNieunikatowa

	}

}
