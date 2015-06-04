package gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JButton;

import sound.Sound;
import utils.RBGroup;
import connection.ConnectionManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.JComboBox;

public class AddCommWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4803957557945976529L;
	private JTextField txtexample;
	private JTextField txtThisCommand;
	private final ButtonGroup plgroup = new ButtonGroup();
	private final ButtonGroup typegroup = new ButtonGroup();
	JSpinner spinner;
	private final ButtonGroup costgaingroup = new ButtonGroup();
	JComboBox<String> comboBox;
	boolean callmethod = false;

	public AddCommWindow() {
		setTitle("Add Command");
		getContentPane().setLayout(null);

		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(10, 11, 38, 19);
		getContentPane().add(lblName);

		txtexample = new JTextField();
		txtexample.setText("!example");
		txtexample.setBounds(99, 10, 384, 20);
		getContentPane().add(txtexample);
		txtexample.setColumns(10);

		JRadioButton rdbtnSay = new JRadioButton("SAY");
		rdbtnSay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sayClicked();
			}
		});
		rdbtnSay.setSelected(true);
		typegroup.add(rdbtnSay);
		rdbtnSay.setToolTipText("Says something in response");
		rdbtnSay.setBounds(99, 35, 52, 23);
		getContentPane().add(rdbtnSay);

		JRadioButton rdbtnCallmethod = new JRadioButton("CALLMETHOD");
		rdbtnCallmethod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				callMethodCliecked();
			}
		});
		typegroup.add(rdbtnCallmethod);
		rdbtnCallmethod.setToolTipText("Calls a predefined Method");
		rdbtnCallmethod.setBounds(159, 37, 265, 19);
		getContentPane().add(rdbtnCallmethod);

		JLabel lblType = new JLabel("Type:");
		lblType.setBounds(10, 36, 46, 19);
		getContentPane().add(lblType);

		JLabel lblPermissionLevel = new JLabel("Permission Level:");
		lblPermissionLevel.setBounds(10, 65, 82, 14);
		getContentPane().add(lblPermissionLevel);

		JRadioButton rdbtnAll = new JRadioButton("ALL");
		rdbtnAll.setSelected(true);
		plgroup.add(rdbtnAll);
		rdbtnAll.setBounds(99, 61, 52, 19);
		getContentPane().add(rdbtnAll);

		JRadioButton rdbtnRegular = new JRadioButton("REGULAR");
		plgroup.add(rdbtnRegular);
		rdbtnRegular.setBounds(159, 62, 71, 17);
		getContentPane().add(rdbtnRegular);

		JRadioButton rdbtnSubscriber = new JRadioButton("SUBSCRIBER");
		plgroup.add(rdbtnSubscriber);
		rdbtnSubscriber.setBounds(236, 61, 87, 19);
		getContentPane().add(rdbtnSubscriber);

		JRadioButton rdbtnMod = new JRadioButton("MOD");
		plgroup.add(rdbtnMod);
		rdbtnMod.setBounds(325, 60, 52, 20);
		getContentPane().add(rdbtnMod);

		JRadioButton rdbtnBroadcaster = new JRadioButton("BROADCASTER");
		plgroup.add(rdbtnBroadcaster);
		rdbtnBroadcaster.setBounds(383, 61, 105, 19);
		getContentPane().add(rdbtnBroadcaster);

		JLabel lblCost = new JLabel("Cost/Gain:");
		lblCost.setBounds(10, 90, 82, 14);
		getContentPane().add(lblCost);

		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0),
				null, new Integer(1)));
		spinner.setBounds(99, 87, 52, 20);
		getContentPane().add(spinner);

		JLabel lblValue = new JLabel("Value:");
		lblValue.setBounds(10, 115, 82, 14);
		getContentPane().add(lblValue);

		txtThisCommand = new JTextField();
		txtThisCommand.setText("<sender>, this command is working!");
		txtThisCommand.setBounds(99, 115, 384, 19);
		getContentPane().add(txtThisCommand);
		txtThisCommand.setColumns(10);

		JButton btnNewButton = new JButton("Add Command");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addCommand();

			}
		});
		btnNewButton.setBounds(10, 140, 143, 39);
		getContentPane().add(btnNewButton);
		
		JRadioButton rdbtnCost = new JRadioButton("COST");
		rdbtnCost.setSelected(true);
		costgaingroup.add(rdbtnCost);
		rdbtnCost.setBounds(159, 86, 61, 23);
		getContentPane().add(rdbtnCost);
		
		JRadioButton rdbtnGain = new JRadioButton("GAIN");
		costgaingroup.add(rdbtnGain);
		rdbtnGain.setBounds(236, 86, 61, 23);
		getContentPane().add(rdbtnGain);
		
		comboBox = new JComboBox<String>();
		comboBox.setBounds(97, 113, 387, 23);
		getContentPane().add(comboBox);
		comboBox.setVisible(false);
		for (String s:ConnectionManager.methods()) {
			comboBox.addItem(s);
		}
		setSize(510, 232);
		setVisible(true);
	}

	protected void callMethodCliecked() {
		comboBox.setVisible(true);
		txtThisCommand.setVisible(false);
		callmethod = true;
	}

	protected void sayClicked() {
		comboBox.setVisible(false);
		txtThisCommand.setVisible(true);
		callmethod = false;
		
	}

	protected void addCommand() {
		int cost = (int) spinner.getValue();
		if (RBGroup.getSelectedButtonText(costgaingroup).equals("GAIN")) {
			cost = cost*-1;
		}
		String value;
		if (callmethod) {
			value = (String) comboBox.getSelectedItem();
			System.out.println("Value = "+value);
		} else {
			value = txtThisCommand.getText();
		}
		if (ConnectionManager.addCommand(txtexample.getText(),
				RBGroup.getSelectedButtonText(typegroup),
				RBGroup.getSelectedButtonText(plgroup),
				(cost) + "", value)) {
			JOptionPane.showMessageDialog(null, "Successfully added "
					+ txtexample.getText());
			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		} else {
			JOptionPane.showMessageDialog(null,
					"Could not add the command. Maybe it exists already?");
		}
		File file = new File("addcomm.wav");
		if (file.exists()) {
			try {
				Sound.playFile(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
