package blib.util;

import java.awt.image.*;
import java.awt.*;

public class AnimTexture extends Texture{ // it's a texture. but * AnImAtEd!! *

    AnimImage aImg;

    long elapsedTime = 0, lastTime = -1;
    
    public AnimTexture(AnimImage i){
        super();
        aImg = i;
    }
    public AnimTexture(AnimImage i, int width, int height){
        super(width, height);
        aImg = i;
        aImg.resize(width, height);
    }
    public AnimTexture(AnimImage i, double sizeMult){
        super(sizeMult);
        aImg = i;
        aImg.resize(aImg.width, aImg.height);
    }

    public void draw(Graphics g, int x, int y, int width, int height){
        g.drawImage(getImage(width, height), x, y, null);
    }

    public Image getImage(int width, int height){
        BufferedImage i = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = i.getGraphics();
        for(int y = 0; y <= height; y += aImg.height){
            for(int x = 0; x <= width; x += aImg.width){
                aImg.paint(null, g, x, y);
            }
        }

        if(lastTime == -1) lastTime = System.currentTimeMillis();
        else{
            long now = System.currentTimeMillis();
            elapsedTime = Math.abs(now - lastTime);
            lastTime = now;

            aImg.update(elapsedTime);
        }

        return (Image)i;
    }
}
