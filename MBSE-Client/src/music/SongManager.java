package music;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import connection.ConnectionManager;
import options.Option;
import utils.TXT;

public class SongManager {

	static Option songlocation;
	static ArrayList<Song> songs = new ArrayList<Song>();
	static Song currentSong = new Song("NOT PLAYING");
	static Timer timer;
	static TimerTask getRatingsFromServer;
	public static boolean songMode = false;
	static String currUser = " ";

	public static Song getCurrentSong() {
		return currentSong;
	}

	public static boolean updateCurrentSong() {
		List<String> lines = null;
		try {
			lines = TXT.readFromFile(songlocation.getValue());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Error Reading current song file. Please consider checking your options.");
			JOptionPane.showMessageDialog(null, "Could not read Current Song file. Though this is not critical, please consider updating it in the options.txt file and restarting the program.", "ERROR", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		String realCurrentSong = lines.get(0);
		System.out.println("Currently playing song: "+realCurrentSong);
		if (songMode) {
			realCurrentSong = "Listening to "+currUser+"'s Song Request!";
		}
		if (realCurrentSong.equals(currentSong.name)) {
			try {
				System.out.println("Same song still playing.");
				TXT.writeToFile("currentsong.txt", currentSong.toFancyString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.err.println("Error writing currentsong.txt - Please run as admin!");
				JOptionPane.showMessageDialog(null, "Could not write to currentsong.txt - Please restart the Program with administrative priviliges.\n Program will now exit.", "ERROR", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
				
			}
			return false;
		}
		
		if (!isOnList(realCurrentSong)) {
			System.out.println("Song was not on list.");
			songs.add(new Song(realCurrentSong, new ArrayList<Integer>()));
		}
		
		for (Song s:songs) {
			if (s.name.equals(realCurrentSong)) {
				currentSong = s;
				break;
			}
		}
		System.out.println("Writing to current song file...");
		try {
			TXT.writeToFile("currentsong.txt", currentSong.toFancyString());
			ConnectionManager.newSong(currentSong.toFancyString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Error writing currentsong.txt - Please run as admin!");
			JOptionPane.showMessageDialog(null, "Could not write to currentsong.txt - Please restart the Program with administrative priviliges.\n Program will now exit.", "ERROR", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
			
		}
		System.out.println("Done refreshing.");
		return true;
		
		
		
	}

	public static boolean isOnList(String songname) {
		for (Song s : songs) {
			if (s.name.equals(songname)) {
				return true;
			}

		}
		return false;
	}

	public static void initialize(Option songslocation) {
		System.out.println("Initializing SongManager...");
		songlocation = songslocation;
		File file = new File("songs.txt");

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		List<String> lines = null;
		try {
			lines = TXT.readFromFile(file.getAbsolutePath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (String line : lines) {
			String[] parts = line.split("#");
			ArrayList<Integer> ratings = new ArrayList<Integer>();
			for (String p : parts) {
				if (p.equals(parts[0]))
					continue;
				ratings.add(Integer.parseInt(p));
			}

			songs.add(new Song(parts[0], ratings));
		}
		
		timer = new Timer();
		getRatingsFromServer = new TimerTask() {

			@Override
			public void run() {
				ArrayList<Integer> list = ConnectionManager.getRatings();
				for (int i:list) {
					rateCurrentSong(i);
				}
				updateCurrentSong();
				rewrite();
				
			}
			
		};
		timer.schedule(getRatingsFromServer, 10000, 10000);
		System.out.println("Timer scheduled.");
		updateCurrentSong();
		System.out.println("Updated Current song.");
		rewrite();
		System.out.println("Done loading SongManager.");
		
	}

	public static void rewrite() {
		ArrayList<String> lines = new ArrayList<String>();

		for (Song s : songs) {
			lines.add(s.toString());
		}
		
		try {
			TXT.writeToFile("songs.txt", lines);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void rateCurrentSong(int rating) {
		currentSong.rate(rating);
		updateCurrentSong();
	}
	
	public static void setUser(String user) {
		currUser = user;
		ConnectionManager.setUser(user);
	}
	
	public static void toggleSongMode() {
		songMode = ConnectionManager.toggleSongMode();
	}
	
}
