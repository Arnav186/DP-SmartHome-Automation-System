package smarthome;

// Bridge abstraction
public class SmartDevice {
    private final String name;
    private final Communication communication;
    private boolean on;

    public SmartDevice(String name, Communication communication) {
        this.name = name;
        this.communication = communication;
    }

    public void powerOn() {
        communication.send(name, "POWER ON");
        on = true;
    }

    public void powerOff() {
        communication.send(name, "POWER OFF");
        on = false;
    }

    public void status() {
        communication.send(name, "STATUS = " + (on ? "ON" : "OFF"));
    }
}
