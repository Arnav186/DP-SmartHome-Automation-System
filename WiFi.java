public class WiFi implements Communication {
    public void send(String device,String command){System.out.println("[WiFi] "+command+" -> "+device);}
}