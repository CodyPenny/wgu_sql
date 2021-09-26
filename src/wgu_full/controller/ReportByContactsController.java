package wgu_full.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import wgu_full.model.Appointment;
import wgu_full.model.Contact;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static wgu_full.DAO.AppointmentDao.getAppointmentsByContact;
import static wgu_full.model.Contact.getAllContacts;

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
    @FXML private TableColumn<Appointment, Integer> customerCol;
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


    public void generateReport(ActionEvent event){
        if (contactCombo.getSelectionModel().isEmpty()){
            showError(true, "Select a contact to generate report.");
            return;
        } else {
            showError(false, "");
        }
        int contact = contactCombo.getSelectionModel().getSelectedItem().getId();
        contactReportTable.setItems(getAppointmentsByContact(contact));

    }

    /**
     * Returns to the main page
     *
     * @param event the cancel or saved button has fired
     * @throws IOException if I/O operation fails
     */
    public void backToMain(ActionEvent event) throws IOException{
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/main.fxml"));
            root = loader.load();
            MainController controller = loader.getController();
            controller.selectTab(3);
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showError(true, "Can not load the main page.");
        }
    }

    /**
     * Shows or the hides the custom error
     *
     * @param showOrHide sets the error to be visible or invisible
     * @param errorText the custom error message to show
     */
    public void showError(boolean showOrHide, String errorText){
        errorLabel.setText(errorText);
        errorLabel.setVisible(showOrHide);
    }



    /**
     * Populates the columns of the appointments table
     */
    public void setupAppointmentColumns(){
        apptIdCol.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));
        typeCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("end"));
        customerCol.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("customer"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("user"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupAppointmentColumns();
        contactCombo.setItems(getAllContacts());
    }
}
