package wgu_full.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import wgu_full.model.Customer;

import java.sql.ResultSet;

import static wgu_full.DAO.JDBC.closeConnection;
import static wgu_full.DAO.JDBC.openConnection;

public class CustomerDao {

    /**
     * Accesses the customers and divisions table
     *
     * @return the ObservableList containing all customers
     */
    public static ObservableList<Customer> getAllCustomers(){
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        try {
            openConnection();
            String query = "SELECT c.*, d.Division FROM customers AS c LEFT JOIN first_level_divisions as d on (c.Division_ID = d.Division_ID)";
            Query.makeQuery(query);
            ResultSet result = Query.getResult();

            while(result.next()){
                int id = result.getInt("Customer_ID");
                String name = result.getString("Customer_Name");
                String address = result.getString("Address");
                String postal = result.getString("Postal_Code");
                String phone = result.getString("Phone");
                String division = result.getString("Division");
                Customer customerResult = new Customer(id, name, address, postal, phone, division, "");
                allCustomers.add(customerResult);
            }
            closeConnection();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return allCustomers;
    }
}
