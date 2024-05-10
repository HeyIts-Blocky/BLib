package blib.game;
import javax.swing.*;

import blib.input.KeyManager;
import blib.util.AnimImage;
import blib.util.Position;

import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;
public class Player extends PlayerController{

    public Camera camera;
    private ImageIcon[] images;
    private AnimImage[] animImages;
    private boolean moving = false;
    private int direction = 1;
    public boolean shortCollision = false;
    public static int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
    
    // camera smoothing
    public int smoothType = 1;
    public static final int NOSMOOTH = 0, BASICSMOOTH = 1;
    public double smoothAmount = 2;
    private Position offset = new Position();

    public Player(Position pos, KeyManager km, double speed, JPanel panel, String imgPath){
        super(pos, km, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, speed, 700);
        camera = new Camera(pos.copy(), panel);
        images = new ImageIcon[4];
        images[0] = new ImageIcon(imgPath + "/idleN.png");
        images[1] = new ImageIcon(imgPath + "/idleS.png");
        images[2] = new ImageIcon(imgPath + "/idleE.png");
        images[3] = new ImageIcon(imgPath + "/idleW.png");

        animImages = new AnimImage[4];
        animImages[0] = new AnimImage(imgPath + "/walkN.png", 4, 6, 16, 16);
        animImages[1] = new AnimImage(imgPath + "/walkS.png", 4, 6, 16, 16);
        animImages[2] = new AnimImage(imgPath + "/walkE.png", 4, 6, 16, 16);
        animImages[3] = new AnimImage(imgPath + "/walkW.png", 4, 6, 16, 16);
    }
    public Player(Position pos, KeyManager km, double speed, JPanel panel, String imgPath, int frameWidth, int frameHeight){
        super(pos, km, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, speed, 700);
        camera = new Camera(pos.copy(), panel);
        images = new ImageIcon[4];
        images[0] = new ImageIcon(imgPath + "/idleN.png");
        images[1] = new ImageIcon(imgPath + "/idleS.png");
        images[2] = new ImageIcon(imgPath + "/idleE.png");
        images[3] = new ImageIcon(imgPath + "/idleW.png");

        animImages = new AnimImage[4];
        animImages[0] = new AnimImage(imgPath + "/walkN.png", 4, 6, frameWidth, frameHeight);
        animImages[1] = new AnimImage(imgPath + "/walkS.png", 4, 6, frameWidth, frameHeight);
        animImages[2] = new AnimImage(imgPath + "/walkE.png", 4, 6, frameWidth, frameHeight);
        animImages[3] = new AnimImage(imgPath + "/walkW.png", 4, 6, frameWidth, frameHeight);
    }

    public void update(long elapsedTime){
        super.update(elapsedTime);
        if(getVelocity().x != 0 || getVelocity().y != 0) moving = true;
        else moving = false;
        if(km.getKeyDown(up)) direction = 0;
        if(km.getKeyDown(down)) direction = 1;
        if(km.getKeyDown(left)) direction = 3;
        if(km.getKeyDown(right)) direction = 2;

        for(AnimImage i: animImages) i.update(elapsedTime);

        // camera smoothing

        if(smoothType == NOSMOOTH){
            offset = new Position();
        }
        if(smoothType == BASICSMOOTH){
            offset = new Position(-getVelocity().x * smoothAmount * 20 * speed, -getVelocity().y * smoothAmount * 20 * speed);
        }

        camera.pos = new Position(getX() + offset.x, getY() + offset.y);
    }

    public void render(JPanel panel, Graphics g, int x, int y){
        if(moving){
            animImages[direction].paint(panel, g, x - (int)offset.x, y - (int)offset.y);
        }else{
            images[direction].paintIcon(panel, g, x - (int)offset.x, y - (int)offset.y);
        }
    }

    public int getDirection(){
        return direction;
    }

    public void setDirection(int dir){
        direction = dir;
    }

    public boolean getMoving(){
        return moving;
    }

    public Position getOffset(){
        return offset.copy();
    }

    public void resizeImages(int width, int height){
        for(int i = 0; i < images.length; i++){
            images[i].setImage(images[i].getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
        }
        for(AnimImage i: animImages){
            i.resize(width, height);
        }
    }

    public Rectangle getRect(){
        Rectangle plrRect = new Rectangle((int)getX() - images[0].getIconWidth() / 2, (int)getY() - images[0].getIconHeight() / 2, images[0].getIconWidth(), images[0].getIconHeight());
        if(shortCollision) plrRect = new Rectangle((int)getX() - (int)(images[0].getIconWidth() * 0.8) / 2, (int)getY(), (int)(images[0].getIconWidth() * 0.8), images[0].getIconHeight() / 2);
        return plrRect;
    }

    public void updateWithCollision(long elapsedTime, ArrayList<Rectangle> collisions){
        Position startPos = getPos().copy();
        update(elapsedTime);
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
                goToPos(getX(), startPos.y);
                setVelocity(new Position(vel.x, 0));
                plrRect = getCollision();

                // check x
                if(rect.intersects(plrRect)){
                    goToPos(startPos.x, getY());
                    setVelocity(new Position(0, vel.y));
                    plrRect = getCollision();
                }
            }
        }
        if(getVelocity().x != 0 || getVelocity().y != 0) moving = true;
        else moving = false;
    }

    public Rectangle getCollision(){
        Rectangle plrRect = new Rectangle((int)getX() - images[0].getIconWidth() / 2, (int)getY() - images[0].getIconHeight() / 2, images[0].getIconWidth(), images[0].getIconHeight());
        if(shortCollision) plrRect = new Rectangle((int)getX() - images[0].getIconWidth() / 2, (int)getY(), images[0].getIconWidth(), images[0].getIconHeight() / 2);
        return plrRect;
    }

    public void setAnimDebug(boolean debug){
        for(AnimImage i: animImages){
            i.debug = debug;
        }
    }
}
