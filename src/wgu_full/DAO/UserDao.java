package wgu_full.DAO;

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


}
