package wgu_full.model;

import javafx.collections.ObservableList;

import static wgu_full.DAO.CountryDao.getAllCountries;
import static wgu_full.DAO.DivisionDao.getAllDivisions;
import static wgu_full.DAO.DivisionDao.getAllDivisionsByCountry;

public class Division {
    private int id;
    private String division;

    /**
     * Constructor
     *
     * @param id
     * @param division
     */
    public Division(int id, String division){
        this.id = id;
        this.division = division;
    }

    /**
     * @return the division id
     */
    public int getId(){ return this.id; }

    /**
     * @param id the division id to set
     */
    public void setId(int id){ this.id = id; }

    /**
     * @return the division
     */
    public String getDivision(){ return this.division; }

    /**
     * @param division the division to set
     */
    public void setDivision(String division){ this.division = division; }

    /**
     * @return all divisions from the data store
     */
    public static ObservableList<Division> getAllDivision(){
        return getAllDivisions();
    }

    /**
     * @return all divisions by country from the data store
     */
    public static ObservableList<Division> getAllDivisionByCountry(int countryId){
        return getAllDivisionsByCountry(countryId);
    }

    /**
     * Overrides the toString method to customize display in comboBox
     *
     * @return
     */
    @Override
    public String toString(){ return this.division; }

}
