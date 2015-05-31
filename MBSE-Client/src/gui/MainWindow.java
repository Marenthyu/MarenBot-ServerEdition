package gui;

import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import java.awt.Toolkit;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import connection.ConnectionManager;

import javax.swing.JTextField;

import sound.Sound;
import music.SongManager;

public class MainWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7497833988556627886L;
	JButton joinleave = new JButton(), togglebets, wongame, btnLostGame,
			btnRefresh, copysr, delsr, btnAddCommand, btnRemoveCommand;
	JLabel lblBotStatus, botstatus = new JLabel("STATUS"), lblBetsStatus,
			betstatus = new JLabel("STATUS");
	private JLabel lblRequestee;
	private JLabel requestee;
	private JLabel lblLink;
	private JTextField link;
	private Timer timer;
	private TimerTask refresher;
	private JButton btnCopyAndDelete;

	public MainWindow() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		timer = new Timer();
		refresher = new TimerTask() {
			public void run() {
				refresh();
			}
		};
		timer.schedule(refresher, 0, 10000);
		setTitle("MarenBot SE Client");
		setSize(433, 560);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		joinleave = new JButton("Join");
		joinleave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				joinleave();
			}
		});
		joinleave.setBounds(10, 241, 89, 82);
		getContentPane().add(joinleave);
		togglebets = new JButton("Toggle Bets");
		togglebets.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				togglebets();
			}
		});
		togglebets.setBounds(163, 334, 150, 82);
		getContentPane().add(togglebets);
		wongame = new JButton("Won Game");
		wongame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConnectionManager.wonGame();
			}
		});
		wongame.setBounds(323, 334, 85, 82);
		getContentPane().add(wongame);
		btnLostGame = new JButton("Lost Game");
		btnLostGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConnectionManager.lostGame();
			}
		});
		btnLostGame.setBounds(323, 241, 85, 82);
		getContentPane().add(btnLostGame);
		lblBotStatus = new JLabel("Bot Status: ");
		lblBotStatus.setBounds(10, 11, 71, 14);
		getContentPane().add(lblBotStatus);
		botstatus = new JLabel("STATUS");
		botstatus.setBounds(70, 11, 83, 14);
		getContentPane().add(botstatus);
		lblBetsStatus = new JLabel("Bets Status:");
		lblBetsStatus.setBounds(10, 36, 71, 14);
		getContentPane().add(lblBetsStatus);
		betstatus = new JLabel("STATUS");
		betstatus.setBounds(70, 36, 83, 14);
		getContentPane().add(betstatus);
		btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refresh();
			}
		});
		btnRefresh.setBounds(10, 148, 89, 82);
		getContentPane().add(btnRefresh);
		copysr = new JButton("Copy SongRequest");
		copysr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				copySongRequest();
			}
		});
		copysr.setBounds(109, 148, 155, 82);
		getContentPane().add(copysr);
		delsr = new JButton("Delete SongRequest");
		delsr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteSongRequest();
			}
		});
		delsr.setBounds(274, 148, 134, 82);
		getContentPane().add(delsr);
		btnAddCommand = new JButton("Add Command");
		btnAddCommand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AddCommWindow();
			}
		});
		btnAddCommand.setBounds(163, 7, 101, 130);
		getContentPane().add(btnAddCommand);
		btnRemoveCommand = new JButton("Remove Command");
		btnRemoveCommand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				delcomm(JOptionPane
						.showInputDialog("Please enter the command to remove!"));
			}
		});
		btnRemoveCommand.setBounds(274, 7, 130, 130);
		getContentPane().add(btnRemoveCommand);
		lblRequestee = new JLabel("Requestee:");
		lblRequestee.setBounds(10, 61, 56, 14);
		getContentPane().add(lblRequestee);
		requestee = new JLabel("NaN");
		requestee.setBounds(70, 61, 95, 14);
		getContentPane().add(requestee);
		lblLink = new JLabel("Link:");
		lblLink.setBounds(10, 86, 27, 14);
		getContentPane().add(lblLink);
		link = new JTextField();
		link.setEditable(false);
		link.setText("NaN");
		link.setBounds(10, 109, 127, 25);
		getContentPane().add(link);
		link.setColumns(10);

		JButton btnToggleSong = new JButton("Toggle Song");
		btnToggleSong.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SongManager.toggleSongMode();
			}
		});
		btnToggleSong.setBounds(10, 334, 140, 82);
		getContentPane().add(btnToggleSong);

		btnCopyAndDelete = new JButton("Delete and Copy SongRequest");
		btnCopyAndDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteSongRequest();
				copySongRequest();
			}
		});
		btnCopyAndDelete.setBounds(109, 241, 204, 82);
		getContentPane().add(btnCopyAndDelete);

		JButton sound1 = new JButton("Sound 1");
		sound1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sound1();
			}
		});
		sound1.setBounds(10, 427, 89, 82);
		getContentPane().add(sound1);

		JButton sound2 = new JButton("Sound 2");
		sound2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sound2();
			}
		});
		sound2.setBounds(109, 427, 89, 82);
		getContentPane().add(sound2);

		JButton sound3 = new JButton("Sound 3");
		sound3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sound3();
			}
		});
		sound3.setBounds(208, 427, 89, 82);
		getContentPane().add(sound3);

		JButton sound4 = new JButton("Sound 4");
		sound4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sound4();
			}
		});
		sound4.setBounds(307, 427, 101, 82);
		getContentPane().add(sound4);

		refresh();
	}

	protected void sound1() {

		File file = new File("sound1.wav");
		if (file.exists()) {
			try {
				System.out.println("WAV file found.");
				Sound.playFile(file);
				return;
			} catch (Exception e) {
				System.err.println("Couldn't play file. Odd.");
				e.printStackTrace();
				JOptionPane
						.showMessageDialog(null,
								"Couldn't play file, but it exists. Sorry.",
								"ERROR: Couldn't play file.",
								JOptionPane.ERROR_MESSAGE);
			}
		}
		file = new File("sound1.mp3");
		if (file.exists()) {
			try {
				System.out.println("MP3 file found.");
				JOptionPane
						.showMessageDialog(
								null,
								"Sorry, we are currently only supporting .wav files, but you gave an .mp3 file. Please convert it to .wav.");
				return;
			} catch (Exception e) {
			}
		}

	}

	protected void sound2() {
		File file = new File("sound2.wav");
		if (file.exists()) {
			try {
				System.out.println("WAV file found.");
				Sound.playFile(file);
				return;
			} catch (Exception e) {
				System.err.println("Couldn't play file. Odd.");
				e.printStackTrace();
				JOptionPane
						.showMessageDialog(null,
								"Couldn't play file, but it exists. Sorry.",
								"ERROR: Couldn't play file.",
								JOptionPane.ERROR_MESSAGE);
			}
		}
		file = new File("sound2.mp3");
		if (file.exists()) {
			try {
				System.out.println("MP3 file found.");
				JOptionPane
						.showMessageDialog(
								null,
								"Sorry, we are currently only supporting .wav files, but you gave an .mp3 file. Please convert it to .wav.");
				return;
			} catch (Exception e) {
				System.err.println("Couldn't play file. Odd.");
				e.printStackTrace();
				JOptionPane
						.showMessageDialog(null,
								"Couldn't play file, but it exists. Sorry.",
								"ERROR: Couldn't play file.",
								JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	protected void sound3() {
		File file = new File("sound3.wav");
		if (file.exists()) {
			try {
				System.out.println("WAV file found.");
				Sound.playFile(file);
				return;
			} catch (Exception e) {
				System.err.println("Couldn't play file. Odd.");
				e.printStackTrace();
				JOptionPane
						.showMessageDialog(null,
								"Couldn't play file, but it exists. Sorry.",
								"ERROR: Couldn't play file.",
								JOptionPane.ERROR_MESSAGE);
			}
		}
		file = new File("sound3.mp3");
		if (file.exists()) {
			try {
				System.out.println("MP3 file found.");
				JOptionPane
						.showMessageDialog(
								null,
								"Sorry, we are currently only supporting .wav files, but you gave an .mp3 file. Please convert it to .wav.");
				return;
			} catch (Exception e) {
				System.err.println("Couldn't play file. Odd.");
				e.printStackTrace();
				JOptionPane
						.showMessageDialog(null,
								"Couldn't play file, but it exists. Sorry.",
								"ERROR: Couldn't play file.",
								JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	protected void sound4() {
		File file = new File("sound4.wav");
		if (file.exists()) {
			try {
				System.out.println("WAV file found.");
				Sound.playFile(file);
				return;
			} catch (Exception e) {
				System.err.println("Couldn't play file. Odd.");
				e.printStackTrace();
				JOptionPane
						.showMessageDialog(null,
								"Couldn't play file, but it exists. Sorry.",
								"ERROR: Couldn't play file.",
								JOptionPane.ERROR_MESSAGE);
			}
		}
		file = new File("sound4.mp3");
		if (file.exists()) {
			try {
				System.out.println("MP3 file found.");
				JOptionPane
						.showMessageDialog(
								null,
								"Sorry, we are currently only supporting .wav files, but you gave an .mp3 file. Please convert it to .wav.");
				return;
			} catch (Exception e) {
				System.err.println("Couldn't play file. Odd.");
				e.printStackTrace();
				JOptionPane
						.showMessageDialog(null,
								"Couldn't play file, but it exists. Sorry.",
								"ERROR: Couldn't play file.",
								JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	protected void delcomm(String name) {
		File file = new File("delcomm.wav");
		if (file.exists()) {
			try {
				Sound.playFile(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (ConnectionManager.delcomm(name)) {
			JOptionPane.showMessageDialog(null, "Command " + name
					+ " successfully removed");
		} else {
			JOptionPane.showMessageDialog(null, "Could not remove " + name,
					"ERROR", JOptionPane.ERROR_MESSAGE);
		}

	}

	protected void joinleave() {
		switch (botstatus.getText()) {
		case "Not Connected.": {
			ConnectionManager.join();
			refresh();
			File file = new File("join.wav");
			if (file.exists()) {
				try {
					Sound.playFile(file);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return;
		}
		case "Connected.": {
			ConnectionManager.leave();
			refresh();
			File file = new File("leave.wav");
			if (file.exists()) {
				try {
					Sound.playFile(file);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return;
		}
		}
	}

	protected void deleteSongRequest() {
		File file = new File("deletesongrequest.wav");
		if (file.exists()) {
			try {
				Sound.playFile(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ConnectionManager.delOldestSongRequest();
	}

	protected void copySongRequest() {
		File file = new File("copysongrequest.wav");
		if (file.exists()) {
			try {
				Sound.playFile(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String[] s = ConnectionManager.getOldestSongrequest();
		Toolkit.getDefaultToolkit().getSystemClipboard()
				.setContents(new StringSelection(s[1]), null);
		requestee.setText(s[0]);
		link.setText(s[1]);
		SongManager.setUser(s[0]);
		if (s[0].equals("NO REQUESTS") && SongManager.songMode) {
			SongManager.toggleSongMode();
		}
	}

	protected void togglebets() {
		System.out.println("toggling bets...");
		File file = new File("togglebets.wav");
		if (file.exists()) {
			try {
				Sound.playFile(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ConnectionManager.togglebets();
		switch (betstatus.getText()) {
		case "Open": {
			betstatus.setText("Closed");
			break;
		}
		case "Closed": {
			betstatus.setText("Open");
			break;
		}
		default: {
			break;
		}
		}
	}

	protected void refresh() {
		File file = new File("refresh.wav");
		if (file.exists()) {
			try {
				Sound.playFile(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("Refreshing...");
		betstatus.setText("refreshing...");
		botstatus.setText("refreshing...");
		if (ConnectionManager.status()) {
			botstatus.setText("Connected.");
			joinleave.setText("Leave");
			if (ConnectionManager.betstatus()) {
				betstatus.setText("Open");
			} else {
				betstatus.setText("Closed");
			}
		} else {
			botstatus.setText("Not Connected.");
			joinleave.setText("Join");
			betstatus.setText("Not Connected.");
		}
	}
}
