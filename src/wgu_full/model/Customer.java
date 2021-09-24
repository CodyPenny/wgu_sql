package wgu_full.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Customer {
    private ObservableList<Appointment> associatedAppts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private String address;
    private String postal_code;
    private String phone;
    private String division;
    private String country;

    /**
     * Constructor
     *
     * @param id
     * @param name
     * @param address
     * @param postal_code
     * @param phone
     * @param division
     * @param country
     */
    public Customer(int id, String name, String address, String postal_code, String phone, String division, String country){
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
     * Adds an appointment to the associated Appointment list
     *
     * @param appt the appointment
     */
    public void addAssociatedAppointment(Appointment appt){
        this.associatedAppts.add(appt);
        // add to database
    }

    /**
     * @return the ObservableList for associated appointments
     */
    public ObservableList<Appointment> getAllAssociatedAppointments(){
        return this.associatedAppts;
    }

    /**
     *
     * @param appt
     * @return
     */
    public boolean deleteAssociatedAppointment(Appointment appt){
        //delete from database too
        return true;
    }


}
