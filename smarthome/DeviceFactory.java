package smarthome;

// Factory Method pattern
public abstract class DeviceFactory {
    public abstract Device createDevice(int id, String name, String communication);

    public static DeviceFactory forType(String type) {
        return switch (type.toUpperCase()) {
            case "LIGHT" -> new LightFactory();
            case "FAN" -> new FanFactory();
            case "AC" -> new ACFactory();
            case "CAMERA" -> new CameraFactory();
            case "LOCK" -> new LockFactory();
            default -> throw new IllegalArgumentException("Unknown device type: " + type);
        };
    }

    private static class LightFactory extends DeviceFactory {
        public Device createDevice(int id, String name, String communication) {
            return new Light(id, name, communication);
        }
    }

    private static class FanFactory extends DeviceFactory {
        public Device createDevice(int id, String name, String communication) {
            return new Fan(id, name, communication);
        }
    }

    private static class ACFactory extends DeviceFactory {
        public Device createDevice(int id, String name, String communication) {
            return new AC(id, name, communication);
        }
    }

    private static class CameraFactory extends DeviceFactory {
        public Device createDevice(int id, String name, String communication) {
            return new Camera(id, name, communication);
        }
    }

    private static class LockFactory extends DeviceFactory {
        public Device createDevice(int id, String name, String communication) {
            return new Lock(id, name, communication);
        }
    }
}
