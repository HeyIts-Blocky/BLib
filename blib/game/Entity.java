package blib.game;
import java.awt.*;
import javax.swing.*;

import blib.util.Position;
public class Entity { // meant to be extended off of
    public Position position;
    public int renderType = 0;
    public static final int TALL = 0, UNDER = 1, ABOVE = 2, TOPPRIORITY = 3, BOTTOMPRIORITY = 4;
    /*
     * TALL = Camera will put this so that it will render so you can go in front or behind the object
     * UNDER = Camera will always put this under the player
     * ABOVE = Camera will always put this above the player
     * TOPPRIORITY = Camera will put this in a select group that is always rendered on top
     * BOTTOMPRIORITY = Camera will put this in a select group that is always rendered on bottom
     */

    public Entity(Position position){
        this.position = position;
    }

    public void update(long elapsedTime){} // run every server tick

    public void render(Graphics g, JPanel panel, int x, int y){} // render at (x, y)
}
