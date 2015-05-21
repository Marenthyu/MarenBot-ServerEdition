package gui;

import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import connection.ConnectionManager;

import javax.swing.JTextField;

public class MainWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7497833988556627886L;
	JButton joinleave, togglebets, wongame, btnLostGame, btnRefresh, copysr,
			delsr, btnAddCommand, btnRemoveCommand;
	JLabel lblBotStatus, botstatus, lblBetsStatus, betstatus;
	private JLabel lblRequestee;
	private JLabel requestee;
	private JLabel lblLink;
	private JTextField link;

	public MainWindow() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		setTitle("MarenBot SE Client");
		setSize(434,437);
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
		togglebets.setBounds(109, 241, 89, 82);
		getContentPane().add(togglebets);
		wongame = new JButton("Won Game");
		wongame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConnectionManager.wonGame();
			}
		});
		wongame.setBounds(208, 241, 105, 82);
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
				delcomm(JOptionPane.showInputDialog("Please enter the command to remove!"));
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
		setVisible(true);
		refresh();
	}

	protected void delcomm(String name) {
		if (ConnectionManager.delcomm(name)) {
			JOptionPane.showMessageDialog(null, "Command "+name+" successfully removed");
		} else {
			JOptionPane.showMessageDialog(null, "Could not remove "+name, "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		
	}

	protected void joinleave() {
		switch (botstatus.getText()) {
		case "Not Connected.": {
			ConnectionManager.join();
			refresh();
			return;
		}
		case "Connected.": {
			ConnectionManager.leave();
			refresh();
			return;
		}
		}
	}

	protected void deleteSongRequest() {
		ConnectionManager.delOldestSongRequest();
	}

	protected void copySongRequest() {
		String[] s = ConnectionManager.getOldestSongrequest();
		Toolkit.getDefaultToolkit().getSystemClipboard()
				.setContents(new StringSelection(s[1]), null);
		requestee.setText(s[0]);
		link.setText(s[1]);
	}

	protected void togglebets() {
		System.out.println("toggling bets...");
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
