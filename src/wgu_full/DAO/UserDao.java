package wgu_full.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import wgu_full.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

import static wgu_full.DAO.JDBC.closeConnection;
import static wgu_full.DAO.JDBC.openConnection;

public class UserDao {

    /**
     * Allows access to the Users table and validates the input username and password
     *
     * @param username the input username
     * @param pw the input password
     * @return true if the user exists
     * @throws SQLException JDBC encountered an error with the data source
     */
    public static boolean validateUser(String username, String pw) throws SQLException {
        openConnection();
        Query.makeLoginQuery(username, pw);
        ResultSet result = Query.getResult();
        if (!result.isBeforeFirst()) {
            return false;
        }
        closeConnection();
        return true;
    }

    /**
     * Allows access to the users table
     *
     * @return ObservableList of all users
     */
    public static ObservableList<User> getAllUsers(){
        ObservableList<User> allUsers = FXCollections.observableArrayList();
        try {
            openConnection();
            String query = "SELECT User_ID, User_Name FROM users";
            Query.makeQuery(query);
            ResultSet result = Query.getResult();

            while(result.next()){
                int id = result.getInt("User_ID");
                String name = result.getString("User_Name");
                User newUser = new User(id, name, "");
                allUsers.add(newUser);
            }

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return allUsers;
    }


}
