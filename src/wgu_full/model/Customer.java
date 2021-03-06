package wgu_full.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import static wgu_full.DAO.CustomerDao.getAllCustomers;

/**
 * The Customer class
 */
public class Customer {
    private int id;
    private String name;
    private String address;
    private String postal_code;
    private String phone;
    private String division;
    private int country;

    /**
     * Constructor
     *
     * @param id the id
     * @param name the name
     * @param address the address
     * @param postal_code the postal code
     * @param phone the phone number
     * @param division the division
     * @param country the country
     */
    public Customer(int id, String name, String address, String postal_code, String phone, String division, int country){
        this.id = id;
        this.name = name;
        this.address = address;
        this.postal_code = postal_code;
        this.phone = phone;
        this.division = division;
        this.country = country;
    }

    /**
     * @return the id
     */
    public int getId(){ return this.id; }

    /**
     * @return the name
     */
    public String getName(){ return this.name; }

    /**
     * @return the address
     */
    public String getAddress(){ return this.address; }

    /**
     * @return the postal code
     */
    public String getPostal(){ return this.postal_code; }

    /**
     * @return the phone number
     */
    public String getPhone(){ return this.phone; }

    /**
     * @return the division
     */
    public String getDivision(){ return this.division; }

    /**
     * @return the country id
     */
    public int getCountry(){ return this.country;}

    /**
     * @param id the id to set
     */
    public void setId(int id){ this.id = id; }

    /**
     * @param name the name to set
     */
    public void setName(String name){ this.name = name; }

    /**
     * @param address the address to set
     */
    public void setAddress(String address){ this.address= address; }

    /**
     * @param postal_code the postal code to set
     */
    public void setPostal(String postal_code){ this.postal_code = postal_code; }

    /**
     * @param phone the phone number to set
     */
    public void setPhone(String phone){ this.phone = phone; }

    /**
     * @param country the country id to set
     */
    public void setCountry(int country){ this.country = country;}

    /**
     * @return The ObservableList for all customers from the data store
     */
    public static ObservableList<Customer> getAllCusts(){return getAllCustomers();}

    /**
     * Overrides the toString method to customize display in comboBox
     * @return the id and name
     */
    @Override
    public String toString(){ return (Integer.toString(this.id) + " - " + this.name); }


}
