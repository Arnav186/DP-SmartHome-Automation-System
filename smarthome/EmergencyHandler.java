package smarthome;

public class EmergencyHandler extends SecurityHandler {
    public void handle(SecurityEvent event) {
        if (event.getType().equalsIgnoreCase("EMERGENCY")) {
            System.out.println("EMERGENCY response triggered at " + event.getLocation());
        } else if (next != null) {
            System.out.println("Unknown event. Passing through security chain.");
        }
    }
}
