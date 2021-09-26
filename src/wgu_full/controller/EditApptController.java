package wgu_full.controller;

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
import wgu_full.model.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static wgu_full.model.Contact.getAllContacts;
import static wgu_full.model.Customer.getAllCusts;
import static wgu_full.model.Location.getAllLocations;
import static wgu_full.model.Type.getAllTypes;
import static wgu_full.model.User.getUsers;

public class EditApptController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    Appointment selectedRow;

    /**
     * The textfields in the add appointment form
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

    @FXML private Label errorLabel;



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
