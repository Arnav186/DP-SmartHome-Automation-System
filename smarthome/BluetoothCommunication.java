package smarthome;

public class BluetoothCommunication implements Communication {
    public void send(String deviceName, String command) {
        System.out.println("[Bluetooth] " + command + " -> " + deviceName);
    }

    public String getName() { return "Bluetooth"; }
}
