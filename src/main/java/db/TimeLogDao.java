package db;

import fx.SimpleStringDialogController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.TimeLog;
import observer.TimeLogObservable;
import observer.TimeLogObserver;
import utils.DisplayDialog;

import javax.swing.*;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class TimeLogDao {
    private ConnectionManager cm;
    private final String SQL_INSERT = "INSERT INTO TimeLog (RegDate, WorkDay, Comment, WorkedHours, DueHours) " +
            "values (?, ?, ?, ?, ?)";
    private final String SQL_GET_LOGS = "SELECT * FROM TimeLog WHERE WorkDay BETWEEN ? AND ?";
    private final String SQL_GET_ALL_LOGS = "SELECT * FROM TimeLog";
    private final String SQL_GET_LOGS_WITH_LIMIT = "SELECT * FROM TimeLog ORDER BY WorkDay DESC LIMIT ?";
    private final String SQL_GET_BALANCE = "SELECT SUM(WorkedHours - DueHours) FROM TimeLog";

    public TimeLogDao() {
        this.cm = new ConnectionManager();
    }

    public boolean insertTimeLog(TimeLog timeLog) throws IOException {
        try (Connection connection = cm.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT)) {
            preparedStatement.setString(1, timeLog.getRegDate().toString());
            preparedStatement.setString(2, timeLog.getWorkDay().toString());
            preparedStatement.setString(3, timeLog.getComment());
            preparedStatement.setDouble(4, timeLog.getWorkedHours());
            preparedStatement.setDouble(5, timeLog.getDueHours());
            preparedStatement.execute();

            return true;
        } catch (SQLException sqle){
            String stacktrace = String.format("%d%n%s%n%s%n", sqle.getErrorCode(),
                                                sqle.getSQLState(), sqle.getMessage());
            DisplayDialog dialog = new DisplayDialog();
            dialog.displaySimpleDialog("Fel vid sparning i databas", stacktrace);
        }
        return false;
    }

    public ArrayList<TimeLog> getTimeLogs(LocalDate fromDate, LocalDate toDate) throws IOException {
        ArrayList<TimeLog> list = new ArrayList<>();
        ResultSet resultSet = null;
        try (Connection connection = cm.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_LOGS)) {
            preparedStatement.setString(1, fromDate.toString());
            preparedStatement.setString(2, toDate.toString());
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                list.add(new TimeLog(resultSet.getInt("TimeLogID"),
                        LocalDate.parse(resultSet.getString("RegDate")),
                        LocalDate.parse(resultSet.getString("WorkDay")),
                        resultSet.getString("Comment"),
                        resultSet.getDouble("WorkedHours"), resultSet.getDouble("DueHours")));
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            String stacktrace = String.format("%d%n%s%n%s%n", sqle.getErrorCode(),
                    sqle.getSQLState(), sqle.getMessage());
            DisplayDialog dialog = new DisplayDialog();
            dialog.displaySimpleDialog("Fel vid hämtning från databas", stacktrace);
            }
        return list;
    }
    public ArrayList<TimeLog> getAllTimeLogs() throws IOException {
        ArrayList<TimeLog> list = new ArrayList<>();
        ResultSet resultSet = null;
        try (Connection connection = cm.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_ALL_LOGS)) {
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                list.add(new TimeLog(resultSet.getInt("TimeLogID"),
                        LocalDate.parse(resultSet.getString("RegDate")),
                        LocalDate.parse(resultSet.getString("WorkDay")),
                        resultSet.getString("Comment"),
                        resultSet.getDouble("WorkedHours"), resultSet.getDouble("DueHours")));
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            String stacktrace = String.format("%d%n%s%n%s%n", sqle.getErrorCode(),
                    sqle.getSQLState(), sqle.getMessage());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx/SimpleStringDialog.fxml"));
            Parent root = loader.load();
            DisplayDialog dialog = new DisplayDialog();
            dialog.displaySimpleDialog("Fel vid hämtning från databas", stacktrace);       }
        return list;
    }
    public ArrayList<TimeLog> getLogsWithLimit(int limit) throws IOException {
        ArrayList<TimeLog> list = new ArrayList<>();
        ResultSet resultSet = null;
        try (Connection connection = cm.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_LOGS_WITH_LIMIT)) {
            preparedStatement.setInt(1, limit);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                list.add(new TimeLog(resultSet.getInt("TimeLogID"),
                        LocalDate.parse(resultSet.getString("RegDate")),
                        LocalDate.parse(resultSet.getString("WorkDay")),
                        resultSet.getString("Comment"),
                        resultSet.getDouble("WorkedHours"), resultSet.getDouble("DueHours")));
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            String stacktrace = String.format("%d%n%s%n%s%n", sqle.getErrorCode(),
                    sqle.getSQLState(), sqle.getMessage());
            DisplayDialog dialog = new DisplayDialog();
            dialog.displaySimpleDialog("Fel vid hämtning från databas", stacktrace);
        }
        return list;
    }
    public double getTimeBalance() throws IOException {
        ResultSet resultSet = null;
        try (Connection connection = cm.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_BALANCE)){
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return resultSet.getDouble(1);
            }
        } catch (SQLException sqle) {
            String stacktrace = String.format("%d%n%s%n%s%n", sqle.getErrorCode(),
                    sqle.getSQLState(), sqle.getMessage());
            DisplayDialog dialog = new DisplayDialog();
            dialog.displaySimpleDialog("Fel vid hämtning från databas", stacktrace);

        }
        return 0.0;
    }
}
