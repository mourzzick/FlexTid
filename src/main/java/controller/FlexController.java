package controller;

import db.TimeLogDao;
import model.TimeLog;
import observer.TimeLogObservable;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class FlexController extends TimeLogObservable {
    private TimeLogDao timeLogDao;
    private ArrayList<TimeLog> timeLogArrayList;
    private double dueHours;

    public FlexController(double dueHours) {
        this.timeLogArrayList = new ArrayList<>();
        this.dueHours = dueHours;
        timeLogDao = new TimeLogDao();
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
