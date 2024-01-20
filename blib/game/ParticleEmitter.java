package blib.game;
import javax.swing.*;

import blib.util.AnimImage;
import blib.util.BTools;
import blib.util.Position;

import java.awt.*;
import java.util.ArrayList;
public class ParticleEmitter extends Entity{

    public ArrayList<Particle> particles = new ArrayList<Particle>();
    public boolean running = true;
    public long spawnTimer = 0, spawnTime;
    public int spawnAmount = 1;
    public boolean debug = false;
    public boolean worldBased = false;

    // Particle data
    public boolean spawnCircles = false;
    public boolean spawnSquares = true;
    public boolean spawnImages = false;

    public ArrayList<Color> circleColors = new ArrayList<Color>();
    public ArrayList<Color> squareColors = new ArrayList<Color>();
    public ArrayList<ImageIcon> images = new ArrayList<ImageIcon>();
    public ArrayList<AnimImage> animImages = new ArrayList<AnimImage>();

    public boolean fadeOut = false;

    public int spawnType;
    public static final int CIRCLE = 0, CONE = 1, RECT = 2;
    // cone
    public int direction = 0, coneSize = 60;
    // rect
    public Dimension spawnRect = new Dimension(10, 10);

    public double speed = 0.25;
    public long minLife = 700, maxLife = 2000;
    public int size = 10;
    
    public ParticleEmitter(Position pos, long sTime, int sType, boolean world){
        super(pos);
        spawnTime = sTime;
        spawnType = sType;
        squareColors.add(Color.white);
        circleColors.add(Color.white);
        worldBased = world;
    }

    public void start(){
        running = true;
    }
    public void stop(){
        running = false;
    }

    public void render(Graphics g, JPanel panel, int x, int y){
        for(Particle p: particles){
            if(!worldBased) p.render(panel, g, x + (int)p.position.x, y + (int)p.position.y);
            else{
                // get cam position
                Position camPos = new Position(-x + (panel.getWidth() / 2) + position.x, -y + (panel.getHeight() / 2) + position.y);
                // render particle based off cam
                p.render(panel, g, (int)(((p.position.x) - camPos.x) + (panel.getWidth() / 2)), (int)(((p.position.y) - camPos.y) + (panel.getHeight() / 2)));
            }
        }
    }

    public void update(long elapsedTime){
        // update existing particles
        for(int i = 0; i < particles.size(); i++){
            Particle p = particles.get(i);
            p.update(elapsedTime);

            if(p.life <= 0){
                particles.remove(i);
                i--;
            }
        }

        // spawn new particles
        spawnTimer -= elapsedTime;
        if(spawnTimer <= 0){
            spawnTimer = spawnTime;
            if(!running) return;
            ArrayList<String> types = new ArrayList<String>();
            if(spawnCircles) types.add("circles");
            if(spawnSquares) types.add("squares");
            if(spawnImages) types.add("images");
            for(int i = 0; i < spawnAmount; i++){
                if(spawnType == CIRCLE){
                    // pick direction
                    int direction = BTools.randInt(0, 360);
                    Position vector = BTools.angleToVector(Math.toRadians(direction));

                    // pick type
                    String type = types.get(BTools.randInt(0, types.size()));
                    
                    // spawn particle
                    Particle particle = null;
                    Color color = null;
                    switch(type){
                    case "circles":
                        color = circleColors.get(BTools.randInt(0, circleColors.size()));
                        particle = new ShapeParticle(vector, speed, (long)BTools.randInt((int)minLife, (int)maxLife), ShapeParticle.CIRCLE, color, size);
                        break;
                    case "squares":
                        color = squareColors.get(BTools.randInt(0, squareColors.size()));
                        particle = new ShapeParticle(vector, speed, (long)BTools.randInt((int)minLife, (int)maxLife), ShapeParticle.SQUARE, color, size);
                        break;
                    case "images":
                        boolean checkImg = images.size() > 0, checkAnim = animImages.size() > 0;
                        if(checkImg && !checkAnim){
                            ImageIcon image = images.get(BTools.randInt(0, images.size()));
                            particle = new ImageParticle(vector, speed, (long)BTools.randInt((int)minLife, (int)maxLife), image, size);
                        }else if(!checkImg && checkAnim){
                            AnimImage image = animImages.get(BTools.randInt(0, animImages.size())).copy(false);
                            particle = new ImageParticle(vector, speed, (long)BTools.randInt((int)minLife, (int)maxLife), image, size);
                        }
                        
                    }

                    if(worldBased){
                        particle.position.x += position.x;
                        particle.position.y += position.y;
                    }
                    particles.add(particle);
                }
                
                if(spawnType == CONE){
                    // pick direction
                    int dir = BTools.randInt(direction - coneSize / 2, direction + coneSize / 2);
                    Position vector = BTools.angleToVector(Math.toRadians(dir));

                    // pick type
                    String type = types.get(BTools.randInt(0, types.size()));
                    
                    // spawn particle
                    Particle particle = null;
                    Color color = null;
                    switch(type){
                    case "circles":
                        color = circleColors.get(BTools.randInt(0, circleColors.size()));
                        particle = new ShapeParticle(vector, speed, (long)BTools.randInt((int)minLife, (int)maxLife), ShapeParticle.CIRCLE, color, size);
                        break;
                    case "squares":
                        color = squareColors.get(BTools.randInt(0, squareColors.size()));
                        particle = new ShapeParticle(vector, speed, (long)BTools.randInt((int)minLife, (int)maxLife), ShapeParticle.SQUARE, color, size);
                        break;
                    case "images":
                        boolean checkImg = images.size() > 0, checkAnim = animImages.size() > 0;
                        if(checkImg && !checkAnim){
                            ImageIcon image = images.get(BTools.randInt(0, images.size()));
                            particle = new ImageParticle(vector, speed, (long)BTools.randInt((int)minLife, (int)maxLife), image, size);
                        }else if(!checkImg && checkAnim){
                            AnimImage image = animImages.get(BTools.randInt(0, animImages.size())).copy(false);
                            particle = new ImageParticle(vector, speed, (long)BTools.randInt((int)minLife, (int)maxLife), image, size);
                        }      
                    }

                    if(worldBased){
                        particle.position.x += position.x;
                        particle.position.y += position.y;
                    }
                    particles.add(particle);
                }

                if(spawnType == RECT){
                    // select pos
                    Position pos = new Position(BTools.randInt(0, spawnRect.width) - spawnRect.width / 2, BTools.randInt(0, spawnRect.height) - spawnRect.height / 2);

                    // pick type
                    String type = types.get(BTools.randInt(0, types.size()));
                    
                    // spawn particle
                    Particle particle = null;
                    Color color = null;
                    switch(type){
                    case "circles":
                        color = circleColors.get(BTools.randInt(0, circleColors.size()));
                        particle = new ShapeParticle(new Position(), 0, (long)BTools.randInt((int)minLife, (int)maxLife), ShapeParticle.CIRCLE, color, size);
                        break;
                    case "squares":
                        color = squareColors.get(BTools.randInt(0, squareColors.size()));
                        particle = new ShapeParticle(new Position(), 0, (long)BTools.randInt((int)minLife, (int)maxLife), ShapeParticle.SQUARE, color, size);
                        break;
                    case "images":
                        boolean checkImg = images.size() > 0, checkAnim = animImages.size() > 0;
                        if(checkImg && !checkAnim){
                            ImageIcon image = images.get(BTools.randInt(0, images.size()));
                            particle = new ImageParticle(new Position(), 0, (long)BTools.randInt((int)minLife, (int)maxLife), image, size);
                        }else if(!checkImg && checkAnim){
                            AnimImage image = animImages.get(BTools.randInt(0, animImages.size())).copy(false);
                            particle = new ImageParticle(new Position(), 0, (long)BTools.randInt((int)minLife, (int)maxLife), image, size);
                        }
                    }

                    particle.position.x += pos.x;
                    particle.position.y += pos.y;
                    if(worldBased){
                        particle.position.x += position.x;
                        particle.position.y += position.y;
                    }
                    particles.add(particle);
                }
            }
        }
    }






