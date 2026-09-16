
import java.sql.*;

public class SmartHomeController {

    private static SmartHomeController instance;
    private final DatabaseConnection db;
    private final HomeSubject subject;

    private SmartHomeController() {
        db = DatabaseConnection.getInstance();
        subject = new HomeSubject();
        subject.addObserver(new MobileApp());
        subject.addObserver(new Admin());
    }

    public static synchronized SmartHomeController getInstance() {
        if (instance == null) {
            instance = new SmartHomeController();

        }
        return instance;
    }

    public boolean register(String u, String p) {
        try (PreparedStatement ps = db.getConnection().prepareStatement("INSERT INTO users(username,password) VALUES(?,?)")) {
            ps.setString(1, u);
            ps.setString(2, p);
            ps.executeUpdate();
            System.out.println("Registration successful.");
            return true;
        } catch (SQLException e) {
            System.out.println("Username already exists.");
            return false;
        }
    }

    public boolean login(String u, String p) {

        try (PreparedStatement ps
                = db.getConnection().prepareStatement(
                        "SELECT id FROM users WHERE username=? AND password=?")) {

            ps.setString(1, u);
            ps.setString(2, p);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    System.out.println(
                            "Login successful. Welcome " + u + "!"
                    );

                    return true;
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Login error: " + e.getMessage()
            );
        }

        // Wrong password notification
        System.out.println("Invalid username or password.");

        NotificationFactory factory
                = new HouseOwnerNotificationFactory();

        factory.sendNotification(
                "Incorrect password entered for user: " + u
        );

        return false;
    }

    private Communication communication(String c) {
        return c.equalsIgnoreCase("wifi") ? new WiFi() : new Bluetooth();
    }

    public Device createDevice(String type, String name, String comm) {
        return DeviceFactory.getFactory(type).create(name, communication(comm));
    }

    public Device createHomeDevice(String type, String name, String comm) {
        HomeFactory f = type.equalsIgnoreCase("security") ? new SecurityHomeFactory() : new NormalHomeFactory();
        return f.create(name, communication(comm));
    }

    public void notifyChange(String m) {
        subject.notifyObservers(m);
    }

    public void securityEvent(String e) {
        SecurityHandler chain = new MotionHandler();
        chain.setNext(new DoorHandler()).setNext(new AlarmHandler());
        chain.handle(e);
        subject.notifyObservers("Security event: " + e);
    }

    public void close() {
        db.close();
    }
}
