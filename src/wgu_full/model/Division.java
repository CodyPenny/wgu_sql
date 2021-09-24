package wgu_full.model;

public class Division {
    private int division_id;
    private String division;
    private int country_id;

    /**
     * Constructor
     *
     * @param division_id
     * @param division
     * @param country_id
     */
    public Division(int division_id, String division, int country_id){
        this.division_id = division_id;
        this.division = division;
        this.country_id = country_id;
    }

    /**
     * @return the division id
     */
    public int getDivisionId(){ return this.division_id; }

    /**
     * @param id the division id to set
     */
    public void setDivisionId(int id){ this.division_id = id; }

    /**
     * @return the division
     */
    public String getDivision(){ return this.division; }

    /**
     * @param division the division to set
     */
    public void setDivision(String division){ this.division = division; }

    /**
     * @return the country id
     */
    public int getCountryId(){ return this.country_id; }

    /**
     * @param id the country id to set
     */
    public void setCountryId(int id){ this.country_id = id; }
}
