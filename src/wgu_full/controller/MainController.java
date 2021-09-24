package wgu_full.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import wgu_full.model.Appointment;
import wgu_full.model.Customer;

import java.net.URL;
import java.util.ResourceBundle;

import static wgu_full.DAO.AppointmentDao.getAllAppointments;
import static wgu_full.DAO.CustomerDao.getAllCustomers;

/**
 * The interface for the main page
 */

public class MainController implements Initializable {

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
        customerTable.setItems(getAllCustomers());
        appointmentTable.setItems(getAllAppointments());
    }
}
