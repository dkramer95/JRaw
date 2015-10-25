package shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

public class ControlAnchorPoint extends AnchorPoint {

	public ControlAnchorPoint(Point p) {
		super(p);
		// TODO Auto-generated constructor stub
	}
	
	public ControlAnchorPoint(int x, int y) {
		super(x, y);
	}
	
	public void render(Graphics g) {
		final Graphics2D g2d = (Graphics2D)g.create();
		
		try {
			g2d.setColor(color);
			g2d.setStroke(new BasicStroke(2.0f));
			g2d.drawOval(x, y, width, height);	// outer rectangle outline
			g2d.setColor(Color.WHITE);
			g2d.fillOval(x, y, width, width);	// inner rectangle
			g2d.setColor(color);
			g2d.fillOval(x + 1, y + 1, width - 2, width - 2);
		} finally {
			g2d.dispose();
		}
	}
	
	public void setColor(Color color) {
		this.color = color;
	}

}
