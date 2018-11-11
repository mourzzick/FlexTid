package utils;

import java.time.Duration;
import java.time.LocalTime;

public class Utils {
    public static double timeParser(String arrived, String left) {
        if (arrived.length() < 5){
            arrived = new StringBuffer(arrived).insert(2, ":").toString();
        }
        if (left.length() < 5){
            left = new StringBuffer(left).insert(2, ":").toString();
        }
        long duration = Duration.between(LocalTime.parse(arrived), LocalTime.parse(left)).toMinutes();
        return (double) duration/60;
    }

    public static double deductLunch(double workedHours, double lunch){
        return workedHours - (lunch/60);
    }
}
