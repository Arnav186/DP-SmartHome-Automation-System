package smarthome;

public class Fan extends AbstractDevice {
    public Fan(int id, String name, String communication) {
        super(id, name, communication);
    }

    @Override
    public String getType() { return "FAN"; }
}
