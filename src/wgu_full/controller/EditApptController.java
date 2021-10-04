package wgu_full.controller;

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
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.LocalTimeStringConverter;
import wgu_full.model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static wgu_full.DAO.AppointmentDao.getSameDateAppointments;
import static wgu_full.DAO.AppointmentDao.updateAppointment;
import static wgu_full.model.Contact.getAllContacts;
import static wgu_full.model.Customer.getAllCusts;
import static wgu_full.model.Location.getAllLocations;
import static wgu_full.model.Type.getAllTypes;
import static wgu_full.model.User.getUsers;

/**
 * The interface for the Edit Appointment form
 */
public class EditApptController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    Appointment selectedRow;

    /**
     * Holds the start and end local date times
     */
    LocalDateTime startLDT;
    LocalDateTime endLDT;
    /**
     * Holds the start and end zoned date time
     */
    ZonedDateTime startZDT;
    ZonedDateTime endZDT;

    /**
     * An ObservableList of possible overlapping appointments
     */
    private static ObservableList<Appointment> overlaps;

    /**
     * The text fields in the add appointment form
     */
    @FXML
    private TextField titleText, descriptionText, idText;

    /**
     * ComboBoxes
     */
    @FXML
    private ComboBox<Location> locationCombo;
    @FXML
    private ComboBox<Contact> contactCombo;
    @FXML
    private ComboBox<Type> typeCombo;
    @FXML
    private ComboBox<Customer> customerCombo;
    @FXML
    private ComboBox<User> userCombo;


    /**
     * DatePicker
     */
    @FXML
    private DatePicker dateBox;

    /**
     * Spinners
     */
    @FXML
    private Spinner<Integer> startHour,startMin, endHour, endMin;
    @FXML
    private Spinner<LocalTime> testSpinner;

    @FXML private Label errorLabel;

    /**
     * Updates the selected appointment with newly entered input
     * Checks for any appointment overlaps
     *
     * @param event when the save button is fired
     * @throws IOException whe I/O operation fails
     */
    public void saveAppointment(ActionEvent event) throws IOException {
        if(!validateInputFields()){
            return;
        }
        if(!validateTime()){
            return;
        }
        LocalDate date = startLDT.toLocalDate();
        int customer = customerCombo.getSelectionModel().getSelectedItem().getId();
        overlaps = getSameDateAppointments(customer, date);
        if(overlaps.size() > 0){
            boolean noOverlap = validateOverlap(overlaps);
            if (!noOverlap){
                showError(true, "Selected time for customer overlaps with another appointment. Please select another time.");
                return;
            }
        }
        // update db
        String titleField = titleText.getText();
        String descriptionField = descriptionText.getText();
        String loc = locationCombo.getSelectionModel().getSelectedItem().toString();
        int contact = contactCombo.getSelectionModel().getSelectedItem().getId();
        String ty = typeCombo.getSelectionModel().getSelectedItem().toString();
        int user = userCombo.getSelectionModel().getSelectedItem().getId();
        Timestamp start = convertZDT(startZDT);
        Timestamp end = convertZDT(endZDT);
        updateAppointment(selectedRow.getId(), titleField, descriptionField, loc, start, end, ty, user, contact, customer);
        backToMain(event);
    }

    /**
     * Validates the logic of the selected start and end times
     *
     * @return false if the logic is incorrect
     */
    public boolean validateTime(){
        if(startHour.getValue() > endHour.getValue()){
            showError(true, "The start time can not be greater than the end time.");
            return false;
        }
        if(startHour.getValue() == endHour.getValue()){
            if(startMin.getValue() >= endMin.getValue()){
                showError(true, "The start time can not be greater or the same as the end time.");
                return false;
            }
        }

        startLDT = convertToTimeObject(startHour.getValue(), startMin.getValue());
        endLDT = convertToTimeObject(endHour.getValue(), endMin.getValue());

        System.out.println("start " + startLDT );
        System.out.println("end " + endLDT );

        if(startLDT.isAfter(endLDT) || startLDT.isEqual(endLDT)){
            showError(true, "The start time can not be greater or the same as the end time.");
            return false;
        }
        startZDT = convertToSystemZonedDateTime(startLDT);
        endZDT = convertToSystemZonedDateTime(endLDT);

        if (!validateZonedDateTimeBusiness(startZDT)){
            return false;
        }
        if (!validateZonedDateTimeBusiness(endZDT)){
            return false;
        };
        return true;
    }

    /**
     * Takes the selected date, hour, and minute and creates a LocalDateTime object
     *
     * @param hr the hour
     * @param min the min
     * @return LocalDateTime object of the input time
     */
    public LocalDateTime convertToTimeObject(int hr, int min) {
        LocalDate date = dateBox.getValue();
        return LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), hr, min);
    }

    /**
     * Validates the selected time against business hours
     *
     * @param zdt an instance of the ZonedDateTime object
     * @return true if the selected time is within business hours
     */
    public boolean validateZonedDateTimeBusiness(ZonedDateTime zdt){
        ZonedDateTime est = zdt.withZoneSameInstant(ZoneId.of("America/New_York"));
        ZonedDateTime open = est.withHour(8);
        ZonedDateTime close = est.withHour(22);

        if(est.isAfter(close)){
            showError(true, "The selected time is outside of business hours. Please select a time within 8am-10pm est.");
            return false;
        }
        if(est.isBefore(open)) {
            showError(true, "The selected time is outside of business hours. Please select a time within 8am-10pm est.");
            return false;
        }
        return true;
    }

    /**
     * Sets the ZonedDateTime reference to UTC and then converts the object to a Timestamp
     *
     * @param zdt the ZonedDateTime reference
     * @return the timestamp
     */
    public Timestamp convertZDT(ZonedDateTime zdt){
        return Timestamp.valueOf(zdt.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime());
    }

    /**
     * Looks for any overlaps between the input list of appointments and the selected appointment times
     * Skips the appointment if it is the same appointment that was returned
     *
     * @param test the list of appointments with possible conflicts
     * @return false if there is an overlap
     */
    public boolean validateOverlap(ObservableList<Appointment> test){
        LocalDateTime A = startLDT;
        LocalDateTime Z = endLDT;

        for(Appointment appt : test){
            if(appt.getId() == selectedRow.getId()){
                continue;
            }
            LocalDateTime S = LocalDateTime.parse(appt.getStart(), formatter);
            LocalDateTime E = LocalDateTime.parse(appt.getEnd(), formatter);
            //case 1 - when the start is in the window
            if((A.isAfter(S) || A.isEqual(S)) && A.isBefore(E)){
                return false;
            }
            //case 2 - when the end is in the window
            if(Z.isAfter(S) && (Z.isBefore(E) || Z.isEqual(E))){
                return false;
            }
            //case 3 - when the start and end are outside of the window
            if(((A.isBefore(S) || A.isEqual(S)) && (Z.isAfter(E) || Z.isEqual(E)))){
                return false;
            }
        }
        return true;
    }

    /**
     * Validates text fields are complete and the comboBoxes are selected
     *
     * @return false if incomplete
     */
    public boolean validateInputFields(){
        if(titleText.getText().isEmpty() || descriptionText.getText().isEmpty()){
            showError(true, "All fields are required. Please complete.");
            return false;
        }
        if(locationCombo.getSelectionModel().isEmpty()){
            showError(true, "Please select a location.");
            return false;
        }
        if(contactCombo.getSelectionModel().isEmpty()){
            showError(true, "Please select a contact.");
            return false;
        }
        if(typeCombo.getSelectionModel().isEmpty()){
            showError(true, "Please select the type.");
            return false;
        }
        if(customerCombo.getSelectionModel().isEmpty()){
            showError(true, "Please select a customer.");
            return false;
        }
        if(userCombo.getSelectionModel().isEmpty()){
            showError(true, "Please select a user.");
            return false;
        }
        return true;
    }

    /**
     * Converts the LocalDateTime object to a ZonedDateTime object
     *
     * @param ldt an instance of LocalDateTime
     * @return an instance of ZonedDateTime relative to the user's system time
     */
    public ZonedDateTime convertToSystemZonedDateTime(LocalDateTime ldt){
        return ZonedDateTime.of(ldt, ZoneId.systemDefault());
    }


    /**
     * Populates the form with the attributes of the selected appointment
     * Converts UTC time to the local time zone
     *
     * @param row the appointment
     */
    public void populateForm(Appointment row){
        selectedRow = row;
        idText.setText(Integer.toString(row.getId()));
        titleText.setText(row.getTitle());
        descriptionText.setText(row.getDescription());
        for(Location item : locationCombo.getItems()){
            if(item.getCity_state().equals(row.getLocation())){
                locationCombo.getSelectionModel().select(item);
                break;
            }
        }
        for(Contact person : contactCombo.getItems()){
            if(person.getName().equals(row.getContact())){
                contactCombo.getSelectionModel().select(person);
                break;
            }
        }
        for(Type meeting : typeCombo.getItems()){
            if(meeting.getAppointment_type().equals(row.getType())){
                typeCombo.getSelectionModel().select(meeting);
                break;
            }
        }
        for(Customer cust : customerCombo.getItems()){
            if(cust.getId() == row.getCustomer()){
                customerCombo.getSelectionModel().select(cust);
                break;
            }
        }
        for(User user : userCombo.getItems()){
            if(user.getId() == row.getUser()){
                userCombo.getSelectionModel().select(user);
                break;
            }
        }
        LocalDateTime startLDT = LocalDateTime.parse(row.getStart(), formatter);
        LocalDateTime endLDT = LocalDateTime.parse(row.getEnd(), formatter);
        dateBox.setValue(startLDT.toLocalDate());
        setSpinners(startLDT.getHour(), endLDT.getHour(), startLDT.getMinute(), endLDT.getMinute());
    }

    /**
     * Takes an arbitrary hour and takes an instant of it in Eastern Standard Time and converts it to the local zone hour.
     *
     * @param hr the hour
     * @return the hour in local zone time
     */
    public int setBusinessHoursToLocalHours(int hr){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime ldt = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), hr, 0);
        return ZonedDateTime.
                of(ldt, ZoneId.of("America/New_York"))
                .toOffsetDateTime()
                .atZoneSameInstant(ZoneId.systemDefault())
                .toLocalDateTime().toLocalTime().getHour();
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
            controller.selectTab(1);
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showError(true, "Can not load the protected page.");
        }
    }

    /**
     * Disable past dates on the DatePicker widget
     * (Taken from the Oracle documentation)
     */
    private void disablePastDates() {
        dateBox.setValue(LocalDate.now());
        final Callback<DatePicker, DateCell> dayCellFactory =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item.isBefore(
                                        dateBox.getValue().plusDays(1))
                                ) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                                }
                            }
                        };
                    }
                };
        dateBox.setDayCellFactory(dayCellFactory);
    }

    /**
     * Initializes the start and end hours/minutes of the spinners based on business hours
     *
     * @param initStartHr the start hour
     * @param initEndHr the end hour
     * @param initStartMin the start minute
     * @param initEndMin the end minute
     */
    public void setSpinners(int initStartHr, int initEndHr, int initStartMin, int initEndMin){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        ObservableList<LocalTime> time = FXCollections.observableArrayList();

        int startHr = setBusinessHoursToLocalHours(8);
        int endHr = setBusinessHoursToLocalHours(22);
        SpinnerValueFactory<LocalTime> timeFactory = new SpinnerValueFactory<LocalTime>() {
            {
                setConverter(new LocalTimeStringConverter(formatter,null));
            }
            @Override
            public void decrement(int steps) {
                steps = 15;
                if (getValue() == null)
                    setValue(LocalTime.now());
                else {
                    LocalTime time = (LocalTime) getValue();
                    setValue(time.minusMinutes(steps));
                }
            }

            @Override
            public void increment(int steps) {
                steps = 15;
                if (this.getValue() == null)
                    setValue(LocalTime.now());
                else {
                    LocalTime time = (LocalTime) getValue();
                    if(time.isBefore(LocalTime.of(20,00))){
                        setValue(time.plusMinutes(steps));
                    }
                }
            }
        };

        if(startHr > endHr){
            SpinnerValueFactory<Integer> startHourFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(startHr, 24, initStartHr);
            SpinnerValueFactory<Integer> endHourFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, endHr, initEndHr);
            startHour.setValueFactory(startHourFactory);
            endHour.setValueFactory(endHourFactory);
            startHourFactory.setWrapAround(true);
            endHourFactory.setWrapAround(true);

        } else {
            SpinnerValueFactory<Integer> startHourFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(startHr, endHr, initStartHr);
            SpinnerValueFactory<Integer> endHourFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(startHr, endHr, initEndHr);
            startHour.setValueFactory(startHourFactory);
            endHour.setValueFactory(endHourFactory);
        }
        SpinnerValueFactory<Integer> startMinFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 45, initStartMin, 15);
        SpinnerValueFactory<Integer> endMinFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 45, initEndMin, 15);
        startMin.setValueFactory(startMinFactory);
        endMin.setValueFactory(endMinFactory);

        timeFactory.setValue(LocalTime.now());

        testSpinner.setValueFactory(timeFactory);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contactCombo.setItems(getAllContacts());
        customerCombo.setItems(getAllCusts());
        userCombo.setItems(getUsers());
        locationCombo.setItems(getAllLocations());
        typeCombo.setItems(getAllTypes());
        setSpinners(0, 0, 0,0);
        disablePastDates();
    }

}
