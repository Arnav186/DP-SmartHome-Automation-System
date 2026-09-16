package smarthome;

public class DoorHandler extends SecurityHandler {
    public void handle(SecurityEvent event) {
        if (event.getType().equalsIgnoreCase("DOOR")) {
            System.out.println("Door security event at " + event.getLocation());
        } else if (next != null) {
            next.handle(event);
        }
    }
}
