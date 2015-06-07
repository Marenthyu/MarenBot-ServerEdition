package gui.main;

import gui.commands.AddCommWindow;
import gui.commands.EditCommandWindow;
import gui.messages.EditMessageWindow;
import gui.visualdisplay.VisualDisplayHUB;

import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import java.awt.Toolkit;
import java.io.File;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import connection.ConnectionManager;

import javax.swing.JTextField;

import sound.Sound;
import music.SongManager;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

public class MainWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7497833988556627886L;
	JButton joinleave = new JButton(), togglebets, wongame, btnLostGame,
			btnRefresh, copysr, delsr, btnAddCommand;
	JLabel lblBotStatus, botstatus = new JLabel("STATUS"), lblBetsStatus,
			betstatus = new JLabel("STATUS");
	private JLabel lblRequestee;
	private JLabel requestee;
	private JLabel lblLink;
	private JTextField link;
	private Timer timer;
	private TimerTask refresher;
	private JButton btnCopyAndDelete;
	private JButton btnEditMessage;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JToggleButton tglbtnShow;
	private JPanel soundPanel;
	private JToggleButton toggleButton;
	private JPanel bettingPanel;
	private JLabel lblSounds_1;
	private JSeparator separator;
	private JPanel requestsPanel;
	private JToggleButton toggleButton_1;
	private JSeparator separator_2;
	private JLabel lblSongRequests;
	private JLabel lblBetting;
	private JSeparator separator_1;
	

	public MainWindow() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e1) {
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
		setSize(433, 285);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		joinleave = new JButton("Join");
		joinleave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				joinleave();
			}
		});
		
		tglbtnShow = new JToggleButton("Show");
		tglbtnShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toggleSongs();
			}
		});
		
		lblSounds_1 = new JLabel("Sounds");
		lblSounds_1.setBounds(7, 201, 43, 14);
		getContentPane().add(lblSounds_1);
		tglbtnShow.setBounds(348, 196, 58, 23);
		getContentPane().add(tglbtnShow);
		
		soundPanel = new JPanel();
		soundPanel.setBounds(9, 219, 397, 104);
		soundPanel.setVisible(false);
		getContentPane().add(soundPanel);
		soundPanel.setLayout(null);
		
				JButton sound1 = new JButton("Sound 1");
				sound1.setBounds(8, 10, 89, 82);
				soundPanel.add(sound1);
				
						JButton sound2 = new JButton("Sound 2");
						sound2.setBounds(105, 10, 89, 82);
						soundPanel.add(sound2);
						
								JButton sound3 = new JButton("Sound 3");
								sound3.setBounds(202, 10, 89, 82);
								soundPanel.add(sound3);
								
										JButton sound4 = new JButton("Sound 4");
										sound4.setBounds(299, 10, 89, 82);
										soundPanel.add(sound4);
										sound4.addActionListener(new ActionListener() {
											public void actionPerformed(ActionEvent e) {
												sound4();
											}
										});
										sound3.addActionListener(new ActionListener() {
											public void actionPerformed(ActionEvent e) {
												sound3();
											}
										});
										sound2.addActionListener(new ActionListener() {
											public void actionPerformed(ActionEvent e) {
												sound2();
											}
										});
										sound1.addActionListener(new ActionListener() {
											public void actionPerformed(ActionEvent e) {
												sound1();
											}
										});
		
		separator = new JSeparator();
		separator.setBounds(53, 207, 285, 2);
		getContentPane().add(separator);
		joinleave.setBounds(17, 78, 89, 59);
		getContentPane().add(joinleave);
		lblBotStatus = new JLabel("Bot Status: ");
		lblBotStatus.setBounds(17, 11, 59, 14);
		getContentPane().add(lblBotStatus);
		botstatus = new JLabel("STATUS");
		botstatus.setBounds(17, 32, 83, 14);
		getContentPane().add(botstatus);
		btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refresh();
			}
		});
		btnRefresh.setBounds(123, 6, 130, 59);
		getContentPane().add(btnRefresh);
		btnAddCommand = new JButton("Add Command");
		buttonGroup.add(btnAddCommand);
		btnAddCommand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AddCommWindow();
			}
		});
		btnAddCommand.setBounds(123, 78, 130, 59);
		getContentPane().add(btnAddCommand);

		JButton btnEditCommand = new JButton("Edit Command");
		buttonGroup.add(btnEditCommand);
		btnEditCommand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new EditCommandWindow();
			}
		});
		btnEditCommand.setBounds(270, 78, 130, 59);
		getContentPane().add(btnEditCommand);

		btnEditMessage = new JButton("Edit Message");
		buttonGroup.add(btnEditMessage);
		btnEditMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new EditMessageWindow();
			}
		});
		btnEditMessage.setBounds(270, 6, 130, 60);
		getContentPane().add(btnEditMessage);
										
										lblBetting = new JLabel("Betting");
										lblBetting.setBounds(9, 176, 43, 14);
										getContentPane().add(lblBetting);
										
										separator_1 = new JSeparator();
										separator_1.setBounds(53, 182, 285, 2);
										getContentPane().add(separator_1);
										
										toggleButton = new JToggleButton("Show");
										toggleButton.addActionListener(new ActionListener() {
											public void actionPerformed(ActionEvent e) {
												toggleBetting();
											}
										});
										toggleButton.setBounds(348, 172, 58, 23);
										getContentPane().add(toggleButton);
														
														toggleButton_1 = new JToggleButton("Show");
														toggleButton_1.addActionListener(new ActionListener() {
															public void actionPerformed(ActionEvent e) {
															toggleRequests();
															}
														});
														toggleButton_1.setBounds(348, 149, 58, 23);
														getContentPane().add(toggleButton_1);
														
														separator_2 = new JSeparator();
														separator_2.setBounds(90, 159, 248, 2);
														getContentPane().add(separator_2);
														
														lblSongRequests = new JLabel("Song Requests");
														lblSongRequests.setBounds(9, 153, 89, 14);
														getContentPane().add(lblSongRequests);
														
														requestsPanel = new JPanel();
														requestsPanel.setBounds(9, 172, 397, 124);
														getContentPane().add(requestsPanel);
														requestsPanel.setLayout(null);
														requestsPanel.setVisible(false);
														
																JButton btnToggleSong = new JButton("Toggle Song");
																btnToggleSong.setBounds(10, 77, 98, 23);
																requestsPanel.add(btnToggleSong);
																buttonGroup.add(btnToggleSong);
																copysr = new JButton("Copy SongRequest");
																copysr.setBounds(10, 41, 125, 23);
																requestsPanel.add(copysr);
																buttonGroup.add(copysr);
																delsr = new JButton("Delete SongRequest");
																delsr.setBounds(145, 41, 134, 23);
																requestsPanel.add(delsr);
																buttonGroup.add(delsr);
																
																		btnCopyAndDelete = new JButton("Delete and Copy SongRequest");
																		btnCopyAndDelete.setBounds(10, 5, 269, 23);
																		requestsPanel.add(btnCopyAndDelete);
																		buttonGroup.add(btnCopyAndDelete);
																		lblRequestee = new JLabel("Requestee:");
																		lblRequestee.setBounds(289, 9, 56, 14);
																		requestsPanel.add(lblRequestee);
																		requestee = new JLabel("NaN");
																		requestee.setBounds(289, 28, 95, 14);
																		requestsPanel.add(requestee);
																		lblLink = new JLabel("Link:");
																		lblLink.setBounds(289, 50, 27, 14);
																		requestsPanel.add(lblLink);
																		link = new JTextField();
																		link.setBounds(118, 76, 269, 25);
																		requestsPanel.add(link);
																		link.setEditable(false);
																		link.setText("NaN");
																		link.setColumns(10);
																		
																		bettingPanel = new JPanel();
																		bettingPanel.setBounds(9, 196, 397, 112);
																		getContentPane().add(bettingPanel);
																		bettingPanel.setLayout(null);
																		bettingPanel.setVisible(false);
																		
																		togglebets = new JButton("Toggle Bets");
																		togglebets.setBounds(34, 6, 89, 90);
																		bettingPanel.add(togglebets);
																		buttonGroup.add(togglebets);
																		lblBetsStatus = new JLabel("Bets Status:");
																		lblBetsStatus.setBounds(165, 6, 71, 14);
																		bettingPanel.add(lblBetsStatus);
																		betstatus = new JLabel("STATUS");
																		betstatus.setBounds(274, 6, 83, 14);
																		bettingPanel.add(betstatus);
																		wongame = new JButton("Won Game");
																		wongame.setBounds(157, 35, 85, 61);
																		bettingPanel.add(wongame);
																		buttonGroup.add(wongame);
																		btnLostGame = new JButton("Lost Game");
																		btnLostGame.setBounds(276, 35, 85, 61);
																		bettingPanel.add(btnLostGame);
																		buttonGroup.add(btnLostGame);
																		
																		JMenuBar menuBar = new JMenuBar();
																		setJMenuBar(menuBar);
																		
																		JMenu mnFile = new JMenu("File");
																		menuBar.add(mnFile);
																		
																		JMenuItem mntmExit = new JMenuItem("Exit");
																		mntmExit.addActionListener(new ActionListener() {
																			public void actionPerformed(ActionEvent e) {
																				System.exit(0);
																			}
																		});
																		mnFile.add(mntmExit);
																		
																		JMenu mnVisualDisplay = new JMenu("Visual Display");
																		menuBar.add(mnVisualDisplay);
																		
																		JMenuItem mntmOpen = new JMenuItem("Open");
																		mntmOpen.addActionListener(new ActionListener() {
																			public void actionPerformed(ActionEvent e) {
																				new VisualDisplayHUB();
																			}
																		});
																		mnVisualDisplay.add(mntmOpen);
																		
																		JMenuItem mntmAbout = new JMenuItem("About");
																		mntmAbout.addActionListener(new ActionListener() {
																			public void actionPerformed(ActionEvent e) {
																			JOptionPane.showMessageDialog(null, "Copyright 2015 by Marenthyu");
																			}
																		});
																		menuBar.add(mntmAbout);
																		btnLostGame.addActionListener(new ActionListener() {
																			public void actionPerformed(ActionEvent e) {
																				ConnectionManager.lostGame();
																			}
																		});
																		wongame.addActionListener(new ActionListener() {
																			public void actionPerformed(ActionEvent e) {
																				ConnectionManager.wonGame();
																			}
																		});
																		togglebets.addActionListener(new ActionListener() {
																			public void actionPerformed(ActionEvent e) {
																				togglebets();
																			}
																		});
																		btnCopyAndDelete.addActionListener(new ActionListener() {
																			public void actionPerformed(ActionEvent e) {
																				deleteSongRequest();
																				copySongRequest();
																			}
																		});
																		delsr.addActionListener(new ActionListener() {
																			public void actionPerformed(ActionEvent e) {
																				deleteSongRequest();
																			}
																		});
																		copysr.addActionListener(new ActionListener() {
																			public void actionPerformed(ActionEvent e) {
																				copySongRequest();
																			}
																		});
																		btnToggleSong.addActionListener(new ActionListener() {
																			public void actionPerformed(ActionEvent e) {
																				SongManager.toggleSongMode();
																			}
																		});
		

		refresh();
	}

	protected void toggleRequests() {
		if (toggleButton_1.isSelected()) {
			requestsPanel.setVisible(true);
			JFrame test = this;
			setSize(test.getWidth(), test.getHeight()+requestsPanel.getHeight());
			toggleButton_1.setText("Hide");
			
			
			
			tglbtnShow.setBounds(tglbtnShow.getX(), tglbtnShow.getY()+requestsPanel.getHeight(), tglbtnShow.getWidth(), tglbtnShow.getHeight());
			lblSounds_1.setBounds(lblSounds_1.getX(), lblSounds_1.getY()+requestsPanel.getHeight(), lblSounds_1.getWidth(), lblSounds_1.getHeight());
			separator.setBounds(separator.getX(), separator.getY()+requestsPanel.getHeight(), separator.getWidth(), separator.getHeight());
			soundPanel.setBounds(soundPanel.getX(), soundPanel.getY()+requestsPanel.getHeight(), soundPanel.getWidth(), soundPanel.getHeight());
			
			
			bettingPanel.setBounds(bettingPanel.getX(), bettingPanel.getY()+requestsPanel.getHeight(), bettingPanel.getWidth(), bettingPanel.getHeight());
			lblBetting.setBounds(lblBetting.getX(), lblBetting.getY()+requestsPanel.getHeight(), lblBetting.getWidth(), lblBetting.getHeight());
			separator_1.setBounds(separator_1.getX(), separator_1.getY()+requestsPanel.getHeight(), separator_1.getWidth(), separator_1.getHeight());
			toggleButton.setBounds(toggleButton.getX(), toggleButton.getY()+requestsPanel.getHeight(), toggleButton.getWidth(), toggleButton.getHeight());
			
			
		} else {
			requestsPanel.setVisible(false);
			setSize(this.getWidth(), this.getHeight()-requestsPanel.getHeight());
			toggleButton_1.setText("Show");
			tglbtnShow.setBounds(tglbtnShow.getX(), tglbtnShow.getY()-requestsPanel.getHeight(), tglbtnShow.getWidth(), tglbtnShow.getHeight());
			lblSounds_1.setBounds(lblSounds_1.getX(), lblSounds_1.getY()-requestsPanel.getHeight(), lblSounds_1.getWidth(), lblSounds_1.getHeight());
			separator.setBounds(separator.getX(), separator.getY()-requestsPanel.getHeight(), separator.getWidth(), separator.getHeight());
			soundPanel.setBounds(soundPanel.getX(), soundPanel.getY()-requestsPanel.getHeight(), soundPanel.getWidth(), soundPanel.getHeight());
			
			bettingPanel.setBounds(bettingPanel.getX(), bettingPanel.getY()-requestsPanel.getHeight(), bettingPanel.getWidth(), bettingPanel.getHeight());
			lblBetting.setBounds(lblBetting.getX(), lblBetting.getY()-requestsPanel.getHeight(), lblBetting.getWidth(), lblBetting.getHeight());
			separator_1.setBounds(separator_1.getX(), separator_1.getY()-requestsPanel.getHeight(), separator_1.getWidth(), separator_1.getHeight());
			toggleButton.setBounds(toggleButton.getX(), toggleButton.getY()-requestsPanel.getHeight(), toggleButton.getWidth(), toggleButton.getHeight());
			
			
		}
		
	}

	protected void toggleBetting() {
		if (toggleButton.isSelected()) {
			
			bettingPanel.setVisible(true);
			JFrame test = this;
			setSize(test.getWidth(), test.getHeight()+bettingPanel.getHeight());
			toggleButton.setText("Hide");
			
			
			
			tglbtnShow.setBounds(tglbtnShow.getX(), tglbtnShow.getY()+bettingPanel.getHeight(), tglbtnShow.getWidth(), tglbtnShow.getHeight());
			lblSounds_1.setBounds(lblSounds_1.getX(), lblSounds_1.getY()+bettingPanel.getHeight(), lblSounds_1.getWidth(), lblSounds_1.getHeight());
			separator.setBounds(separator.getX(), separator.getY()+bettingPanel.getHeight(), separator.getWidth(), separator.getHeight());
			soundPanel.setBounds(soundPanel.getX(), soundPanel.getY()+bettingPanel.getHeight(), soundPanel.getWidth(), soundPanel.getHeight());
			
			
		} else {
			bettingPanel.setVisible(false);
			setSize(this.getWidth(), this.getHeight()-bettingPanel.getHeight());
			toggleButton.setText("Show");
			tglbtnShow.setBounds(tglbtnShow.getX(), tglbtnShow.getY()-bettingPanel.getHeight(), tglbtnShow.getWidth(), tglbtnShow.getHeight());
			lblSounds_1.setBounds(lblSounds_1.getX(), lblSounds_1.getY()-bettingPanel.getHeight(), lblSounds_1.getWidth(), lblSounds_1.getHeight());
			separator.setBounds(separator.getX(), separator.getY()-bettingPanel.getHeight(), separator.getWidth(), separator.getHeight());
			soundPanel.setBounds(soundPanel.getX(), soundPanel.getY()-bettingPanel.getHeight(), soundPanel.getWidth(), soundPanel.getHeight());
			
		}
		
	}

	protected void toggleSongs() {
		if (tglbtnShow.isSelected()) {
			soundPanel.setVisible(true);
			tglbtnShow.setText("Hide");
			setSize(this.getWidth(),this.getHeight()+soundPanel.getHeight());
		} else {
			soundPanel.setVisible(false);
			tglbtnShow.setText("Show");
			setSize(this.getWidth(),this.getHeight()-soundPanel.getHeight());
		}
		
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

			for (Enumeration<AbstractButton> buttons = buttonGroup
					.getElements(); buttons.hasMoreElements();) {
				AbstractButton button = buttons.nextElement();
				button.setEnabled(true);
			}

			if (ConnectionManager.betstatus()) {
				betstatus.setText("Open");
			} else {
				betstatus.setText("Closed");
			}
		} else {
			botstatus.setText("Not Connected.");
			joinleave.setText("Join");
			betstatus.setText("Not Connected.");
			for (Enumeration<AbstractButton> buttons = buttonGroup
					.getElements(); buttons.hasMoreElements();) {
				AbstractButton button = buttons.nextElement();
				button.setEnabled(false);
			}
		}
	}
}
