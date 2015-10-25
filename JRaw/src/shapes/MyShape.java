package shapes;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;

import gfx.Decoration;
import input.Mouse;
import menus.ShapePopupMenu;

/**
 * This is the base class for which all other shapes will inherit from. This class enforces basic
 * shape properties, and forces all sub-classes to implement their own methods for drawing
 * the shape to the screen.
 * @author DavidKramer
 *
 */
public abstract class MyShape implements Serializable, Cloneable {
	public static double SCALE_FACTOR = 1.0;
	
	protected Point oldStartPt;
	protected Point oldEndPt;
	protected Point startPt;
	protected Point endPt;
	
	protected Rectangle boundRect;
	protected BoundingBox boundBox;
	protected Decoration decoration;
	
	protected double area;
	protected double perimeter;
	protected int width;
	protected int height;
	protected int zIndex;					// stacking index of the shape
	
	protected boolean isDragging;			// are we dragging out the shape right now?
	protected boolean isMoving;
	protected boolean isResizing;
	protected boolean isSelected;
	protected boolean canMove;
	protected boolean canResize;
	
	protected static boolean constrainX;
	protected static boolean constrainY;
	
	protected ShapePopupMenu shapeMenu;		// the context menu of the shape
	
	/**
	 * Default constructor creates an empty shape.
	 */
	public MyShape() {
		startPt = new Point(0, 0);
		endPt = new Point(0, 0);
		boundRect = new Rectangle();
		boundBox = new BoundingBox(new Point(0, 0), new Point(0, 0)); // default initialization
		decoration = new Decoration();
	}
	
	/**
	 * Constructs a new shape with specified start and end pts. It also calls the
	 * checkPoints() method to ensure that the start / end pts are correct.
	 * @param startPt
	 * @param endPt
	 */
	public MyShape(Point startPt, Point endPt) {
		this.startPt = startPt;
		this.endPt = endPt;
		boundRect = new Rectangle();
		decoration = new Decoration();
		checkPoints();
	}
	
	/**
	 * Provides the capability of duplicating shapes by making an exact
	 * clone of a shape that is already exisitng.
	 */
	public Object clone() {
		try {
			return (MyShape)super.clone();
		} catch (CloneNotSupportedException e) {
			System.out.println("Clone not supported!");		// this should never be thrown!
		}
		return null;
	}
	
	//********************************************************
	//* 			  ABSTRACT DRAWING METHODS				 *
	//********************************************************
	
	public abstract void render(Graphics g);
	
	//********************************************************
	//* 			   ABSTRACT MOUSE METHODS				 *
	//********************************************************
	
	public abstract void handleClick(Mouse mouse);
	public abstract void handleRelease(Mouse mouse);
	public abstract void handleMove(Mouse mouse);
	public abstract void handleDrag(Mouse mouse);
	
	//********************************************************
	//* 			  SHAPE MANIPULATION METHODS			 *
	//********************************************************
	
	public void update() {
		System.out.println("Is Resizing? I shouldn't be but I " + isResizing);
		System.out.println("Am I moving? " + isMoving);
		if (!isMoving && !isResizing) {
			oldStartPt = startPt;
			oldEndPt = endPt;
			System.out.println("Shape Updated!");
		}
		checkPoints();
		updateBoundingBox();
	}
	
	/**
	 * Moves the shape distance x and distance y from Point p.
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
		checkPoints(newStartPt, newEndPt);
		isMoving = true;		
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
		checkPoints(newStartPt, newEndPt);
	}
	
	//********************************************************
	//* 				  UTILITY METHODS					 *
	//********************************************************
	
	protected void updateBoundingBox() {
		if (getWidth() != 0 && getHeight() != 0) {
			boundBox.setStartPt(startPt);
			boundBox.setEndPt(endPt);
			System.out.println("Bounding Box Updated!");
		}
	}
	
	/**
	 * Checks to make sure that the start and end points of the shape are correct, in that
	 * the end point should never be less than start point, and vice versa.
	 */
	protected void checkPoints() {
		int startX = Math.min(startPt.x, endPt.x);
		int startY = Math.min(startPt.y, endPt.y);
		int endX = Math.max(startPt.x, endPt.x);
		int endY = Math.max(startPt.y, endPt.y);
		
		startPt = new Point(startX, startY);
		endPt = new Point(endX, endY);	
	}
	
	/**
	 * Checks to make sure that the start and end points of the shape are correct, in that
	 * the end point should never be less than start point, and vice versa. This method
	 * allows you to specify 2 points to compare.
	 */
	protected void checkPoints(Point p1, Point p2) {
		int startX = Math.min(p1.x, p2.x);
		int startY = Math.min(p1.y, p2.y);
		int endX = Math.max(p1.x, p2.x);
		int endY = Math.max(p1.y, p2.y);
		
		startPt = new Point(startX, startY);
		endPt = new Point(endX, endY);
	}
	
	//********************************************************
	//* 			  STATIC UTILITY METHODS				 *
	//********************************************************
	
	/**
	 * Returns a new point that is the distance between the current drag point and the
	 * starting click point of the mouse.
	 * @param mouse
	 * @return
	 */
	public static Point getOffsetPt(Mouse mouse) {
		int x = mouse.getDragPt().x - mouse.getClickPt().x;
		int y = mouse.getDragPt().y - mouse.getClickPt().y;
		
		return new Point(x, y);
	}
	
	//********************************************************
	//* 				  MUTATOR METHODS					 *
	//********************************************************
	
	public void setStartPt(Point startPt) {
		this.startPt = startPt;
		checkPoints();
	}
	
	public void setEndPt(Point endPt) {
		this.endPt = endPt;
		checkPoints();
	}
	
	public void setDecoration(Decoration decoration) {
		this.decoration = decoration;
	}
	
	public void setIsSelected(boolean b) {
		isSelected = b;
	}
	
	public void setIsMoving(boolean b) {
		isMoving = b;
	}
	
	public void setIsResizing(boolean b) {
		isResizing = b;
	}
	
	public void setZIndex(int z) {
		zIndex = z;
	}
	
	public static void setConstrainX(boolean b) {
		constrainX = b;
	}
	
	public static void setConstrainY(boolean b) {
		constrainY = b;
	}
	
	public void showBoundingBox(boolean b) {
		boundBox.setVisible(b);
	}
	
	//********************************************************
	//* 				  ACCESSOR METHODS					 *
	//********************************************************
	
	public Point getStartPt() {
		return startPt;
	}
	
	public Point getEndPt() {
		return endPt;
	}
	
	public BoundingBox getBoundingBox() {
		return boundBox;
	}
	
	public Decoration getDecoration() {
		return decoration;
	}
	
	public Rectangle getBoundRect() {
		boundRect.setBounds(startPt.x, startPt.y, getWidth(), getHeight());	
		return boundRect;
	}
	
	public double getArea() {
		return area = width * height;
	}
	
	public double getPerimeter() {
		return perimeter = (width + width) + (height + height);
	}
	
	public int getWidth() {
		return width = Math.abs(endPt.x - startPt.x);
	}
	
	public int getHeight() {
		return height = Math.abs(endPt.y - startPt.y);
	}
	
	public int getZIndex() {
		return zIndex;
	}
	
	public boolean isDragging() {
		return isDragging;
	}
	
	public boolean isMoving() {
		return isMoving;
	}
	
	public boolean isResizing() {
		return isResizing;
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	
	public boolean canMove() {
		return canMove;
	}
	
	public boolean canResize() {
		return canResize;
	}	
	
}
