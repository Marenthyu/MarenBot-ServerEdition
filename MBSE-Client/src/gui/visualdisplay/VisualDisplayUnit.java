package gui.visualdisplay;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class VisualDisplayUnit extends JFrame {

	private JPanel panel;
	private JTextField txtConstant;

	private final int MIN_WIDTH = 400, MIN_HEIGHT = 140;
	private Dimension minDimension = new Dimension(MIN_WIDTH, MIN_HEIGHT);
	private JPanel panel_1;
	private JSpinner scaleSpinner;
	private ImageIcon image;
	private ImageIcon scaledImage;
	private JLabel lblNewLabel, stringDisplay;
	private FontMetrics fm;
	private JCheckBox chckbxEnabled;

	@SuppressWarnings("unused")
	public VisualDisplayUnit(ImageIcon image) {

		setVisible(true);
		this.image = image;
		this.scaledImage = image;

		image = this.scaledImage;

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		Dimension imageDimension = new Dimension(image.getIconWidth(),
				image.getIconHeight());

		this.setMinimumSize(minDimension);
		this.setSize((int) image.getIconWidth() + 50,
				(int) image.getIconHeight() + 100);

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);

				panel_1.setSize(getWidth() - 40, panel_1.getHeight());

			}
		});

		int width = image.getIconWidth();
		int height = image.getIconHeight();

		getContentPane().setLayout(null);

		panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null,
				null));
		panel.setBounds(10, 50, 364, 40);
		getContentPane().add(panel);
		panel.setLayout(null);

		panel.setSize(imageDimension);
		

		lblNewLabel = new JLabel("New label");
		lblNewLabel.setIcon(image);
		lblNewLabel
				.setBounds(0, 0, image.getIconWidth(), image.getIconHeight());
		panel.add(lblNewLabel);

		panel_1 = new JPanel();
		panel_1.setBounds(10, 0, 364, 52);
		getContentPane().add(panel_1);

		scaleSpinner = new JSpinner();
		scaleSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				updateScale();
			}
		});
		scaleSpinner.setToolTipText("The scale of the image, in percent");
		scaleSpinner.setModel(new SpinnerNumberModel(100, 1, 300, 1));

		JLabel lblText = new JLabel("Text:");

		chckbxEnabled = new JCheckBox("Enabled");
		chckbxEnabled.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateScale();
			}
		});
		chckbxEnabled.setSelected(true);

		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setEnabled(false);

		txtConstant = new JTextField();
		txtConstant.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				updateScale();
			}
		});
		txtConstant.setText("Constant");
		txtConstant.setColumns(10);
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_panel_1
						.createSequentialGroup()
						.addGap(7)
						.addComponent(scaleSpinner, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(lblText, GroupLayout.PREFERRED_SIZE, 40,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(chckbxEnabled,
								GroupLayout.PREFERRED_SIZE, 85,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 85,
								GroupLayout.PREFERRED_SIZE)
						.addGap(18)
						.addComponent(txtConstant, GroupLayout.DEFAULT_SIZE,
								60, Short.MAX_VALUE).addContainerGap()));
		gl_panel_1
				.setVerticalGroup(gl_panel_1
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_panel_1
										.createSequentialGroup()
										.addGap(7)
										.addGroup(
												gl_panel_1
														.createParallelGroup(
																Alignment.LEADING)
														.addComponent(
																txtConstant,
																GroupLayout.PREFERRED_SIZE,
																23,
																GroupLayout.PREFERRED_SIZE)
														.addGroup(
																gl_panel_1
																		.createParallelGroup(
																				Alignment.BASELINE)
																		.addComponent(
																				scaleSpinner,
																				GroupLayout.PREFERRED_SIZE,
																				23,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(
																				lblText,
																				GroupLayout.PREFERRED_SIZE,
																				23,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(
																				chckbxEnabled)
																		.addComponent(
																				comboBox,
																				GroupLayout.PREFERRED_SIZE,
																				23,
																				GroupLayout.PREFERRED_SIZE)))
										.addGap(22)));
		panel_1.setLayout(gl_panel_1);
		fm = getGraphics().getFontMetrics();
		
		stringDisplay = new JLabel("Constant");
		
		
		int stringWidth = fm.stringWidth("Constant");
		
		stringDisplay.setBounds(((int) panel.getWidth()/2)-((int) stringWidth/2), ((int) panel.getHeight()/2)-((int) fm.getHeight()/2), stringWidth, fm.getHeight());
		
		panel.add(stringDisplay);
		panel.setComponentZOrder(stringDisplay, 0);
	}

	protected void updateScale() {

		int size = (int) scaleSpinner.getValue();

		float factor = ((float) (size)) / ((float) 100);

		scaledImage = new ImageIcon(image.getImage().getScaledInstance(
				(int) (image.getIconWidth() * factor),
				(int) (image.getIconHeight() * factor), 0));

		lblNewLabel.setIcon(scaledImage);
		lblNewLabel.setSize(scaledImage.getIconWidth(),
				scaledImage.getIconHeight());
		panel.setSize(lblNewLabel.getSize());
		setSize(panel.getWidth() + 50, panel.getHeight() + 100);
		
		
		int stringWidth = fm.stringWidth(txtConstant.getText());
		stringDisplay.setBounds(((int) panel.getWidth()/2)-((int) stringWidth/2), ((int) panel.getHeight()/2)-((int) fm.getHeight()/2), stringWidth, fm.getHeight());
		stringDisplay.setText(txtConstant.getText());

		if (chckbxEnabled.isSelected()) {
			stringDisplay.setVisible(true);
		} else {
			stringDisplay.setVisible(false);
		}
	}
}
