package wgu_full;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import wgu_full.DAO.JDBC;

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
        Parent root = FXMLLoader.load(getClass().getResource("view/login.fxml"));
        primaryStage.setTitle("Global Consulting Scheduling");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
       //JDBC.openConnection();
       //JDBC.closeConnection();
    }
}
