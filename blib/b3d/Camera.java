package blib.b3d;

import blib.util.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.image.*;
public class Camera {

    public Position position;
    public double direction; // in radians
    public Dimension size = null; // uses panel size if null
    public double fov = Math.toRadians(60);
    public int rendDist = 1000, fhp = 20;
    public Color floorColor = Color.gray, skyColor = Color.cyan;
    
    public Camera(Position pos, double dir){
        position = pos;
        direction = dir;
    }

    public Camera(Position pos, double dir, Dimension s){
        position = pos;
        direction = dir;
        size = s;
    }

    public void render(JPanel panel, Graphics g, ArrayList<Wall> map, int verticalOffset){
        while(direction > Math.PI * 2) direction -= Math.PI * 2;
        while(direction < 0) direction += Math.PI * 2;

        Dimension screen = size;
        if(screen == null) screen = new Dimension(panel.getWidth(), panel.getHeight());

        g.setColor(floorColor);
        g.fillRect(0, 0, (int)screen.getWidth(), (int)screen.getHeight());
        g.setColor(skyColor);
        g.fillRect(0, 0, (int)screen.getWidth(), (int)screen.getHeight() / 2 + verticalOffset);

        double leftDir = direction - fov / 2;
        double rightDir = direction + fov / 2;
        double dirDiff = fov / screen.width; // one dir for every pixel

        int pixel = 0;
        for(double dir = leftDir; dir <= rightDir; dir += dirDiff){
            Line ray = Line.ray(position.copy(), dir, rendDist);
            ArrayList<Position> intersectPoints = new ArrayList<Position>();
            ArrayList<Wall> walls = new ArrayList<Wall>();
            for(int i = 0; i < map.size(); i++){
                if(ray.intersects(map.get(i))){
                    intersectPoints.add(ray.intersection(map.get(i)));
                    walls.add(map.get(i));
                }
            }
            if(intersectPoints.size() > 0){
                Position pos = intersectPoints.get(0);
                Wall w = walls.get(0);
                ArrayList<Position> renderPos = new ArrayList<Position>();
                ArrayList<Wall> renderWalls = new ArrayList<Wall>();
                
                boolean loop = false;
                do{
                    pos = intersectPoints.get(0);
                    w = walls.get(0);
                    
                    loop = false;
                    for(int i = 1; i < intersectPoints.size(); i++){
                        double dist1 = BTools.getDistance(position, pos);
                        double dist2 = BTools.getDistance(position, intersectPoints.get(i));
                        if(dist2 < dist1){
                            pos = intersectPoints.get(i);
                            w = walls.get(i);
                        }
                    }
                    renderPos.add(0, pos);
                    renderWalls.add(0, w);
                    intersectPoints.remove(pos);
                    walls.remove(w);
                    if(w.transparent && intersectPoints.size() > 1){
                        loop = true;
                    }
                }while(loop);
                
                for(int i = 0; i < renderPos.size(); i++){
                    Wall wall = renderWalls.get(i);
                    pos = renderPos.get(i);

                    Color c = wall.color;
                    ImageIcon texture = wall.texture;
                    int length = wall.texLength;
                    Position startPos = wall.start;

                    double dist = BTools.flip(BTools.getDistance(position, pos), (rendDist - fhp)); 
                    double percent = (double)(dist - fhp) / (rendDist);
                    if(percent < 0){
                        pixel++;
                        continue;
                    }

                    percent = Math.pow(percent, 6);

                    double topPercent = percent;
                    double bottomPercent = percent;

                    int topPixel = BTools.flip((int)(topPercent * (screen.getHeight() / 2)), (int)screen.getHeight() / 2) + verticalOffset;
                    int height = (int)(bottomPercent * (screen.getHeight() / 2)) + (BTools.flip(topPixel - verticalOffset, (int)screen.getHeight() / 2));

                    percent = BTools.clamp(percent, 0, 1);

                    if(texture != null && height > 0){
                        // draw texture
                        percent = BTools.flip(percent, 1);
                        Color overlayCol = new Color(0f, 0f, 0f, (float)percent);

                        BufferedImage texSlice = new BufferedImage(1, height, BufferedImage.TYPE_INT_ARGB);
                        int wallDist = (int)BTools.getDistance(pos, startPos);
                        double wallPercent = wallDist / (double)(length);
                        while(wallPercent > 1) wallPercent -= 1;
                        while(wallPercent < 0) wallPercent += 1;

                        Graphics g2 = texSlice.getGraphics();
                        g2.drawImage(texture.getImage(), (int)(texture.getIconWidth() * -wallPercent) + (wallPercent > 0.5 ? 1 : 0), 0, texture.getIconWidth(), height, null);
                        g2.setColor(overlayCol);
                        g2.fillRect(0, 0, 1, height);

                        g.drawImage(texSlice, pixel, topPixel, null);
                    }else{
                        // darken the color depending on distance
                        c = new Color((int)(c.getRed() * percent), (int)(c.getGreen() * percent), (int)(c.getBlue() * percent));

                        g.setColor(c);
                        g.fillRect(pixel, topPixel, 1, height);
                    }
                }
                
            }
            pixel++;
        }
    }
}