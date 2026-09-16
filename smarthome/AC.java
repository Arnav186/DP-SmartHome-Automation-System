package smarthome;

public class AC extends AbstractDevice {
    public AC(int id, String name, String communication) {
        super(id, name, communication);
    }

    @Override
    public String getType() { return "AC"; }
}
