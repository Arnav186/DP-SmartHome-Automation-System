
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static final Scanner sc = new Scanner(System.in);
    static final SmartHomeController home = SmartHomeController.getInstance();
    static ArrayList<Device> devices = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=================================");
        System.out.println("       SMART HOME SYSTEM");
        System.out.println("=================================");
        boolean run = true;
        while (run) {
            System.out.println("\n1. Register\n2. Login\n3. Exit");
            int c = number("Enter choice: ");
            if (c == 1) {
                register();
            } else if (c == 2) {
                login();
            } else if (c == 3) {
                run = false;
            } else {
                System.out.println("Invalid choice.");
            }
        }
        home.close();
    }

    static void register() {
        System.out.println("\n--- REGISTER ---");
        String u = input("Username: "), p = input("Password: "), cp = input("Confirm password: ");
        if (!p.equals(cp)) {
            System.out.println("Passwords do not match.");
            return;
        }
        home.register(u, p);
    }

    static void login() {
        System.out.println("\n--- LOGIN ---");
        String u = input("Username: "), p = input("Password: ");
        System.out.println(" ");
        if (home.login(u, p)) {
            menu();
        }
    }

    static void menu() {
        boolean run = true;
        while (run) {
            System.out.println("\n========== HOME MENU ==========");
            System.out.println("1. Create Device");
            System.out.println("2. Turn ON");
            System.out.println("3. Turn OFF");
            System.out.println("4. Status");
            System.out.println("5. Security Event");
            System.out.println("6. Abstract Factory");
            System.out.println("7. Logout");
            int c = number("Enter choice: ");
            switch (c) {
                case 1 ->
                    createDevice();
                case 2 ->
                    control(1);
                case 3 ->
                    control(2);
                case 4 ->
                    control(3);
                case 5 ->
                    security();
                case 6 ->
                    abstractFactory();
                case 7 ->
                    run = false;
                default ->
                    System.out.println("Invalid choice.");
            }
        }
    }

    static void createDevice() {

        System.out.println("\n--- CREATE DEVICE ---");

        String type = input("Device type (Light/Fan): ");
        String name = input("Device name: ");
        String comm = input("Communication (WiFi/Bluetooth): ");

        try {

            Device device = home.createDevice(type, name, comm);

            devices.add(device);

            System.out.println("Device created successfully.");
            System.out.println("Device ID: " + devices.size());

        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());

        }
    }

    static Device selectDevice() {

        if (devices.isEmpty()) {

            System.out.println("No devices available.");
            return null;
        }

        System.out.println("\n--- YOUR DEVICES ---");

        for (int i = 0; i < devices.size(); i++) {

            System.out.println((i + 1) + ". " + devices.get(i).getName());
        }

        int choice = number("Select device: ");

        if (choice < 1 || choice > devices.size()) {

            System.out.println("Invalid device.");
            return null;
        }

        return devices.get(choice - 1);
    }

    static void control(int action) {

        Device device = selectDevice();
        if (device == null) {
            return;
        }

        // Proxy controls access to the device
        Device proxy = new SecureDeviceProxy(device, true);

        if (action == 1) {
            proxy.on();
            home.notifyChange(device.getName() + " turned ON.");

        } else if (action == 2) {
            proxy.off();
            home.notifyChange(device.getName() + " turned OFF.");

        } else {
            proxy.status();
        }
    }

    static void security() {
        System.out.println("1. Motion\n2. Door\n3. Alarm");
        int c = number("Choose event: ");
        if (c == 1) {
            home.securityEvent("motion");
        } else if (c == 2) {
            home.securityEvent("door");
        } else if (c == 3) {
            home.securityEvent("alarm");
        } else {
            System.out.println("Invalid event.");
        }
    }

    static void abstractFactory() {
        System.out.println("1. Normal Home\n2. Security Home");
        int c = number("Choose home type: ");
        String name = input("Device name: "), comm = input("Communication (WiFi/Bluetooth): ");
        Device d = home.createHomeDevice(c == 2 ? "security" : "normal", name, comm);
        System.out.println("Device created by Abstract Factory:");
        d.status();
    }

    static String input(String p) {
        while (true) {
            System.out.print(p);
            String s = sc.nextLine().trim();
            if (!s.isEmpty()) {
                return s;

            }
            System.out.println("Cannot be empty.");
        }
    }

    static int number(String p) {
        while (true) {
            try {
                return Integer.parseInt(input(p));
            } catch (Exception e) {
                System.out.println("Enter a number.");
            }
        }
    }
}
