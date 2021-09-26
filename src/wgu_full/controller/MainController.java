package wgu_full.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import wgu_full.DAO.AppointmentDao;
import wgu_full.DAO.CustomerDao;
import wgu_full.model.Appointment;
import wgu_full.model.Customer;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafx.scene.control.Alert.AlertType.CONFIRMATION;
import static javafx.scene.control.Alert.AlertType.INFORMATION;
import static wgu_full.DAO.AppointmentDao.getAllAppointments;
import static wgu_full.DAO.AppointmentDao.getSameDateAppointmentsByUser;


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
    @FXML private TableView<Appointment> upcomingTable;
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
     * The columns of the upcoming appointments table
     */
    @FXML private TableColumn<Appointment, Integer> upApptIdCol;
    @FXML private TableColumn<Appointment, String> upDateCol;
    @FXML private TableColumn<Appointment, String> upTimeCol;

    /**
     * Label
     */
    @FXML private Label errorLabel, upcomingLabel, usernameLabel ;

    /**
     * Tabs and the TabPane
     */
    @FXML private TabPane mainTabPane;
    @FXML private Tab apptTab, custTab, reportTab;

    /**
     * Radio buttons and its ToggleGroup
     */
    @FXML private RadioButton allRadio, monthRadio, weekRadio;
    @FXML private ToggleGroup view;


    public void searchUpcomingAppointments(int user_id){
        ObservableList<Appointment> inFifteen = FXCollections.observableArrayList();
        LocalTime current = LocalTime.now();
        System.out.println("current "+ current);
        for(Appointment meet : getSameDateAppointmentsByUser(user_id, LocalDate.now())){
            LocalTime otherTime = meet.getStart().toLocalDateTime().toLocalTime();
            System.out.println("other time "+ otherTime);
            long timeDiff = ChronoUnit.MINUTES.between(current, otherTime);
            System.out.println("timeDiff"+timeDiff);
            if(timeDiff < 0) {
                timeDiff *= -1;
            }
            if(timeDiff > 0 && timeDiff <= 15){
                inFifteen.add(meet);
            }
        }

        displayUpcomingApptLabel(inFifteen.size());
        upcomingTable.setItems(inFifteen);
    }

    public void displayUpcomingApptLabel(int count){
        if(count > 0){
            upcomingLabel.setText("You have " + count + " upcoming appointment(s).");
            return;
        }
    }

    /**
     * Filters the list of appointments based on the same month
     */
    public void filterAppointmentsByMonth(){
        ObservableList<Appointment> monthList = FXCollections.observableArrayList();
        LocalDate today = LocalDate.now();
        for(Appointment meet : getAllAppointments()){
            if(today.getMonthValue() == meet.getStart().toLocalDateTime().getMonthValue()){
                monthList.add(meet);
            }
        }
        appointmentTable.setItems(monthList);
    }

    /**
     * Calculates the front and back-facing gaps of days from last Sunday to next Sunday, and filters appointments within that window.
     */
    public void filterAppointmentsByWeek(){
        ObservableList<Appointment> weekList = FXCollections.observableArrayList();
        LocalDateTime today = LocalDateTime.now();
        int day = today.getDayOfWeek().getValue();
        int addedDay = 0;
        int minusDay = 0;
        if(day == 1) {
            addedDay = 7;
            minusDay = 2;
        }
        if(day == 2) {
            addedDay = 6;
            minusDay = 3;
        }
        if(day == 3) {
            addedDay = 5;
            minusDay = 4;
        }
        if(day == 4) {
            addedDay = 4;
            minusDay = 5;
        }
        if(day == 5) {
            addedDay = 3;
            minusDay = 6;
        }
        if(day == 6) {
            addedDay = 2;
            minusDay = 7;
        }
        if(day == 7) {
            addedDay = 8;
            minusDay = 1;
        }
        LocalDateTime endDay = today.plusDays(addedDay);
        LocalDateTime startDay = today.minusDays(minusDay);
        for(Appointment meet : getAllAppointments()){
            if((meet.getStart().toLocalDateTime().isBefore(endDay)) && (meet.getStart().toLocalDateTime().isAfter(startDay))){
                weekList.add(meet);
            }
        }
        appointmentTable.setItems(weekList);
    }

    /**
     * The radioButton listens for any selection changes and filters the table based on all, week, or month.
     */
    public void filterPerSelection(){
        view.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle t1) {
                RadioButton rb = (RadioButton) view.getSelectedToggle();
                if(rb != null){
                    if(rb.getText().equals("Month")){
                        filterAppointmentsByMonth();
                    }
                    if(rb.getText().equals("Week")){
                        filterAppointmentsByWeek();
                    }
                    if(rb.getText().equals("All")){
                        appointmentTable.setItems(getAllAppointments());
                    }
                }
            }
        });
    }

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
     * Opens the add new appointment form
     *
     * @param event when the create new button is fired
     * @throws IOException if I/O operation fails
     */
    public void openAddAppointmentForm(ActionEvent event) throws IOException {
        try {
            root = FXMLLoader.load(getClass().getResource("../view/addApptForm.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
     * Opens the edit appointment form
     *
     * @param event when the edit button is fired
     * @throws IOException if I/O operation fails
     */
    public void openEditAppointmentForm(ActionEvent event) throws IOException {
        try {
            Appointment row = appointmentTable.getFocusModel().getFocusedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/editApptForm.fxml"));
            root = loader.load();
            EditApptController controller = loader.getController();
            controller.populateForm(row);

            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showError(true, "Error opening edit form.");
        } catch (Exception e){
            showError(true, "Error opening edit form.");
        }
    }

    /**
     * Deletes a customer from the customer table
     *
     * @param event when the delete button is fired
     */
    public void deleteCustomer(ActionEvent event) {
        try {
            Customer selectedCustomer = customerTable.getFocusModel().getFocusedItem();
            int customerId = selectedCustomer.getId();
            Alert alert = new Alert(CONFIRMATION, "Are you sure? All of the customer's appointments will also be deleted.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if(!CustomerDao.deleteCustomer(customerId)){
                    showError(true, "Error with deletion");
                    return;
                }
                if(!deleteAssociatedAppointments(customerId)){
                    showError(true, "Error with deletion");
                    return;
                }
                Alert okAlert = new Alert(INFORMATION, "The customer and all associated appointments have been deleted.");
                okAlert.show();
                customerTable.setItems(Customer.getAllCusts());
                appointmentTable.setItems(getAllAppointments());
            }
        } catch (Exception e){
            showError(true, "Error with deletion");
        }
    }


    /**
     * Deletes the selected appointment
     *
     * @param event when the delete button is fired
     */
    public void deleteAppointment(ActionEvent event){
        try {
            Appointment selectedAppt = appointmentTable.getFocusModel().getFocusedItem();
            Alert alert = new Alert(CONFIRMATION, "Are you sure?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if(!AppointmentDao.deleteAppointment(selectedAppt.getId())){
                    showError(true, "Error with deletion");
                    return;
                } else {
                    Alert okAlert = new Alert(INFORMATION, "Appointment ID: " + selectedAppt.getId() + ";  Type-" + selectedAppt.getType() + " has been removed.");
                    okAlert.show();
                    appointmentTable.setItems(getAllAppointments());
                }
            }
        } catch (Exception e){
            showError(true, "Error with deletion");
        }
    }

    /**
     * Deletes all appointments associated with a customer
     *
     * @param customer_id the customer id
     * @return false if deletion fails
     */
    public boolean deleteAssociatedAppointments(int customer_id){
        try {
            for(Appointment meet : getAllAppointments()){
                if(meet.getCustomer() == customer_id){
                    if(!AppointmentDao.deleteAppointment(meet.getId())){
                        return false;
                    }
                }
            }
        }catch (Exception e){
            showError(true, "Error with deletion");
            return false;
        }
        return true;
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
     * Selects the tab of the TabPane object
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
     * Populates the columns of the upcoming appointments table
     */
    public void setupUpcomingColumns(){
        upApptIdCol.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("id"));
        upDateCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("date"));
        upTimeCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("time"));
    }


    /**
     * Populates the tables with its corresponding columns and data from the data store.
     * The radioButton from the appointments table listens for any selection changes.
     * Searches for any upcoming appointments the user has within 15 minutes of logging in.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupCustomerColumns();
        setupAppointmentColumns();
//        setupUpcomingColumns();
        customerTable.setItems(Customer.getAllCusts());
        appointmentTable.setItems(getAllAppointments());
        filterPerSelection();

    }
}
