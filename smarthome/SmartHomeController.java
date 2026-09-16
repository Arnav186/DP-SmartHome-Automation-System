package smarthome;

import java.sql.*;
import java.util.*;

// Singleton Facade/controller
public final class SmartHomeController {
    private static SmartHomeController instance;
    private final DatabaseConnection db;
    private final DeviceSubject subject;

    private SmartHomeController() {
        db = DatabaseConnection.getInstance();
        subject = new DeviceSubject();
        subject.addObserver(new MobileAppObserver());
        subject.addObserver(new AdminObserver());
    }

    public static synchronized SmartHomeController getInstance() {
        if (instance == null) {
            instance = new SmartHomeController();
        }
        return instance;
    }

    public Integer registerUser(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            System.out.println("Username and password cannot be empty.");
            return null;
        }

        String sql = "INSERT INTO users(username, password_hash) VALUES(?, ?)";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, username.trim());
            ps.setString(2, PasswordUtil.hash(password));
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    System.out.println("Registration successful.");
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("unique")) {
                System.out.println("Username already exists.");
            } else {
                System.out.println("Registration failed: " + e.getMessage());
            }
        }
        return null;
    }

    public Integer login(String username, String password) {
        String sql = "SELECT id FROM users WHERE username = ? AND password_hash = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, username.trim());
            ps.setString(2, PasswordUtil.hash(password));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Login successful. Welcome, " + username + "!");
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.out.println("Login failed: " + e.getMessage());
        }
        System.out.println("Invalid username or password.");
        return null;
    }

    public Integer addRoom(int userId, String roomName) {
        String sql = "INSERT INTO rooms(user_id, name) VALUES(?, ?)";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, userId);
            ps.setString(2, roomName);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    System.out.println("Room added with ID: " + id);
                    return id;
                }
            }
        } catch (SQLException e) {
            System.out.println("Could not add room: " + e.getMessage());
        }
        return null;
    }

    public void listRooms(int userId) {
        String sql = "SELECT id, name FROM rooms WHERE user_id = ? ORDER BY id";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("\n--- YOUR ROOMS ---");
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    System.out.println("ID: " + rs.getInt("id") + " | " + rs.getString("name"));
                }
                if (!found) System.out.println("No rooms found.");
            }
        } catch (SQLException e) {
            System.out.println("Could not list rooms: " + e.getMessage());
        }
    }

    public boolean roomBelongsToUser(int roomId, int userId) {
        String sql = "SELECT 1 FROM rooms WHERE id = ? AND user_id = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, roomId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public Integer addDevice(int userId, int roomId, String type, String name, String communication) {
        if (!roomBelongsToUser(roomId, userId)) {
            System.out.println("Invalid room. You can only add devices to your own room.");
            return null;
        }

        try {
            DeviceFactory factory = DeviceFactory.forType(type);
            Device device = factory.createDevice(0, name, communication);

            String sql = "INSERT INTO devices(room_id, name, type, communication, status) VALUES(?, ?, ?, ?, ?)";
            try (PreparedStatement ps = db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, roomId);
                ps.setString(2, device.getName());
                ps.setString(3, device.getType());
                ps.setString(4, device.getCommunication());
                ps.setString(5, device.getStatus());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        System.out.println("Device added successfully. Device ID: " + id);
                        return id;
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println("Could not add device: " + e.getMessage());
        }
        return null;
    }

    public void listDevices(int userId) {
        String sql = """
            SELECT d.id, d.name, d.type, d.communication, d.status, r.name AS room
            FROM devices d JOIN rooms r ON d.room_id = r.id
            WHERE r.user_id = ? ORDER BY d.id
        """;
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("\n--- YOUR DEVICES ---");
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    System.out.printf("ID:%d | %-15s | %-7s | Room:%-12s | %-7s | %s%n",
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("type"),
                            rs.getString("room"),
                            rs.getString("status"),
                            rs.getString("communication"));
                }
                if (!found) System.out.println("No devices found.");
            }
        } catch (SQLException e) {
            System.out.println("Could not list devices: " + e.getMessage());
        }
    }

    public void controlDevice(int userId, int deviceId, String action) {
        String sql = """
            SELECT d.id, d.name, d.type, d.communication, d.status
            FROM devices d JOIN rooms r ON d.room_id = r.id
            WHERE d.id = ? AND r.user_id = ?
        """;

        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, deviceId);
            ps.setInt(2, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("Device not found or access denied.");
                    return;
                }

                DeviceFactory factory = DeviceFactory.forType(rs.getString("type"));
                Device realDevice = factory.createDevice(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("communication")
                );

                if ("ON".equalsIgnoreCase(rs.getString("status"))) realDevice.turnOn();
                else realDevice.turnOff();

                Device secureDevice = new SecureDeviceProxy(realDevice, true);
                String normalized = action.toUpperCase();

                if (normalized.equals("ON")) secureDevice.turnOn();
                else if (normalized.equals("OFF")) secureDevice.turnOff();
                else if (normalized.equals("STATUS")) secureDevice.showStatus();
                else {
                    System.out.println("Use ON, OFF or STATUS.");
                    return;
                }

                String newStatus = secureDevice.getStatus();
                try (PreparedStatement update = db.getConnection().prepareStatement(
                        "UPDATE devices SET status = ? WHERE id = ?")) {
                    update.setString(1, newStatus);
                    update.setInt(2, deviceId);
                    update.executeUpdate();
                }

                logEvent(userId, secureDevice.getName(), "DEVICE_CONTROL",
                        "Device " + normalized + ": " + secureDevice.getName());

                subject.notifyObservers("Device '" + secureDevice.getName() +
                        "' changed to " + newStatus + ".");
            }
        } catch (Exception e) {
            System.out.println("Device control failed: " + e.getMessage());
        }
    }

    public void logSecurityEvent(int userId, String type, String location) {
        SecurityHandler chain = new MotionHandler();
        chain.setNext(new DoorHandler())
             .setNext(new AlarmHandler())
             .setNext(new EmergencyHandler());

        SecurityEvent event = new SecurityEvent(type, location);
        chain.handle(event);

        logEvent(userId, location, "SECURITY", type + " event at " + location);
        subject.notifyObservers("Security event: " + type + " at " + location);
    }

    private void logEvent(int userId, String deviceName, String eventType, String message) {
        String sql = "INSERT INTO events(user_id, device_name, event_type, event_message) VALUES(?, ?, ?, ?)";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, deviceName);
            ps.setString(3, eventType);
            ps.setString(4, message);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Event logging failed: " + e.getMessage());
        }
    }

    public void listEvents(int userId) {
        String sql = """
            SELECT event_type, event_message, event_time
            FROM events WHERE user_id = ?
            ORDER BY id DESC LIMIT 20
        """;
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("\n--- RECENT EVENTS ---");
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    System.out.println(rs.getString("event_time") + " | " +
                            rs.getString("event_type") + " | " +
                            rs.getString("event_message"));
                }
                if (!found) System.out.println("No events yet.");
            }
        } catch (SQLException e) {
            System.out.println("Could not list events: " + e.getMessage());
        }
    }
}
