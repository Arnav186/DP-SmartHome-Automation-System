public class Fan implements Device {

    private final String name;
    private final Communication communication;
    private boolean on;

    public Fan(String name, Communication communication) {
        this.name = name;
        this.communication = communication;
    }

    public String getName() {
        return name;
    }

    public void on() {
        on = true;
        communication.send(name, "ON");
    }

    public void off() {
        on = false;
        communication.send(name, "OFF");
    }

    public void status() {
        System.out.println(name + " is " + (on ? "ON" : "OFF"));
    }
}
