package smarthome;

public class SecurityEvent {
    private final String type;
    private final String location;

    public SecurityEvent(String type, String location) {
        this.type = type;
        this.location = location;
    }

    public String getType() { return type; }
    public String getLocation() { return location; }
}
