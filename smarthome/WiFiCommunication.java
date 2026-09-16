package smarthome;

public class WiFiCommunication implements Communication {
    public void send(String deviceName, String command) {
        System.out.println("[WiFi] " + command + " -> " + deviceName);
    }

    public String getName() { return "WiFi"; }
}
