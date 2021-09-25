package wgu_full.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static wgu_full.DAO.JDBC.*;

public class Query {
    private static String query;
    private static Statement stat;
    private static PreparedStatement stmt;
    private static ResultSet result;

    public static void makeQuery(String q) {
        query = q;
        try {
            stat = connection.createStatement();
            if(query.toLowerCase().startsWith("select"))
                result=stat.executeQuery(q);
            if(query.toLowerCase().startsWith("delete")||query.toLowerCase().startsWith("insert")||query.toLowerCase().startsWith("update"))
                stat.executeUpdate(q);

        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Uses the PreparedStatement interface to validate the input username and password
     *
     * @param username the username string
     * @param pw the password string
     * @throws SQLException JDBC encountered an error with the data source
     */

    public static void makeLoginQuery(String username, String pw) throws SQLException {
        query = "Select * from users where user_name = ? and password = ?";
        try {
            stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, pw);
            result = stmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("SQL error -" + e.getMessage());
        }
    }

    public static void makeDivisionByCountryQuery(int countryId) throws SQLException {
        query = "SELECT * FROM first_level_divisions WHERE Country_ID = ?";
        try {
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, countryId);
            result = stmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("SQL error -" + e.getMessage());
        }
    }

    /**
     * Fetches the result referencing the ResultSet object
     *
     * @return the target data
     */
    public static ResultSet getResult(){
        return result;
    }

}
