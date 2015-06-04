package gui;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;

import connection.ConnectionManager;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

public class EditMessageWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -58226994324394551L;
	private JTextField textField;
	private JComboBox<String> comboBox;
	private JButton btnSave;
	private JButton btnDiscard;
	private ArrayList<String> messages;
	private ArrayList<String[]> messages2 = new ArrayList<String[]>();

	public EditMessageWindow() {

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Edit Message");
		getContentPane().setLayout(null);

		comboBox = new JComboBox<String>();

		comboBox.setBounds(10, 9, 414, 20);
		getContentPane().add(comboBox);

		textField = new JTextField();
		textField.setBounds(10, 38, 414, 20);
		getContentPane().add(textField);
		textField.setColumns(10);

		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		btnSave.setBounds(76, 67, 102, 20);
		getContentPane().add(btnSave);

		btnDiscard = new JButton("Discard");
		btnDiscard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newItem();
			}
		});
		btnDiscard.setBounds(254, 67, 102, 20);
		getContentPane().add(btnDiscard);

		messages = ConnectionManager.messages();

		for (String s : messages) {
			String[] parts = s.split("~#");
			comboBox.addItem(parts[0]);
			messages2.add(parts);
		}

		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				newItem();
			}
		});
		setSize(450,135);
		setVisible(true);
	}

	protected void save() {
		if (ConnectionManager.editOption((String) comboBox.getSelectedItem(),
				textField.getText())) {
			JOptionPane.showMessageDialog(null, "Successfully edited Message.");
		} else {
			JOptionPane.showMessageDialog(null, "Message NOT edited!", "ERROR",
					JOptionPane.ERROR_MESSAGE);
		}
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	protected void newItem() {
		String selected = (String) comboBox.getSelectedItem();

		for (String[] s : messages2) {
			if (s[0].equals(selected)) {
				textField.setText(s[1]);
				return;
			}
		}
	}
}
