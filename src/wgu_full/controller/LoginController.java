package wgu_full.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import static wgu_full.DAO.UserDao.validateUser;

/**
 * The interface for the login form
 */
public class LoginController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private String usernameField, passwordField;
    ResourceBundle bundle;

    /**
     * Labels
     */
    @FXML private Label zoneLabel, nameLabel, passwordLabel, companyLabel, errorLabel;

    /**
     * Buttons
     */
    @FXML private Button loginBtn;

    /**
     * Textfields
     */
    @FXML private TextField userNameInput, passwordInput;

    /**
     * Sets the label for zone information with the user's location
     *
     * @param zone the zone or the user's location
     */
    public void setZoneLabel(String zone){
        zoneLabel.setText(zone);
    }

    /**
     * Translates the form text to French if the user's system is set to French
     *
     * @param rb the ResourceBundle
     */
    public void translate(ResourceBundle rb){
        bundle = rb;
        nameLabel.setText(bundle.getString("nameLabel"));
        passwordLabel.setText(bundle.getString("passwordLabel"));
        loginBtn.setText(bundle.getString("loginBtn"));
    }

    /**
     * Validates the username and password
     * Displays the error in French if the user's system is set to French
     *
     * @return 0 if false
     * @throws SQLException if it encounters an error in the driver or database
     */
    public int validateInput() throws SQLException {
        try {
            if(usernameField == null || usernameField.isEmpty()){
                showError(true, bundle.getString("enterUser"));
                return 0;
            }
            if(passwordField == null || passwordField.isEmpty()){
                showError(true, bundle.getString("enterPassword"));
                return 0;
            }
            int result = validateUser(usernameField, passwordField);
            if(result == 0){
                createLog(usernameField, "Failed");
                showError(true, bundle.getString("invalid"));
                return 0;
            }
        } catch (Exception e){
            System.out.println("Error -" + e.getMessage());
        }
        return 1;
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
     * Validates the credentials of the user
     *
     * @param event when the login button is fired
     * @throws IOException if I/O operation fails
     */
    public void login(ActionEvent event) throws IOException {
        try {
            usernameField = userNameInput.getText();
            passwordField = passwordInput.getText();
            int result = validateInput();
            if( result == 0 ){
                return;
            }
            createLog(usernameField, "Success");
            goToMain(event, result);
        } catch(IOException | SQLException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Opens the main page
     *
     * @param event login button has been fired
     * @param user_id the user id
     * @throws IOException if I/O operation fails
     */
    public void goToMain(ActionEvent event, int user_id) throws IOException{
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/main.fxml"));
            root = loader.load();
            MainController controller = loader.getController();
            controller.setupUpcomingColumns();
            controller.searchUpcomingAppointments(user_id);
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            showError(true, "Can not load the protected page.");
        }
    }

    /**
     * Uses the BufferedWriter and FileWriter object to record user login attempts to a text file
     *
     * @param username the username entered
     * @param success the status
     * @throws IOException if I/O operation fails
     */
    public static void createLog(String username, String success) throws IOException {
        String filename = "login_activity.txt";
        String timestamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
        String log = "Username: " + username +  " | Attempted login timestamp: " + timestamp + " | Status: " + success + "\n";
        BufferedWriter buffer = null;
        try {
            buffer = new BufferedWriter(new FileWriter(filename,true));
            buffer.write(log);
        } catch(IOException e){
            System.out.println("File error: " + e.getMessage());
        }
        finally {
            try {
                if (buffer != null) {
                    buffer.close();
                }
            } catch (IOException e) {
                System.out.println("File error: " + e.getMessage());
            }
        }
    }

    /**
     * Initializes the controller after its root element processes
     * Sets the zone id with the user's location
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       ZoneId zone = ZoneId.systemDefault();
        // test zone
        //ZoneId zone = ZoneId.of("Europe/London");
        setZoneLabel(zone.toString());
    }
}
