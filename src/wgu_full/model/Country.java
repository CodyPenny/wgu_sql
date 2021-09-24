package wgu_full.model;

public class Country {
    private int country_id;
    private String country;

    /**
     * Constructor
     *
     * @param id
     * @param country
     */
    public Country(int id, String country){
        this.country_id = id;
        this.country = country;
    }

    /**
     * @return the country id
     */
    public int getId(){ return this.country_id; }

    /**
     * @param id the id to set
     */
    public void setId(int id){ this.country_id = id; }

    /**
     * @return the country
     */
    public String getCountry(){ return this.country; }

    /**
     * @param country the country to set
     */
    public void setCountry(String country){ this.country = country; }
}
