package blib.game;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

import blib.util.BTools;
public class CamShake {

    public Camera camera;

    // shaking data
    public int type = BOTH;
    static int HORIZONTAL = 0, VERTICAL = 1, BOTH = 2;
    public int strength = 5; // in pixels
    public double traumaLoss = 0.0003;

    // runtime data
    public double trauma = 0; // GDC 2016: Juicing Your Cameras With Math
    public int offX = 0;
    public int offY = 0;

    public CamShake(Camera cam){
        camera = cam;
    }

    public void update(long elapsedTime){
        if(trauma > 0) trauma -= traumaLoss * elapsedTime;
        if(trauma < 0) trauma = 0;
        offX = (int)((strength * Math.pow(trauma, 2)) * ((BTools.randInt(0, strength * 2) - strength) / (double)strength));
        offY = (int)((strength * Math.pow(trauma, 2)) * ((BTools.randInt(0, strength * 2) - strength) / (double)strength));
    }
    public void updateNoLoss(){
        offX = (int)((strength * Math.pow(trauma, 2)) * ((BTools.randInt(0, strength * 2) - strength) / (double)strength));
        offY = (int)((strength * Math.pow(trauma, 2)) * ((BTools.randInt(0, strength * 2) - strength) / (double)strength));
    }

    public void renderWithShake(Graphics g, ArrayList<Entity> renderList){
        if(camera == null) return;
        camera.render(g, renderList, offX, offY);
    }
    public void renderbgWithShake(Graphics g, ImageIcon img){
        if(camera == null) return;
        camera.renderbg(g, img, -offX, -offY);
    }
    public void renderbgWithShake(Graphics g, ImageIcon img, int x, int y){
        if(camera == null) return;
        camera.renderbg(g, img, -offX + x, -offY + y);
    }

    public void renderLightWithShake(Graphics g, ArrayList<Entity> renderList, LightManager lightManager){
        lightManager.render(camera, renderList, g, -offX, -offY);
    }

    public void addTrauma(double t){
        trauma = BTools.clamp(trauma + t, 0, 1);
    }
}