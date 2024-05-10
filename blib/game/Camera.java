package blib.game;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import blib.util.*;
import java.awt.image.*;
public class Camera {

    public Position pos;
    public JPanel panel;
    public Dimension setDimension = null;
    public int renderDistance = 800;
    protected double zoom = 1;

    private double targetZoom = 1, startZoom = 1;
    private long zoomTimer = 10, zoomTime = 10;

    public Camera(Position p, JPanel panel){
        pos = p;
        this.panel = panel;
    }

    public void render(Graphics g, ArrayList<Entity> renderList){
        render(g, renderList, 0, 0);
    }
    public void render(Graphics g, ArrayList<Entity> renderList, int offX, int offY){
        if(zoom <= 0) zoom = 1;
        Dimension d = getDimension();
        int width = d.width;
        int height = d.height;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g2 = img.getGraphics();
        for(Entity e: renderList){
            if(BTools.getDistance(pos, e.position) <= (renderDistance / zoom) || e.alwaysRender){
                if(e.ignoreZoom){

                    Dimension d2 = getDimensionNoZoom();
                    e.render(g, panel, (int)(((e.position.x) - (pos.x + offX)) + (d2.width / 2)), (int)(((e.position.y) - (pos.y + offY)) + (d2.height / 2)));

                }else e.render(g2, panel, (int)(((e.position.x) - (pos.x + offX)) + (width / 2)), (int)(((e.position.y) - (pos.y + offY)) + (height / 2)));
            }
        }
        Dimension d2 = getDimensionNoZoom();
        g.drawImage(img, 0, 0, d2.width, d2.height, panel);
    }

    public void update(long elapsedTime){
        if(zoomTimer < zoomTime){
            zoomTimer += elapsedTime;
        }
        double zPercent = (double)zoomTimer / zoomTime;
        zPercent = BTools.clamp(zPercent, 0, 1);

        double delta = Math.abs(startZoom - targetZoom);
        if(targetZoom < startZoom) delta *= -1;

        delta *= zPercent;
        zoom = startZoom + delta;
    }

    public void setTargetZoom(double target, long time){
        startZoom = zoom;
        targetZoom = target;
        zoomTime = time;
        zoomTimer = 0;
    }
    public void setZoom(double z){
        zoom = z;
        startZoom = z;
    }
    public double getZoom(){
        return zoom;
    }

    public Dimension getDimension(){
        int width = (int)(panel.getWidth() * zoom);
        int height = (int)(panel.getHeight() * zoom);
        if(setDimension != null){
            width = (int)(setDimension.width * zoom);
            height = (int)(setDimension.height * zoom);
        }

        return new Dimension(width, height);
    }
    public Dimension getDimensionNoZoom(){
        int width = panel.getWidth();
        int height = panel.getHeight();
        if(setDimension != null){
            width = setDimension.width;
            height = setDimension.height;
        }

        return new Dimension(width, height);
    }

    public Position mouseToPos(Point mousePos){ // does not work with setDimension
        Position pos = new Position(mousePos.x, mousePos.y);
        Dimension d = getDimension();
        int width = d.width;
        int height = d.height;
        pos.x -= width / 2;
        pos.y -= height / 2;
        pos.x += this.pos.x;
        pos.y += this.pos.y;
        return pos;
    }

    public void renderbg(Graphics g, ImageIcon img){
        renderbg(g, img, 0, 0);
    }

    public void renderbg(Graphics g, ImageIcon img, int offX, int offY){
        Dimension d = getDimension();
        int width = d.width;
        int height = d.height;
        BufferedImage i = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g2 = i.getGraphics();
        img.paintIcon(panel, g2, (int)(-pos.x + width / 2 + offX), (int)(-pos.y + height / 2 + offY));
        Dimension d2 = getDimensionNoZoom();
        g.drawImage(i, 0, 0, d2.width, d2.height, panel);
    }

    public Point worldToScreen(Position pos){
        Dimension d = getDimension();
        int width = d.width;
        int height = d.height;
        return new Point((int)(((pos.x) - this.pos.x) + (width / 2)), (int)(((pos.y) - this.pos.y) + (height / 2)));
    }
    public Point worldToScreenNoZoom(Position pos){
        Dimension d = getDimensionNoZoom();
        int width = d.width;
        int height = d.height;
        return new Point((int)(((pos.x) - this.pos.x) + (width / 2)), (int)(((pos.y) - this.pos.y) + (height / 2)));
    }

    public ArrayList<ArrayList<Entity>> splitEntities(ArrayList<Entity> entities){ 
        return splitEntities(entities, 0);
    }
    public ArrayList<ArrayList<Entity>> splitEntities(ArrayList<Entity> entities, int yOffset){
        ArrayList<Entity> drawBehind = new ArrayList<Entity>(), drawInFront = new ArrayList<Entity>(), topPriority = new ArrayList<Entity>(), bottomPriority = new ArrayList<Entity>();
        for(Entity e: entities){
            if(e.renderType == Entity.TALL){
                if(e.position.y > pos.y + yOffset) drawInFront.add(e);
                else drawBehind.add(e);
            }
            if(e.renderType == Entity.UNDER) drawBehind.add(e);
            if(e.renderType == Entity.ABOVE) drawInFront.add(e);
            if(e.renderType == Entity.TOPPRIORITY) topPriority.add(e);
            if(e.renderType == Entity.BOTTOMPRIORITY) bottomPriority.add(e);
        }
        ArrayList<ArrayList<Entity>> list = new ArrayList<ArrayList<Entity>>();
        list.add(drawBehind);
        list.add(drawInFront);
        list.add(bottomPriority);
        list.add(topPriority);
        return list;
    }
}