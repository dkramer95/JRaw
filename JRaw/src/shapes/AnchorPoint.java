package shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import input.Mouse;

/**
 * This class represents a basic anchor pt with an (x,y) coordinate. Also provides
 * basic hover detection capability, to determine if the anchor pt is hovered,
 * so that it can be edited later.
 * @author DavidKramer
 *
 */
public class AnchorPoint extends Point implements MyShapeInterface {
	protected static final Color COLOR = Color.BLUE;
	protected static final Color ACTIVE_COLOR = Color.RED;
	protected static final int WIDTH = 5;
	protected static final int HEIGHT = 5;
	
	protected Rectangle boundRect;
	protected boolean isHovered;			// is the mouse over the anchor pt?
	protected boolean isActive;			// is this pt active so that we can manipulate it?
	protected boolean isMoving;			// are we moving the anchor pt?
	protected int width;
	protected int height;
	
	protected Color color;

	public AnchorPoint(int x, int y) {
		super(x, y);
		width = WIDTH;
		height = HEIGHT;
		boundRect = new Rectangle(x, y, WIDTH, HEIGHT);
		color = COLOR;
	}
	
	public AnchorPoint(Point p) {
		super(p.x, p.y);
		width = WIDTH;
		height = HEIGHT;
		System.out.println(p);
		boundRect = new Rectangle(p.x, p.y, WIDTH, HEIGHT);
		color = COLOR;
	}
	
	//********************************************************
	//* 				  DRAWING METHODS					 *
	//********************************************************
	
	public void render(Graphics g) {
		final Graphics2D g2d = (Graphics2D)g.create();
		
		try {
			g2d.setColor(color);
			g2d.setStroke(new BasicStroke(2.0f));
			g2d.drawRect(x, y, width, height);	// outer rectangle outline
			g2d.setColor(Color.WHITE);
			g2d.fillRect(x, y, width, width);	// inner rectangle
			g2d.setColor(color);
			g2d.fillRect(x + 1, y + 1, width - 2, width - 2);
		} finally {
			g2d.dispose();
		}
	}
	
	/**
	 * Checks to see if Point p is contained within the boundaries of the anchor pt.
	 * @param p
	 * @return
	 */
	public boolean checkHover(Point p) {
		return isHovered = boundRect.contains(p) ? true : false;
	}
	
	//********************************************************
	//* 			       MOUSE METHODS				     *
	//********************************************************

	public void handleClick(Mouse mouse) {
	}

	public void handleRelease(Mouse mouse) {
	}

	public void handleMove(Mouse mouse) {
	
	}
	
	public void handleDrag(Mouse mouse) {
		System.out.println("Anchor pt drag");
		setLocation(mouse.getDragPt());
	}
	
	//********************************************************
	//* 			  SHAPE MANIPULATION METHODS			 *
	//********************************************************

	public void move(Point p) {
	}

	public void resize(Point p, int direction) {
		//Do nothing. We shouldn't resize an anchor pt
	}
	
	//********************************************************
	//* 			       MUTATOR METHODS				     *
	//********************************************************
	
	public void setIsActive(boolean b) {
		isActive = b;
	}
	
	public void displayActive(boolean b) {
		if (b) {
			color = ACTIVE_COLOR;
		} else {
			color = COLOR;
		}
	}
	
	/**
	 * Slightly enlarges the size of the anchor pt to make it easier to see if it is
	 * hovered or not.
	 * @param b
	 */
	public void displayHover(boolean b) {
		if (b) {
			width = 8;
			height = 8;
		} else {
			width = WIDTH;
			height = HEIGHT;
		}
	}
	
	//********************************************************
	//* 			       ACCESSOR METHODS				     *
	//********************************************************
	
	public boolean isHovered() {
		return isHovered;
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public boolean isMoving() {
		return isMoving;
	}

}
