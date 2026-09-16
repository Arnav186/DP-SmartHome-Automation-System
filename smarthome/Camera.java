package smarthome;

public class Camera extends AbstractDevice {
    public Camera(int id, String name, String communication) {
        super(id, name, communication);
    }

    @Override
    public String getType() { return "CAMERA"; }
}
