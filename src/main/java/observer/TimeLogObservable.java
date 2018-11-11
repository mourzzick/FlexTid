package observer;

import java.io.IOException;
import java.util.ArrayList;

public class TimeLogObservable {
    private static ArrayList<TimeLogObserver> observers = new ArrayList<>();

    public void addListener(TimeLogObserver o){
        observers.add(o);
    }

    public void removeListener(TimeLogObserver o) {
        observers.remove(o);
    }

    public void notifyTimeLogObservers() throws IOException {
        for (TimeLogObserver observer : observers) {
            observer.update();
        }
    }
}