    // **********************************       PARTICLES       **********************************************
    private class Particle extends Entity{
        Position velocity;
        double speed;
        long life;

        public Particle(Position vel, double s, long l){
            super(new Position());
            velocity = vel;
            speed = s;
            life = l;
        }

        public void render(JPanel panel, Graphics g, int x, int y){}

        public final void update(long elapsedTime){
            life -= elapsedTime;
            position.x += velocity.x * speed * elapsedTime;
            position.y += velocity.y * speed * elapsedTime;
            customUpdate(elapsedTime);
        }
        public void customUpdate(long elapsedTime){}
    }

    private class ShapeParticle extends Particle{

        int type;
        static final int CIRCLE = 0, SQUARE = 1;
        Color color;
        int size;

        long startLife = 0;

        public ShapeParticle(Position vel, double s, long l, int t, Color c, int size){
            super(vel, s, l);
            type = t;
            color = c;
            this.size = size;
            if(fadeOut) startLife = l;
        }

        public void render(JPanel panel, Graphics g, int x, int y){
            if(life < 0) return;
            if(type == CIRCLE){
                g.setColor(color);
                if(fadeOut) g.setColor(new Color((color.getRed() / 255f), (color.getGreen() / 255f), (color.getBlue() / 255f), life / (float)startLife * (color.getAlpha() / 255f)));
                g.fillOval(x - size / 2, y - size / 2, size, size);
            }
            if(type == SQUARE){
                g.setColor(color);
                if(fadeOut) g.setColor(new Color((color.getRed() / 255f), (color.getGreen() / 255f), (color.getBlue() / 255f), life / (float)startLife * (color.getAlpha() / 255f)));
                g.fillRect(x - size / 2, y - size / 2, size, size);
            }
        }
    }

    private class ImageParticle extends Particle{

        ImageIcon image = null;
        AnimImage animImage = null;
        int size;

        public ImageParticle(Position vel, double s, long l, ImageIcon img, int size){
            super(vel, s, l);
            img.setImage(img.getImage().getScaledInstance(size, size, Image.SCALE_FAST));
            image = img;
            this.size = size;
        }
        public ImageParticle(Position vel, double s, long l, AnimImage img, int size){
            super(vel, s, l);
            img.resize(size, size);
            animImage = img;
            this.size = size;
        }

        public void render(JPanel panel, Graphics g, int x, int y){
            if(image != null) image.paintIcon(panel, g, x - size / 2, y - size / 2);
            else animImage.paint(panel, g, x - size / 2, y - size / 2);
        }

        public void customUpdate(long elapsedTime){
            if(animImage != null) animImage.update(elapsedTime);
        }
    }
}
