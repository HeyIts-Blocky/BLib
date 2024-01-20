package blib.util;
public class Time {
    
    boolean hoursEnabled = false;
    int minutes, seconds;
    
    public Time(int min, int sec){
        minutes = min;
        seconds = sec;
    }
    public Time(long ms, boolean isMicro){
        int sec = (int)(ms / 1000);
        if(isMicro) sec = (int)(ms / 1000000);
        int min = sec / 60;
        sec %= 60;
        minutes = min;
        seconds = sec;
    }

    public static String format(Time t){
        if(!t.hoursEnabled || t.minutes < 60){
            String str = t.minutes + ":";
            if(t.seconds < 10) str += "0";
            str += t.seconds;
            return str;
        }else{
            int hours = t.minutes / 60;
            t.minutes %= 60;
            String str = hours + ":";
            if(t.minutes < 10) str += "0";
            str += t.minutes + ":";
            if(t.seconds < 10) str += "0";
            str += t.seconds;
            return str;
        }
        
    }
    public String format(){
        return format(this);
    }

}