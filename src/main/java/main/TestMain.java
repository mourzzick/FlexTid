package main;

import db.TimeLogDao;
import model.TimeLog;
import utils.Utils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class TestMain {
    public static void main(String[] args) throws IOException {
        long oneLong = 100;
        double time = (double) oneLong/60;
        System.out.println(time);
    }
}
