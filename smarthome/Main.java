package smarthome;

import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final SmartHomeController controller = SmartHomeController.getInstance();

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("     SMART HOME AUTOMATION & SECURITY");
        System.out.println("==============================================");

        boolean running = true;

        while (running) {
            System.out.println("\n1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");

            int choice = readInt("Enter choice: ");

            switch (choice) {
                case 1 -> register();
                case 2 -> {
                    Integer userId = login();
                    if (userId != null) userMenu(userId);
                }
                case 3 -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }

        DatabaseConnection.getInstance().close();
        sc.close();
        System.out.println("Application closed.");
    }

    private static void register() {
        System.out.println("\n--- REGISTER ---");
        String username = readNonEmpty("Enter username: ");
        String password = readPassword("Create password: ");
        String confirm = readPassword("Confirm password: ");

        if (!password.equals(confirm)) {
            System.out.println("Passwords do not match.");
            return;
        }

        controller.registerUser(username, password);
    }

    private static Integer login() {
        System.out.println("\n--- LOGIN ---");
        String username = readNonEmpty("Enter username: ");
        String password = readPassword("Enter password: ");
        return controller.login(username, password);
    }

    private static void userMenu(int userId) {
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("\n========== USER MENU ==========");
            System.out.println("1. Add Room");
            System.out.println("2. View Rooms");
            System.out.println("3. Add Device");
            System.out.println("4. View Devices");
            System.out.println("5. Control Device");
            System.out.println("6. Trigger Security Event");
            System.out.println("7. View Event History");
            System.out.println("8. Demonstrate Abstract Factory");
            System.out.println("9. Demonstrate Bridge");
            System.out.println("10. Logout");

            int choice = readInt("Enter choice: ");

            switch (choice) {
                case 1 -> addRoom(userId);
                case 2 -> controller.listRooms(userId);
                case 3 -> addDevice(userId);
                case 4 -> controller.listDevices(userId);
                case 5 -> controlDevice(userId);
                case 6 -> securityEvent(userId);
                case 7 -> controller.listEvents(userId);
                case 8 -> abstractFactoryDemo();
                case 9 -> bridgeDemo();
                case 10 -> {
                    loggedIn = false;
                    System.out.println("Logged out.");
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void addRoom(int userId) {
        System.out.println("\n--- ADD ROOM ---");
        String name = readNonEmpty("Enter room name: ");
        controller.addRoom(userId, name);
    }

    private static void addDevice(int userId) {
        System.out.println("\n--- ADD DEVICE ---");
        controller.listRooms(userId);

        int roomId = readInt("Enter room ID: ");
        String type = readNonEmpty("Device type (LIGHT/FAN/AC/CAMERA/LOCK): ").toUpperCase();
        String name = readNonEmpty("Enter device name: ");
        String communication = readNonEmpty("Communication (WiFi/Bluetooth): ");

        if (!communication.equalsIgnoreCase("WiFi") &&
                !communication.equalsIgnoreCase("Bluetooth")) {
            System.out.println("Communication must be WiFi or Bluetooth.");
            return;
        }

        controller.addDevice(userId, roomId, type, name, communication);
    }

    private static void controlDevice(int userId) {
        System.out.println("\n--- CONTROL DEVICE ---");
        controller.listDevices(userId);

        int deviceId = readInt("Enter device ID: ");
        String action = readNonEmpty("Action (ON/OFF/STATUS): ");
        controller.controlDevice(userId, deviceId, action);
    }

    private static void securityEvent(int userId) {
        System.out.println("\n--- SECURITY EVENT ---");
        System.out.println("Available: MOTION, DOOR, ALARM, EMERGENCY");
        String type = readNonEmpty("Enter event type: ").toUpperCase();
        String location = readNonEmpty("Enter location: ");
        controller.logSecurityEvent(userId, type, location);
    }

    private static void abstractFactoryDemo() {
        System.out.println("\n--- ABSTRACT FACTORY DEMO ---");
        System.out.println("1. Standard Home");
        System.out.println("2. Security Home");
        int choice = readInt("Choose family: ");

        HomeFactory factory = (choice == 2)
                ? new SecurityHomeFactory()
                : new StandardHomeFactory();

        String room = readNonEmpty("Enter room name for demo: ");
        String communication = readNonEmpty("Communication (WiFi/Bluetooth): ");

        Device light = factory.createLightingDevice(0, room + " Light", communication);
        Device climate = factory.createClimateDevice(0, room + " Climate", communication);
        Device security = factory.createSecurityDevice(0, room + " Security", communication);

        System.out.println("Created device family:");
        light.showStatus();
        climate.showStatus();
        security.showStatus();
    }

    private static void bridgeDemo() {
        System.out.println("\n--- BRIDGE DEMO ---");
        String name = readNonEmpty("Enter smart device name: ");
        String method = readNonEmpty("Communication (WiFi/Bluetooth): ");

        Communication communication = method.equalsIgnoreCase("Bluetooth")
                ? new BluetoothCommunication()
                : new WiFiCommunication();

        SmartDevice device = new SmartDevice(name, communication);

        System.out.println("1. ON");
        System.out.println("2. OFF");
        System.out.println("3. STATUS");
        int action = readInt("Choose action: ");

        switch (action) {
            case 1 -> device.powerOn();
            case 2 -> device.powerOff();
            case 3 -> device.status();
            default -> System.out.println("Invalid action.");
        }
    }

    private static String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = sc.nextLine().trim();
            if (!value.isEmpty()) return value;
            System.out.println("Input cannot be empty.");
        }
    }

    private static String readPassword(String prompt) {
        // Scanner input is used so this works in VS Code, IntelliJ and Eclipse.
        // Password is hashed before it is stored in the database.
        return readNonEmpty(prompt);
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = sc.nextLine().trim();
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
