package utils;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalTime;

public class Utils {
    /*
    Returns -0.1 when time is
     */
    public static double timeParser(String arrived, String left) {
        double time = 0.0;
        long duration;
        if (arrived.length() > 5 || arrived.length()< 4 || left.length() > 5 || left.length() < 4 ){
            return -1.0;
        }
        if (arrived.length() < 5){
            arrived = new StringBuffer(arrived).insert(2, ":").toString();
        }
        if (left.length() < 5){
            left = new StringBuffer(left).insert(2, ":").toString();
        }
        try {
            duration = Duration.between(LocalTime.parse(arrived), LocalTime.parse(left)).toMinutes();
        } catch (DateTimeException ex) {
            return -1.0;
        }
        return (double) duration/60;
    }

    public static double deductLunch(double workedHours, double lunch){
        return workedHours - (lunch/60);
    }
}
