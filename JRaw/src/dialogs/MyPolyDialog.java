package dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import gfx.Canvas;
import gfx.Decoration;
import shapes.MyPoly;

/**
 * This class allows the user to specify n-pts and a radius to create a perfect
 * polygon.
 * @author DavidKramer
 *
 */
public class MyPolyDialog extends JDialog {
	private Canvas canvas;
	private Decoration decoration;
	private JPanel dialogPanel;
	private MyPoly poly;			// the shape that we will add to the canvas!
	
	// Radius Panel
	private JPanel radiusPanel;
	private JLabel radiusLabel;
	private SpinnerNumberModel radiusModel;
	private JSpinner radiusSpinner;
	private JLabel unitsLabel;
	
	// Points Panel
	private JPanel ptsPanel;
	private JLabel ptsLabel;
	private JCheckBox upRightBox;
	private SpinnerNumberModel ptsModel;
	private JSpinner ptsSpinner;
	
	// Preview Panel
	private JPanel previewPanel;
	private PolyPreview polyPreview;
	
	// Button Panel
	private JPanel btnPanel;
	private JButton okBtn;
	private JButton cancelBtn;
	
	private boolean didCancel;
	
	public MyPolyDialog(Canvas canvas) {
		this.canvas = canvas;
		this.decoration = canvas.getDecoration();
		init();
		
		setModal(true);
		setTitle("Create Polygon");
		setSize(new Dimension(350, 400));
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	
	private void init() {
		dialogPanel = new JPanel();
		dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
		createRadiusPanel();
		createPointsPanel();
		createPreviewPanel();
		createButtonPanel();
		polyPreview.setPoly((int) ptsSpinner.getValue(), upRightBox.isSelected());
		
		dialogPanel.add(radiusPanel, BorderLayout.NORTH);
		dialogPanel.add(ptsPanel);
		dialogPanel.add(Box.createVerticalStrut(10));
		dialogPanel.add(previewPanel);
		dialogPanel.add(Box.createVerticalGlue());
		dialogPanel.add(btnPanel);
		add(dialogPanel);
	}
	
	/**
	 * Creates all the components to modify the radius of a polygon.
	 */
	private void createRadiusPanel() {
		radiusPanel = new JPanel();
		radiusPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		radiusPanel.setMaximumSize(new Dimension(300, 40));
		radiusLabel = new JLabel("Radius");
		radiusModel = new SpinnerNumberModel(50, 25, 1000, 10);
		radiusSpinner = new JSpinner(radiusModel);
		radiusSpinner.setMaximumSize(new Dimension(25, 25));
		unitsLabel = new JLabel("px");
		
		radiusSpinner.addChangeListener( e -> {
			System.out.println("Radius changed!");
		});
		
		// add components to radius panel
		radiusPanel.add(radiusLabel);
		radiusPanel.add(radiusSpinner);
		radiusPanel.add(unitsLabel);
	}
	
	/**
	 * Creates all of the components to adjust the n-pts of a polygon.
	 */
	private void createPointsPanel() {
		ptsPanel = new JPanel();
		ptsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		ptsPanel.setMaximumSize(new Dimension(300, 40));
		ptsLabel = new JLabel("No. Pts");
		ptsModel = new SpinnerNumberModel(5, 3, 20, 1);	// max (20-gon)
		ptsSpinner = new JSpinner(ptsModel);
		upRightBox = new JCheckBox("Upright", true);
		ptsSpinner.setMaximumSize(new Dimension(25, 25));
		
		ptsSpinner.addChangeListener( e -> {
			polyPreview.setPoly((int) ptsSpinner.getValue(), upRightBox.isSelected());
			polyPreview.repaint();
		});
		
		upRightBox.addActionListener( e -> {
			polyPreview.setPoly((int) ptsSpinner.getValue(), upRightBox.isSelected());
			polyPreview.repaint();
		});
		
		// add components to pts panel
		ptsPanel.add(ptsLabel);
		ptsPanel.add(ptsSpinner);
		ptsPanel.add(upRightBox);
	}
	
	/**
	 * Creates the panel for containing the live preview of the polygon.
	 */
	private void createPreviewPanel() {
		previewPanel = new JPanel();
		previewPanel.setBorder(BorderFactory.createTitledBorder("Preview "));
		previewPanel.setPreferredSize(new Dimension(200, 200));
		previewPanel.setMaximumSize(new Dimension(200, 200));
		previewPanel.setAlignmentX(CENTER_ALIGNMENT);
		polyPreview = new PolyPreview(decoration);
		previewPanel.add(polyPreview);
	}
	
	/**
	 * Creates the
	 */
	private void createButtonPanel() {
		btnPanel = new JPanel();
		okBtn = new JButton("Ok");
		cancelBtn = new JButton("Cancel");
		
		okBtn.addActionListener( e -> {
			addPolyToCanvas();
			dispose();
		});
		
		cancelBtn.addActionListener( e -> {
			didCancel = true;
			dispose();
		});
		
		btnPanel.add(okBtn);
		btnPanel.add(cancelBtn);
	}
	
	private void addPolyToCanvas() {
		poly = MyPoly.createFromPoly(polyPreview.getPoly());
		poly.setDecoration(decoration);
		poly.move(new Point(300, 300));
		poly.update();
		canvas.addShape(poly);
	}
	
	public boolean didCancel() {
		return didCancel;
	}
	
	//********************************************************
	//* 				  POLY PREVIEW CLASS				 *
	//********************************************************
	/**
	 * This class provides the user with a visual preview of what the polygon is going
	 * to look like that they are creating from a dialog.
	 * @author DavidKramer
	 *
	 */
	private static class PolyPreview extends JLabel {
		private Decoration decoration;
		private Polygon previewPoly;		// the poly that we will preview
		
		public PolyPreview(Decoration decoration) {
			this.decoration = decoration;
			setPreferredSize(new Dimension(160, 160));
			previewPoly = generateNPoly(3, false);
		}
		
		public void setPoly(int numSides, boolean upRight) {
			previewPoly = generateNPoly(numSides, upRight);
		}
		
		public void paint(Graphics g) {
			final Graphics2D g2d = (Graphics2D)g.create();
			System.out.println("Previewing Poly Render");
			try {
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setStroke(decoration.getStroke());
				g2d.setColor(decoration.getStrokeColor());
				g2d.drawPolygon(previewPoly);
			
			} finally {
				g2d.dispose();
			}
		}
		
		public Polygon getPoly() {
			return previewPoly;
		}
		
		/**
		 * Generates a polygon with nSides.
		 * @throws IllegalArgumentException if nSides is less than 3. 
		 * @param nSides num sides of the polygon
		 * @param upRight should the polygon be rendered upright?
		 * @return
		 */
		private static Polygon generateNPoly(int nSides, boolean upRight) {
			if (nSides < 3) {	// must be at least 3 sides
				throw new IllegalArgumentException("NumSides must be > 3");
			}
			int[] x = new int[nSides];
			int[] y = new int[nSides];
			int[] center = {80, 80};
			int radius = 70;
			
			double theta = upRight ? -Math.PI / 2 : 0 / nSides;
			
			for (int i = 0; i < nSides; i++) {
				x[i] = (int) (radius * Math.cos(2 * Math.PI * i/nSides + theta) + center[0]);
				y[i] = (int) (radius * Math.sin(2 * Math.PI * i/nSides + theta) + center[1]);
				System.out.println("X [" + i + "] = " + x[i]);
				System.out.println("Y [" + i + "] = " + y[i]);
				System.out.println("Theta: " + theta);
			}
			
			return new Polygon(x, y, nSides);
		}
	}
}