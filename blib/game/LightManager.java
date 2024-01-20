package blib.game;
import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;

import blib.util.BTools;
import com.jhlabs.image.*;

import java.awt.geom.*;
public class LightManager {

    public int defaultLight; // x / 255
    public BoxBlurFilter blur = new BoxBlurFilter();
    public boolean fancyEdges = true;

    public LightManager(int defLight){
        defaultLight = defLight;
    }

    public void render(Camera cam, ArrayList<Entity> renderList, Graphics g){
        render(cam, renderList, g, 0, 0);
    }
    public void render(Camera cam, ArrayList<Entity> renderList, Graphics g, int offX, int offY){
        defaultLight = BTools.clamp(defaultLight, 0, 255); // make sure it stays between 0-255
        // get all entities that are a Light entity
        ArrayList<Light> lights = new ArrayList<Light>();
        for(Entity e: renderList){
            if(e instanceof Light){
                lights.add((Light)e);
            }
        }

        // get graphics
        Dimension dimension = new Dimension(cam.panel.getWidth(), cam.panel.getHeight());
        if(cam.setDimension != null) dimension = cam.setDimension;
        BufferedImage light = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = light.createGraphics();

        // light
        Area area = new Area(new Rectangle2D.Double(0, 0, light.getWidth() + 1, light.getHeight() + 1));
        for(Light l: lights){
            // subtract
            Point screenPos = cam.worldToScreen(l.position);
            area.subtract(new Area(new Ellipse2D.Double(screenPos.x - l.radius + offX, screenPos.y - l.radius + offY, l.radius * 2, l.radius * 2)));
            
        }

        // fancy
        applyRenderHints(g2d);

        g2d.setColor(Color.BLACK);
        g2d.setComposite(AlphaComposite.SrcOver.derive(BTools.flip(defaultLight, 255) / 255f));
        g2d.fill(area);

        for(Light l: lights){
            if(l.color != null){
                Point screenPos = cam.worldToScreen(l.position);
                Area colorArea = new Area(new Ellipse2D.Double(screenPos.x - l.radius + offX, screenPos.y - l.radius + offY, l.radius * 2, l.radius * 2));
                g2d.setColor(l.color);
                g2d.setComposite(AlphaComposite.SrcOver.derive(l.color.getAlpha() / 255f));
                g2d.fill(colorArea);
            }
        }

        g2d.dispose();

        if(fancyEdges){
            blur.filter(light, light);
        }

        g.drawImage(light, 0, 0, cam.panel);
    }
    private void applyRenderHints(Graphics2D g2d){
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }
}
