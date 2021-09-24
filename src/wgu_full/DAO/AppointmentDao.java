package wgu_full.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import wgu_full.model.Appointment;
import wgu_full.model.Customer;

import java.sql.ResultSet;

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
}
