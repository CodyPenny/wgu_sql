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
import wgu_full.model.Type;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafx.scene.control.Alert.AlertType.CONFIRMATION;
import static javafx.scene.control.Alert.AlertType.INFORMATION;
import static wgu_full.DAO.AppointmentDao.*;
import static wgu_full.model.Type.getAllTypes;



@FunctionalInterface
interface DayCalculator {
    public int addDays(int num);
}

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
    @FXML private Label errorLabel, upcomingLabel, typeErrorLabel, reportQtyLabel ;

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

    /**
     * ComboBoxes
     */
    @FXML private ComboBox<Integer> reportMthCombo;
    @FXML private ComboBox<Type> reportTypeCombo;


    /**
     * Validates the type and month comboBox selections are not null and counts the number of appointments by type in the selected month
     */
    public void generateMonthTypeReport(){
        typeErrorLabel.setVisible(false);
        if (!validateMonthTypeComboBox()){
            return;
        }
        int count = getTypeByMonth(reportMthCombo.getValue(), reportTypeCombo.getValue().toString());
        reportQtyLabel.setText(Integer.toString(count));
    }

    /**
     * Validates a selection has been made on the comboBoxes
     */
    public boolean validateMonthTypeComboBox(){
        if(reportMthCombo.getSelectionModel().isEmpty() || reportTypeCombo.getSelectionModel().isEmpty()){
            typeErrorLabel.setText("Please make a selection on both comboBoxes");
            typeErrorLabel.setVisible(true);
            return false;
        }
        return true;
    }
    /**
     * Searches the data store for upcoming appointments for the user that is scheduled in the next 15 minutes
     *
     * @param user_id the user id of the user
     */
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

    /**
     * Displays the count
     *
     * @param count the number of appointments in the next 15 minutes since login
     */
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
     * Demonstrates the use of lambda expressions. It helped eliminate 7 if statements simplifying the overall code.
     */
    public void filterAppointmentsByWeek(){
        ObservableList<Appointment> weekList = FXCollections.observableArrayList();
        LocalDateTime today = LocalDateTime.now();
        int day = today.getDayOfWeek().getValue();

        DayCalculator d = x -> x < 7 ? 8 - x : 8;
        DayCalculator m = x -> x < 7 ? x + 1 : 1;

        int addedDay = d.addDays(day);
        int minusDay = m.addDays(day);

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

    public void openReportsByContacts(ActionEvent event){
        try {
            root = FXMLLoader.load(getClass().getResource("../view/reportByContacts.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            showError(true, "Error opening report.");
        } catch (Exception e){
            showError(true, "Error opening report.");
        }
    }

    public void openReportsByLocation(ActionEvent event){
        try {
            root = FXMLLoader.load(getClass().getResource("../view/reportByLocation.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            showError(true, "Error opening report.");
        } catch (Exception e){
            showError(true, "Error opening report.");
        }
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
     * Prompts confirmation from the user on exit. Closes the program on Ok.
     */
    public void exitProgram(){
        Alert alert = new Alert(CONFIRMATION, "Are you sure?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
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
     * Populates the comboBox with an integer representation of each month
     */
    public void initiateMonthComboBox(){
        ObservableList<Integer> months = FXCollections.observableArrayList();
        for ( int i = 1; i < 13; i++){
            months.add(i);
        }
        reportMthCombo.setItems(months);
    }

    /**
     * Populates the tables with its corresponding columns and data from the data store.
     * The radioButton from the appointments table listens for any selection changes.
     * Searches for any upcoming appointments the user has within 15 minutes of logging in.
     * Populates the month and type comboBox in Reports
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
        filterPerSelection();
        reportTypeCombo.setItems(getAllTypes());
        initiateMonthComboBox();
        filterAppointmentsByWeek();

    }
}
