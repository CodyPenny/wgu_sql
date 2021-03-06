package wgu_full.DAO;

import java.sql.*;

import static wgu_full.DAO.JDBC.*;

/**
 * Query DAO
 */
public class Query {
    private static String query;
    private static Statement stat;
    private static PreparedStatement stmt;
    private static ResultSet result;

    /**
     * Queries the database
     * @param q the query string
     */
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
     * Uses a PreparedStatement interface to validate the input username and password
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

    /**
     * Uses a PreparedStatement interface to retrieve divisions by country
     *
     * @param countryId the country id
     * @throws SQLException JDBC encountered an error with the data source
     */
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
     * Uses a PreparedStatement interface to create a new customer
     *
     * @param name the name of the customer
     * @param address the address of the customer
     * @param postal the postal code of the customer
     * @param phone the phone number
     * @param division the state or province id
     * @throws SQLException JDBC encountered an error with the data source
     */
    public static void createCustomer(String name, String address, String postal, String phone, int division) throws SQLException{
        query = "INSERT INTO customers " + "(Customer_Name, Address, Postal_Code, Phone, Division_ID)" + "values (?,?,?,?,?)";
        try {
            stmt = connection.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, address);
            stmt.setString(3, postal);
            stmt.setString(4, phone);
            stmt.setInt(5, division);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("SQL error -" + e.getMessage());
        }
    }

    /**
     * Updates a customer instance
     *
     * @param id the customer id
     * @param name the name of the customer
     * @param address the address of the customer
     * @param postal the postal code of the customer
     * @param phone the phone number
     * @param division the state or province id
     * @throws SQLException JDBC encountered an error with the data source

     */
    public static void changeCustomer(int id ,String name, String address, String postal, String phone, int division) throws SQLException {
        query = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? where Customer_ID = ?";
        try {
            stmt = connection.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, address);
            stmt.setString(3, postal);
            stmt.setString(4, phone);
            stmt.setInt(5, division);
            stmt.setInt(6, id);
            stmt.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            System.out.println("SQL error -" + e.getMessage());
        }
    }

    /**
     * Deletes a customer
     *
     * @param id the customer id
     * @return true if customer is deleted without any errors
     * @throws SQLException JDBC encountered an error with the data source
     */
    public static boolean removeCustomer(int id) throws SQLException {
        query = "DELETE FROM customers WHERE Customer_ID = ?";
        try {
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("SQL error -" + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes an appointment
     *
     * @param id the appointment id
     * @return false if deletion fails
     * @throws SQLException JDBC encountered an error with the data source
     */
    public static boolean removeAppt(int id) throws SQLException {
        query = "DELETE FROM appointments WHERE Appointment_ID = ?";
        try {
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("SQL error -" + e.getMessage());
            return false;
        }
    }

    /**
     * Creates an instance of the appointment using PreparedStatement
     * @param title the title
     * @param desc the description
     * @param loc the location
     * @param type the type
     * @param start the start date and time
     * @param end the end date and time
     * @param custId the customer Id
     * @param userId the user Id
     * @param contactId the contact ID
     * @throws SQLException JDBC encountered an error with the data source
     */
    public static void createAppt(String title, String desc, String loc, String type, Timestamp start, Timestamp end, int custId, int userId, int contactId) throws SQLException {
        query = "INSERT INTO appointments " + "(Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID)" + "values (?,?,?,?,?,?,?,?,?)";
        try {
            stmt = connection.prepareStatement(query);
            stmt.setString(1, title);
            stmt.setString(2, desc);
            stmt.setString(3, loc);
            stmt.setString(4, type);
            stmt.setTimestamp(5, start);
            stmt.setTimestamp(6, end);
            stmt.setInt(7, custId);
            stmt.setInt(8, userId);
            stmt.setInt(9, contactId);
            stmt.executeUpdate();
            stmt.close();
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
