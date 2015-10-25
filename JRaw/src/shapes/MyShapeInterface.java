package shapes;

import java.awt.Graphics;
import java.awt.Point;

import input.Mouse;

/**
 * Provides basic enforcement for handling mouse actions, manipulating the appearance,
 * and drawing itself to the screen.
 * @author DavidKramer
 *
 */
public interface MyShapeInterface {
	
	//********************************************************
	//* 			        DRAWING METHODS			      	 *
	//********************************************************

	public void render(Graphics g);
	
	//********************************************************
	//* 			     MOUSE HANDLING METHODS			     *
	//********************************************************
	
	public void handleClick(Mouse mouse);
	public void handleRelease(Mouse mouse);
	public void handleMove(Mouse mouse);
	public void handleDrag(Mouse mouse);
	
	//********************************************************
	//* 			  SHAPE MANIPULATION METHODS			 *
	//********************************************************
	
	public void move(Point p);
	public void resize(Point p, int direction);
	
}
