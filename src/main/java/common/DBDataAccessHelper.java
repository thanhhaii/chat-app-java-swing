package common;

import java.sql.*;

public class DBDataAccessHelper {

    private static Connection connection = null;
    private static String url  = "jdbc:sqlserver://";
    private static String serverName = "THANHHAI";
    private static String portNumber = "1433";
    private static String databaseName = "eProject2";
    private static String usernameDb = "sa";
    private static String passwordDb = "thanhhai";

    public DBDataAccessHelper() {
    }

    public static String stringCon(){
        var StringCon = url + serverName + ":" + portNumber + "; databaseName=" + databaseName + "; user=" + usernameDb + "; password=" + passwordDb;
        return StringCon;
    }

    public static Connection connection(){
        try
        {
            connection = DriverManager.getConnection(stringCon());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

}