import java.util.*;
public class HomeSubject {
    private final List<Observer> observers=new ArrayList<>();
    public void addObserver(Observer o){observers.add(o);}
    public void notifyObservers(String message){for(Observer o:observers)o.update(message);}
}