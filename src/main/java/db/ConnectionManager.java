package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    private Properties properties;
    private final String DB_URL = "jdbc:sqlite:resources/flex.db";
    private String DB_URL2;
    public ConnectionManager(){
        this.properties = new Properties();
    }

    public Connection openConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

}
