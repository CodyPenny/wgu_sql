package wgu_full.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.time.ZoneId;
import java.util.ResourceBundle;

/**
 * The interface for the login form
 */
public class LoginController implements Initializable {

    /**
     * Zone label
     */
    @FXML private Label zoneLabel;


    /**
     * Sets a label on the login form with the user's location
     *
     * @param zone the zone or the user's location
     */
    public void setZoneLabel(String zone){
        zoneLabel.setText(zone);
    }

    /**
     * Initializes the controller after its root element processes
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ZoneId zone = ZoneId.systemDefault();
        setZoneLabel(zone.toString());
    }
}
