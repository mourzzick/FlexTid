package model;

import java.time.LocalDate;

public class TimeLog {
    private int timeLogID;
    private LocalDate regDate;
    private LocalDate workDay;
    private String comment;
    private double workedHours;
    private double dueHours;

    public TimeLog() {
    }

    public TimeLog(int timeLogID, LocalDate regDate, LocalDate workDay, String comment, double workedHours, double dueHours) {
        this.timeLogID = timeLogID;
        this.regDate = regDate;
        this.workDay = workDay;
        this.comment = comment;
        this.workedHours = workedHours;
        this.dueHours = dueHours;
    }

    public TimeLog(LocalDate regDate, LocalDate workDay, String comment, double workedHours, double dueHours) {
        this.regDate = regDate;
        this.workDay = workDay;
        this.comment = comment;
        this.workedHours = workedHours;
        this.dueHours = dueHours;
    }

    public int getTimeLogID() {
        return timeLogID;
    }

    public void setTimeLogID(int timeLogID) {
        this.timeLogID = timeLogID;
    }

    public LocalDate getRegDate() {
        return regDate;
    }

    public void setRegDate(LocalDate regDate) {
        this.regDate = regDate;
    }

    public LocalDate getWorkDay() {
        return workDay;
    }

    public void setWorkDay(LocalDate workDay) {
        this.workDay = workDay;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getWorkedHours() {
        return workedHours;
    }

    public void setWorkedHours(double workedHours) {
        this.workedHours = workedHours;
    }

    public double getDueHours() {
        return dueHours;
    }

    public void setDueHours(double dueHours) {
        this.dueHours = dueHours;
    }

    public double getBalance(){
        return workedHours - dueHours;
    }
}
