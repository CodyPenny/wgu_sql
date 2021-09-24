package wgu_full.DAO;

import wgu_full.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

import static wgu_full.DAO.JDBC.closeConnection;
import static wgu_full.DAO.JDBC.openConnection;

public class UserDao {

    public static boolean validateUser(String username, String pw) throws SQLException {
        openConnection();
        Query.makeLoginQuery(username, pw);
        User userResult;
        ResultSet result = Query.getResult();
        if (!result.isBeforeFirst()) {
            return false;
        }
        closeConnection();
        return true;
    }


}
