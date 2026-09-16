package smarthome;

import java.util.ArrayList;
import java.util.List;

// Observer subject
public class DeviceSubject {
    private final List<DeviceObserver> observers = new ArrayList<>();

    public void addObserver(DeviceObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers(String message) {
        for (DeviceObserver observer : observers) {
            observer.update(message);
        }
    }
}
