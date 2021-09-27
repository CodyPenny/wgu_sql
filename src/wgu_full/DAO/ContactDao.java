package wgu_full.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import wgu_full.model.Contact;

import java.sql.ResultSet;

import static wgu_full.DAO.JDBC.closeConnection;
import static wgu_full.DAO.JDBC.openConnection;

/**
 * The DAO for Contacts
 */

public class ContactDao {

    /**
     * Retreives all contacts
     *
     * @return the ObservableList containing all contacts
     */
    public static ObservableList<Contact> getContacts(){
        ObservableList<Contact> allContacts = FXCollections.observableArrayList();
        try {
            openConnection();
            String query = "SELECT * FROM contacts";
            Query.makeQuery(query);
            ResultSet result = Query.getResult();

            while(result.next()){
                int id = result.getInt("Contact_ID");
                String name = result.getString("Contact_Name");
                String email = result.getString("Email");
                Contact contactResult = new Contact(id, name, email);
                allContacts.add(contactResult);
            }
            closeConnection();
        } catch (Exception e){
        System.out.println(e.getMessage());
        }
        return allContacts;
    }
}
