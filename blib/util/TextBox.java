package blib.util;
import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;
public class TextBox {

    public String text;
    public int alignment;
    public static final int LEFT = 0, CENTER = 1, RIGHT = 2;
    public static final int NOMAXWIDTH = -1;
    private Font font;
    public boolean debug = false;

    public TextBox(String text, int alignment, Font font){
        this.text = text;
        this.alignment = alignment;
        this.font = font;
    }

    public void draw(Graphics g, int x, int y, int maxWidth){
        g.setFont(font);
        ArrayList<String> lines = new ArrayList<String>();
        String newString = "";
        boolean newLine = false;
        for(int i = 0; i < text.length(); i++){ // check for newline characters
            if(text.charAt(i) == '\\'){
                i++;
                if(i >= text.length()){
                    newString += text.charAt(i - 1);
                    lines.add(newString);
                    newString = "";
                    break;
                }
                if(text.charAt(i) == 'n'){
                    newLine = true;
                }
            }
            if(!newLine) newString += text.charAt(i);
            if((text.charAt(i) == '\n' || i == text.length() - 1 || newLine)){
                lines.add(newString);
                newString = "";
                newLine = false;
            }
            
        }
        if(maxWidth != NOMAXWIDTH){
            ArrayList<String> newLines = new ArrayList<String>();
            for(int i = 0; i < lines.size(); i++){
                Scanner reader = new Scanner(lines.get(i));
                while(reader.hasNext()){
                    newString += reader.next() + " ";
                    if(g.getFontMetrics().stringWidth(newString) >= maxWidth){
                        newLines.add(newString);
                        newString = "";
                    }
                }
                reader.close();
            }
            newLines.add(newString);
            lines = newLines;
        }
        

        for(int i = 0; i < lines.size(); i++){
            if(alignment == LEFT){
                g.drawString(lines.get(i), x, y + font.getSize() / 2 + font.getSize() * i);
            }
            if(alignment == CENTER){
                int textWidth = g.getFontMetrics().stringWidth(lines.get(i));
                g.drawString(lines.get(i), x - textWidth / 2, y + font.getSize() / 2 + font.getSize() * i);
            }
            if(alignment == RIGHT){
                int textWidth = g.getFontMetrics().stringWidth(lines.get(i));
                g.drawString(lines.get(i), x - textWidth, y + font.getSize() / 2 + font.getSize() * i);
            }
        }

        if(debug){
            g.setColor(Color.red);
            if(alignment == LEFT){
                g.drawRect(x, y - font.getSize() / 2, maxWidth, font.getSize());
            }
            if(alignment == CENTER){
                g.drawRect(x - maxWidth / 2, y - font.getSize() / 2, maxWidth, font.getSize());
            }
            if(alignment == RIGHT){
                g.drawRect(x - maxWidth, y - font.getSize() / 2, g.getFontMetrics().stringWidth(text), font.getSize());
            }
        }
    }

    public static void draw(String text, Graphics g, int x, int y){
        draw(text, g, x, y, LEFT);
    }
    public static void draw(String text, Graphics g, int x, int y, int alignment){
        draw(text, g, x, y, alignment, NOMAXWIDTH);
    }
    public static void draw(String text, Graphics g, int x, int y, int alignment, int maxWidth){
        TextBox textBox = new TextBox(text, alignment, g.getFont());
        textBox.draw(g, x, y, maxWidth);
    }
}
