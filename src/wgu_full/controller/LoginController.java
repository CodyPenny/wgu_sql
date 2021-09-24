package wgu_full.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The interface for the login form
 */
public class LoginController implements Initializable {

    private String system_language = "";

    /**
     * labels
     */
    @FXML private Label zoneLabel, nameLabel, passwordLabel, companyLabel, errorLabel;

    /**
     * buttons
     */
    @FXML private Button loginBtn;

    /**
     * Sets the zone label on the login form with the user's location
     *
     * @param zone the zone or the user's location
     */
    public void setZoneLabel(String zone){
        zoneLabel.setText(zone);
    }

    public void translate(){
        system_language = Locale.getDefault().toString();
        if( system_language.equals("fr")){
            nameLabel.setText("Nom d'utilisateur");
            passwordLabel.setText("Mot de passe");
            loginBtn.setText("Connexion");
        }
    }

    /**
     * Initializes the controller after its root element processes
     * Sets the zone id with the user's location
     * Translates the form to French if user's system is set to French
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ZoneId zone = ZoneId.systemDefault();
        setZoneLabel(zone.toString());
        //test French
        Locale.setDefault(new Locale("fr"));
        translate();
    }
}
