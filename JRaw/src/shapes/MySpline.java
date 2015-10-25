package shapes;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.CubicCurve2D;
import java.io.Serializable;

/**
 * Represents a simple cubic spine that has 2 ctrl pts and a start 
 * and end pt.
 * @author DavidKramer
 *
 */
public class MySpline implements Serializable {
	private AnchorPoint p0;
	private ControlAnchorPoint c1;
	private ControlAnchorPoint c2;
	private AnchorPoint p3;
	private Point2D pFinal;
	private double t;
	
	public MySpline() {	// default point initialization
		p0 = new AnchorPoint(0, 0);
		c1 = new ControlAnchorPoint(0, 0);
		c2 = new ControlAnchorPoint(0, 0);
		p3 = new AnchorPoint(0, 0);
	}
	
	public void render(Graphics g) {
		final Graphics2D g2d = (Graphics2D)g.create();
		if (c1 == null && c2 == null) {
			return;
		}
		CubicCurve2D.Double curve = new CubicCurve2D.Double(p0.x, p0.y, c1.x, c1.y,		// curve that will render
															c2.x, c2.y, p3.x, p3.y);
		try {
			for (t = 0; t < 1; t += .01) {
//				calcPFinal();
//				int x = (int)pFinal.getX();
//				int y = (int)pFinal.getY();
//				g2d.fillRect((int)pFinal.getX(), (int)pFinal.getY(), 2, 2);
				g2d.setStroke(new BasicStroke(2.0f));
				g2d.draw(curve);
			}
		} finally {
			g2d.dispose();
		}
	}
	
	//********************************************************
	//* 				  MUTATOR METHODS					 *
	//********************************************************
	
	public void setP0(AnchorPoint p0) {
		this.p0 = p0;
	}
	
	public void setC1(ControlAnchorPoint c1) {
		this.c1 = c1;
	}
	
	public void setC2(ControlAnchorPoint c2) {
		this.c2 = c2;
	}
	
	public void setP3(AnchorPoint p3) {
		this.p3 = p3;
	}
	
	//********************************************************
	//* 				  ACCESSOR METHODS					 *
	//********************************************************
	
	public AnchorPoint getP0() {
		return p0;
	}
	
	public ControlAnchorPoint getC1() {
		return c1;
	}
	
	public ControlAnchorPoint getC2() {
		return c2;
	}
	
	public AnchorPoint getP3() {
		return p3;
	}
	
	/**
	 * Calculates the value of a point on the curve from 0 <= t <= 1
	 * and sets the value of pFinal.
	 * @return
	 */
	private Point2D calcPFinal() {
		pFinal = new Point2D(0, 0);
		pFinal.x = Math.pow(1 - t, 3) * p0.x 
					+ Math.pow(1 - t, 2) * 3 * t * c1.x
					+ (1 - t) * 3 * t * t * c2.x
					+ t * t * t * p3.x;
		
		pFinal.y = Math.pow(1 - t, 3) * p0.y 
				+ Math.pow(1 - t, 2) * 3 * t * c1.y
				+ (1 - t) * 3 * t * t * c2.y
				+ t * t * t * p3.y;
		pFinal = new Point2D(pFinal.x, pFinal.y);
		return pFinal;
	}
}
