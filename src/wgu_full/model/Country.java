package wgu_full.model;

import javafx.collections.ObservableList;

import static wgu_full.DAO.CountryDao.getAllCountries;

public class Country {
    private int id;
    private String country;

    /**
     * Constructor
     *
     * @param id
     * @param country
     */
    public Country(int id, String country){
        this.id = id;
        this.country = country;
    }

    /**
     * @return the country id
     */
    public int getId(){ return this.id; }

    /**
     * @param id the id to set
     */
    public void setId(int id){ this.id = id; }

    /**
     * @return the country
     */
    public String getCountry(){ return this.country; }

    /**
     * @param country the country to set
     */
    public void setCountry(String country){ this.country = country; }

    /**
     * Fetches all countries from the data store
     * @return all countries
     */
    public static ObservableList<Country> getAllCountry(){
        return getAllCountries();
    }

    /**
     * Override the toString method to customize display in comboBox
     * @return
     */
    @Override
    public String toString(){
        return ("#" + Integer.toString(this.id) + ' ' +this.country);
    }
}
