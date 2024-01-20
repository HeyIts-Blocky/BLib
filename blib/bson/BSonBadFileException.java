package blib.bson;
public class BSonBadFileException extends RuntimeException{
    public BSonBadFileException(){
        super("File does not follow proper BSon syntax.\nMake sure it follows the following syntax: \n<type> <varName> <data>\nAlso, make sure it has an empty line at the end.");
    }
    
    public BSonBadFileException(String type){
        super("Unknown Type: " + type);
    }

    public BSonBadFileException(int i){
        super("Incorrect file extension.");
    }
}
