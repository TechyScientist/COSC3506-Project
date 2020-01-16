package cosc3506.project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Johnny Console
 * Class: Database
 * Purpose: Hold Database credentials
 * and connection function
 * Written: 16 Jan 2020
 */
public class Database {
    public static final String HOST = "";
    public static final String DB = "project";
    public static final String USER = "dev";
    public static final String PASS = "project";

    public static Connection connect(String host, String user, String pass, String db) {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://" + host + ":3306/" + db;
            conn = DriverManager.getConnection(url, user, pass);
        }
        catch (ClassNotFoundException ex) {
            System.out.println("Could Not Load MySQL Driver");
        }
        catch (SQLException ex) {
            System.out.println("Could Not Connect to MySQL Server");
        }

        return conn;
    }


}
