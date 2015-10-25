package shapes;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

import input.Mouse;

public class BoundingBox implements Serializable {
	private Point startPt;
	private Point endPt;
	private int width;
	private int height;
	
	private RectHandle activeHandle;
	private ArrayList<RectHandle> handles;
	
	private boolean isVisible;	// should the resize handles be rendered?

	public BoundingBox(Point startPt, Point endPt) {
		this.startPt = startPt;
		this.endPt = endPt;
		this.width = Math.abs(endPt.x - startPt.x);
		this.height = Math.abs(endPt.y - startPt.y);
		handles = new ArrayList<>();		
		buildHandles();
	}
	
	//********************************************************
	//* 				  DRAWING METHODS					 *
	//********************************************************
	
	public void render(Graphics g) {
		final Graphics2D g2d = (Graphics2D)g.create();
		
		try {
			if (isVisible) {
				updateRectHandles();
				for (int i = 0; i < handles.size(); i++) {
					RectHandle r = handles.get(i);
					r.render(g2d);
				}
			}
		} finally {
			g2d.dispose();
		}
	}
	
	//********************************************************
	//* 				  UTILITY METHODS					 *
	//********************************************************
	
	/**
	 * Checks to see if Point p is above any of the resize handles in the bounding box.
	 * @param p Point (should be from a mouse)
	 * @return
	 */
	public boolean checkHandleHover(Point p) {
		for (int i = 0; i < handles.size(); i++) {
			if(handles.get(i).checkHover(p)) {
				activeHandle = handles.get(i);
				return true;
			} else {
				activeHandle = null; // clear out if not hovered
			}
		}
		return false;
	}
	
	/**
	 * Updates all 8 resize handles and ensures that they are in the correct spot.
	 */
	public void updateRectHandles() {
		int startX = Math.min(startPt.x, endPt.x);
		int startY = Math.min(startPt.y, endPt.y);
		int endX = Math.max(startPt.x, endPt.x);
		int endY = Math.max(startPt.y, endPt.y);
		
		startPt = new Point(startX, startY);
		endPt = new Point(endX, endY);	
		width = Math.abs(startPt.x - endPt.x);
		height = Math.abs(startPt.y - endPt.y);
		
		handles.clear();
		buildHandles();
	}
	
	/**
	 * Creates N,S,E,W,NE,NW,SE,SW resize handles.
	 */
	private void buildHandles() {
		RectHandle northRect = new RectHandle(startPt.x + (width / 2), startPt.y, RectHandle.NORTH);
		RectHandle southRect = new RectHandle(startPt.x + (width / 2), endPt.y, RectHandle.SOUTH);
		RectHandle eastRect = new RectHandle(endPt.x, endPt.y - (height / 2), RectHandle.EAST);
		RectHandle westRect = new RectHandle(startPt.x, endPt.y - (height / 2), RectHandle.WEST);
		RectHandle northWestRect = new RectHandle(startPt.x, startPt.y, RectHandle.NORTH_WEST);
		RectHandle northEastRect = new RectHandle(endPt.x, startPt.y, RectHandle.NORTH_EAST);
		RectHandle southEastRect = new RectHandle(endPt.x, endPt.y, RectHandle.SOUTH_EAST);
		RectHandle southWestRect = new RectHandle(startPt.x , endPt.y, RectHandle.SOUTH_WEST);
		handles.add(northRect);
		handles.add(southRect);
		handles.add(eastRect);
		handles.add(westRect);
		handles.add(northWestRect);
		handles.add(northEastRect);
		handles.add(southEastRect);
		handles.add(southWestRect);
	}
	
	//********************************************************
	//* 				    MOUSE METHODS					 *
	//********************************************************
	 
	public void handleClick(Mouse mouse) {
		checkHandleHover(mouse.getClickPt());
	}

	public void handleRelease(Mouse mouse) {
	}
	
	public void handleMove(Mouse mouse) {
		checkHandleHover(mouse.getMovePt());
	}
	
	public void handleDrag(Mouse mouse) {
	}
	
	
	//********************************************************
	//* 				  MUTATOR METHODS					 *
	//********************************************************
	
	public void setStartPt(Point p) {
		startPt = p;
	}
	
	public void setEndPt(Point p) {
		endPt = p;
	}
	
	public void setVisible(boolean b) {
		isVisible = b;
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
	
	public boolean isVisible() {
		return isVisible;
	}
	
	public ArrayList<RectHandle> getHandles() {
		return handles;
	}
	
	public RectHandle getActiveHandle() {
		return activeHandle;
	}
	
	public int getWidth() {
		return width = Math.abs(startPt.x - endPt.x);
	}
	
	public int getHeight() {
		return height = Math.abs(startPt.y - endPt.y);
	}
	
	public String toString() {
		String str = "[Bound Box] -> StartPt: " + startPt + ", EndPt: " + endPt + ", Width: " 
					 + getWidth() + ", Height: " + getHeight() + ", isVisible?: " + isVisible;
		return str;
	}

}
