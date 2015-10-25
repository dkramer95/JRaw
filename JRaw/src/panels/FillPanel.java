package panels;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.ColorSelectionModel;

import gfx.Decoration;

/**
 * This class contains all of the components required to modify the
 * fill style of a decoration.
 * @author DavidKramer
 *
 */
public class FillPanel extends JPanel {
	protected Decoration decoration;					// the decoration object to apply styles to
	protected boolean changedDecoration;				// have we modified the decoration?
	private JPanel fillTypePanel;
	private JRadioButton noFillBtn;
	private JRadioButton solidFillBtn;
	private JRadioButton gradientFillBtn;
	private ButtonGroup fillBtnGroup;
	private JComboBox<String> gradientComboBox;
	protected SpinnerNumberModel alphaModel;			// model that represents the alpha value of a color
	protected ColorSelectionModel colorModel;			// model that represents a color
	protected AbstractColorChooserPanel colorPanel;
	
	public FillPanel(Decoration decoration) {
		this.decoration = decoration;
		createFillTypePanel();
		createComboBox();
		createRadioButtons();
		createColorPanel();
		addComponents();
	}
	
	/**
	 * Adds all of the panels to the main fill panel.
	 */
	private void addComponents() {
		fillTypePanel.add(noFillBtn);
		fillTypePanel.add(solidFillBtn);
		fillTypePanel.add(gradientFillBtn);
		fillTypePanel.add(gradientComboBox);
		add(fillTypePanel);
		add(colorPanel);
		if (!decoration.hasFill()) {
			colorPanel.setVisible(false);
		}
	}
	
	//********************************************************
	//* 			   PANEL CREATION METHODS				 *
	//********************************************************
	
	private void createFillTypePanel() {
		fillTypePanel = new JPanel();
		fillTypePanel.setBorder(BorderFactory.createTitledBorder("Fill Type "));
		fillTypePanel.setLayout(new BoxLayout(fillTypePanel, BoxLayout.Y_AXIS));
	}
	
	/**
	 * Creates and adds action listeners to the fill radio buttons.
	 */
	private void createRadioButtons() {
		noFillBtn = new JRadioButton("No Fill");
		solidFillBtn = new JRadioButton("Solid Fill");
		gradientFillBtn = new JRadioButton("Gradient Fill");
		fillBtnGroup = new ButtonGroup();
		fillBtnGroup.add(noFillBtn);
		fillBtnGroup.add(solidFillBtn);
		fillBtnGroup.add(gradientFillBtn);
		
		if (decoration.hasFill()) {
			solidFillBtn.setSelected(true);
			gradientComboBox.setEnabled(false);
		} else if (decoration.hasGradient()) {
			gradientFillBtn.setSelected(true);
		} else {
			noFillBtn.setSelected(true);
		}
		
		// radio button listeners
		
		noFillBtn.addActionListener( e -> {
			decoration.setHasFill(false);
			gradientComboBox.setEnabled(false);
			colorPanel.setVisible(false);
		});
		
		solidFillBtn.addActionListener( e -> {
			decoration.setHasFill(true);
			gradientComboBox.setEnabled(false);
			colorPanel.setVisible(true);
		});
		
		gradientFillBtn.addActionListener( e -> {
			decoration.setHasGradient(true);
			gradientComboBox.setEnabled(true);
		});
	}
	
	/**
	 * Creates and adds action listeners to the gradient combo box.
	 */
	private void createComboBox() {
		gradientComboBox = new JComboBox<>();
		gradientComboBox.addItem("Linear");
		gradientComboBox.addItem("Radial");
		
		gradientComboBox.addItemListener( e -> {
			System.out.println("Gradient combo box changed!");
		});
	}
	
	/**
	 * Creates the color panel which the user can use to pick from a palette of
	 * colors. This is using the HSV color selection model.
	 */
	private void createColorPanel() {
		JColorChooser colorChooser = new JColorChooser();
		AbstractColorChooserPanel[] panels = colorChooser.getChooserPanels();
		colorPanel = panels[2];
		colorPanel.setBorder(BorderFactory.createTitledBorder("Fill Color "));
		colorPanel.getColorSelectionModel().setSelectedColor(decoration.getFillColor());
		colorModel = colorPanel.getColorSelectionModel();
		
		colorModel.addChangeListener( e -> {
			decoration.setFillColor(colorModel.getSelectedColor());
		});
	}
	
	//********************************************************
	//* 				  ACCESSOR METHODS					 *
	//********************************************************
	
	public boolean changedDecoration() {
		return changedDecoration;
	}
	
	public Decoration getDecoration() {
		return decoration;
	}
	
	public AbstractColorChooserPanel getColorPanel() {
		return colorPanel;
	}
	
	public Color getSelectedColor() {
		return colorModel.getSelectedColor();
	}
	
}
