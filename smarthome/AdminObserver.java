package smarthome;

public class AdminObserver implements DeviceObserver {
    public void update(String message) {
        System.out.println("[Admin Notification] " + message);
    }
}
