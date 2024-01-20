package blib.anim;
import java.util.ArrayList;
import blib.util.*;
public class Animator {
    // REQUIREMENTS
    //>Animation.java
    //>Position.java

    private Position parent, originalPosition;
    private ArrayList<Animation> animations;

    private Animation currentAnimation = null;
    private long currentTime = 0;

    public Animator(Position parent, ArrayList<Animation> anims){
        this.parent = parent;
        originalPosition = parent.copy();
        animations = anims;
    }

    public void update(long elapsedTime){
        if(currentAnimation == null){
            parent.x = originalPosition.x;
            parent.y = originalPosition.y;
            return;
        }
        if(currentTime < currentAnimation.getLength()){
            currentTime += elapsedTime;
        }else{
            if(currentAnimation.loop) currentTime = 0;
            else currentTime = currentAnimation.getLength();
        }

        // set position
        Position newPos = currentAnimation.getPos(currentTime, originalPosition);
        parent.x = newPos.x;
        parent.y = newPos.y;
    }

    public void play(String anim){
        currentTime = 0;
        for(Animation a: animations){
            if(a.name.equals(anim)){
                currentAnimation = a;
                return;
            }
        }
        currentAnimation = null;
    }

    public void stop(){
        currentTime = 0;
        currentAnimation = null;
    }

    public boolean isPlaying(){
        return currentTime < currentAnimation.getLength();
    }
}
