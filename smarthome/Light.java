package smarthome;

public class Light extends AbstractDevice {
    public Light(int id, String name, String communication) {
        super(id, name, communication);
    }

    @Override
    public String getType() { return "LIGHT"; }
}
