package blib.util;
import java.awt.event.*;
import javax.swing.*;
public class Server {

    private Timer timer = new Timer(1, new ServerListener());
    private long elapsedTime, previousStartTime = -1;
    private ActionListener listener;
    private JPanel panel;
    
    public Server(ActionListener a){
        listener = a;
        timer.start();
    }

    public Server(ActionListener a, JPanel p){
        panel = p;
        listener = a;
        timer.start();
    }

    public long getElapsedTime(){
        return elapsedTime;
    }

    private class ServerListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            long now = System.currentTimeMillis();
			elapsedTime = previousStartTime != -1 ? now - previousStartTime : 0;
			previousStartTime = now;
            listener.actionPerformed(e);
            if(panel != null) panel.repaint();
        }
    }
}
