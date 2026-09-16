package smarthome;

public class MotionHandler extends SecurityHandler {
    public void handle(SecurityEvent event) {
        if (event.getType().equalsIgnoreCase("MOTION")) {
            System.out.println("Motion detected at " + event.getLocation());
        } else if (next != null) {
            next.handle(event);
        }
    }
}
