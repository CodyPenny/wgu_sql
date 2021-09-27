package wgu_full.model;

import javafx.collections.ObservableList;
import static wgu_full.DAO.ContactDao.getContacts;

/**
 * The Contact class
 */
public class Contact {
    private int contact_id;
    private String contact_name;
    private String contact_email;

    /**
     * Constructor
     *
     * @param id the contact id
     * @param name the contact name
     * @param email the contact email
     */

    public Contact(int id, String name, String email){
        this.contact_id = id;
        this.contact_name =  name;
        this.contact_email = email;
    }

    /**
     * @return the id
     */
    public int getId() { return this.contact_id;}

    /**
     * @param id the id to set
     */
    public void setId(int id) { this.contact_id = id;}

    /**
     * @return the name
     */
    public String getName() { return this.contact_name; }

    /**
     * @param name the name to set
     */
    public void setName(String name) { this.contact_name = name; }

    /**
     * @return the email
     */
    public String getEmail() { return this.contact_email; }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) { this.contact_email = email; }

    /**
     * @return ObservableList of all contacts from the data store
     */
    public static ObservableList<Contact> getAllContacts(){ return getContacts();}

    /**
     * Overrides the toString method to customize display in comboBox
     * @return the contact name
     */
    @Override
    public String toString(){ return this.contact_name; }

}
