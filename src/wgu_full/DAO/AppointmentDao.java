package wgu_full.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import wgu_full.model.Appointment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;

import static wgu_full.DAO.JDBC.closeConnection;
import static wgu_full.DAO.JDBC.openConnection;
import static wgu_full.DAO.Query.createAppt;
import static wgu_full.DAO.Query.removeAppt;

/**
 * The Appointment data access object interface
 */
public class AppointmentDao {

    /**
     * Creates the query to retrieve all appointments
     *
     * @return the ObservableList containing all appointments
     */
    public static ObservableList<Appointment> getAllAppointments(){
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        try {
            openConnection();
            String query = "SELECT a.*, c.Customer_ID, u.User_ID, o.Contact_Name FROM appointments AS a LEFT JOIN customers as c ON (c.Customer_ID = a.Customer_ID) JOIN contacts AS o ON (a.Contact_ID = o.Contact_ID) JOIN users AS u ON (a.User_ID = u.User_ID)";
            Query.makeQuery(query);
            ResultSet result = Query.getResult();

            while(result.next()){
                int id = result.getInt("Appointment_ID");
                String title = result.getString("title");
                String description = result.getString("description");
                String location = result.getString("location");
                String type = result.getString("type");
                Timestamp start = result.getTimestamp("start");
                Timestamp end = result.getTimestamp("end");
                int customer = result.getInt("Customer_ID");
                int user = result.getInt("User_ID");
                String contact = result.getString("Contact_Name");
                Appointment appointmentResult = new Appointment(id, title, description, location, type, start, end, customer, user, contact);
                allAppointments.add(appointmentResult);

            }
            closeConnection();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return allAppointments;
    }

    /**
     * Retrieves appointments by customer id and the date
     *
     * @param customerId the customer id
     * @param date the date
     * @return ObservableList of appointments
     */
    public static ObservableList<Appointment> getSameDateAppointments(int customerId, LocalDate date){
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try {
            openConnection();
            String query = "SELECT * FROM appointments WHERE Start >= '" + date + "' AND Start < '" + date + "' + INTERVAL 1 DAY AND Customer_ID = " + customerId;
            Query.makeQuery(query);
            ResultSet result = Query.getResult();
            while(result.next()){
                int id = result.getInt("Appointment_ID");
                Timestamp start = result.getTimestamp("Start");
                Timestamp end = result.getTimestamp("End");
                int customer = result.getInt("Customer_ID");
                Appointment same = new Appointment(id, "","","","",start, end, customer, 0,"");
                appointments.add(same);
            }
            closeConnection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return appointments;
    }

    /**
     * Retrieves appointments by the user id and date
     *
     * @param userId the user id
     * @param date the date
     * @return ObservableList of appointments
     */
    public static ObservableList<Appointment> getSameDateAppointmentsByUser(int userId, LocalDate date){
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try {
            openConnection();
            String query = "SELECT * FROM appointments WHERE Start >= '" + date + "' AND Start < '" + date + "' + INTERVAL 1 DAY AND User_ID = " + userId;
            Query.makeQuery(query);
            ResultSet result = Query.getResult();
            while(result.next()){
                int id = result.getInt("Appointment_ID");
                Timestamp start = result.getTimestamp("Start");
                Timestamp end = result.getTimestamp("End");
                int user = result.getInt("User_ID");
                Appointment same = new Appointment(id, "","","","",start, end, 0, user,"");
                appointments.add(same);
            }
            closeConnection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return appointments;
    }

    /**
     * Retrieves appointments by the contact id
     *
     * @param contactId the contact id
     * @return ObservableList of appointments
     */
    public static ObservableList<Appointment> getAppointmentsByContact(int contactId){
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try {
            openConnection();
            String query = "SELECT * FROM appointments WHERE Contact_ID = " + contactId;
            Query.makeQuery(query);
            ResultSet result = Query.getResult();

            while(result.next()){
                int id = result.getInt("Appointment_ID");
                String title = result.getString("title");
                String description = result.getString("description");
                String location = result.getString("location");
                String type = result.getString("type");
                Timestamp start = result.getTimestamp("start");
                Timestamp end = result.getTimestamp("end");
                int customer = result.getInt("Customer_ID");
                int user = result.getInt("User_ID");
                Appointment appt = new Appointment(id, title,description,location,type,start, end, customer, user,"");
                appointments.add(appt);
            }
            closeConnection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return appointments;
    }

    /**
     * Retreives appointments by location
     *
     * @param place the location
     * @return ObservableList of appointments
     */
    public static ObservableList<Appointment> getAppointmentsByLocation(String place){
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try {
            openConnection();
            String query = "SELECT * FROM appointments WHERE Location = '" + place + "'";
            Query.makeQuery(query);
            ResultSet result = Query.getResult();

            while(result.next()){
                int id = result.getInt("Appointment_ID");
                String title = result.getString("title");
                String description = result.getString("description");
                String contact = result.getString("Contact_ID");
                String type = result.getString("type");
                Timestamp start = result.getTimestamp("start");
                Timestamp end = result.getTimestamp("end");
                int customer = result.getInt("Customer_ID");
                int user = result.getInt("User_ID");
                Appointment appt = new Appointment(id, title,description,"",type,start, end, customer, user,contact);
                appointments.add(appt);
            }
            closeConnection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return appointments;
    }



    /**
     * Creates an appointment instance in the database
     *
     * @param title the title
     * @param desc the description
     * @param loc the location
     * @param type the type of appointment
     * @param start the start date and time
     * @param end the end date and time
     * @param custId the customer id
     * @param userId the user id
     * @param contactId the contact id
     * @throws SQLException JDBC encountered an error with the data source
     */
    public static void createAppointment(String title, String desc, String loc, String type, Timestamp start, Timestamp end, int custId, int userId, int contactId) throws SQLException {
        openConnection();
        createAppt(title, desc, loc, type, start, end, custId, userId, contactId);
        closeConnection();
        return;
    }

    /**
     * Updates the appointment instance
     *
     * @param id the appointment id
     * @param title the title
     * @param description the description
     * @param location the location
     * @param start the start date and time
     * @param end the end date and time
     * @param type the type of appointment
     * @param user the user
     * @param contact the contact
     * @param customer the customer
     */
    public static void updateAppointment(int id, String title, String description, String location, Timestamp start, Timestamp end, String type, int user, int contact, int customer ){
        try {
            openConnection();
            String query = "UPDATE appointments SET Title = '" + title + "', Description = '" + description + "', Location = '" + location + "', Type = '" + type + "', Start = '" + start + "', End = '" + end + "', Customer_ID = " + customer + ", User_ID = " + user + ", Contact_ID = " + contact + " WHERE Appointment_ID = " + id;
            Query.makeQuery(query);
            closeConnection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Deletes the selected appointment
     *
     * @param id the appointment id
     * @return false if deletion fails
     * @throws SQLException JDBC encountered an error with the data source
     */
    public static boolean deleteAppointment(int id) throws SQLException {
        openConnection();
        if(!removeAppt(id)){
            return false;
        }
        closeConnection();
        return true;
    }

    /**
     * Counts the number of appointment instances that match the month and 'type'
     *
     * @param mth the month
     * @param type the type
     * @return the count
     */
    public static int getTypeByMonth(int mth, String type){
        int count = 0;
        try{
            openConnection();
            String query = "SELECT * FROM appointments WHERE MONTH(START) = " + mth + " AND Type = '" + type +"'";
            Query.makeQuery(query);
            ResultSet result = Query.getResult();
            while(result.next()){
                int id = result.getInt("Appointment_ID");
                Timestamp start = result.getTimestamp("Start");
                count++;
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        closeConnection();
        return count;
    }

}
