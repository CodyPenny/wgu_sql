package wgu_full.model;

import java.sql.Timestamp;

public class Appointment {
    private int id;
    private String title;
    private String description;
    private String location;
    private String type;
    private Timestamp start;
    private Timestamp end;
    private int customer;
    private int user;
    private String contact;

    /**
     * Constructor
     *  @param id
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param customer
     * @param user
     * @param contact
     */
    public Appointment(int id, String title, String description, String location, String type, Timestamp start, Timestamp end, int customer, int user, String contact){
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customer = customer;
        this.user = user;
        this.contact = contact;
    }

    /**
     * @return the appointment id
     */
    public int getId(){
        return this.id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id){ this.id = id;}

    /**
     * @return the title
     */
    public String getTitle(){ return this.title; }

    /**
     * @param title the title to set
     */
    public void setTitle(String title){ this.title = title;}

    /**
     * @return the description
     */
    public String getDescription(){ return this.description; }

    /**
     * @param description the description to set
     */
    public void setDescription(String description){this.description = description; }

    /**
     * @return the location
     */
    public String getLocation(){ return this.location; }

    /**
     * @param location the location to set
     */
    public void setLocation(String location){ this.location = location; }

    /**
     * @return the type
     */
    public String getType(){ return this.type; }

    /**
     * @param type the type to set
     */
    public void setType(String type){ this.type = type; }

    /**
     * @return the start date and time
     */
    public Timestamp getStart(){ return this.start; }

    /**
     * @param start the start date and time to set
     */
    public void setStart(Timestamp start){ this.start = start; }

    /**
     * @return the end date and time
     */
    public Timestamp getEnd(){ return this.end; }

    /**
     * @param end the end date and time
     */
    public void setEnd(Timestamp end){ this.end = end; }

    /**
     * @return the customer id
     */
    public int getCustomer(){ return this.customer; }

    /**
     * @param customer the customer id to set
     */
    public void setCustomer(int customer){ this.customer = customer; }

    /**
     * @return the user id
     */
    public int getUser(){ return this.user; }

    /**
     * @param user the user id to set
     */
    public void setUser(int user){ this.user = user; }

    /**
     * @return the contact id
     */
    public String getContact(){ return this.contact; }

    /**
     * @param contact the contact id to set
     */
    public void setContact(String contact){ this.contact = contact; }



}
