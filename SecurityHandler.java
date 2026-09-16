// Chain of Responsibility
public abstract class SecurityHandler {
    protected SecurityHandler next;
    public SecurityHandler setNext(SecurityHandler n){next=n;return n;}
    public abstract void handle(String event);
}