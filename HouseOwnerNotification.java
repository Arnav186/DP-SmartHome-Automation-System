public class HouseOwnerNotification implements Notification {

    @Override
    public void send(String message) {
        System.out.println("\n[SECURITY ALERT]");
        System.out.println("Notification sent to house owner:");
        System.out.println(message);
    }
}