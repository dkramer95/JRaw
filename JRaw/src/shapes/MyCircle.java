package shapes;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * This class represents a basic circle.
 * @author DavidKramer
 *
 */
public class MyCircle extends MyRect {

	//********************************************************
	//* 				  DRAWING METHODS					 *
	//********************************************************
	public void render(Graphics g) {
		final Graphics2D g2d = (Graphics2D)g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		try {
			if (decoration.hasStroke()) {
				g2d.setColor(decoration.getStrokeColor());
				g2d.setStroke(decoration.getStroke());
				g2d.drawOval(startPt.x, startPt.y, getWidth(), getHeight());
			}
			
			if (decoration.hasFill()) {
				g2d.setColor(decoration.getFillColor());
				g2d.fillOval(startPt.x, startPt.y, getWidth(), getHeight());
			}
			
			if (boundBox.isVisible() && isSelected) {
				boundBox.render(g);	
			}
			
		} finally {
			g2d.dispose();
		}
	}
	
	
}
