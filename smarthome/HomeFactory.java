package smarthome;

// Abstract Factory pattern
public interface HomeFactory {
    Device createLightingDevice(int id, String name, String communication);
    Device createClimateDevice(int id, String name, String communication);
    Device createSecurityDevice(int id, String name, String communication);
}
