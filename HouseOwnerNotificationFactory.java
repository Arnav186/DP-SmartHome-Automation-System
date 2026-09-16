public class HouseOwnerNotificationFactory
        extends NotificationFactory {

    @Override
    public Notification createNotification() {
        return new HouseOwnerNotification();
    }
}