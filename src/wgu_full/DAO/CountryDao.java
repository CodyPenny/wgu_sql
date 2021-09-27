package wgu_full.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import wgu_full.model.Country;

import java.sql.ResultSet;

import static wgu_full.DAO.JDBC.closeConnection;
import static wgu_full.DAO.JDBC.openConnection;

/**
 * DAO for Country
 */

public class CountryDao {

    /**
     * Retrieves all the country instances
     *
     * @return the ObservableList containing all countries
     */
    public static ObservableList<Country> getAllCountries(){
        ObservableList<Country> allCountries = FXCollections.observableArrayList();
        try {
            openConnection();
            String query = "SELECT * FROM countries";
            Query.makeQuery(query);
            ResultSet result = Query.getResult();
            while(result.next()){
                int id = result.getInt("Country_ID");
                String name = result.getString("Country");
                Country countryResult = new Country(id, name);
                allCountries.add(countryResult);
            }
            closeConnection();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return allCountries;
    }
}
