package blib.game;

import blib.util.Position;
import java.awt.*;

public class Light extends Entity{
    public int radius = 10;
    public Color color = null;
    public Light(Position pos){
        super(pos);
    }
    public Light(Position pos, int r){
        super(pos);
        radius = r;
    }
    public Light(Position pos, int r, Color c){
        super(pos);
        radius = r;
        color = c;
    }
}
