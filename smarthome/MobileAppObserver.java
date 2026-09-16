package smarthome;

public class MobileAppObserver implements DeviceObserver {
    public void update(String message) {
        System.out.println("[Mobile App Notification] " + message);
    }
}
