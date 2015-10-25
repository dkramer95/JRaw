package buttons;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JToolTip;
import javax.swing.SwingUtilities;

import dialogs.FillStrokeDialog;
import gfx.Canvas;
import gfx.Decoration;

/**
 * This class creates a button which the user can click on to modify the decoration of
 * shapes by launching the fill/stroke dialog. This button also renders a preview of
 * the current decoration style.
 * @author DavidKramer
 *
 */
public class FillStrokeButton extends JButton {
	private static final Dimension SIZE = new Dimension(36, 36);
	protected Decoration decoration;
	protected Canvas canvas;
	
	protected boolean hasFill;
	protected boolean hasStroke;
	
	public FillStrokeButton(Canvas canvas) {
		this.canvas = canvas;
		decoration = this.canvas.getDecoration();
		setPreferredSize(SIZE);
		setMinimumSize(SIZE);
		setMaximumSize(SIZE);
		setBorder(BorderFactory.createLoweredBevelBorder());
		setOpaque(true);
		setBorderPainted(true);
		setToolTipText("Double click to adjust fill/stroke");
		
		addMouseListener(new MouseListener() {
			public void mouseExited(MouseEvent e) {
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			
			public void mouseEntered(MouseEvent e) {	// changes the cursor to a paint bucket cursor when hovered
				Toolkit tk = Toolkit.getDefaultToolkit();
				Image img = tk.createImage("res/cursors/paintCursor.png");
				Cursor c = tk.createCustomCursor(img, new Point(0, 0), "paint");
				setCursor(c);
				repaint();
			}
			
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					FillStrokeDialog dialog = new FillStrokeDialog(canvas);
					repaint();
				}
				
				if(SwingUtilities.isRightMouseButton(e)) { // right click to recolor shapes in selection
					if (canvas.hasSelection()) {
						Decoration.decorateSelection(canvas.getSelection(), decoration);
						canvas.repaint();
					}
				}
			}
			
			// Unused mouse methods
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
		});
	}
	
	/**
	 * Creates a tooltip with a modified (darker) appearance.
	 */
	public JToolTip createToolTip() {
		JToolTip tip = super.createToolTip();
		tip.setBackground(new Color(0, 0, 0, 200));
		tip.setForeground(Color.WHITE);
		return tip;
	}
	
	//********************************************************
	//* 				  DRAWING METHODS					 *
	//********************************************************
	
	public void paint(Graphics g) {
		final Graphics2D g2d = (Graphics2D)g.create();
		
		try {
			
			if (canvas.getDecoration().hasFill()) {
				g2d.setColor(canvas.getDecoration().getFillColor());	// fill representation
				g2d.fillRect(0, 0, getWidth(), getHeight());		
			} else {
				g2d.setColor(Color.WHITE);
				g2d.fillRect(0, 0, getWidth(), getHeight());
				g2d.setColor(Color.RED);
				g2d.drawLine(0, 0, getWidth(), getHeight());
				g2d.drawLine(getWidth(), 0, 0, getHeight());
			}
			
			if (canvas.getDecoration().hasStroke()) {
				g2d.setStroke(new BasicStroke(2.0f));					
				g2d.setColor(canvas.getDecoration().getStrokeColor());	// stroke representation
				g2d.drawRect(4, 4, SIZE.width - 6, SIZE.height - 6);
			}
			
			// "Swatch" decoration
			Point p1 = new Point(SIZE.width, SIZE.height);
			Point p2 = new Point(SIZE.width / 2, SIZE.height);
			Point p3 = new Point(SIZE.width, SIZE.height / 2);
			Polygon p = new Polygon();
			p.addPoint(p1.x, p1.y);
			p.addPoint(p2.x, p2.y);
			p.addPoint(p3.x, p3.y);
			
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setColor(Color.WHITE);
			g2d.fillPolygon(p);
			g2d.setColor(Color.BLACK);
			g2d.fillRect(SIZE.width - 6, SIZE.height - 6, SIZE.width / 9, SIZE.height / 9);
			
			paintBorder(g2d);	
		} finally {
			g2d.dispose();
		}
		
	}
	
	//********************************************************
	//* 				  MUTATOR METHODS					 *
	//********************************************************
	
	public void setDecoration(Decoration d) {
		decoration = d;
	}
	
	//********************************************************
	//* 				  ACCESSOR METHODS					 *
	//********************************************************
	
	public Decoration getDecoration() {
		return decoration;
	}

}
