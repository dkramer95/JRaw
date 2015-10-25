package shapes;

import java.awt.Rectangle;
import java.io.Serializable;

public class Point2D extends Rectangle implements Serializable {

	protected double x;
	protected double y;
	
	public Point2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	//********************************************************
	//* 				ACCESSOR METHODS					 *
	//********************************************************
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	//********************************************************
	//* 				MUTATOR METHODS						 *
	//********************************************************
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
}
