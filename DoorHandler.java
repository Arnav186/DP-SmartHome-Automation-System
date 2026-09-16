public class DoorHandler extends SecurityHandler {
    public void handle(String e){if(e.equalsIgnoreCase("door"))System.out.println("Door event detected!");else if(next!=null)next.handle(e);}
}