package main;

import controller.FlexController;
import db.TimeLogDao;
import model.TimeLog;
import utils.Utils;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class TestMain {
    public static void main(String[] args) throws IOException {

        long l = Duration.between(LocalTime.parse("20:20"), LocalTime.parse("20:15")).toMinutes();
        System.out.println(l);
    }
}
