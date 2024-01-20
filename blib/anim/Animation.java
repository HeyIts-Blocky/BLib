package blib.anim;
import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;
import blib.util.*;
public class Animation { 
    // REQUIREMENTS
    //>Position.java
    //>AnimKeyFrame.java

    public ArrayList<AnimKeyFrame> keyFrames;
    protected String name;
    protected boolean loop;

    public Animation(String filePath) throws FileNotFoundException{
        // load from file
        Scanner reader = new Scanner(new File(filePath + ".anim"));
        name = reader.nextLine();
        loop = reader.nextBoolean();
        keyFrames = new ArrayList<AnimKeyFrame>();
        while(reader.hasNext()){
            double startX = reader.nextDouble();
            double startY = reader.nextDouble();
            double endX = reader.nextDouble();
            double endY = reader.nextDouble();
            long travelTime = reader.nextLong();
            keyFrames.add(new AnimKeyFrame(new Position(startX, startY), new Position(endX, endY), travelTime));
        }
        reader.close();
    }

    public Animation(ArrayList<AnimKeyFrame> kf, String n, boolean l){
        keyFrames = kf;
        name = n;
        loop = l;
    }

    public Position getPos(long t, Position origin){
        if(keyFrames.size() == 0) return origin;
        // get overall time
        if(t <= 0) return new Position(keyFrames.get(0).start.x + origin.x, keyFrames.get(0).start.y + origin.y);
        else if(t >= getLength()) {
            Position pos = keyFrames.get(keyFrames.size() - 1).end.copy();
            pos.x += origin.x;
            pos.y += origin.y;
            return pos;
        }
        //figure out which keyframe to get from
        for(AnimKeyFrame k: keyFrames){
            if(t <= k.travelTime){
                return k.getPosition(t, origin);
            }else{
                t -= k.travelTime;
            }
        }
        return null;
    }

    public Position getOffset(long t){
        return getPos(t, new Position());
    }

    public long getLength(){
        long totalTime = 0;
        for(AnimKeyFrame kf: keyFrames) totalTime += kf.travelTime;
        return totalTime;
    }

    public void save(String filePath){
        File file = new File(filePath + ".anim");
        try {
            file.createNewFile();
        } catch (IOException e) {}
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.println(name);
            writer.println(loop);
            for(AnimKeyFrame k: keyFrames){
                writer.println(k.start.x);
                writer.println(k.start.y);
                writer.println(k.end.x);
                writer.println(k.end.y);
                writer.println(k.travelTime);
            }
            writer.close();
        } catch (FileNotFoundException e) {}
    }
}
