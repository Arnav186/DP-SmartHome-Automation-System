// Factory Method
public abstract class DeviceFactory {
    public abstract Device create(String name,Communication communication);
    public static DeviceFactory getFactory(String type){
        if(type.equalsIgnoreCase("light")) return new LightFactory();
        if(type.equalsIgnoreCase("fan")) return new FanFactory();
        throw new IllegalArgumentException("Only Light and Fan are supported.");
    }
    static class LightFactory extends DeviceFactory{
        public Device create(String n,Communication c){return new Light(n,c);}
    }
    static class FanFactory extends DeviceFactory{
        public Device create(String n,Communication c){return new Fan(n,c);}
    }
}