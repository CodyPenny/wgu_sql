package wgu_full;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import wgu_full.controller.LoginController;
import wgu_full.model.Location;
import wgu_full.model.Type;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/*
    Javadoc files are in the doc folder
 */

/**
 * Starts the execution of the program
 */
public class Main extends Application {

    /**
     * Startup Location data
     */
    public void populateLocation(){
        Location loc1 = new Location("White Plains, NY");
        Location loc2 = new Location("Phoenix, AZ");
        Location loc3 = new Location("Montreal, Canada");
        Location loc4 = new Location("London, England");
        Location.addLocation(loc1);
        Location.addLocation(loc2);
        Location.addLocation(loc3);
        Location.addLocation(loc4);
    }

    /**
     * Startup Type data
     */
    public void populateType(){
        Type type1 = new Type("Planning session");
        Type type2 = new Type("De-briefing");
        Type type3 = new Type("Consultation");
        Type type4 = new Type("Happy Hour");
        Type.addType(type1);
        Type.addType(type2);
        Type.addType(type3);
        Type.addType(type4);
    }

    /**
     * Loads the view and the resource bundle based on the system language
     * Translates the form to English or French
     *
     * @param primaryStage the top level JavaFX container
     * @param local an instance of the Locale object
     */
    public void loadView(Stage primaryStage, Locale local){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/login.fxml"));
            ResourceBundle rb = ResourceBundle.getBundle("bundle", local);
            loader.setResources(rb);
            Parent root = loader.load();
            LoginController controller = loader.getController();
            controller.translate(rb);
            primaryStage.setTitle("Global Consulting Scheduling");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Populates the location and the type local ObservableList
     * Translates the form to French if user's system is set to French
     * Loads the login form
     *
     * @param primaryStage the top level JavaFX container
     * @throws IOException if start operation fails
     */
    @Override
    public void start(Stage primaryStage) throws IOException{
        populateLocation();
        populateType();
        // test French
        //Locale.setDefault(new Locale("fr"));
        Locale systemLanguage = Locale.getDefault();
        System.out.println("print lang->" + systemLanguage); // en_US
        loadView(primaryStage, systemLanguage);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
