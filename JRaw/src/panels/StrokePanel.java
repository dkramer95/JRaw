package panels;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.colorchooser.AbstractColorChooserPanel;

import gfx.Decoration;

/**
 * This class creates all the necessary components required to modify
 * the stroke style of a decoration.
 * @author DavidKramer
 *
 */
public class StrokePanel extends FillPanel {
	// stroke appearance
	private JPanel strokeTypePanel;
	private JPanel strokePreviewPanel;
	private StrokePreview strokePreview;
	private SpinnerNumberModel strokeWidthModel;
	private JSpinner strokeWidthSpinner;
	private JRadioButton noStrokeBtn;
	private JRadioButton solidStrokeBtn;
	private ButtonGroup strokeBtnGroup;
	
	// stroke join
	private JPanel joinPanel;
	private JRadioButton joinMiterBtn;
	private JRadioButton joinRoundBtn;
	private JRadioButton joinBevelBtn;
	private ButtonGroup joinBtnGroup;
	private JLabel joinRoundLabel;
	private JLabel joinMiterLabel;
	private JLabel joinBevelLabel;
	
	// stroke cap
	private JPanel capPanel;
	private JRadioButton capButtBtn;
	private JRadioButton capRoundBtn;
	private JRadioButton capSquareBtn;
	private ButtonGroup capBtnGroup;
	private JLabel capButtLabel;
	private JLabel capRoundLabel;
	private JLabel capSquareLabel;
	
	public StrokePanel(Decoration decoration) {
		super(decoration);
		removeAll();	// clear any previous components
		createStrokeTypePanel();
		createPreviewPanel();
		createJoinPanel();
		createCapPanel();
		createColorPanel();
		addComponents();
	}
	
	/**
	 * Adds all of the panels to the main stroke panel.
	 */
	private void addComponents() {
		strokeTypePanel.add(strokePreviewPanel);
		strokeTypePanel.add(joinPanel);
		strokeTypePanel.add(capPanel);
		add(strokeTypePanel);
		add(colorPanel);
		if (!decoration.hasStroke()) {
			setControlsEnabled(false);
		}
	}
	
	//********************************************************
	//* 			   PANEL CREATION METHODS				 *
	//********************************************************
	
	private void createStrokeTypePanel() {
		strokeTypePanel = new JPanel();
		strokeTypePanel.setBorder(BorderFactory.createTitledBorder("Stroke Properties "));
		strokeTypePanel.setLayout(new BoxLayout(strokeTypePanel, BoxLayout.X_AXIS));
	}
	
	/**
	 * Creates all the components for the stroke preview panel including basic
	 * controls to turn a stroke on and off, as well as adjusting the width
	 * of the stroke. This contains a preview window for what the stroke will
	 * look like based on the current decoration context.
	 */
	private void createPreviewPanel() {		
		strokePreviewPanel = new JPanel();
		strokePreviewPanel.setLayout(new BoxLayout(strokePreviewPanel, BoxLayout.Y_AXIS));
		strokePreviewPanel.setBorder(BorderFactory.createTitledBorder("Width "));
		
		strokeWidthModel = new SpinnerNumberModel(5, 0, 50, 1);
		strokeWidthSpinner = new JSpinner(strokeWidthModel);
		strokeWidthSpinner.setPreferredSize(new Dimension(100, 10));
		strokeWidthSpinner.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		strokeWidthSpinner.setValue(decoration.getStrokeWidth());
		
		strokeWidthSpinner.addChangeListener( e -> {
			decoration.setStrokeWidth((int)strokeWidthSpinner.getValue());
			
			if ((int)strokeWidthSpinner.getValue() == 0) {
				decoration.setHasStroke(false);
				setControlsEnabled(false);
				noStrokeBtn.setSelected(true);
				updateNoStrokeBtn(Color.RED, Font.BOLD);
			} else {
				setControlsEnabled(true);
				solidStrokeBtn.setSelected(true);
			}
		});
		
		strokePreview = new StrokePreview(decoration);
		strokePreview.setBorder(BorderFactory.createTitledBorder("Preview "));
		strokePreview.setPreferredSize(new Dimension(200, 140));
		
		JPanel previewPanel = new JPanel();	// contains the preview of the stroke
		previewPanel.add(strokePreview);
		JPanel buttonPanel = new JPanel();	// contains the radio buttons
		buttonPanel.setLayout(new GridLayout(2, 1));
		
		noStrokeBtn = new JRadioButton("None");
		solidStrokeBtn = new JRadioButton("Solid");
		strokeBtnGroup = new ButtonGroup();
		
		if (decoration.hasStroke()) {
			solidStrokeBtn.setSelected(true);
		} else {
			noStrokeBtn.setSelected(true);
		}
		
		noStrokeBtn.addActionListener(e -> {
			decoration.setHasStroke(false);
			setControlsEnabled(false);
			updateNoStrokeBtn(Color.RED, Font.BOLD);	// make red and bold so it stands out
		});
		
		solidStrokeBtn.addActionListener(e -> {
			if (!decoration.hasStroke()) {
				if (strokeWidthSpinner.getValue().equals(0)) {	
					strokeWidthSpinner.setValue(1); // has to at least be 1 for a stroke to show up!
				}
				decoration.setHasStroke(true);
				setControlsEnabled(true);
				updateNoStrokeBtn(Color.BLACK, Font.PLAIN);	// return to normal style
			}
		});
		
		strokeBtnGroup.add(noStrokeBtn);
		strokeBtnGroup.add(solidStrokeBtn);
		
		// add components to button and preview panels
		buttonPanel.add(noStrokeBtn);
		buttonPanel.add(solidStrokeBtn);
		buttonPanel.add(strokeWidthSpinner);
		previewPanel.add(strokePreview);
		
		// add components to strokeWidthPanel
		strokePreviewPanel.add(buttonPanel);
		strokePreviewPanel.add(previewPanel);
	}
	
