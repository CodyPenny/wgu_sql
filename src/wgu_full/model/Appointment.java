package wgu_full.model;

public class Appointment {
    private int appt_id;
    private String title;
    private String description;
    private String location;
    private String type;
    private String start;
    private String end;
    private int customer_id;
    private int user_id;
    private int contact_id;

    /**
     * Constructor
     *
     * @param appt_id
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param customer_id
     * @param user_id
     * @param contact_id
     */
    public Appointment(int appt_id, String title, String description, String location, String type, String start, String end, int customer_id, int user_id, int contact_id){
        this.appt_id = appt_id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customer_id = customer_id;
        this.user_id = user_id;
        this.contact_id = contact_id;
    }

    /**
     * @return the appointment id
     */
    public int getApptId(){
        return this.appt_id;
    }

    /**
     * @param id the id to set
     */
    public void setAppt_id(int id){ this.appt_id = id;}

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
    public String getStart(){ return this.start; }

    /**
     * @param start the start date and time to set
     */
    public void setStart(String start){ this.start = start; }

    /**
     * @return the end date and time
     */
    public String getEnd(){ return this.end; }

    /**
     * @param end the end date and time
     */
    public void setEnd(String end){ this.end = end; }

    /**
     * @return the customer id
     */
    public int getCustomerId(){ return this.customer_id; }

    /**
     * @param customer_id the customer id to set
     */
    public void setCustomerId(int customer_id){ this.customer_id = customer_id; }

    /**
     * @return the user id
     */
    public int getUserId(){ return this.user_id; }

    /**
     * @param user_id the user id to set
     */
    public void setUserId(int user_id){ this.user_id = user_id; }

    /**
     * @return the contact id
     */
    public int getContactId(){ return this.contact_id; }

    /**
     * @param contact_id the contact id to set
     */
    public void setContactId(int contact_id){ this.contact_id = contact_id; }



}
