package wgu_full.model;

public class Contact {
    private String contact_id;
    private String contact_name;
    private String contact_email;

    /**
     * Constructor
     *
     * @param id the contact id
     * @param name the contact name
     * @param email the contact email
     */

    public Contact(String id, String name, String email){
        this.contact_id = id;
        this.contact_name =  name;
        this.contact_email = email;
    }

    /**
     * @return the id
     */
    public String getId() { return this.contact_id;}

    /**
     * @param id the id to set
     */
    public void setId(String id) { this.contact_id = id;}

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

}