	/**
	 * Creates all the components required to modify the join of a stroke.
	 */
	private void createJoinPanel() {
		joinPanel = new JPanel();
		GridLayout joinPanelLayout = new GridLayout(3, 3);
		joinPanelLayout.setVgap(4);
		joinPanel.setBorder(BorderFactory.createTitledBorder("Joins "));
		joinPanel.setLayout(joinPanelLayout);
		
		joinRoundBtn = new JRadioButton("Join Round");
		joinMiterBtn = new JRadioButton("Join Miter");
		joinBevelBtn = new JRadioButton("Join Bevel");
		joinBtnGroup = new ButtonGroup();
		joinRoundLabel = new JLabel(new ImageIcon("res/icons/roundJoin.gif"));
		joinMiterLabel = new JLabel(new ImageIcon("res/icons/miterJoin.gif"));
		joinBevelLabel = new JLabel(new ImageIcon("res/icons/bevelJoin.gif"));
		
		joinBtnGroup.add(joinRoundBtn);
		joinBtnGroup.add(joinMiterBtn);
		joinBtnGroup.add(joinBevelBtn);
		
		// determine which join button should be selected
		if (decoration.getJoin() == BasicStroke.JOIN_MITER) {
			joinMiterBtn.setSelected(true);
		} else if (decoration.getJoin() == BasicStroke.JOIN_ROUND) {
			joinRoundBtn.setSelected(true);
		} else if (decoration.getJoin() == BasicStroke.JOIN_BEVEL) {
			joinBevelBtn.setSelected(true);
		}
		
		// join radio button listeners
		joinMiterBtn.addActionListener( e -> {
			decoration.setJoinType(BasicStroke.JOIN_MITER);
			strokePreview.repaint();
		});
		
		joinRoundBtn.addActionListener ( e -> {
			decoration.setJoinType(BasicStroke.JOIN_ROUND);
			strokePreview.repaint();
		});
		
		joinBevelBtn.addActionListener( e -> {
			decoration.setJoinType(BasicStroke.JOIN_BEVEL);
			strokePreview.repaint();
		});
		
		// add join components to joinPanel
		joinPanel.add(joinMiterLabel);
		joinPanel.add(joinMiterBtn);
		joinPanel.add(joinRoundLabel);
		joinPanel.add(joinRoundBtn);
		joinPanel.add(joinBevelLabel);
		joinPanel.add(joinBevelBtn);
	}
	
	/**
	 * Creates all the components required to modify the cap of a stroke.
	 */
	private void createCapPanel() {
		capPanel = new JPanel();
		GridLayout capPanelLayout = new GridLayout(3, 3);
		capPanelLayout.setVgap(4);
		capPanel.setBorder(BorderFactory.createTitledBorder("Caps "));
		capPanel.setLayout(capPanelLayout);
		
		capButtBtn = new JRadioButton("Cap Butt");
		capRoundBtn = new JRadioButton("Cap Round");
		capSquareBtn = new JRadioButton("Cap Square");
		capBtnGroup = new ButtonGroup();
		capButtLabel = new JLabel(new ImageIcon("res/icons/buttCap.gif"));
		capRoundLabel = new JLabel(new ImageIcon("res/icons/roundCap.gif"));
		capSquareLabel = new JLabel(new ImageIcon("res/icons/squareCap.gif"));
		
		capBtnGroup.add(capButtBtn);
		capBtnGroup.add(capRoundBtn);
		capBtnGroup.add(capSquareBtn);
		
		// determine which cap button should be selected
		if (decoration.getCap() == BasicStroke.CAP_BUTT) {
			capButtBtn.setSelected(true);
		} else if (decoration.getCap() == BasicStroke.CAP_ROUND) {
			capRoundBtn.setSelected(true);
		} else if (decoration.getCap() == BasicStroke.CAP_SQUARE) {
			capSquareBtn.setSelected(true);
		}
		
		// cap radio button listeners
		capButtBtn.addActionListener( e -> {
			decoration.setCapType(BasicStroke.CAP_BUTT);
			strokePreview.repaint();
		});
		
		capRoundBtn.addActionListener( e -> {
			decoration.setCapType(BasicStroke.CAP_ROUND);
			strokePreview.repaint();
		});
		
		capSquareBtn.addActionListener( e -> {
			decoration.setCapType(BasicStroke.CAP_SQUARE);
			strokePreview.repaint();
		});
		
		// add cap components to capPanel
		capPanel.add(capButtLabel);
		capPanel.add(capButtBtn);
		capPanel.add(capRoundLabel);
		capPanel.add(capRoundBtn);
		capPanel.add(capSquareLabel);
		capPanel.add(capSquareBtn);
	}
	
