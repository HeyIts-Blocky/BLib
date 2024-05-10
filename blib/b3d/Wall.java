package blib.b3d;

import blib.util.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.image.*;
public class Wall extends Line{

    public Color color;
    public boolean rightFront;
    public ImageIcon texture = null;
    public int texLength = 100; 
    public boolean transparent = false;
    
    public Wall(Position start, Position end, Color c, boolean right){
        super(start, end);
        color = c;
        rightFront = right;
    }

    public Wall(Position start, Position end, Color c, boolean right, ImageIcon img, int length){
        super(start, end);
        color = c;
        rightFront = right;
        texture = img;
        texLength = length;
    }

    public Wall(Position start, Position end, Color c, boolean right, ImageIcon img, int length, boolean t){
        super(start, end);
        color = c;
        rightFront = right;
        texture = img;
        texLength = length;
        transparent = t;
    }

    public static Wall[] rectToWalls(Rectangle rect, Color c){
        Wall[] walls = new Wall[4];
        walls[0] = new Wall(new Position(rect.x, rect.y), new Position(rect.x, rect.y + rect.height), c, true);
        walls[1] = new Wall(new Position(rect.x, rect.y + rect.height), new Position(rect.x + rect.width, rect.y + rect.height), c, true);
        walls[2] = new Wall(new Position(rect.x + rect.width, rect.y + rect.height), new Position(rect.x + rect.width, rect.y), c, true);
        walls[3] = new Wall(new Position(rect.x + rect.width, rect.y), new Position(rect.x, rect.y), c, true);

        return walls;
    }
    public static Wall[] rectToWalls(Rectangle rect, Color c, ImageIcon texture, int length){
        Wall[] walls = new Wall[4];
        walls[0] = new Wall(new Position(rect.x, rect.y), new Position(rect.x, rect.y + rect.height), c, true, texture, length);
        walls[1] = new Wall(new Position(rect.x, rect.y + rect.height), new Position(rect.x + rect.width, rect.y + rect.height), c, true, texture, length);
        walls[2] = new Wall(new Position(rect.x + rect.width, rect.y + rect.height), new Position(rect.x + rect.width, rect.y), c, true, texture, length);
        walls[3] = new Wall(new Position(rect.x + rect.width, rect.y), new Position(rect.x, rect.y), c, true, texture, length);

        return walls;
    }
    public static Wall[] rectToWalls(Rectangle rect, Color c, ImageIcon texture, int length, boolean t){
        Wall[] walls = new Wall[4];
        walls[0] = new Wall(new Position(rect.x, rect.y), new Position(rect.x, rect.y + rect.height), c, true, texture, length, t);
        walls[1] = new Wall(new Position(rect.x, rect.y + rect.height), new Position(rect.x + rect.width, rect.y + rect.height), c, true, texture, length, t);
        walls[2] = new Wall(new Position(rect.x + rect.width, rect.y + rect.height), new Position(rect.x + rect.width, rect.y), c, true, texture, length, t);
        walls[3] = new Wall(new Position(rect.x + rect.width, rect.y), new Position(rect.x, rect.y), c, true, texture, length, t);

        return walls;
    }

    public static void lightWalls(ArrayList<Wall> walls, double lightDir){
        for(Wall w: walls){
            double normalDir = BTools.getAngle(w.start, w.end) + (Math.toRadians(90) * (w.rightFront ? 1 : -1));

            double relDir = Math.abs(normalDir - lightDir);

            double percent = relDir / Math.PI;
            percent = Math.pow(0.5 * percent, 2) + 0.5;

            w.color = new Color((int)(w.color.getRed() * percent), (int)(w.color.getGreen() * percent), (int)(w.color.getBlue() * percent));
        }
    }
}
