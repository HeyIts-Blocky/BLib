package blib.util;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
public class Texture { // useful for drawing repeating textures, but if you're just doing a sprite still use ImageIcon/AnimImage

    private static int SCALE = 0, REPEAT = 1, REPEAT_CUSTOM = 2;

    ImageIcon img = null;
    int scaleType = REPEAT;
    int xRep = 1, yRep = 1;
    
    public Texture(String path){
        img = new ImageIcon(path);
        scaleType = SCALE;
    }
    public Texture(ImageIcon i){
        img = i;
        scaleType = SCALE;
    }
    public Texture(String path, int width, int height){
        img = new ImageIcon(path);
        BTools.resizeImgIcon(img, width, height);
    }
    public Texture(ImageIcon i, int width, int height){
        img = i;
        BTools.resizeImgIcon(img, width, height);
    }
    public Texture(String path, double sizeMult){
        img = new ImageIcon(path);
        BTools.resizeImgIcon(img, (int)(img.getIconWidth() * sizeMult), (int)(img.getIconHeight() * sizeMult));
    }

    protected Texture(){
        scaleType = SCALE;
    }
    protected Texture(int width, int height){}
    protected Texture(double sizeMult){}

    public void resizeImg(int width, int height){
        BTools.resizeImgIcon(img, width, height);
    }

    public void setScale(){
        scaleType = SCALE;
    }
    public void setRepeat(){
        scaleType = REPEAT;
    }
    public void setCustom(int repX, int repY){
        if(repX <= 0 || repY <= 0) return;
        scaleType = REPEAT_CUSTOM;
        xRep = repX;
        yRep = repY;
    }

    public int getScaleType(){
        return scaleType;
    }

    public void draw(Graphics g, int x, int y, int width, int height){
        g.drawImage(getImage(width, height), x, y, null);
    }

    public Image getImage(int width, int height){
        BufferedImage i = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = i.getGraphics();
        for(int y = 0; y <= height; y += img.getIconHeight()){
            for(int x = 0; x <= width; x += img.getIconWidth()){
                img.paintIcon(null, g, x, y);
            }
        }
        return (Image)i;
    }
}
