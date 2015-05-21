package main;

import gui.MainWindow;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

import connection.ConnectionManager;

public class Starter {

	static String channelname = "";
	static String channel = "";

	public static void main(String[] args) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		channelname = JOptionPane
				.showInputDialog("Please input your channel's name!");
		channel = "#" + channelname;
		JWindow window = new JWindow();
		window.getContentPane().add(
				new JLabel("Connecting to MarenBot Server...",
						SwingConstants.CENTER));
		window.setBounds((int) ((width / 2) - 150), (int) ((height / 2) - 100),
				300, 200);
		window.setVisible(true);
		try {
			Thread.sleep(500);
			ConnectionManager.connect(channelname);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		window.setVisible(false);
		@SuppressWarnings("unused")
		JFrame frame = new MainWindow();
		window.dispose();

	}

}