	/**
	 * Sets all of the control components enabled or disabled, depending on
	 * the boolean value that is passed in.
	 * @param b
	 */
	private void setControlsEnabled(boolean b) {
		strokeWidthSpinner.setEnabled(b);
		strokePreview.setRenderStroke(b);
		solidStrokeBtn.setSelected(b);
		strokePreview.repaint();
		colorPanel.setEnabled(b);
		joinPanel.setEnabled(b);
		joinRoundBtn.setEnabled(b);
		joinRoundLabel.setEnabled(b);
		joinMiterBtn.setEnabled(b);
		joinMiterLabel.setEnabled(b);
		joinBevelBtn.setEnabled(b);
		joinBevelLabel.setEnabled(b);
		capPanel.setEnabled(b);
		capButtBtn.setEnabled(b);
		capButtLabel.setEnabled(b);
		capRoundBtn.setEnabled(b);
		capRoundLabel.setEnabled(b);
		capSquareBtn.setEnabled(b);
		capSquareLabel.setEnabled(b);
		colorPanel.setVisible(b);
	}
	
	private void updateNoStrokeBtn(Color color, int fontStyle) {
		Font f = noStrokeBtn.getFont();
		noStrokeBtn.setForeground(color);
		noStrokeBtn.setFont(new Font(f.getName(), fontStyle, f.getSize()));
	}
	
	
	/**
	 * Creates the color picker panel to allow the user to select the 
	 * color of the stroke.
	 */
	private void createColorPanel() {
		JColorChooser colorChooser = new JColorChooser();
		AbstractColorChooserPanel[] panels = colorChooser.getChooserPanels();
		colorPanel = panels[2];
		colorPanel.setBorder(BorderFactory.createTitledBorder("Stroke Color "));
		colorPanel.getColorSelectionModel().setSelectedColor(decoration.getStrokeColor());
		colorModel = colorPanel.getColorSelectionModel();
		
		colorModel.addChangeListener( e -> {
			strokePreview.repaint();
			decoration.setStrokeColor(colorModel.getSelectedColor());
		});
	}
	
	
	
	//********************************************************
	//* 				 STROKE PREVIEW CLASS				 *
	//********************************************************
	/**
	 * This provides the user with a visual preview of what the stroke is going to 
	 * look like as they modify the properties of the stroke.
	 * @author DavidKramer
	 *
	 */
	private static class StrokePreview extends JLabel {
		private static boolean RENDER_STROKE;	// flag that controls rendering appearance
		private Decoration decoration;			// the decoration object to represent
		
		public StrokePreview(Decoration decoration) {
			this.decoration = decoration;
			RENDER_STROKE = true;
		}
		
		public void paint(Graphics g) {
			Graphics2D g2d = (Graphics2D)g;			
			int startX = 20;
			int startY = 20;
			int endX = getWidth() - 40;
			int endY = getHeight() - 40;
			
			if (RENDER_STROKE) {
				g2d.setStroke(decoration.getStroke());
				g2d.setColor(decoration.getStrokeColor());
			} else {
				g2d.setColor(Color.RED); 
				g2d.setStroke(new BasicStroke(1.0f));
				g2d.setFont(new Font("Courier New", Font.BOLD, 14));
				g2d.drawString("No Stroke", 65, 110);
			}
			g2d.drawRect(startX, startY, endX, endY);
			g2d.drawLine(startX, startY, endX + 20, endY + 20);
			g2d.drawLine(endX + 20, startY, startX, endY + 20);
			
			g2d.setColor(Color.BLACK);
			g2d.setStroke(new BasicStroke(1.0f));
			paintBorder(g2d);	
		}
		
		public void setRenderStroke(boolean b) {
			RENDER_STROKE = b;
		}
	}
}
