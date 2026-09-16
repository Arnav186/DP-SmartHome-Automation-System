public class MotionHandler extends SecurityHandler {
    public void handle(String e){if(e.equalsIgnoreCase("motion"))System.out.println("Motion detected!");else if(next!=null)next.handle(e);}
}