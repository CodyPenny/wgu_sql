package wgu_full.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import wgu_full.model.Country;
import wgu_full.model.Division;

import java.net.URL;
import java.util.ResourceBundle;

public class AddCustomerController implements Initializable {

    /**
     * Variables that will hold the text entered in the textfields
     */
    private String nameField, addressField, postalField, phoneField, countryField, divisionField;

    /**
     * The textfields in the add customer form
     */
    @FXML private TextField custNameText, custAddressText, postalText, phoneText;

    /**
     * The comboBoxes in the add customer form
     */
    @FXML private ComboBox<Country> countryCombo;
    @FXML private ComboBox<Division> stateOrProvinceCombo;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countryCombo.setItems(Country.getAllCountry());
        countryCombo.setVisibleRowCount(5);
    }
}
