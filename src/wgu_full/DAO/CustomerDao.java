package wgu_full.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import wgu_full.model.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;

import static wgu_full.DAO.JDBC.closeConnection;
import static wgu_full.DAO.JDBC.openConnection;
import static wgu_full.DAO.Query.*;

/**
 * The DAO for Customer
 */

public class CustomerDao {

    /**
     * Creates a new customer instance
     *
     * @param name the name of the customer
     * @param address the address of the customer
     * @param postal the postal code of the customer
     * @param phone the phone number
     * @param division the state or province id
     * @throws SQLException JDBC encountered an error with the data source
     */
    public static void addCustomer(String name, String address, String postal, String phone, int division) throws SQLException {
        openConnection();
        createCustomer(name, address, postal, phone, division);
        closeConnection();
        return;
    }

    /**
     * Updates the customer attributes
     *
     * @param id the customer id
     * @param name the name of the customer
     * @param address the address of the customer
     * @param postal the postal code of the customer
     * @param phone the phone number
     * @param division the state or province id
     * @throws SQLException JDBC encountered an error with the data source
     */
    public static void updateCustomer(int id ,String name, String address, String postal, String phone, int division) throws SQLException {
        openConnection();
        changeCustomer(id, name, address, postal, phone, division);
        closeConnection();
        return;
    }

    /**
     * Deletes a customer
     *
     * @param id the customer id
     * @return true if the customer is deleted without any errors
     * @throws SQLException JDBC encountered an error with the data source
     */
    public static boolean deleteCustomer(int id) throws SQLException {
        openConnection();
        if(!removeCustomer(id)){
            return false;
        }
        closeConnection();
        return true;
    }

    /**
     * Retrieves all customers
     *
     * @return the ObservableList containing all customers
     */
    public static ObservableList<Customer> getAllCustomers(){
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        try {
            openConnection();
            String query = "SELECT c.*, d.Division, d.Country_ID FROM customers AS c LEFT JOIN first_level_divisions as d on (c.Division_ID = d.Division_ID)";
            Query.makeQuery(query);
            ResultSet result = Query.getResult();

            while(result.next()){
                int id = result.getInt("Customer_ID");
                String name = result.getString("Customer_Name");
                String address = result.getString("Address");
                String postal = result.getString("Postal_Code");
                String phone = result.getString("Phone");
                String division = result.getString("Division");
                int country = result.getInt("Country_ID");
                Customer customerResult = new Customer(id, name, address, postal, phone, division, country);
                allCustomers.add(customerResult);
            }
            closeConnection();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return allCustomers;
    }
}
