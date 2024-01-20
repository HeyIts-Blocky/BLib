package blib.game;
import java.awt.*;

import blib.input.KeyManager;
import blib.util.BTools;
import blib.util.Position;
public class PlayerController { // Generic player class that doesn't do too much on its own

    // >>> REQUIREMENTS <<<
    //  1. KeyManager
    //  2. BTools
    //  3. Position
    // >***<

    private Position position;
    public KeyManager km = null;
    public int up, down, left, right;
    public double speed;

    public final boolean advMovement;
    public double acceleration;
    private Position velocity = new Position();

    public PlayerController(Position pos){ // Only holds a point as a position
        position = pos;
        advMovement = false;
    }
    public PlayerController(Position pos, KeyManager km, int up, int down, int left, int right, double speed){ // can be updated and it moves itself
        position = pos;
        this.km = km;
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.speed = speed;
        advMovement = false;
    }

    public PlayerController(Position pos, KeyManager km, int up, int down, int left, int right, double speed, int acceleration){
        position = pos;
        this.km = km;
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.speed = speed;
        advMovement = true;
        this.acceleration = acceleration / 100.0 / 1000.0; // % of speed per second
    }

    public void update(long elapsedTime){
        if(km == null) return; // only go forward if keymanager is present
        Point moveAmount = new Point();
        if(km.getKeyDown(up)) moveAmount.y--;
        if(km.getKeyDown(down)) moveAmount.y++;
        if(km.getKeyDown(left)) moveAmount.x--;
        if(km.getKeyDown(right)) moveAmount.x++;

        if(advMovement){
            if(moveAmount.x != 0){ // speedup x
                velocity.x += moveAmount.x * acceleration * elapsedTime;
            }else{ // slowdown x
                if(Math.abs(velocity.x) < 0.1){
                    velocity.x = 0;
                }else{
                    double dir = velocity.x / Math.abs(velocity.x);
                    velocity.x -= dir * acceleration * elapsedTime;
                }

                
            }

            if(moveAmount.y != 0){ // speedup y
                velocity.y += moveAmount.y * acceleration * elapsedTime;
            }else{ // slowdown y
                if(Math.abs(velocity.y) < 0.1){
                    velocity.y = 0;
                }else{
                    double dir = velocity.y / Math.abs(velocity.y);
                    velocity.y -= dir * acceleration * elapsedTime;
                }
                
            }

            // if moving, clamp velocity
            if(moveAmount.x != 0 || moveAmount.y != 0){
                double magnitude = BTools.getDistance(new Position(), velocity);
                if(magnitude >= 1) velocity = BTools.getDirectionTrig(new Position(), velocity); // normalize vector
            }
            velocity.x = BTools.clamp(velocity.x, -100, 100); // if not moving, don't clamp much so you can push the player really fast
            velocity.y = BTools.clamp(velocity.y, -100, 100); // and if they don't stop it they can go flying

            move(velocity.x * speed * elapsedTime, velocity.y * speed * elapsedTime);

        }else move(moveAmount.x * speed * elapsedTime, moveAmount.y * speed * elapsedTime);
    }

    public void move(double x, double y){
        position.x += x;
        position.y += y;
    }

    public void move(Point p){
        move(p.x, p.y);
    }

    public void goToPoint(int x, int y){
        position.x = x;
        position.y = y;
    }
    public void goToPoint(Point p){
        goToPoint(p.x, p.y);
    }

    public void goToPos(double x, double y){
        position.x = x;
        position.y = y;
    }
    public void goToPos(Position p){
        goToPos(p.x, p.y);
    }

    public void clampPos(int minx, int maxx, int miny, int maxy){
        position.x = BTools.clamp(position.x, minx, maxx);
        position.y = BTools.clamp(position.y, miny, maxy);
    }

    public double getX(){
        return position.x;
    }
    public double getY(){
        return position.y;
    }
    public Position getPos(){
        return position;
    }

    public Position getVelocity(){
        if(advMovement) return velocity;
        else return null;
    }

    public void addVelocity(Position vel){
        velocity.x += vel.x;
        velocity.y += vel.y;
    }
    public void addVelocity(double degrees, double strength){
        double radians = Math.toRadians(degrees);
        Position direction = BTools.angleToVector(radians);
        direction.x *= strength;
        direction.y *= strength;
        addVelocity(direction);
    }

    public void setVelocity(Position vel){
        velocity = vel.copy();
    }
}
