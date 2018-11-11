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
        return timeLogDao.getTimeLogs(fromDate, toDate);
    }
    public ArrayList<TimeLog> getTimeLogsWithLimit(int limit) throws IOException {
        return timeLogDao.getLogsWithLimit(limit);
    }
    public double getTimeBalance(){
        double balance = 0;
        for (TimeLog log : timeLogArrayList){
            balance += log.getWorkedHours() - log.getDueHours();
        }
        return balance;
    }

    public double getDueHours() {
        return dueHours;
    }
}
