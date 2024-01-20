package blib.util;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import javax.swing.*;
import java.awt.image.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
public class BTools { // general class for general tools that i use a lot

    public static int getDistance(Point p1, Point p2){ // gets the distance between p1 and p2
        int distX = Math.abs(p2.x - p1.x);
        int distY = Math.abs(p2.y - p1.y);

        // a^2 + b^2 = c^2
        // c = sqrt(a^2 + b^2)
        int dist = (int)(Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2)));

        return dist;
    }
    public static double getDistance(Position p1, Position p2){ // gets the distance between p1 and p2
        double distX = Math.abs((int)p2.x - (int)p1.x);
        double distY = Math.abs((int)p2.y - (int)p1.y);

        // a^2 + b^2 = c^2
        // c = sqrt(a^2 + b^2)
        double dist = Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));

        return dist;
    }

    public static double[] getDirection(Point p1, Point p2){
        p1 = new Point(p1.x, p1.y);
        p2 = new Point(p2.x, p2.y); // make copies to not mess up things
        double[] result = new double[2];
        p2.x -= p1.x;
        p2.y -= p1.y;
        int absX, absY;
        absX = Math.abs(p2.x);
        absY = Math.abs(p2.y);
        int total = absX + absY;
        if(total == 0){
            result[0] = 0;
            result[1] = 0;
            return result;
        }
        double x = (double)absX / total;
        double y = (double)absY / total;
        if(p2.x < 0) x *= -1;
        if(p2.y < 0) y *= -1;
        result[0] = x;
        result[1] = y;
        return result;
    }

    public static Position getDirectionTrig(Position p1, Position p2){
        double angle = Math.atan2((p2.y - p1.y), (p2.x - p1.x));
        return angleToVector(angle);
    }

    public static Position angleToVector(double radians){
        if(Double.isNaN(radians)) return new Position();
        Position result = new Position(Math.cos(radians), Math.sin(radians));
        return result;
    }

    public static double getAngle(Position p1, Position p2){
        double angle = Math.atan2((p2.y - p1.y), (p2.x - p1.x));
        return angle;
    }

    public static double vectorToAngle(Position vector){
        return getAngle(new Position(), vector);
    }

    public static int randInt(int min, int max){
        if(max < min){ // min is bigger, swap
            int temp = max;
            max = min;
            min = temp;
        }
        if(max == min) return max; // same number, just return the number
        Random rand = new Random();
        return rand.nextInt(max - min) + min; // generate number
    }

    public static double randDouble(double min, double max){
        if(max < min){ // min is bigger, swap
            double temp = max;
            max = min;
            min = temp;
        }
        if(min == max) return min;
        int minInt = 0;
        if(min >= 1){
            minInt = (int)min;
        }
        int maxInt = 0;
        if(max >= 1){
            maxInt = (int)max;
        }
        int randInt = 0;
        if(minInt > 0){
            // get random for int part
            randInt = randInt(minInt, maxInt);

            // make min and max not have int part
            min -= minInt;
            max -= maxInt;
        }
        double randDecimal = 0;
        Random rand = new Random();
        do{
            randDecimal = rand.nextDouble();
        }while(randDecimal < min || randDecimal > max);
        return randInt + randDecimal;
    }

    public static boolean collides(Rectangle r1, Rectangle r2){
        return r1.intersects(r2);
    }

    public static boolean rectContains(Rectangle r, Point p){
        return r.contains(p);
    }

    public static int clamp(int a, int min, int max){
        a = Math.min(a, max);
        a = Math.max(a, min);
        return a;
    }
    public static double clamp(double a, double min, double max){
        a = Math.min(a, max);
        a = Math.max(a, min);
        return a;
    }

    public static String keyToString(int key){
        if(key < 0){
            // mouse button
            if(key == -1) return "Left Click";
            if(key == -2) return "Scroll Wheel Click";
            if(key == -3) return "Right Click";
            return "Unknown Mouse Button";
        }
        return KeyEvent.getKeyText(key);
    }

    /* Line doesn't exist in later versions of Java. this could be used if/when i make my own Line class
    public static Position intersectsAt(Line line1, Line line2){
        double slope1 = 0, yint1 = 0;
        boolean line1Vertical = true;
        if(line1.getStartX() != line1.getEndX()){ 
            line1Vertical = false;
            // get slope
            slope1 = (line1.getEndY() - line1.getStartY()) / (line1.getEndX() - line1.getStartY());
            // get y-intersection
            yint1 = -line1.getStartY() - slope1 * line1.getStartX();
        }
        double slope2 = 0, yint2 = 0;
        boolean line2Vertical = true;
        if(line2.getStartX() != line2.getEndX()){ 
            line2Vertical = false;
            // get slope
            slope2 = (line2.getEndY() - line2.getStartY()) / (line2.getEndX() - line2.getStartY());
            // get y-intersection
            yint2 = -line2.getStartY() - slope2 * line2.getStartX();
        }

        if(line1Vertical){
            double x = line1.getStartX();
            double y = slope2 * x + yint2;

            if(x > Math.max(line2.getStartX(), line2.getEndX()) || x < Math.min(line2.getStartX(), line2.getEndX())) return null; // don't actually intersect

            return new Position(x, y);
        }
        if(line2Vertical){
            double x = line2.getStartX();
            double y = slope1 * x + yint1;

            if(x > Math.max(line1.getStartX(), line1.getEndX()) || x < Math.min(line1.getStartX(), line1.getEndX())) return null; // don't actually intersect

            return new Position(x, y);
        }
        if(slope1 == slope2) return null; // parrallel lines

        double x = (yint2 - yint1) / (slope1 + slope2);
        double y = slope1 * x + yint1;
        Position intersect = new Position(x, y);

        if(x > Math.max(line1.getStartX(), line1.getEndX()) || x < Math.min(line1.getStartX(), line1.getEndX())) return null; // don't actually intersect

        return intersect;
    }
    */

    public static void openWebsite(String url){
        try{
            Desktop.getDesktop().browse(new URI(url));
        }catch(Exception e){}
    }

    public static int flip(int i, int max){
        return -i + max;
    }
    public static double flip(double i, double max){
        return -i + max;
    }

    public static void resizeImgIcon(ImageIcon img, int width, int height){
        img.setImage(img.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
    }

    public static String getLastSubString(String s, int n){ // gets the last n characters in a string
        return s.substring(s.length() - n, s.length());
    }

    public static boolean hasImage(ImageIcon i){
        return i.getImageLoadStatus() != MediaTracker.ERRORED;
    }

    public static void addFont(String fontPath, JPanel panel){
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try{
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(fontPath)));
        }catch(Exception e){
            ErrorHandler.handle(e);
        }
    }

    public static Cursor getBlankCursor(){
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
        return blankCursor;
    }

    public static Cursor getCustomCursor(ImageIcon img, String name){
        Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(img.getImage(), new Point(0, 0), name);
        return cursor;
    }
    public static Cursor getCustomCursor(ImageIcon img){
        return getCustomCursor(img, "custom cursor");
    }

    public static JFrame getWindowFullscreen(JFrame window, boolean fullscreen, JPanel parent){
        return getWindowFullscreen(window, fullscreen, parent, 700, 500);
    }
    public static JFrame getWindowFullscreen(JFrame window, boolean fullscreen, JPanel parent, int minWidth, int minHeight){
        String title = window.getTitle();
        Image icon = window.getIconImage();
        window.dispose();
        JFrame newWindow = new JFrame(title);
        newWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newWindow.setSize(minWidth, minHeight);
        newWindow.setResizable(true);
        newWindow.setMinimumSize(new Dimension(minWidth, minHeight));
        newWindow.setIconImage(icon);
        // panel
        newWindow.add(parent);
        //
        newWindow.setUndecorated(fullscreen);
        if(fullscreen) newWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
        newWindow.setVisible(true);
        return newWindow;
    }

    public static boolean hasExtension(File file, String extension){
        String strEnd = getLastSubString(file.getAbsolutePath(), extension.length());
        return strEnd.equals(extension);
    }

    public static void copyFile(File fileToCopy, File copiedFile){
        try{
            Files.copy(fileToCopy.toPath(), copiedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void openHighlightFile(String path){
        try{
            Runtime.getRuntime().exec("explorer.exe /select, " + path);
        }catch(Exception e){}
    }
    
}
