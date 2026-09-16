package smarthome;

public class AlarmHandler extends SecurityHandler {
    public void handle(SecurityEvent event) {
        if (event.getType().equalsIgnoreCase("ALARM")) {
            System.out.println("ALARM activated at " + event.getLocation());
        } else if (next != null) {
            next.handle(event);
        }
    }
}
