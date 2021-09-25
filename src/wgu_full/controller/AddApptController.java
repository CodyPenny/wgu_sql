package wgu_full.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddApptController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    /**
     * Variables that will hold the text entered in the textfields
     */
    private String titleField, descriptionField;

    /**
     * The textfields in the add appointment form
     */
    @FXML
    private TextField titleText, descriptionText;

    /**
     * ComboBoxes
     */
    //@FXML
//    private ComboBox<> locationCombo;
//    private ComboBox<> contactCombo;
//    private ComboBox<> typeCombo;
//    private ComboBox<> customerCombo;
//    private ComboBox<> userCombo;

    /**
     * DatePicker
     */
    @FXML
    private DatePicker startTime, endTime;

    @FXML Label errorLabel;

}
