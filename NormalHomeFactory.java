
public class NormalHomeFactory implements HomeFactory {
    public Device create(String name,Communication c){return new Light(name,c);}
}