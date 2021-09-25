package wgu_full.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import wgu_full.model.Appointment;
import wgu_full.model.Customer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static wgu_full.DAO.AppointmentDao.getAllAppointments;
import static wgu_full.DAO.CustomerDao.getAllCustomers;

/**
 * The interface for the main page
 */

public class MainController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    /**
     * The tables
     */
    @FXML private TableView<Customer> customerTable;
    //@FXML private TableView<> upcomingTable;
    @FXML private TableView<Appointment> appointmentTable;
    //@FXML private TableView<> typeCountTable;

    /**
     * The columns of the appointment table
     */
    @FXML private TableColumn<Appointment, Integer> apptIdCol;
    @FXML private TableColumn<Appointment, String> titleCol;
    @FXML private TableColumn<Appointment, String> descriptionCol;
    @FXML private TableColumn<Appointment, String> locationCol;
    @FXML private TableColumn<Appointment, String> contactCol;
    @FXML private TableColumn<Appointment, String> typeCol;
    @FXML private TableColumn<Appointment, String> startCol;
    @FXML private TableColumn<Appointment, String> endCol;
    @FXML private TableColumn<Appointment, Integer> apptCustIdCol;
    @FXML private TableColumn<Appointment, Integer> userIdCol;

    /**
     * The columns of the customer table
     */
    @FXML private TableColumn<Customer, Integer> custIdCol;
    @FXML private TableColumn<Customer, String> custNameCol;
    @FXML private TableColumn<Customer, String> custAddressCol;
    @FXML private TableColumn<Customer, String> custProvinceCol;
    @FXML private TableColumn<Customer, String> custPostalCol;
    @FXML private TableColumn<Customer, String> custPhoneCol;

    /**
     * Label
     */
    @FXML private Label errorLabel;

    /**
     * Tabs
     */
    @FXML private TabPane mainTabPane;
    @FXML private Tab apptTab, custTab, reportTab;

    /**
     * Opens add new customer form
     *
     * @param event when the create new button is fired
     * @throws IOException if I/O operation fails
     */
    public void openAddCustomerForm(ActionEvent event) throws IOException {
        try {
            root = FXMLLoader.load(getClass().getResource("../view/addCustomerForm.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showError(true, "Error opening add form.");
        } catch (Exception e){
            showError(true, "Error opening add form.");
        }
    }

    /**
     * Opens the edit/update customer form
     *
     * @param event when the edit button is fired
     * @throws Exception if I/O operation fails
     */
    public void openEditCustomerForm(ActionEvent event) throws Exception{
        try{
            Customer row = customerTable.getFocusModel().getFocusedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/editCustomerForm.fxml"));
            root = loader.load();
            EditCustomerController controller = loader.getController();
            controller.populateForm(row);

            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }  catch (Exception e){
            showError(true, "Error opening edit form.");
        }
    }

    /**
     * Opens add new appointment form
     *
     * @param event when the create new button is fired
     * @throws IOException if I/O operation fails
     */
    public void openAddAppointmentForm(ActionEvent event) throws IOException {
        try {
            root = FXMLLoader.load(getClass().getResource("../view/addApptForm.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showError(true, "Error opening add form.");
        } catch (Exception e){
            showError(true, "Error opening add form.");
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
     * Selects the tab of the Tabpane object
     *
     * @param i the index of the Tab starting from 1
     */
    public void selectTab( int i){
        if(i == 1){
            mainTabPane.getSelectionModel().select(apptTab);
        } else if (i == 2){
            mainTabPane.getSelectionModel().select(custTab);
        } else {
            mainTabPane.getSelectionModel().select(reportTab);
        }
    }

    /**
     * Populates the columns of the customers table
     */
    public void setupCustomerColumns(){
        custIdCol.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("id"));
        custNameCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
        custAddressCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
        custProvinceCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("division"));
        custPostalCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("postal"));
        custPhoneCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("phone"));
    }

    /**
     * Populates the columns of the appointments table
     */
    public void setupAppointmentColumns(){
        apptIdCol.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("contact"));
        typeCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("end"));
        apptCustIdCol.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("customer"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("user"));
    }

    /**
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupCustomerColumns();
        setupAppointmentColumns();
        customerTable.setItems(Customer.getAllCusts());
        appointmentTable.setItems(getAllAppointments());
    }
}
