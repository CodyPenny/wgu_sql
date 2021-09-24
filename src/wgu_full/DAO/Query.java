package wgu_full.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import static wgu_full.DAO.JDBC.connection;

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

}
