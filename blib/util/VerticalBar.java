package blib.util;
import java.awt.*;
public class VerticalBar extends ProgressBar{
    
    public VerticalBar(Rectangle r, Color c){
        super(r, c);
    }
    public VerticalBar(Rectangle r, Color c, Color bg){
        super(r, c, bg);
    }

    public void paintBar(Graphics g, double percent){
        if(bg != null){
            g.setColor(bg);
            g.fillRect(rect.x, rect.y, rect.width, rect.height);
        }
        g.setColor(color);
        g.fillRect(rect.x, rect.y + (int)(rect.height * percent), rect.width, (int)(rect.height * percent));
    }
}
