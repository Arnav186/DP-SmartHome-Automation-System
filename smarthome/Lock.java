package smarthome;

public class Lock extends AbstractDevice {
    public Lock(int id, String name, String communication) {
        super(id, name, communication);
    }

    @Override
    public String getType() { return "LOCK"; }
}
