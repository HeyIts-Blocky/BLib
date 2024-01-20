package blib.input;
import javax.swing.*;
import java.awt.*;
public class InputAdapter extends KeyManager{ // only serves as an extended KeyManager that can run custom code on a key or mouse press when extended itself
    public InputAdapter(JPanel panel){
        super(panel);
    }

    public void onKeyPressed(int key){} // write over

    public void onMousePressed(int mb, Point mousePos){} // write over

    public void onScroll(int scroll){} // write over
}
