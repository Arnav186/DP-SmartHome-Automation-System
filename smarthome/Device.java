package smarthome;

public interface Device {
    int getId();
    String getName();
    String getType();
    String getCommunication();
    String getStatus();
    void turnOn();
    void turnOff();
    void showStatus();
}
