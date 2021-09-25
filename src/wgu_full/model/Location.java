package wgu_full.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Class that holds the business locations
 */
public class Location {
    private static ObservableList<Location> allLocations = FXCollections.observableArrayList();
    private String city_state;

    /**
     * Constructor
     *
     * @param city_state the city and state/province
     */
    public Location(String city_state){
        this.city_state = city_state;
    }

    /**
     * @return the city and state/province
     */
    public String getCity_state(){ return this.city_state; }

    /**
     * @param location the location to set
     */
    public void setCity_state(String location){ this.city_state = location; }

    /**
     * @return the ObservableList of all locations
     */
    public static ObservableList<Location> getAllLocations(){ return allLocations; }

    /**
     * @param newLocation the new location to add
     */
    public static void addLocation(Location newLocation){ allLocations.add(newLocation); }

    /**
     * Overrides the toString method to customize display in comboBox
     * @return
     */
    @Override
    public String toString(){ return this.city_state; }
}
