package controller;

import db.TimeLogDao;
import model.TimeLog;
import observer.TimeLogObservable;
import utils.DisplayDialogs;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;

public class FlexController extends TimeLogObservable {
    private TimeLogDao timeLogDao;
    private ArrayList<TimeLog> timeLogArrayList;
    private double dueHours = 0;
    private InputStream inputStream;

    public FlexController() {
        Properties properties = new Properties();
        try {
            inputStream = new FileInputStream("resources/application.config");
            properties.load(inputStream);
            this.dueHours = Double.parseDouble(properties.getProperty("dueHours"));
        } catch (IOException ioe) {
            DisplayDialogs dialogs = new DisplayDialogs();
            try {
                dialogs.displaySimpleDialog("Fel vid laddning av värden",
                        "Kunde inte ladda konfigurationsfilen");
            } catch (IOException e) {
                e.printStackTrace();
            }

        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();

            }
        }

        this.timeLogArrayList = new ArrayList<>();
        timeLogDao = new TimeLogDao();
    }

    public double deductLunch(double workedHours, double lunch){
        return workedHours - (lunch/60);
    }

    public boolean addToLog(TimeLog timeLog) throws IOException {
        boolean isInsertOK = timeLogDao.insertTimeLog(timeLog);
        super.notifyTimeLogObservers();
        return isInsertOK;
    }

    public ArrayList<TimeLog> getTimeLogs(LocalDate fromDate, LocalDate toDate) throws IOException {
        timeLogArrayList = timeLogDao.getTimeLogs(fromDate, toDate);
        return timeLogArrayList;
    }
    public ArrayList<TimeLog> getTimeLogsWithLimit(int limit) throws IOException {
        return timeLogDao.getLogsWithLimit(limit);
    }

    public boolean updateTimeLog(TimeLog timeLog) throws IOException {
        boolean isUpdateOK = timeLogDao.updateTimeLog(timeLog);
        super.notifyTimeLogObservers();
        return isUpdateOK;
    }
    public boolean deleteTimeLog(TimeLog timeLog) throws IOException {
        boolean isDeleteOK = timeLogDao.deleteTimeLog(timeLog);
        super.notifyTimeLogObservers();
        return isDeleteOK;
    }
    public double getTimeBalance() throws IOException {
        return timeLogDao.getTimeBalance();
    }

    public double getDueHours() {
        return dueHours;
    }
}
