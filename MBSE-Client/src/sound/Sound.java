package sound;

import java.io.*;


import javax.sound.sampled.*;



public class Sound {

	public static void playFile(File file) throws Exception {
		
		AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
		
		AudioFormat format = audioStream.getFormat();
		
		DataLine.Info info = new DataLine.Info(Clip.class, format);
		
		Clip audioClip = (Clip) AudioSystem.getLine(info);
		
		audioClip.open(audioStream);
		
		audioClip.start();
		Thread.sleep(100);
		while (audioClip.isActive()) {
			System.out.println("File still playing. Waiting...");
			Thread.sleep(100);
		}
		audioClip.close();
		audioStream.close();
		
	}
	
}
