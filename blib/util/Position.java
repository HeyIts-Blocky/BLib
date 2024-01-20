package blib.util;

import java.awt.*;
public class Position {
    public double x = 0;
    public double y = 0;
    public Position(double x, double y){
        this.x = x;
        this.y = y;
    }
    public Position(){}

    public Position copy(){
        return new Position(x, y);
    }

    public String toString(){
        return x + ", " + y;
    }

    public String toStringSimple(){
        int a = (int)(x * 10);
        int b = (int)(y * 10);
        String xString = a + "";
        String yString = b + "";

        // format x
        char lastX = xString.charAt(xString.length() - 1);
        String string1 = "";
        for(int i = 0; i < xString.length() - 1; i++){
            string1 += xString.charAt(i);
        }
        string1 += "." + lastX;

        // format y
        char lastY = yString.charAt(yString.length() - 1);
        String string2 = "";
        for(int i = 0; i < yString.length() - 1; i++){
            string2 += yString.charAt(i);
        }
        string2 += "." + lastY;

        return string1 + ", " + string2;
    }

    public Point toPoint(){
        return new Point((int)x, (int)y);
    }
}
