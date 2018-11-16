package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private final String DB_URL = "jdbc:sqlite:resources/flex.db";

    public Connection openConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

}
