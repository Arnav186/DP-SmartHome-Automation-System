public class AlarmHandler extends SecurityHandler {
    public void handle(String e){if(e.equalsIgnoreCase("alarm"))System.out.println("Alarm activated!");else if(next!=null)next.handle(e);}
}