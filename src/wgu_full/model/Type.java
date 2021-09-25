package wgu_full.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Class that holds the type of appointments
 */

public class Type {
    private static ObservableList<Type> allTypes = FXCollections.observableArrayList();
    private String appointment_type;

    Type(String appointment_type){
        this.appointment_type = appointment_type;
    }

    /**
     * @return the appointment type
     */
    public String getAppointment_type(){ return this.appointment_type; }

    /**
     * @param type the type to set
     */
    public void setAppointment_type(String type){ this.appointment_type = type; }

    /**
     * @return the ObservableList containing all types
     */
    public static ObservableList<Type> getAllTypes(){ return allTypes; }

    /**
     * @param newType the new Type to add to the list
     */
    public static void addType(Type newType){ allTypes.add(newType); }
}
