package buttons;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import gfx.Canvas;
import gfx.Decoration;

public class SwatchButton extends FillStrokeButton {
	
	private static final Dimension SIZE = new Dimension(20, 20);
	
	public SwatchButton(Canvas canvas) {
		super(canvas);
		setPreferredSize(SIZE);
		setMaximumSize(SIZE);
		decoration.setHasStroke(false);
		setToolTipText("Click to apply as fill to selection!");
		MouseListener[] listeners = getMouseListeners();
		for (MouseListener mouseListener : listeners) {
			removeMouseListener(mouseListener);
		}
		addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {				
				if (canvas.hasSelection()) {
					Decoration.decorateSelection(canvas.getSelection(), decoration, true, false);
					canvas.repaint();
				}
			}
			
			// Unused mouse methods
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
		});
	}
	
	public void paint(Graphics g) {
		final Graphics2D g2d = (Graphics2D)g.create();
		
		try {
			
			if (decoration.hasFill()) {
				g2d.setColor(decoration.getFillColor());	// fill representation
				g2d.fillRect(0, 0, getWidth(), getHeight());		
			} else {
				g2d.setColor(Color.WHITE);
				g2d.fillRect(0, 0, getWidth(), getHeight());
				g2d.setColor(Color.RED);
				g2d.drawLine(0, 0, getWidth(), getHeight());
				g2d.drawLine(getWidth(), 0, 0, getHeight());
			}
			
			if (decoration.hasStroke()) {
				g2d.setStroke(new BasicStroke(2.0f));					
				g2d.setColor(decoration.getStrokeColor());	// stroke representation
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

}
