package gui;

import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JButton;

import connection.ConnectionManager;

import java.awt.AWTEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.Enumeration;

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
		typegroup.add(rdbtnSay);
		rdbtnSay.setToolTipText("Says something in response");
		rdbtnSay.setBounds(99, 35, 52, 23);
		getContentPane().add(rdbtnSay);
		
		JRadioButton rdbtnCallmethod = new JRadioButton("CALLMETHOD");
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
		plgroup.add(rdbtnAll);
		rdbtnAll.setBounds(99, 61, 52, 19);
		getContentPane().add(rdbtnAll);
		
		JRadioButton rdbtnRegular = new JRadioButton("REGULAR");
		plgroup.add(rdbtnRegular);
		rdbtnRegular.setBounds(159, 64, 71, 17);
		getContentPane().add(rdbtnRegular);
		
		JRadioButton rdbtnSubscriber = new JRadioButton("SUBSCRIBER");
		plgroup.add(rdbtnSubscriber);
		rdbtnSubscriber.setBounds(236, 63, 87, 19);
		getContentPane().add(rdbtnSubscriber);
		
		JRadioButton rdbtnMod = new JRadioButton("MOD");
		plgroup.add(rdbtnMod);
		rdbtnMod.setBounds(325, 62, 52, 20);
		getContentPane().add(rdbtnMod);
		
		JRadioButton rdbtnBroadcaster = new JRadioButton("BROADCASTER");
		plgroup.add(rdbtnBroadcaster);
		rdbtnBroadcaster.setBounds(383, 62, 105, 19);
		getContentPane().add(rdbtnBroadcaster);
		
		JLabel lblCost = new JLabel("Cost:");
		lblCost.setBounds(10, 90, 82, 14);
		getContentPane().add(lblCost);
		
		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
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
		setSize(510, 232);
		setVisible(true);
	}
	
	
	protected void addCommand() {
		if (ConnectionManager.addCommand(txtexample.getText(), getSelectedButtonText(typegroup), getSelectedButtonText(plgroup), ((int) spinner.getValue())+"", txtThisCommand.getText())) {
			JOptionPane.showMessageDialog(null, "Successfully added "+txtexample.getText());
			 dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		} else {
			JOptionPane.showMessageDialog(null, "Could not add the command. Maybe it exists already?");
		}
		
	}


	public String getSelectedButtonText(ButtonGroup buttonGroup) {
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
           
            if (button.isSelected()) {
                return button.getText();
            }
        }

        return null;
    }
}
