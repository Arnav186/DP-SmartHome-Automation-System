package smarthome;

// Bridge implementation
public interface Communication {
    void send(String deviceName, String command);
    String getName();
}
