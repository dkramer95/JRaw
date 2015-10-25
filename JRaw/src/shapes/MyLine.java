package shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import input.Mouse;

public class MyLine extends MyShape {
	private boolean showPts;			// should we draw the start and end pts?
	private boolean isConstructing;
	private boolean canEdit;
	
	public MyLine() {
		super();
		startPt = new Point(0,0);
		endPt = new Point(0, 0);
	}
	
	//********************************************************
	//* 			       DRAWING METHODS				     *
	//********************************************************
	
	public void render(Graphics g) {
		final Graphics2D g2d = (Graphics2D)g.create();
		
		try {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			if (showPts) {
				g2d.setColor(Color.BLUE);
				g2d.fillOval(startPt.x, startPt.y, 10, 10);
				g2d.fillOval(endPt.x, endPt.y, 10, 10);
			}

			g2d.setColor(decoration.getStrokeColor());
			g2d.setStroke(decoration.getStroke());
			
			g2d.drawLine(startPt.x, startPt.y, endPt.x, endPt.y);	
			
		} finally {
			g2d.dispose();
		}
	}
	
	//********************************************************
	//* 			       MOUSE METHODS				     *
	//********************************************************
	
	public void handleClick(Mouse mouse) {
		if (!isConstructing) {
			isConstructing = true;
			startPt = mouse.getClickPt();
			endPt = mouse.getClickPt();
		} 
		
		if (canEdit) {
			System.out.println("Let's try to edit this!");
		}
	}

	public void handleRelease(Mouse mouse) {
		if (isConstructing) {
			isConstructing = false;
			updateBoundingBox();
			canEdit = true;
		}
	}

	public void handleMove(Mouse mouse) {}

	public void handleDrag(Mouse mouse) {
		if (!isConstructing) {
			startPt = (Point)mouse.getDragPt().clone();
			endPt = (Point)mouse.getDragPt().clone();
			isConstructing = true;
		} else {
			endPt = (Point)mouse.getDragPt();
		}
	}
	
	//********************************************************
	//* 			  SHAPE MANIPULATION METHODS			 *
	//********************************************************
	
	/**
	 * Moves the shape distance x and distance y from Point p. Overridden for
	 * this shape because of the way the start and end pts are.
	 * @param x
	 * @param y
	 */
	public void move(Point p) {
		if ((oldStartPt == null || oldEndPt == null) && !isMoving) {
			oldStartPt = startPt;
			oldEndPt = endPt;
		}
		
		Point newStartPt = new Point(oldStartPt.x + p.x, oldStartPt.y + p.y);
		Point newEndPt = new Point(oldEndPt.x + p.x, oldEndPt.y + p.y);

		isMoving = true;
		startPt = newStartPt;
		endPt = newEndPt;
		
		System.out.println("Old Start: " + oldStartPt + "Old End Pt: " + oldEndPt);
		System.out.println("Start Pt: " + startPt + " End Pt: " + endPt);
	}
	
	/**
	 * Updates the bounding box of the shape.
	 */
	public void update() {
		if (!isMoving && !isResizing) {
			oldStartPt = startPt;
			oldEndPt = endPt;
			System.out.println("Shape Updated!");
		}
		updateBoundingBox();
	}
	
	/**
	 * Resizes the shape based on the direction specified on the hovered resize handle.
	 * @param p
	 * @param direction
	 */
	public void resize(Point p, int direction) {
		if ((oldStartPt == null || oldEndPt == null)) {
			oldStartPt = startPt;
			oldEndPt = endPt;
		}
		
		Point newStartPt = new Point(oldStartPt.x, oldStartPt.y);
		Point newEndPt = new Point(oldEndPt.x, oldEndPt.y);
		
		isSelected = true;
		isResizing = true;
		
		switch (direction) {
		case RectHandle.NORTH:
			newStartPt.y += p.y;
			break;
		case RectHandle.SOUTH:
			newEndPt.y += p.y;
			break;
		case RectHandle.EAST:
			newEndPt.x += p.x;
			break;
		case RectHandle.WEST:
			newStartPt.x += p.x;
			break;
		case RectHandle.NORTH_EAST:
			newStartPt.y += p.y;
			newEndPt.x += p.x;
			break;
		case RectHandle.NORTH_WEST:
			newStartPt.x += p.x;
			newStartPt.y += p.y;
			break;
		case RectHandle.SOUTH_EAST:
			newEndPt.x += p.x;
			newEndPt.y += p.y;
			break;
		case RectHandle.SOUTH_WEST:
			newStartPt.x += p.x;
			newEndPt.y += p.y;
			break;
		}	
		
		startPt = newStartPt;
		endPt = newEndPt;
	}
	
	//********************************************************
	//* 			       ACCESSOR METHODS				     *
	//********************************************************
	
	/**
	 * Updates the bounding box of the line object. Overridden for this shape 
	 * because of the the way the start and end pts are.
	 */
	protected void updateBoundingBox() {
		int startX = Math.min(startPt.x, endPt.x);
		int startY = Math.min(startPt.y, endPt.y);
		int endX = Math.max(startPt.x, endPt.x);
		int endY = Math.max(startPt.y, endPt.y);
		
		Point p1 = new Point(startX, startY);
		Point p2 = new Point(endX, endY);
		
		boundBox.setStartPt(p1);
		boundBox.setEndPt(p2);
	}
	
	/**
	 * Returns the bound rect of the object. Overridden for this shape because of
	 * the way the start and end pts are.
	 */
	public Rectangle getBoundRect() {
		int startX = Math.min(startPt.x, endPt.x);
		int startY = Math.min(startPt.y, endPt.y);
		
		Point p1 = new Point(startX, startY);
		boundRect.setBounds(p1.x, p1.y, getWidth(), getHeight());
		return boundRect;
	}
}
