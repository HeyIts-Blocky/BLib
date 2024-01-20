package blib.anim;
import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import blib.util.Event;
import blib.util.Position;
public class Sequence {
    
    // commands
    private static int WAIT = 0, TP = 1, GOTO = 2, PLAYANIM = 3, TEXTBOX = 4, RUNSCRIPT = 5, STOPANIM = 6;
    private ArrayList<Command> commands = new ArrayList<Command>();

    // actors & animators
    public Position[] actors;
    public Animator[] animators;
    private Event scripts;
    private AnimKeyFrame actorMover = new AnimKeyFrame(new Position(), new Position(), 1000);

    // runtime
    private long timer = 0;
    private int currentCmd = 0;
    public boolean running = false;
    public String title = "", text = "";

    // constructor
    public Sequence(String filePath, int numActors, Animator[] anims, Event scripts){
        actors = new Position[numActors];
        for(int i = 0; i < actors.length; i++){
            actors[i] = new Position();
        }
        animators = anims;
        this.scripts = scripts;

        try{
            File file = new File(filePath);
            Scanner reader = new Scanner(file);

            while(reader.hasNext()){
                Command command = null;
                String line = reader.nextLine();
                switch(line){
                case "wait":
                    int waitTime = reader.nextInt();
                    reader.nextLine();
                    command = new Command(waitTime);
                    break;
                case "tp":
                    int actor = reader.nextInt();
                    reader.nextLine();
                    int x = reader.nextInt(), y = reader.nextInt();
                    reader.nextLine();
                    command = new Command(actor, new Position(x, y));
                    break;
                case "goto":
                    int actor2 = reader.nextInt();
                    reader.nextLine();
                    int x2 = reader.nextInt(), y2 = reader.nextInt();
                    reader.nextLine();
                    int travelTime = reader.nextInt();
                    reader.nextLine();
                    command = new Command(actor2, new Position(x2, y2), travelTime);
                    break;
                case "playanim":
                    int animator = reader.nextInt();
                    reader.nextLine();
                    String animation = reader.nextLine();
                    command = new Command(animator, animation);
                    break;
                case "textbox":
                    String title = reader.nextLine();
                    String text = reader.nextLine();
                    command = new Command(title, text);
                    break;
                case "runscript":
                    int script = reader.nextInt();
                    reader.nextLine();
                    command = new Command(script, 0);
                    break;
                case "stopanim":
                    int anim = reader.nextInt();
                    reader.nextLine();
                    command = new Command(anim, 0, 0);
                    break;
                }

                if(command != null) commands.add(command);
            }
            reader.close();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Something went wrong while loading Sequence!", "Sequence error!", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void update(long elapsedTime){
        if(!running) return;
        Command cmd = commands.get(currentCmd);
        if(cmd.type == WAIT){
            timer += elapsedTime;
            if(timer >= cmd.data) next();
        }
        if(cmd.type == GOTO){
            timer += elapsedTime;
            actors[cmd.data].x = actorMover.getOffset(timer).x;
            actors[cmd.data].y = actorMover.getOffset(timer).y;
            if(timer >= cmd.data2) next();
        }
    }

    public void next(){
        if(!running) return;
        timer = 0;
        currentCmd++;
        if(currentCmd >= commands.size()){
            running = false;
            return;
        }

        Command cmd = commands.get(currentCmd);
        if(cmd.type == GOTO){
            actorMover = new AnimKeyFrame(actors[cmd.data].copy(), cmd.pos.copy(), cmd.data2);
        }
        if(cmd.type == TP){
            actors[cmd.data].x = cmd.pos.x;
            actors[cmd.data].y = cmd.pos.y;
            next();
        }
        if(cmd.type == PLAYANIM){
            animators[cmd.data].play(cmd.str);
            next();
        }
        if(cmd.type == STOPANIM){
            animators[cmd.data].stop();
            next();
        }
        if(cmd.type == TEXTBOX){
            title = cmd.str;
            text = cmd.str2;
        }
        if(cmd.type == RUNSCRIPT){
            scripts.event(cmd.data);
            next();
        }
    }

    public boolean textBox(){
        if(!running) return false;
        return commands.get(currentCmd).type == TEXTBOX;
    }

    public void start(){
        running = true;
        currentCmd = -1;
        next();
    }


    private class Command {
        int type = 0;
        int data = 0, data2 = 0;
        Position pos = new Position();
        String str = "", str2 = "";

        public Command(int waitTime){// wait
            type = WAIT;
            data = waitTime;
        }
        public Command(int actor, Position p){// tp
            type = TP;
            data = actor;
            pos = p;
        }
        public Command(int actor, Position p, int travelTime){// goto
            type = GOTO;
            data = actor;
            pos = p;
            data2 = travelTime;
        }
        public Command(int animator, String animationName){// playanim
            type = PLAYANIM;
            data = animator;
            str = animationName;
        }
        public Command(String title, String text){// textbox
            type = TEXTBOX;
            str = title;
            str2 = text;
        }
        public Command(int script, int a){// runscript
            type = RUNSCRIPT;
            data = script;
        }
        public Command(int anim, int a, int b){// stopanim
            type = STOPANIM;
            data = anim;
        }
    }
}
