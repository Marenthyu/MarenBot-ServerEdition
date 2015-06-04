package gui;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.ButtonGroup;

import utils.RBGroup;
import connection.ConnectionManager;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class EditCommandWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2361420933901665462L;
	private JTextField textField;
	private final ButtonGroup saymethodgroup = new ButtonGroup();
	private final ButtonGroup permlevelgroup = new ButtonGroup();
	private final ButtonGroup costgaingroup = new ButtonGroup();
	private ArrayList<String> commands = ConnectionManager.commands();
	JComboBox<String> cmdComboBox;
	JRadioButton radioButton;
	private JRadioButton radioButton_1;
	private JRadioButton radioButton_2;
	private JRadioButton radioButton_3;
	private JRadioButton radioButton_4;
	private JRadioButton radioButton_5;
	private JRadioButton radioButton_6;
	private JRadioButton radioButton_7;
	private JRadioButton radioButton_8;
	private JComboBox<String> valueComboBox;
	private JSpinner spinner;
	private JButton btnDelete;
	public EditCommandWindow() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Edit Command");
		getContentPane().setLayout(null);
		setSize(510, 206);
		
		JLabel lblCommand = new JLabel("Command:");
		lblCommand.setBounds(10, 11, 93, 14);
		getContentPane().add(lblCommand);
		
		cmdComboBox = new JComboBox<String>();
		
		cmdComboBox.setBounds(108, 8, 375, 20);
		getContentPane().add(cmdComboBox);
		
		if (commands.size()==0) {
			JOptionPane.showMessageDialog(null, "No Commands were added yet, sorry :(");
			this.getToolkit().getSystemEventQueue().postEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			return;
			
			
		}
		for (String s:commands) {
			String[] parts = s.split("~#");
			cmdComboBox.addItem(parts[0]);
		}
		
		cmdComboBox.setSelectedIndex(0);
		
		
		
		JLabel lblType = new JLabel("Type:");
		lblType.setBounds(10, 36, 93, 14);
		getContentPane().add(lblType);
		
		JLabel lblPermissionLevel = new JLabel("Permission Level:");
		lblPermissionLevel.setBounds(10, 61, 93, 14);
		getContentPane().add(lblPermissionLevel);
		
		JLabel lblCostgain = new JLabel("Cost/Gain:");
		lblCostgain.setBounds(10, 84, 93, 20);
		getContentPane().add(lblCostgain);
		
		JLabel lblValue = new JLabel("Value:");
		lblValue.setBounds(10, 111, 93, 14);
		getContentPane().add(lblValue);
		
		radioButton = new JRadioButton("SAY");
		saymethodgroup.add(radioButton);
		radioButton.setToolTipText("Says something in response");
		radioButton.setSelected(true);
		radioButton.setBounds(108, 36, 52, 14);
		getContentPane().add(radioButton);
		
		radioButton_1 = new JRadioButton("CALLMETHOD");
		saymethodgroup.add(radioButton_1);
		radioButton_1.setToolTipText("Calls a predefined Method");
		radioButton_1.setBounds(168, 36, 265, 14);
		getContentPane().add(radioButton_1);
		
		radioButton_2 = new JRadioButton("ALL");
		permlevelgroup.add(radioButton_2);
		radioButton_2.setSelected(true);
		radioButton_2.setBounds(108, 61, 52, 14);
		getContentPane().add(radioButton_2);
		
		radioButton_3 = new JRadioButton("REGULAR");
		permlevelgroup.add(radioButton_3);
		radioButton_3.setBounds(168, 61, 71, 14);
		getContentPane().add(radioButton_3);
		
		radioButton_4 = new JRadioButton("SUBSCRIBER");
		permlevelgroup.add(radioButton_4);
		radioButton_4.setBounds(245, 61, 87, 14);
		getContentPane().add(radioButton_4);
		
		radioButton_5 = new JRadioButton("MOD");
		permlevelgroup.add(radioButton_5);
		radioButton_5.setBounds(334, 61, 52, 14);
		getContentPane().add(radioButton_5);
		
		radioButton_6 = new JRadioButton("BROADCASTER");
		permlevelgroup.add(radioButton_6);
		radioButton_6.setBounds(388, 61, 105, 14);
		getContentPane().add(radioButton_6);
		
		spinner = new JSpinner();
		spinner.setBounds(108, 84, 52, 20);
		getContentPane().add(spinner);
		
		radioButton_7 = new JRadioButton("COST");
		costgaingroup.add(radioButton_7);
		radioButton_7.setSelected(true);
		radioButton_7.setBounds(168, 84, 61, 20);
		getContentPane().add(radioButton_7);
		
		radioButton_8 = new JRadioButton("GAIN");
		costgaingroup.add(radioButton_8);
		radioButton_8.setBounds(245, 84, 61, 20);
		getContentPane().add(radioButton_8);
		
		textField = new JTextField();
		textField.setBounds(108, 108, 375, 20);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		valueComboBox = new JComboBox<String>();
		valueComboBox.setBounds(108, 108, 375, 20);
		getContentPane().add(valueComboBox);
		for (String s:ConnectionManager.methods()) {
			valueComboBox.addItem(s);
		}
		
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		btnSave.setBounds(56, 136, 89, 23);
		getContentPane().add(btnSave);
		
		JButton btnDiscard = new JButton("Discard");
		btnDiscard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				discard();
			}
		});
		btnDiscard.setBounds(201, 136, 89, 23);
		getContentPane().add(btnDiscard);
		
		btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				delete();
			}
		});
		btnDelete.setBounds(346, 136, 89, 23);
		getContentPane().add(btnDelete);
		
		cmdComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				newItem();
			}
		});
		
		newItem();
		setVisible(true);
	}
	protected void delete() {
		ConnectionManager.delcomm((String) cmdComboBox.getSelectedItem());
		JOptionPane.showMessageDialog(null, "Command deleted.");
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		
	}
	protected void discard() {
		newItem();
		
	}
	protected void save() {
		
		int cost = (int) spinner.getValue();
		
		if(RBGroup.getSelectedButtonText(costgaingroup).equals("GAIN")) {
			cost = cost*-1;
		}
		
		String value = textField.getText();
		
		if (RBGroup.getSelectedButtonText(saymethodgroup).equals("CALLMETHOD")) {
			value = (String) valueComboBox.getSelectedItem();
		}
		ConnectionManager.delcomm((String) cmdComboBox.getSelectedItem());
		ConnectionManager.addCommand((String) cmdComboBox.getSelectedItem(), (String) RBGroup.getSelectedButtonText(saymethodgroup), RBGroup.getSelectedButtonText(permlevelgroup), cost+"", value);
		JOptionPane.showMessageDialog(null, "Command edited.");
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
	private void newItem() {
		
		for (String s:commands) {
			
			
			String[] parts = s .split("~#");
			 if (cmdComboBox.getSelectedItem().equals(parts[0])) {
				switch (parts[1]) {
				case "SAY" : {
					radioButton.setSelected(true);
					radioButton_1.setSelected(false);
					
					valueComboBox.setVisible(false);
					textField.setVisible(true);
					textField.setText(parts[4]);
					
					break;
				}
				case "CALLMETHOD" : {
					radioButton.setSelected(false);
					radioButton_1.setSelected(true);
					
					valueComboBox.setVisible(true);
					textField.setVisible(false);
					valueComboBox.setSelectedItem(parts[4]);
					
					
					break;
				}
				default: {
					System.err.println("That command didn't have SAY or CALLMETHOD. How awkward. Apparently it has "+parts[1]+"!");
					JOptionPane.showMessageDialog(null, parts[0]+" apparently had "+parts[1]+" as Type. That's impossible. Aborting.");
					this.getToolkit().getSystemEventQueue().postEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
					return;
				}
				}
				switch (parts[2]) {
				case "ALL" : {
					radioButton_2.setSelected(true);
					radioButton_3.setSelected(false);
					radioButton_4.setSelected(false);
					radioButton_5.setSelected(false);
					radioButton_6.setSelected(false);
					break;
				}
				case "REGULAR" : {
					radioButton_2.setSelected(false);
					radioButton_3.setSelected(true);
					radioButton_4.setSelected(false);
					radioButton_5.setSelected(false);
					radioButton_6.setSelected(false);
					break;
				}
				case "SUBSCRIBER" : {
					radioButton_2.setSelected(false);
					radioButton_3.setSelected(false);
					radioButton_4.setSelected(true);
					radioButton_5.setSelected(false);
					radioButton_6.setSelected(false);
					break;
				}
				case "MOD" : {
					radioButton_2.setSelected(false);
					radioButton_3.setSelected(false);
					radioButton_4.setSelected(false);
					radioButton_5.setSelected(true);
					radioButton_6.setSelected(false);
					break;
				}
				case "BROADCASTER" : {
					radioButton_2.setSelected(false);
					radioButton_3.setSelected(false);
					radioButton_4.setSelected(false);
					radioButton_5.setSelected(false);
					radioButton_6.setSelected(true);
					break;
				}
				default: {
					System.err.println("That command didn't have ALL, REGULAR, SUBSCRIBER, MOD or BROADCASTER. How awkward. Apparently it has "+parts[2]+"!");
					JOptionPane.showMessageDialog(null, parts[0]+" apparently had "+parts[2]+" as Permlevel. That's impossible. Aborting.");
					this.getToolkit().getSystemEventQueue().postEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
					return;
				}
				}
				int i;
				if (( i = Integer.parseInt(parts[3]))>=0) {
					spinner.setValue(i);
					radioButton_7.setSelected(true);
					radioButton_8.setSelected(false);
				} else {
					spinner.setValue(i*-1);
					radioButton_7.setSelected(false);
					radioButton_8.setSelected(true);
				}
				
			}
		}
		
	}
}
