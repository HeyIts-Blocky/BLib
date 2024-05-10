package blib.game;

import blib.input.*;
import blib.util.*;
import java.awt.*;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import java.awt.event.*;
public class PlatformerController extends PlayerController{

    public double gravity;
    public boolean moving = false;
    public boolean shortCollision = false;
    public int direction = Player.EAST;
    public double smoothAmount = 2;
    public double jumpPower = 1;
    public int width, height;
    
    public PlatformerController(Position pos, KeyManager km, double speed, double g, int w, int h){
        super(pos, km, KeyEvent.VK_SPACE, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, speed, 700);
        gravity = g;
        width = w;
        height = h;
    }
    
    public void update(long elapsedTime, ArrayList<Rectangle> collisions){
        Position startPos = getPos().copy();
        Point moveAmount = new Point();
        if(km.getKeyDown(left)) moveAmount.x--;
        if(km.getKeyDown(right)) moveAmount.x++;

        if(moveAmount.x > 0) direction = Player.EAST;
        else if(moveAmount.x < 0) direction = Player.WEST;

        if(!grounded(collisions)) moveAmount.y = 1;
        else{
            velocity.y = 0;
            if(km.getKeyDown(up)){
                velocity.y = -jumpPower;
            }
        }

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
                velocity.y += moveAmount.y * acceleration * elapsedTime * gravity;
            }else{ // slowdown y
                if(Math.abs(velocity.y) < 0.1){
                    velocity.y = 0;
                }else{
                    double dir = velocity.y / Math.abs(velocity.y);
                    velocity.y -= dir * acceleration * elapsedTime;
                }
                
            }

            
            velocity.x = BTools.clamp(velocity.x, -1, 1);
            velocity.y = BTools.clamp(velocity.y, -100, 100);

            move(velocity.x * speed * elapsedTime, velocity.y * speed * elapsedTime);

        }else move(moveAmount.x * speed * elapsedTime, moveAmount.y * speed * elapsedTime);
        
        
        Rectangle plrRect = getCollision();
        for(Rectangle rect: collisions){
            Position vel = getVelocity().copy();
            // check x
            if(rect.intersects(plrRect)){
                Position pos = getPos().copy();
                goToPos(startPos.x, getY());
                setVelocity(new Position(0, vel.y));
                plrRect = getCollision();
                if(rect.intersects(plrRect)){
                    goToPos(pos);
                    plrRect = getCollision();
                }
            }
            // check y
            if(rect.intersects(plrRect)){
                Position pos = getPos().copy();
                goToPos(getX(), startPos.y);
                setVelocity(new Position(vel.x, 0));
                plrRect = getCollision();

                // check x
                if(rect.intersects(plrRect)){
                    goToPos(startPos.x, pos.y);
                    setVelocity(new Position(0, vel.y));
                    plrRect = getCollision();
                }
            }
        }
        if(getVelocity().x != 0 || getVelocity().y != 0) moving = true;
        else moving = false;
    }

    public void update(long elapsedTime, ArrayList<Rectangle> collisions, Player plr){ // override a player
        plr.speed = 0; // make sure they don't move on their own >:)
        update(elapsedTime, collisions);

        plr.update(elapsedTime); // update the images (position and direction will be overridden anyways)
        plr.setDirection(direction);
        plr.position = position.copy();

        Position offset;
        offset = new Position(-getVelocity().x * smoothAmount * 20 * speed, -getVelocity().y * smoothAmount * 20 * speed);
        plr.camera.pos = new Position(getX() + offset.x, getY() + offset.y);
    }

    public Rectangle getCollision(){
        Rectangle plrRect = new Rectangle((int)getX() - width / 2, (int)getY() - height / 2, width, height);
        if(shortCollision) plrRect = new Rectangle((int)getX() - width / 2, (int)getY(), width, height / 2);
        return plrRect;
    }

    public boolean grounded(ArrayList<Rectangle> collision){
        Rectangle rect = new Rectangle((int)position.x - (getCollision().width - 2) / 2, (int)position.y + getCollision().height / 2, getCollision().width - 2, 5);

        for(Rectangle r: collision){
            if(rect.intersects(r)){
                return true;
            }
        }
        return false;
    }
}
