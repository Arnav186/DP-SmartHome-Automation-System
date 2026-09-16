// Proxy
public class SecureDeviceProxy implements Device {
    private final Device device; private final boolean loggedIn;
    public SecureDeviceProxy(Device device,boolean loggedIn){this.device=device;this.loggedIn=loggedIn;}
    private boolean allowed(){if(!loggedIn){System.out.println("Access denied.");return false;}return true;}
    public String getName(){return device.getName();}
    public void on(){if(allowed())device.on();}
    public void off(){if(allowed())device.off();}
    public void status(){if(allowed())device.status();}
}