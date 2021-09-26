package wgu_full.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import wgu_full.model.Appointment;
import wgu_full.model.Contact;
import wgu_full.model.Customer;

import java.net.URL;
import java.util.ResourceBundle;

public class ReportByContactsController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    /**
     * The tables
     */
    @FXML
    private TableView<Appointment> contactReportTable;

    /**
     * The columns of the appointment table
     */
    @FXML private TableColumn<Appointment, Integer> apptIdCol;
    @FXML private TableColumn<Appointment, String> titleCol;
    @FXML private TableColumn<Appointment, String> descriptionCol;
    @FXML private TableColumn<Appointment, String> locationCol;
    @FXML private TableColumn<Appointment, String> customerCol;
    @FXML private TableColumn<Appointment, String> typeCol;
    @FXML private TableColumn<Appointment, String> startCol;
    @FXML private TableColumn<Appointment, String> endCol;
    @FXML private TableColumn<Appointment, Integer> userIdCol;

    /**
     * Label
     */
    @FXML private Label errorLabel;

    /**
     * ComboBoxes
     */
    @FXML private ComboBox<Contact> contactCombo;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
