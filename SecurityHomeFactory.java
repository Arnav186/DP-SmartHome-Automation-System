public class SecurityHomeFactory implements HomeFactory {
    public Device create(String name,Communication c){return new Fan(name,c);}
}