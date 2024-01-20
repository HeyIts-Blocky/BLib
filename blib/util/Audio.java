package blib.util;
import java.io.*;
import javax.sound.sampled.*;
public class Audio {

	public static boolean enabled = true;
	public static FloatControl fc;
	public static int volume = 75; // 0 - 100 (%)

	public static Clip play(String FileName) {
		if(volume == 0) return null;
		volume = BTools.clamp(volume, 0, 100);
		if(!enabled) return null;
		try{
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(FileName).getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
			fc.setValue((float)volume / 2 - 45f);
			clip.start();
			return clip;
		}catch(Exception e){}
		return null;
	}

	public static Clip play(String FileName, double vol) {
		if(volume == 0) return null;
		volume = BTools.clamp(volume, 0, 100);
		if(!enabled) return null;
		try{
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(FileName).getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
			fc.setValue((float)(volume / 2 * vol) - 45f);
			clip.start();
			return clip;
		}catch(Exception e){}
		return null;
	}

}
