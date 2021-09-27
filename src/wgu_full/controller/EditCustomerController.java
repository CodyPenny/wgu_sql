package wgu_full.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import wgu_full.model.Country;
import wgu_full.model.Customer;
import wgu_full.model.Division;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static wgu_full.DAO.CustomerDao.updateCustomer;

/**
 * The interface for the
 */

public class EditCustomerController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    /**
     * Variables that will hold the text entered in the textfields
     */
    private String nameField, addressField, postalField, phoneField;
    private int countryField, divisionField, country_id, customer_id;

    /**
     * Holds the selected customer object
     */
    private Customer selectedCustomer;

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
     * Populates the comboBoxes and pre-selects the comboBoxes
     *
     * @param customer the selected customer
     */
    public void populateForm(Customer customer){
        selectedCustomer = customer;
        country_id = customer.getCountry();
        customer_id = customer.getId();
        idText.setText(String.valueOf(customer_id));
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

    /**
     * Saves the customer information in the database
     *
     * @param event when the save button is fired
     */
    public void saveForm(ActionEvent event){
        try {
            nameField = custNameText.getText();
            addressField = custAddressText.getText();
            postalField = postalText.getText();
            phoneField = phoneText.getText();
            countryField = countryCombo.getSelectionModel().getSelectedItem().getId();
            if(stateOrProvinceCombo.getSelectionModel().isEmpty()){
                showError(true, "Please select a state/province.");
                return;
            } else {
                divisionField = stateOrProvinceCombo.getSelectionModel().getSelectedItem().getId();
            }
            if(!validateInput()){
                return;
            }
            updateCustomer(customer_id, nameField, addressField, postalField, phoneField, divisionField);
            backToMain(event);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Validates the text fields are complete
     *
     * @return false if a text field is empty
     */
    public boolean validateInput(){
        if(nameField.isEmpty() || addressField.isEmpty() || postalField.isEmpty() || phoneField.isEmpty() ) {
            showError(true, "All fields must be complete.");
            return false;
        }
        return true;
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
            controller.selectTab(2);
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showError(true, "Can not load the protected page.");
        }
    }

    /**
     * Shows or hides the custom error
     *
     * @param showOrHide sets the error to be visible or invisible
     * @param errorText the custom error message to show
     */
    public void showError(boolean showOrHide, String errorText){
        errorLabel.setText(errorText);
        errorLabel.setVisible(showOrHide);
    }

    /**
     * The country comboBox listens for any changes and displays the province or state associated with the selected country
     * Also changes the label from 'state' to 'province' and vice-versa
     */
    public void stateOrProvince(){
        countryCombo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            stateOrProvinceCombo.setItems(Division.getAllDivisionByCountry(newValue.getId()));
            if(newValue.getId() != 1 ){
                stateOrProvinceLabel.setText("Province");
            } else {
                stateOrProvinceLabel.setText("State");
            }

        });
    }

    /**
     * Initializes the country ComboBox and listens for any changes in the state or province
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countryCombo.setItems(Country.getAllCountry());
        stateOrProvince();
    }
}
