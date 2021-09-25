package wgu_full.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import wgu_full.model.Country;
import wgu_full.model.Division;

public class EditCustomerController {

    /**
     * Variables that will hold the text entered in the textfields
     */
    private String nameField, addressField, postalField, phoneField;
    private int countryField, divisionField;

    /**
     * The textfields in the add customer form
     */
    @FXML
    private TextField custNameText, custAddressText, postalText, phoneText, idText;

    /**
     * The comboBoxes in the add customer form
     */
    @FXML private ComboBox<Country> countryCombo;
    @FXML private ComboBox<Division> stateOrProvinceCombo;

    /**
     * Label
     */
    @FXML private Label stateOrProvinceLabel, errorLabel;


}
