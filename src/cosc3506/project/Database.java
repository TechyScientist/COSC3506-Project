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
    public static final String HOST = "10.14.3.46";
    //The above host is only available on AlgomaU networked computers.
    public static final String DB = "project";
    public static final String USER = "dev";
    public static final String PASS = "ProjectDev#2020";

    private static boolean connection = false;

    /**
     * Connects to a database
     * @param host The IP Address or Hostname of the Database Server
     * @param user The username to connect as
     * @param pass The password for the connectiong user
     * @param db The name of the database to use
     * @return A connection to the database
     */
    public static Connection connect(String host, String user, String pass, String db) {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + host + ":3306/" + db;
            conn = DriverManager.getConnection(url, user, pass);
        }
        catch (ClassNotFoundException ex) {

            System.out.println("Could Not Load MySQL Driver");
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Could Not Connect to MySQL Server");
        }

        return conn;
    }


    /**
     * Mutex lock for safe thread syncronization
     * waitFor waits for no active connection,
     * then allows the next thread to have a connection
     */
    @SuppressWarnings("ALL")
    public static void waitFor() {
        while(connection);
        connection = true;
    }

    /**
     * Mutex lock for safe thread syncronization
     * signal deactivates a connection
     */
    public static void signal() {
        connection = false;
    }

}
