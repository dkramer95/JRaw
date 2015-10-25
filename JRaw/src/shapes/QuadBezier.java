package shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Represents a basic Quadratic Bezier curve which consists of 3 pts and 1 ctrl pt.
 * @author DavidKramer
 *
 */
public class QuadBezier {
	private Point2D p0;
	private Point2D c1;					// control pt
	private Point2D p2;
	private Point2D pFinal;
	private double t;
	
	public QuadBezier(Point2D p0, Point2D c1, Point2D p2, double t) {
		this.p0 = p0;
		this.c1 = c1;
		this.p2 = p2;
		this.t = t;
	}
	
	public QuadBezier() {}
	
	//********************************************************
	//* 				ACCESSOR METHODS					 *
	//********************************************************
	
	public Point2D getP0() {
		return p0; 
	}
	
	public Point2D getC1() {
		return c1;
	}
	
	public Point2D getP2() {
		return p2;
	}
	
	public Point2D getPFinal() {
		if (pFinal == null) {
			calcPFinal();
		}
		return pFinal;
	}
	
	//********************************************************
	//* 				MUTATOR METHODS						 *
	//********************************************************
	
	public void setP0(Point2D p0) {
		this.p0 = p0;
	}
	
	public void setC1(Point2D c1) {
		this.c1 = c1;
	}
	
	public void setP2(Point2D p2) {
		this.p2 = p2;
	}
	
	public void calcPFinal() {
		pFinal = new Point2D(0, 0);
		
		double x = Math.pow(1 - t, 2) * p0.getX() + (1 - t) * 2 * t * c1.getX() + (t * t) * p2.getX();
		double y = Math.pow(1 - t, 2) * p0.getY() + (1 - t) * 2 * t * c1.getY() + (t * t) * p2.getY();
		
		pFinal.setX(x);
		pFinal.setY(y);
	}
	
	//********************************************************
	//* 				DRAWING METHODS						 *
	//********************************************************
	
	public void render(Graphics g) {
		final Graphics2D g2d = (Graphics2D)g.create();
		
		try {
			for (t = 0; t < 1; t += .005) {
				calcPFinal();
				g.fillRect((int)pFinal.getX(), (int)pFinal.getY(), 2, 2);
			}
			
			g.setColor(Color.GREEN);
			g.fillOval((int)p0.getX(), (int)p0.getY(), 5, 5);
			g.fillOval((int)p2.getX(), (int)p2.getY(), 5, 5);
			g.setColor(Color.RED);
			g.fillOval((int)c1.getX(), (int)c1.getY() + 20, 5, 5);	
		} finally {
			g2d.dispose();
		}
	}
}
