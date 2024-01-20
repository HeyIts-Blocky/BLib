package blib.bson;
import java.io.*;
import java.util.Scanner;

import blib.util.BTools;

import java.util.ArrayList;
public class BSonParser { // BSon is basically my own version of JSon
    
    public static ArrayList<BSonObject> readFile(String filePath){
        String fileEnd = BTools.getLastSubString(filePath, 5);
        if(!fileEnd.equals(".bson")){
            throw new BSonBadFileException(1);
        }
        ArrayList<BSonObject> objects = new ArrayList<BSonObject>();
        Scanner reader;
        try{
            File file = new File(filePath);
            if(!file.exists()) throw new BSonBadFileException();
            reader = new Scanner(file);
        }catch(Exception e){
            throw new BSonBadFileException();
        }
        while(reader.hasNext()){
            String typeStr = reader.next();
            int type = BSonObject.stringToType(typeStr);
            if(type == BSonObject.UNKNOWN) throw new BSonBadFileException(typeStr);

            String name = reader.next();

            BSonObject obj = null;
            if(type == BSonObject.LIST){
                reader.nextLine();
                obj = new BSonList(name);
                BSonList asList = (BSonList)obj;
                String typeStr2 = reader.next();
                while(!typeStr2.equals("}") && !typeStr2.equals("listEnd") && reader.hasNext()){
                    int type2 = BSonObject.stringToType(typeStr2);
                    if(type2 == BSonObject.UNKNOWN) throw new BSonBadFileException(typeStr2);

                    BSonObject obj2 = null;
                    if(type2 == BSonObject.STRING){
                        String str = reader.nextLine();
                        obj2 = new BSonObject(asList.list.size() + "", str);
                    }
                    if(type2 == BSonObject.INT){
                        int i = reader.nextInt();
                        reader.nextLine();
                        obj2 = new BSonObject(asList.list.size() + "", i);
                    }
                    if(type2 == BSonObject.DOUBLE){
                        double d = reader.nextDouble();
                        reader.nextLine();
                        obj2 = new BSonObject(asList.list.size() + "", d);
                    }
                    if(type2 == BSonObject.LONG){
                        long l = reader.nextLong();
                        reader.nextLine();
                        obj2 = new BSonObject(asList.list.size() + "", l);
                    }
                    if(type2 == BSonObject.BOOLEAN){
                        boolean b = reader.nextBoolean();
                        reader.nextLine();
                        obj2 = new BSonObject(asList.list.size() + "", b);
                    }
                    if(obj == null) throw new BSonBadFileException();
                    asList.list.add(obj2);

                    if(reader.hasNext()) typeStr2 = reader.next();
                }
            }else{
                if(type == BSonObject.STRING){
                    String str = reader.nextLine();
                    obj = new BSonObject(name, str);
                }
                if(type == BSonObject.INT){
                    int i = reader.nextInt();
                    reader.nextLine();
                    obj = new BSonObject(name, i);
                }
                if(type == BSonObject.DOUBLE){
                    double d = reader.nextDouble();
                    reader.nextLine();
                    obj = new BSonObject(name, d);
                }
                if(type == BSonObject.LONG){
                    long l = reader.nextLong();
                    reader.nextLine();
                    obj = new BSonObject(name, l);
                }
                if(type == BSonObject.BOOLEAN){
                    boolean b = reader.nextBoolean();
                    reader.nextLine();
                    obj = new BSonObject(name, b);
                }
            }
            
            if(obj == null) throw new BSonBadFileException();
            objects.add(obj);
        }
        reader.close();
        return objects;
    }

    public static BSonObject getObject(String name, ArrayList<BSonObject> objects){
        for(BSonObject obj: objects){
            if(obj.name.equals(name)) return obj;
        }
        return null;
    }
}
