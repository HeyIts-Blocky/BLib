package blib.util;
import javax.swing.*;

import blib.bson.BSonObject;
import blib.bson.BSonParser;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
public class AnimImage {

    public ImageIcon spriteSheet;
    public int frameCount;
    public long delay;
    public int width, height;

    public int currentFrame;
    public long timer = 0;

    public boolean debug = false;
    
    public AnimImage(String filePath, int fCount, int fps, int frameWidth, int frameHeight){
        spriteSheet = new ImageIcon(filePath);
        frameCount = fCount;
        delay = 1000 / fps;
        width = frameWidth;
        height = frameHeight;
    }
    public AnimImage(ImageIcon sheet, int fCount, int fps, int frameWidth, int frameHeight){
        spriteSheet = sheet;
        frameCount = fCount;
        delay = 1000 / fps;
        width = frameWidth;
        height = frameHeight;
    }
    
    public static AnimImage parseBSon(String sheet, String bsonFile){
        ArrayList<BSonObject> objects = BSonParser.readFile(bsonFile);
        BSonObject obj = BSonParser.getObject("frameCount", objects);
        int fCount = obj.getInt();
        obj = BSonParser.getObject("fps", objects);
        int fps = obj.getInt();
        obj = BSonParser.getObject("frameWidth", objects);
        int frameWidth = obj.getInt();
        obj = BSonParser.getObject("frameHeight", objects);
        int frameHeight = obj.getInt();
        return new AnimImage(sheet, fCount, fps, frameWidth, frameHeight);
    }

    public void paint(JPanel panel, Graphics g, int x, int y){
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g2 = img.getGraphics();
        int f = 0, x2 = 0, y2 = 0;
        boolean leave = false;
        for(int y3 = 0; y3 < spriteSheet.getIconHeight(); y3 += height){
            for(int x3 = 0; x3 < spriteSheet.getIconWidth(); x3 += width){
                if(f == currentFrame){
                    x2 = x3;
                    y2 = y3;
                    leave = true;
                    break;
                }
                f++;
            }
            if(leave) break;
        }
        g2.drawImage(spriteSheet.getImage(), -x2, -y2, panel);
        g.drawImage(img, x, y, panel);
        if(debug){
            g.setColor(Color.red);
            g.drawRect(x, y, width, height);
            g.setColor(Color.blue);
            g.drawRect(-x2 + x, -y2 + y, spriteSheet.getIconWidth(), spriteSheet.getIconHeight());
        }
    }

    public void update(long elapsedTime){
        timer += elapsedTime;
        if(timer >= delay){
            timer = 0;
            currentFrame++;
            if(currentFrame >= frameCount) currentFrame = 0;
        }
    }

    public void resize(int width, int height){
        double widthChange = width / this.width;
        double heightChange = height / this.height;
        this.width = width;
        this.height = height;
        spriteSheet.setImage(spriteSheet.getImage().getScaledInstance((int)(spriteSheet.getIconWidth() * widthChange), (int)(spriteSheet.getIconHeight() * heightChange), Image.SCALE_DEFAULT));
    }

    public AnimImage copy(boolean sameFrame){
        AnimImage img = new AnimImage(spriteSheet, frameCount, 1000 / (int)delay, width, height);
        if(sameFrame) img.currentFrame = currentFrame;
        return img;
    }
}
