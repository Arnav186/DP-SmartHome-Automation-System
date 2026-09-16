package smarthome;

public abstract class AbstractDevice implements Device {
    protected final int id;
    protected final String name;
    protected final String communication;
    protected String status = "OFF";

    protected AbstractDevice(int id, String name, String communication) {
        this.id = id;
        this.name = name;
        this.communication = communication;
    }

    @Override
    public int getId() { return id; }

    @Override
    public String getName() { return name; }

    @Override
    public String getCommunication() { return communication; }

    @Override
    public String getStatus() { return status; }

    @Override
    public void turnOn() {
        status = "ON";
        System.out.println(name + " turned ON using " + communication + ".");
    }

    @Override
    public void turnOff() {
        status = "OFF";
        System.out.println(name + " turned OFF using " + communication + ".");
    }

    @Override
    public void showStatus() {
        System.out.println(name + " [" + getType() + "] = " + status +
                " | Communication: " + communication);
    }
}
