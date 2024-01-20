package blib.util;
import java.awt.*;
public class ProgressBar {
    Rectangle rect;
    Color color, bg;
    
    public ProgressBar(Rectangle r, Color c){
        rect = r;
        color = c;
        bg = null;
    }
    public ProgressBar(Rectangle r, Color c, Color bg){
        rect = r;
        color = c;
        this.bg = bg;
    }

    public void paintBar(Graphics g, double percent){
        if(bg != null){
            g.setColor(bg);
            g.fillRect(rect.x, rect.y, rect.width, rect.height);
        }
        g.setColor(color);
        g.fillRect(rect.x, rect.y, (int)(rect.width * percent), rect.height);
    }
    public final void paintBar(Graphics g, double percent, int x, int y){
        rect.x = x;
        rect.y = y;
        paintBar(g, percent);
    }
}
