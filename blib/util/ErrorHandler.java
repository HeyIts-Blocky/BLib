package blib.util;
import java.io.*;
import javax.swing.*;
public class ErrorHandler {

    public static String flavorText = "Uh oh! Something went wrong!";

    public static void handle(Exception e){
        File file = new File("error.txt");
        try{
            file.createNewFile();
            PrintWriter writer = new PrintWriter(file);
            writer.println(flavorText + "\n");
            e.printStackTrace(writer);
            writer.close();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, "There was an error while trying to handle an error. How do you even manage that?!", "Error Handler", JOptionPane.ERROR_MESSAGE);
        }
        System.exit(1);
    }
}
