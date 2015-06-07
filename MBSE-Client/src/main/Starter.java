package main;

import gui.main.MainWindow;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

import music.SongManager;
import options.Option;
import connection.ConnectionManager;

public class Starter {

	static String channelname = "";
	static String channel = "";
	static Option songlocation;
	

	public static void main(String[] args) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		channelname = JOptionPane
				.showInputDialog("Please input your channel's name!");
		channel = "#" + channelname;
		JWindow window = new JWindow();
		JLabel label = new JLabel("Connecting to MarenBot Server...",
				SwingConstants.CENTER);
		window.getContentPane().add(label);
		window.setBounds((int) ((width / 2) - 150), (int) ((height / 2) - 100),
				300, 200);
		window.setVisible(true);
		try {
			Thread.sleep(500);
			ConnectionManager.connect(channelname);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		label.setText("Connected. Loading Options...");
		
		songlocation = new Option("songlocation");
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (songlocation.getValue().equals(" ")) {
			songlocation.set(JOptionPane.showInputDialog("Please enter the location of the file reading your current song. (type DISABLE to disable this functionality.)", "C:\\foo\\bar\\currentsong.txt"));
		}
		
		if (!songlocation.getValue().equals("DISABLE")) {
			label.setText("Loading Song Manager...");
			SongManager.initialize(songlocation);
		}
		
		JFrame frame = new MainWindow();
		
		

		frame.setVisible(true);
		window.setVisible(false);
		window.dispose();

	}
}
