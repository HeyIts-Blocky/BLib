package blib.bson;
public class BSonObject {

    public final int type;
    public static final int LIST = -2, UNKNOWN = -1, STRING = 0, INT = 1, DOUBLE = 2, LONG = 3, BOOLEAN = 4;

    public final String name;

    private String string;
    private int num;
    private double doub;
    private long lon;
    private boolean bool;
    
    public BSonObject(String n, String s){
        name = n;
        type = STRING;
        string = s.substring(1);
    }
    public BSonObject(String n, int i){
        name = n;
        type = INT;
        num = i;
    }
    public BSonObject(String n, double d){
        name = n;
        type = DOUBLE;
        doub = d;
    }
    public BSonObject(String n, long l){
        name = n;
        type = LONG;
        lon = l;
    }
    public BSonObject(String n, boolean b){
        name = n;
        type = BOOLEAN;
        bool = b;
    }
    public BSonObject(String n){
        type = LIST;
        name = n;
    }


    public String typeToString(){
        return typeToString(type);
    }
    public static String typeToString(int type){
        if(type == STRING) return "STRING";
        if(type == INT) return "INT";
        if(type == DOUBLE) return "DOUBLE";
        if(type == LIST) return "LIST";
        if(type == LONG) return "LONG";
        if(type == BOOLEAN) return "BOOLEAN";
        return "UNKNOWN";
    }

    public static int stringToType(String str){
        if(str.toLowerCase().equals("string")) return STRING;
        if(str.toLowerCase().equals("int")) return INT;
        if(str.toLowerCase().equals("double")) return DOUBLE;
        if(str.toLowerCase().equals("list") || str.equals("{")) return LIST;
        if(str.toLowerCase().equals("long")) return LONG;
        if(str.toLowerCase().equals("boolean")) return BOOLEAN;
        return UNKNOWN;
    }

    public String getString(){
        if(type != STRING) throw new WrongBSonTypeException(typeToString(), typeToString(STRING));
        return string;
    }

    public int getInt(){
        if(type != INT) throw new WrongBSonTypeException(typeToString(), typeToString(INT));
        return num;
    }

    public double getDouble(){
        if(type != DOUBLE) throw new WrongBSonTypeException(typeToString(), typeToString(DOUBLE));
        return doub;
    }

    public long getLong(){
        if(type != LONG) throw new WrongBSonTypeException(typeToString(), typeToString(LONG));
        return lon;
    }

    public boolean getBoolean(){
        if(type != BOOLEAN) throw new WrongBSonTypeException(typeToString(), typeToString(BOOLEAN));
        return bool;
    }
}
