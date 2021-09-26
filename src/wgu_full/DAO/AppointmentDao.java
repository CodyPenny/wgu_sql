package wgu_full.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import wgu_full.model.Appointment;
import wgu_full.model.Customer;

import java.sql.ResultSet;
import java.time.LocalDate;

import static wgu_full.DAO.JDBC.closeConnection;
import static wgu_full.DAO.JDBC.openConnection;

public class AppointmentDao {

    /**
     * Allows the Appointments, Users, Customers, and Contact table
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
                String start = result.getString("start");
                String end = result.getString("end");
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
     *
     * @param customerId
     * @param date
     * @return
     */
    public static ObservableList<Appointment> getSameDateAppointments(int customerId, LocalDate date){
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try {
            openConnection();
            String query = "SELECT Appointment_ID FROM appointments WHERE Start = '" + date + "' AND Customer_ID = " + customerId;
            Query.makeQuery(query);
            ResultSet result = Query.getResult();
            while(result.next()){
                int id = result.getInt("Appointment_ID");
                System.out.println("id --> " + id);
            }
            closeConnection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return appointments;
    }

    /**
     * Creates an appointment
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
     */
    public static void createAppointment(String title, String desc, String loc, String type, String start, String end, int custId, int userId, int contactId){
        try {
            openConnection();
            String query = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES ('" + title + "','" + desc + "','" + loc + "','" + type + "','" + start + "','" + end + "'," + custId + "," + userId + "," + contactId + ")";
            Query.makeQuery(query);
            closeConnection();
        } catch (Exception e) {
        System.out.println(e.getMessage());
        }
    }

}
