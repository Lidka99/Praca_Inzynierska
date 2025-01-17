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
import application.controller.TrailersController;
import application.controller.UsersController;
import application.model.Trailers;
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

public class AdminPanelTrailersViewController {

	@FXML
	private TableView trailersTableView;

	@FXML
	private TextField trailerNumberInputField;
	@FXML
	private TextField trailerTypeInputField;
	@FXML
	private TextField maxLoadInputField;

	@FXML
	private TextField searchingInputField;

	private Trailers selectedTrailer;

	private EditingMode editingMode = EditingMode.none;
	
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

	private Main main;

	private ListChangeListener<Trailers> selectionListener;

	public void onDeleteButtonClick() {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Usuni�cie naczepy");
		alert.setHeaderText("Masz zamiar usun�� naczep�");
		alert.setContentText("Czy na pewno chcesz to zrobi�?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			main.getTrailersController().delete(selectedTrailer);
			updateTableView();
			clearTrailersFields();
		} else {

		}

	}

	public void onCreateButtonCick() {

		clearTrailersFields();
		enableTableView(false);
		enableEditButttons(false);
		editingMode = EditingMode.adding;
		enableInputFields(true);
		
		cancelButton.setDisable(false);
		saveButton.setDisable(false);

	}

	public void onEditButtonCick() {

		enableTableView(false);
		enableEditButttons(false);
		editingMode = EditingMode.editing;
		enableInputFields(true);

		cancelButton.setDisable(false);
		saveButton.setDisable(false);
	}

	public void onSaveButtonCick() {

		switch (editingMode) {
		case adding: {

			String trailerNumber = trailerNumberInputField.getText();
			String trailerType = trailerTypeInputField.getText();
			String maxLoad = maxLoadInputField.getText();

			ValidatingStatus trailerNumberStatus = validate(trailerNumber, 10, false);
			ValidatingStatus trailerTypeStatus = validate(trailerType, 10, false);

			if (trailerNumberStatus != ValidatingStatus.sukces || trailerTypeStatus != ValidatingStatus.sukces

			) {

				return;
			}

			main.getTrailersController().create(trailerNumber, trailerType, Integer.parseInt(maxLoad));
		}
			break;

		case editing: {

			String trailerNumber = trailerNumberInputField.getText();
			String trailerType = trailerTypeInputField.getText();
			String maxLoad = maxLoadInputField.getText();

			ValidatingStatus trailerNumberStatus = validate(trailerNumber, 10, false);
			ValidatingStatus trailerTypeStatus = validate(trailerType, 10, false);

			if (trailerNumberStatus != ValidatingStatus.sukces || trailerTypeStatus != ValidatingStatus.sukces) {

				return;
			}

			main.getTrailersController().update(selectedTrailer.getId(), trailerNumber, trailerType, Integer.parseInt(maxLoad));

		}
			break;

		default:
			break;
		}

		clearTrailersFields();

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

	private ValidatingStatus validate(String text, int maxLength, boolean checkTrailerNumberUniqueness) {

		if (text == null || text.length() <= 0)
			return ValidatingStatus.pustePole;

		if (text.length() > maxLength)
			return ValidatingStatus.tekstZaDlugi;

		if (checkTrailerNumberUniqueness && main.getUsersController().checkUsername(text,
				selectedTrailer != null ? selectedTrailer.getId() : -1))
			return ValidatingStatus.wartoscNieunikatowa;

		return ValidatingStatus.sukces;

	}

	public void onCancelButtonCick() {

		clearTrailersFields();

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

		trailerNumberInputField.setDisable(!enabled);
		trailerTypeInputField.setDisable(!enabled);
		maxLoadInputField.setDisable(!enabled);

	}

	public void setUp() {
		
		editButton.setDisable(true);
		deleteButton.setDisable(true);
		cancelButton.setDisable(true);
		saveButton.setDisable(true);

		// dodawanie kolumny id
		TableColumn<Trailers, Integer> column1 = new TableColumn("Id");
		column1.setCellValueFactory(new PropertyValueFactory("id"));
		trailersTableView.getColumns().add(column1);

		// dodawanie kolumny nr rejestracyjny naczepy
		TableColumn<Trailers, String> column2 = new TableColumn("Nr rejestracyjny");
		column2.setCellValueFactory(new PropertyValueFactory("trailerNumber"));
		trailersTableView.getColumns().add(column2);

		// dodawanie kolumny typ naczepy
		TableColumn<Trailers, String> column3 = new TableColumn("Typ");
		column3.setCellValueFactory(new PropertyValueFactory("trailerType"));
		trailersTableView.getColumns().add(column3);

		// dodawanie kolumny maksymalna �adowno�� naczepy
		TableColumn<Trailers, Integer> column4 = new TableColumn("Maksymalna �adowno��");
		column4.setCellValueFactory(new PropertyValueFactory("maxLoad"));
		trailersTableView.getColumns().add(column4);

		
		updateTableView();

		enableInputFields(false);


		
		// nas�uchiwacz do filtrowania

		searchingInputField.textProperty().addListener((observable, oldValue, newValue) -> {
			onSearchTextEdit(newValue);
		});

		// tworzymy tzw "nas�uchiwacz"

		if (selectionListener == null) {
			selectionListener = new ListChangeListener<Trailers>() {
				@Override
				public void onChanged(Change<? extends Trailers> change) {
					if (change.getList() != null && change.getList().size() > 0) {

						onSelectedTrailer(change.getList().get(0));

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
			trailersTableView.getSelectionModel().getSelectedItems().addListener(selectionListener);
		} else {
			trailersTableView.getSelectionModel().getSelectedItems().removeListener(selectionListener);
		}

	}

	private void enableEditButttons(boolean enabled) {

		addButton.setDisable(!enabled);
		editButton.setDisable(!enabled);
		deleteButton.setDisable(!enabled);

	}

	private void onSelectedTrailer(Trailers trailer) {

		selectedTrailer = trailer;
		trailerNumberInputField.setText(selectedTrailer.getTrailerNumber());
		trailerTypeInputField.setText(selectedTrailer.getTrailerType());
		maxLoadInputField.setText(selectedTrailer.getMaxLoad().toString());
		
		editButton.setDisable(trailer == null);
		editButton.setDisable(false);
		deleteButton.setDisable(false);

	}

	private void clearTrailersFields() {

		trailerNumberInputField.setText(null);
		trailerTypeInputField.setText(null);
		maxLoadInputField.setText(null);

		editButton.setDisable(true);

	}

	private void updateTableView() {

		TrailersController controller = main.getTrailersController();

		List<Trailers> AllTrailers = controller.searchTrailersByTrailerNumber(searchingInputField.getText());

		trailersTableView.getItems().clear();

		for (Trailers trailer : AllTrailers) {

			trailersTableView.getItems().add(trailer);
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
