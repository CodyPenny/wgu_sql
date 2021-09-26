package wgu_full.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import wgu_full.model.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static wgu_full.DAO.AppointmentDao.getSameDateAppointments;
import static wgu_full.model.Contact.getAllContacts;
import static wgu_full.model.Customer.getAllCusts;
import static wgu_full.model.Location.getAllLocations;
import static wgu_full.model.Type.getAllTypes;
import static wgu_full.model.User.getUsers;

public class AddApptController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    /**
     * Variables that will hold the text entered in the textfields
     */
    private String titleField, descriptionField;

    /**
     * DateTime object
     */
    LocalDateTime startLDT;
    LocalDateTime endLDT;
    ZonedDateTime startZDT;
    ZonedDateTime endZDT;

    /**
     * The textfields in the add appointment form
     */
    @FXML
    private TextField titleText, descriptionText;

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

    @FXML private Label errorLabel;


    /**
     * Validates the logic of the selected start and end times
     */
    public boolean validateTime(){
        if(startHour.getValue() == 24 && startMin.getValue() > 0){
            showError(true, "Time can not exceed 24 hours.");
            return false;
        }
        if(endHour.getValue() == 24 && endMin.getValue() > 0){
            showError(true, "Time can not exceed 24 hours.");
            return false;
        }
        if(startHour.getValue() > endHour.getValue()){
            showError(true, "Start time can not be greater than the end time.");
            return false;
        }
        if(startHour.getValue() == endHour.getValue()){
            if(startMin.getValue() >= endMin.getValue()){
                showError(true, "Start time can not be greater or the same as the end time.");
                return false;
            }
        }

        startLDT = convertToTimeObject(startHour.getValue(), startMin.getValue());
        endLDT = convertToTimeObject(endHour.getValue(), endMin.getValue());

        if(startLDT.isAfter(endLDT) || startLDT.isEqual(endLDT)){
            showError(true, "Start time can not be greater or the same as the end time.");
            return false;
        }

        if (!validateZonedDateTimeBusiness(startLDT) || !validateZonedDateTimeBusiness(endLDT)){
            return false;
        };

        return true;
    }

    public boolean validateInputFields(){
        if(titleText.getText().isEmpty() || descriptionText.getText().isEmpty()){
            System.out.println("empty");
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

    public void saveAppointment(){
        // check all entries are selected
        if(!validateInputFields()){
            return;
        }
        if(!validateTime()){
            return;
        }
        startZDT = convertToSystemZonedDateTime(startLDT);
        endZDT = convertToSystemZonedDateTime(endLDT);

        // check for overlap with selected customer
        LocalDate date = startLDT.toLocalDate();
        System.out.println("local date " + date);
        getSameDateAppointments( 1, date);
        // convert to utc

        // save to db


    }



    /**
     * Takes the selected date, hour, and minute and creates a LocalDateTime object
     * @param hr the hour
     * @param min the min
     * @return LocalDateTime object of the input time
     */
    public LocalDateTime convertToTimeObject(int hr, int min) {
        LocalDate date = dateBox.getValue();
        return LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), hr, min);
    }

    /**
     * Converts a LocalDateTime object to a ZonedDateTime object
     *
     * @param ldt an instance of LocalDateTime
     * @return ZonedDateTime relative to the user's system time
     */
    public ZonedDateTime convertToSystemZonedDateTime(LocalDateTime ldt){
        return ldt.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
    }

    /**
     * Takes a date-time object and returns a copy with the UTC time zone
     *
     * @param zdt an instance of the ZonedDateTime object
     * @return a UTC date-time object
     */
    public ZonedDateTime convertToUTC(ZonedDateTime zdt){
       return zdt.withZoneSameInstant(ZoneId.of("UTC"));
    }

    /**
     * Validates the selected time against the business hours
     *
     * @param ldt an instance of the LocalDateTime object
     * @return true if the selected time is within business hours
     */
    public boolean validateZonedDateTimeBusiness(LocalDateTime ldt){
        ZonedDateTime zdt = convertToSystemZonedDateTime(ldt);
        ZonedDateTime est = zdt.withZoneSameInstant(ZoneId.of("America/New_York"));
        System.out.println("est " + est);
        ZonedDateTime open = est.withHour(8);
        ZonedDateTime close = est.withHour(22);
        System.out.println("open " + open);
        System.out.println("close " + close);
        if(est.isAfter(close)){
            System.out.println("after close");
            showError(true, "Selected time is after business hours. Please select a time within 8am-10pm est.");
            return false;
        }
        if(est.isBefore(open)) {
            System.out.println("before open");
            showError(true, "Selected time is before business hours. Please select a time within 8am-10pm est.");
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
     * Disable past dates on DatePicker
     * Taken from Oracle documentation
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
     * Populate the comboBoxes
     * Initiate time spinners with hour and minutes
     * Disable past dates on the Date Picker
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contactCombo.setItems(getAllContacts());
        customerCombo.setItems(getAllCusts());
        userCombo.setItems(getUsers());
        locationCombo.setItems(getAllLocations());
        typeCombo.setItems(getAllTypes());

        // limit hour to EST after retrieving local time
        SpinnerValueFactory<Integer> startHourFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 24, 8, 1);
        SpinnerValueFactory<Integer> endHourFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 24, 8);
        SpinnerValueFactory<Integer> startMinFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 50, 0, 10);
        SpinnerValueFactory<Integer> endMinFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 50, 0, 10);
        startHour.setValueFactory(startHourFactory);
        startMin.setValueFactory(startMinFactory);
        endHour.setValueFactory(endHourFactory);
        endMin.setValueFactory(endMinFactory);

        disablePastDates();
    }
}
