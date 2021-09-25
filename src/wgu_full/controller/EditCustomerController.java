package wgu_full.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import wgu_full.model.Country;
import wgu_full.model.Customer;
import wgu_full.model.Division;

import java.net.URL;
import java.util.ResourceBundle;

public class EditCustomerController implements Initializable {

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

    /**
     * Populates the form with the selected customer object from the customer table
     * Populates the comboBoxes
     * Pre-selects the selection on the comboBoxes
     *
     * @param customer the selected customer
     */
    public void populateForm(Customer customer){
        int country_id = customer.getCountry();
        idText.setText(String.valueOf(customer.getId()));
        custNameText.setText(customer.getName());
        custAddressText.setText(customer.getAddress());
        postalText.setText(customer.getPostal());
        phoneText.setText(customer.getPhone());

        for(Country c: countryCombo.getItems()){
            if(country_id == c.getId()){
                countryCombo.getSelectionModel().select(c);
                break;
            }
        }
        stateOrProvinceCombo.setItems(Division.getAllDivisionByCountry(country_id));

        for(Division d : stateOrProvinceCombo.getItems()){
            if(customer.getDivision().equals(d.getDivision())){
                stateOrProvinceCombo.getSelectionModel().select(d);
                break;
            }
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countryCombo.setItems(Country.getAllCountry());
    }
}
