package blib.bson;
import java.util.ArrayList;
public class BSonList extends BSonObject{

    public ArrayList<BSonObject> list = new ArrayList<BSonObject>();
    
    public BSonList(String name){
        super(name);
    }
}
