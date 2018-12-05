package utils;

import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

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
        return round((double) duration/60, 2);
    }

    public static void initTextFormatter(TextInputControl textInputControl){
        Pattern validEditingState = Pattern.compile("-?(([1-9][0-9]*)|0)?(\\.[0-9]*)?");

        UnaryOperator<TextFormatter.Change> filter = c -> {
            String text = c.getControlNewText();
            if (validEditingState.matcher(text).matches()) {
                return c ;
            } else {
                return null ;
            }
        };

        StringConverter<Double> converter = new StringConverter<Double>() {

            @Override
            public Double fromString(String s) {
                if (s.isEmpty() || "-".equals(s) || ".".equals(s) || "-.".equals(s)) {
                    return 0.0;
                } else {
                    return Double.valueOf(s);
                }
            }


            @Override
            public String toString(Double d) {
                return d.toString();
            }
        };

        TextFormatter<Double> textFormatter = new TextFormatter<>(converter, 0.0, filter);
        textInputControl.setTextFormatter(textFormatter);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
