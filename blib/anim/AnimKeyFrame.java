package blib.anim;
import blib.util.*;
public class AnimKeyFrame {
    // REQUIREMENTS
    //>Position.java

    public Position start, end; // relative to original position
    public long travelTime;

    public AnimKeyFrame(Position s, Position e, long t){
        start = s;
        end = e;
        travelTime = t;
    }

    public Position getPosition(long t, Position origin){
        if(t >= travelTime){
            return new Position(origin.x + end.x, origin.y + end.y);
        }
        if(t <= 0){
            return new Position(origin.x + start.x, origin.y + start.y);
        }
        Position newPos = new Position(origin.x, origin.y);
        // get x pos
        double timePercent = (double)t/travelTime;
        double xPos = ((end.x - start.x) * timePercent) + start.x;
        // get y pos
        double yPos = ((end.y - start.y) * timePercent) + start.y;

        newPos.x += xPos;
        newPos.y += yPos;
        return newPos;
    }

    public Position getOffset(long t){
        return getPosition(t, new Position());
    }
}
