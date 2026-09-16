package smarthome;

// Chain of Responsibility
public abstract class SecurityHandler {
    protected SecurityHandler next;

    public SecurityHandler setNext(SecurityHandler next) {
        this.next = next;
        return next;
    }

    public abstract void handle(SecurityEvent event);
}
