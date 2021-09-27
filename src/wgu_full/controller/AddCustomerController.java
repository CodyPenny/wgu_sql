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
import wgu_full.model.Division;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static wgu_full.DAO.CustomerDao.addCustomer;

/**
 * The interface for the Add Customer form
 */
public class AddCustomerController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    /**
     * Variables that will hold the text entered in the textfields
     */
    private String nameField, addressField, postalField, phoneField;
    private int countryField, divisionField;

    /**
     * The textfields in the add customer form
     */
    @FXML private TextField custNameText, custAddressText, postalText, phoneText;

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
     * Takes the input text from the text fields, validates the entry, then creates a new customer
     *
     * @param event the save button has been fired
     */
    public void createCustomer(ActionEvent event) {
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

            addCustomer(nameField, addressField, postalField, phoneField, divisionField);
            backToMain(event);

        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    /**
     * Validates text fields are not empty
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
     * Populates the country and state/province comboBoxes
     * Based on the region, the label changes to 'state' or 'province'
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countryCombo.setItems(Country.getAllCountry());
        countryCombo.setVisibleRowCount(5);
        countryCombo.getSelectionModel().selectFirst();
        stateOrProvince();
        stateOrProvinceCombo.setItems(Division.getAllDivisionByCountry(1));
        stateOrProvinceCombo.setVisibleRowCount(5);

    }
}
