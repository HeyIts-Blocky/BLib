package blib.util;
import java.awt.*;
public class CircleBar extends ProgressBar{
    
    public CircleBar(Rectangle r, Color c){
        super(r, c);
    }
    public CircleBar(Rectangle r, Color c, Color bg){
        super(r, c, bg);
    }

    public void paintBar(Graphics g, double percent){
        if(percent > 1 || percent < 0){
            percent = BTools.clamp(percent, 0, 1);
            paintBar(g, percent);
            return;
        }

        if(bg != null){
            g.setColor(bg);
            g.fillOval(rect.x, rect.y, rect.width, rect.height);
        }
        
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g.setColor(color);
        int angle = -(int)(percent * 360);
        int size = Math.min(rect.width, rect.height);
        g.fillArc(rect.x, rect.y, size, size, 90, angle);
    }
}
