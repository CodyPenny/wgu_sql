package wgu_full.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import wgu_full.model.Division;

import java.sql.ResultSet;

import static wgu_full.DAO.JDBC.closeConnection;
import static wgu_full.DAO.JDBC.openConnection;

/**
 * THe DAO for Division
 */
public class DivisionDao {


    /**
     * Retrieves all Division instances
     *
     * @return the ObservableList containing all divisions
     */
    public static ObservableList<Division> getAllDivisions() {
        ObservableList<Division> allDivisions = FXCollections.observableArrayList();
        try {
            openConnection();
            String query = "SELECT * FROM first_level_divisions";
            Query.makeQuery(query);
            ResultSet result = Query.getResult();

            while (result.next()) {
                int id = result.getInt("Division_ID");
                String name = result.getString("Division");

                Division divisionResult = new Division(id, name);
                allDivisions.add(divisionResult);
            }
            closeConnection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return allDivisions;
    }

    /**
     * Retrieves divisions by country
     *
     * @param countryId the country id
     * @return the ObservableList containing all divisions
     */
    public static ObservableList<Division> getAllDivisionsByCountry(int countryId) {
        ObservableList<Division> allDivisions = FXCollections.observableArrayList();
        try {
            openConnection();
            Query.makeDivisionByCountryQuery(countryId);
            ResultSet result = Query.getResult();

            while (result.next()) {
                int id = result.getInt("Division_ID");
                String name = result.getString("Division");
                Division divisionResult = new Division(id, name);
                allDivisions.add(divisionResult);
            }
            closeConnection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return allDivisions;
    }
}