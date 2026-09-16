package smarthome;

public class StandardHomeFactory implements HomeFactory {
    public Device createLightingDevice(int id, String name, String communication) {
        return new Light(id, name, communication);
    }

    public Device createClimateDevice(int id, String name, String communication) {
        return new Fan(id, name, communication);
    }

    public Device createSecurityDevice(int id, String name, String communication) {
        return new Lock(id, name, communication);
    }
}
