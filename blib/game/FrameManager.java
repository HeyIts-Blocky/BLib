package blib.game;

import java.awt.image.*;
import java.io.File;
import java.util.Calendar;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class FrameManager { // keeps a frame the same resolution

    private BufferedImage currentFrame = null;
    public final int WIDTH, HEIGHT;
    public Color bgColor = Color.black;
    private boolean takeScreenshot = false;
    private String screenshotPath = "";
    
    public FrameManager(int w, int h){
        WIDTH = w;
        HEIGHT = h;
    }
    public FrameManager(){ // use the resolution i normally use
        WIDTH = 684;
        HEIGHT = 462;
    }

    public Graphics newFrame(){
        currentFrame = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = currentFrame.getGraphics();
        g.setColor(bgColor);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        return g;
    }

    public void fillScreen(Color c){
        Graphics g = currentFrame.getGraphics();
        g.setColor(c);
        g.fillRect(0, 0, WIDTH, HEIGHT);
    }

    public void renderFrame(JPanel panel, Graphics graphics){
        double widthChange = (double)panel.getWidth() / WIDTH;
        double heightChange = (double)panel.getHeight() / HEIGHT;
        if(widthChange > heightChange){
            // wider
            int width = (int)(WIDTH * heightChange);
            int height = (int)(HEIGHT * heightChange);
            graphics.drawImage(currentFrame, panel.getWidth() / 2 - width / 2, 0, width, height, panel);
        }else{
            // taller
            int width = (int)(WIDTH * widthChange);
            int height = (int)(HEIGHT * widthChange);
            graphics.drawImage(currentFrame, panel.getWidth() / 2 - width / 2, 0, width, height, panel);
        }

        if(takeScreenshot){
            try{
                Calendar currentDate = Calendar.getInstance();
                String dateString = "Screenshot " + currentDate.get(Calendar.MONTH) + "-" + currentDate.get(Calendar.DAY_OF_MONTH) + "-" + currentDate.get(Calendar.YEAR) + " " + currentDate.get(Calendar.HOUR_OF_DAY) + "-" + currentDate.get(Calendar.MINUTE) + "-" + currentDate.get(Calendar.SECOND) + "-" + currentDate.get(Calendar.MILLISECOND);
                File file = new File(screenshotPath + "/" + dateString + ".png");
                file.mkdirs();
                file.createNewFile();
                ImageIO.write(currentFrame, "png", file);
            }catch(Exception e){
                e.printStackTrace();
            }
            takeScreenshot = false;
        }
    }

    public void saveScreenshot(String path){
        takeScreenshot = true;
        screenshotPath = path;
    }

    public Point getMousePos(JPanel panel, Point mousePos){ // takes mouse pos based off panel, and makes it based off the actual frame. if the mouse is out of the frame, it will default to (0, 0)
        Rectangle frame;
        double widthChange = (double)panel.getWidth() / WIDTH;
        double heightChange = (double)panel.getHeight() / HEIGHT;
        int width, height;
        double selChange;
        if(widthChange > heightChange){
            // wider
            width = (int)(WIDTH * heightChange);
            height = (int)(HEIGHT * heightChange);
            // graphics.drawImage(currentFrame, panel.getWidth() / 2 - width / 2, 0, width, height, panel);
            frame = new Rectangle(panel.getWidth() / 2 - width / 2, 0, width, height);
            selChange = heightChange;
        }else{
            // taller
            width = (int)(WIDTH * widthChange);
            height = (int)(HEIGHT * widthChange);
            // graphics.drawImage(currentFrame, panel.getWidth() / 2 - width / 2, 0, width, height, panel);
            frame = new Rectangle(panel.getWidth() / 2 - width / 2, 0, width, height);
            selChange = widthChange;
        }

        if(!frame.contains(mousePos)) return new Point();
        
        Point p = new Point();
        p.x = (int)((mousePos.x - (panel.getWidth() / 2 - width / 2)) / selChange);
        p.y = (int)(mousePos.y / selChange);

        return p;
    }

    // Used for applying effects such as bloom to the frame
    public BufferedImage getFrame(){
        return currentFrame;
    }
    public void setFrame(BufferedImage img){
        currentFrame = img;
    }
}
