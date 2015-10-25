package dialogs;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import gfx.Canvas;
import gfx.Decoration;
import panels.FillPanel;
import panels.StrokePanel;

/**
 * This class allows the user to have access to the decoration Fill/Stroke styles
 * ina self-contained dialog box. They can choose their desired appearance and 
 * apply it to change the context of the canvas decoration.
 * @author DavidKramer
 *
 */
public class FillStrokeDialog extends JDialog {
	private Canvas canvas;
	private Decoration decoration;				// original decoration (useful if we cancel out dialog)
	private Decoration newDecoration;			// if the decoration has been changed
	private JTabbedPane tabPane;
	private JPanel dialogPanel;					// what all other panels will be glued to
	private FillPanel fillPanel;
	private StrokePanel strokePanel;
	private ButtonPanel buttonPanel;
	
	public FillStrokeDialog(Canvas canvas) {
		this.canvas = canvas;
		this.decoration = canvas.getDecoration();
		init();
	}
	
	/**
	 * Initializes all the components for the dialog.
	 */
	private void init() {
		newDecoration = (Decoration)decoration.clone();
		dialogPanel = new JPanel();
		dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
		tabPane = new JTabbedPane();
		fillPanel = new FillPanel(newDecoration);
		strokePanel = new StrokePanel(newDecoration);
		buttonPanel = new ButtonPanel();
		
		// add panels to tabs
		tabPane.addTab("1 - Fill", fillPanel);
		tabPane.addTab("2 - Stroke", strokePanel);
		
		dialogPanel.add(tabPane);
		dialogPanel.add(Box.createVerticalStrut(5));
		dialogPanel.add(buttonPanel);
		dialogPanel.add(Box.createVerticalStrut(10));
		add(dialogPanel);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Assign Fill / Stroke");
		setSize(new Dimension(750, 660));
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	//********************************************************
	//* 			  	 DECORATION METHODS			 	 	 *
	//********************************************************
	
	/**
	 * Updates the decoration object and assigns its attributes to a selection
	 * if there is one. If not, it just updates the decoration context of the
	 * canvas.
	 */
	private void updateDecoration() {		
		if (!decoration.equals(fillPanel.getDecoration())) {
			decoration.setFillColor(fillPanel.getSelectedColor());
		}
		
		if (!decoration.equals(strokePanel.getDecoration())) {
			decoration = (Decoration)strokePanel.getDecoration().clone();
		} 
		
		if (canvas.hasSelection()) {
			Decoration.decorateSelection(canvas.getSelection(), decoration);
		}
		
		canvas.setDecoration(decoration);
		canvas.getCanvasManager().getToolBar().getFillStrokeBtn().repaint();	// update swatch button
		canvas.repaint();
	}
	
	//********************************************************
	//* 				  MUTATOR METHODS					 *
	//********************************************************
	
	public void setNewDecoration(Decoration d) {
		newDecoration = d;
	}
	
	//********************************************************
	//* 				  ACCESSOR METHODS					 *
	//********************************************************
	
	public Decoration getDecoration() {
		return decoration;
	}
	
	public JTabbedPane getTabPane() {
		return tabPane;
	}
	
	//********************************************************
	//* 				 BUTTON PANEL CLASS					 *
	//********************************************************
	
	/**
	 * This is the panel that is at the bottom of the dialog and allows the user
	 * to apply the active decoration and dismiss the dialog.
	 * @author DavidKramer
	 *
	 */
	private class ButtonPanel extends JPanel {
		private JButton okBtn;
		private JButton cancelBtn;
		private JButton applyBtn;
		
		public ButtonPanel() {
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			createButtons();
		}
		
		private void createButtons() {
			okBtn = new JButton("Ok");
			okBtn.addActionListener( e -> {	// apply changes and close
				updateDecoration();
				dispose();
				canvas.setFocusable(true);
				canvas.requestFocus();
				setVisible(false);
			});
			
			cancelBtn = new JButton("Cancel");
			cancelBtn.addActionListener( e -> {	// just close dialog
				dispose();
				canvas.setFocusable(true);
				canvas.requestFocus();
				setVisible(false);
			});
			
			applyBtn = new JButton("Apply");
			applyBtn.addActionListener( e -> {	// apply changes, don't close
				updateDecoration();
			});
			
			add(okBtn);
			add(cancelBtn);
			add(applyBtn);
		}
	}

}
