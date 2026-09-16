package smarthome;

// Proxy pattern
public class SecureDeviceProxy implements Device {
    private final Device realDevice;
    private final boolean authenticated;

    public SecureDeviceProxy(Device realDevice, boolean authenticated) {
        this.realDevice = realDevice;
        this.authenticated = authenticated;
    }

    private void checkAccess() {
        if (!authenticated) {
            throw new SecurityException("Access denied. Please login first.");
        }
    }

    public int getId() { return realDevice.getId(); }
    public String getName() { return realDevice.getName(); }
    public String getType() { return realDevice.getType(); }
    public String getCommunication() { return realDevice.getCommunication(); }
    public String getStatus() { return realDevice.getStatus(); }

    public void turnOn() {
        checkAccess();
        realDevice.turnOn();
    }

    public void turnOff() {
        checkAccess();
        realDevice.turnOff();
    }

    public void showStatus() {
        checkAccess();
        realDevice.showStatus();
    }
}
