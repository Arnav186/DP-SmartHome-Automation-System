package smarthome;

public class SecurityHomeFactory implements HomeFactory {
    public Device createLightingDevice(int id, String name, String communication) {
        return new Light(id, name, communication);
    }

    public Device createClimateDevice(int id, String name, String communication) {
        return new AC(id, name, communication);
    }

    public Device createSecurityDevice(int id, String name, String communication) {
        return new Camera(id, name, communication);
    }
}
