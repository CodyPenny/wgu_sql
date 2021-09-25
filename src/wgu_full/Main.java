package wgu_full;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import wgu_full.DAO.JDBC;
import wgu_full.model.Location;

import java.io.IOException;
import java.time.ZoneId;

/*
    Javadoc files are in the doc folder
 */

public class Main extends Application {

    /**
     *
     * @param primaryStage the top level JavaFX container
     * @throws IOException if start operation fails
     */
    @Override
    public void start(Stage primaryStage) throws IOException{
        try {
            Location loc1 = new Location("White Plains, NY");
            Location loc2 = new Location("Phoenix, AZ");
            Location loc3 = new Location("Montreal, Canada");
            Location loc4 = new Location("London, England");
            Location.addLocation(loc1);
            Location.addLocation(loc2);
            Location.addLocation(loc3);
            Location.addLocation(loc4);

            Parent root = FXMLLoader.load(getClass().getResource("view/login.fxml"));
            primaryStage.setTitle("Global Consulting Scheduling");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
