package blib.bson;
public class WrongBSonTypeException extends RuntimeException{
    public WrongBSonTypeException(String type, String expected){
        super("Wrong BSon Type: " + type + ", asked for " + expected);
    }
}
