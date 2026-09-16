public class Bluetooth implements Communication {
    public void send(String device,String command){System.out.println("[Bluetooth] "+command+" -> "+device);}
}