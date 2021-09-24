package wgu_full.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static wgu_full.DAO.JDBC.*;

public class Query {
    private static String query;
    private static Statement stat;
    private static PreparedStatement stmt;
    private static ResultSet result;

    public static void makeQuery(String q) {
        query = q;
        try {
            stat = connection.createStatement();

        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void makeLoginQuery(String username, String pw) throws SQLException {
        query = "Select * from users where user_name = ? and password = ?";
        try {
            stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, pw);
            result = stmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("SQL error -" + e.getMessage());
        }
    }

    public static ResultSet getResult(){
        return result;
    }

}
