package shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.ArrayList;

import input.Mouse;

/**
 * This class represents a polygonal shape that contains n-pts.
 * @author DavidKramer
 *
 */
public class MyPoly extends MyShape {
	private static final int MIN_PTS = 4;			// minimum of 4 pts to create a polygon
	
	protected ArrayList<AnchorPoint> anchorPts;
	protected ArrayList<AnchorPoint> oldAnchorPts;
	protected AnchorPoint beginPt;					// the first pt that gets added to begin construction
	protected AnchorPoint lastPt;						// the last pt that we added
	protected AnchorPoint activePt;					// the active pt that is ready to be added
	
	protected boolean showAnchorPts;
	protected boolean isConstructing;					// are we actively adding pts to the polygon?
	private boolean isConcave;
	private boolean isConvex;
	private boolean isComplex;
	protected boolean didClose;						// did we just close the polygon? (so we know to end construction)
	protected boolean canClose;
	protected boolean isClosed;
	
	public MyPoly() {
		super();
		anchorPts = new ArrayList<>();
		oldAnchorPts = new ArrayList<>();
		lastPt = new AnchorPoint(0,0);	// default initialization
		showAnchorPts = true;
		isConstructing = false;
	}
	
	public MyPoly(ArrayList<AnchorPoint> pts) {
		super();
		anchorPts = pts;
	}
	
	//********************************************************
	//* 				  DRAWING METHODS					 *
	//********************************************************

	public void render(Graphics g) {
		final Graphics2D g2d = (Graphics2D)g.create();
		
		try {
			for (int i = 0; i < anchorPts.size(); i++) {
				g2d.setColor(Color.BLUE);
				AnchorPoint curPt = anchorPts.get(i);
				
				if (isConstructing && anchorPts.size() == 1) { // only added the start pt
					g2d.drawLine(beginPt.x, beginPt.y, activePt.x, activePt.y);
					if (showAnchorPts) {
						beginPt.render(g2d);	
					}
					return;
				}
				int j = i + 1;	// next index
				
				if (j > anchorPts.size() - 1) {
					return;
				}
				
				if (isClosed) {	// apply decoration to stroke if it is closed!
					g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2d.setColor(decoration.getStrokeColor());
					g2d.setStroke(decoration.getStroke());
				}
				AnchorPoint nextPt = anchorPts.get(j);
				g2d.drawLine(curPt.x, curPt.y, nextPt.x, nextPt.y);
				
				if (showAnchorPts) {
					curPt.render(g2d);	
				}
				
				if (isConstructing) {
					lastPt = anchorPts.get(anchorPts.size() - 1);
					g2d.drawLine(lastPt.x, lastPt.y, activePt.x, activePt.y);
					if (showAnchorPts) {
						lastPt.render(g2d);	
					}
				}
			}
		} finally {
			g2d.dispose();
		}
	}

	//********************************************************
	//* 			       MOUSE METHODS				     *
	//********************************************************

	public void handleClick(Mouse mouse) {
		if (isConstructing) {
			if (canClose) {
				if (validateClose(mouse.getClickPt())) {
					closePoly();
				}
			} else {
				addPoint(mouse.getClickPt());
			}
		} else {
			addStartPt(mouse.getClickPt());
		}
	}

	public void handleRelease(Mouse mouse) {
		System.out.println("Release operation does nothing in MyPoly");
	}
	
	public void handleMove(Mouse mouse) {
		if (isConstructing) {
			if (activePt != null) {	// update the coordinates of active pt
				activePt.x = mouse.getMovePt().x;
				activePt.y = mouse.getMovePt().y;
			} else {
				activePt = new AnchorPoint(mouse.getMovePt());
			} 
			
			if(validateClose(activePt)) {  // check to see if we can close the poly
				canClose = true;
				beginPt.displayHover(true);	// we are over the starting pt
			} else {
				beginPt.displayHover(false);
			}
		}
	}
	
	public void handleDrag(Mouse mouse) {
		System.out.println("Drag Operation Not Yet Supported In MyPoly");
	}
	
	/**
	 * Creates a poly of this class from a Java polygon.
	 * @param p
	 */
	public static MyPoly createFromPoly(Polygon p) {
		int[] xPts = p.xpoints;
		int[] yPts = p.ypoints;
		ArrayList<AnchorPoint> pts = new ArrayList<>();
		for (int i = 0; i < p.npoints; i++) {
			pts.add(new AnchorPoint(xPts[i], yPts[i]));
		}
		
		MyPoly poly = new MyPoly(pts);
		poly.addStartPt(new AnchorPoint(xPts[0], yPts[0]));
		poly.isClosed = true;
		
		return poly;
	}
	
	//********************************************************
	//* 			  	   UTILITY METHODS			 	  	 *
	//********************************************************
	
	protected boolean validateClose(Point p) {
		if (anchorPts.size() >= MIN_PTS && beginPt.checkHover(p)) {
			canClose = true;
			return true;
		} else if (anchorPts.size() >= MIN_PTS - 1 && beginPt.checkHover(activePt)) { // third pt is active pt
			return true;
		} else {
			canClose = false;
			return false;
		}
	}
	
	/**
	 * Determines if the point passed in matches any points that already exist in
	 * the anchor points array. This is to prevent adding multiple same point values
	 * to a polygon shape.
	 * @param p The point to check
	 * @return
	 */
	protected boolean isDuplicatePt(AnchorPoint p) {
		for (int i = 0; i < anchorPts.size(); i++) {
			AnchorPoint pt = anchorPts.get(i);
			if (anchorPts.size() > 2 && !canClose) {
				if (pt.x == p.x && pt.y == p.y) {
					System.out.println("Duplicate Pt. (Not added)");
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Adds the starting point of the polygon and allows us to continue
	 * construction on the rest of the polygon.
	 * @param p
	 */
	protected void addStartPt(Point p) {
		beginPt = new AnchorPoint(p);
		beginPt.displayActive(true); // make this pt red so it stands out as start pt
		anchorPts.add(beginPt);
		activePt = (AnchorPoint)beginPt.clone();	// initialize active pt to start
		isConstructing = true;
	}
	
	/**
	 * Creates an array of existing pt values so that transformations can
	 * reference them, without causing exponential movement to occur.
	 */
	protected void createAnchorPtBuffer() {
		oldAnchorPts = new ArrayList<AnchorPoint>(anchorPts.size());
		for (AnchorPoint pt : anchorPts) {
			oldAnchorPts.add((AnchorPoint)pt.clone());
		}
	}

	//********************************************************
	//* 			  SHAPE MANIPULATION METHODS			 *
	//********************************************************
	
	/**
	 * Adds a new pt to the anchorpts array.
	 * @param p
	 */
	public void addPoint(Point p) {
		AnchorPoint newPt = new AnchorPoint(p);
		if (!isDuplicatePt(newPt)) {
			anchorPts.add(newPt);
		}
	}
	
	/**
	 * Closes off the polygon by ensuring that the last pt of the polygon
	 * matches the starting pt to create a complete shape. Also ends the
	 * construction of the polygon and hides anchor points.
	 */
	public void closePoly() {
		AnchorPoint closePt = beginPt;
		anchorPts.add(closePt);
		showAnchorPts = false;
		didClose = true;
		isClosed = true;
		isConstructing = false;
		anchorPts.get(0).displayActive(false);
		updateBoundingBox();
		System.out.println("Poly closed. Construction complete");
	}
	
	/**
	 * Moves all the points in the polygon by Point p.
	 */
	public void move(Point p) {
		if (oldAnchorPts == null || !isMoving) {	// creates a buffer of old anchor pts so we don't move exponentially
			createAnchorPtBuffer();
		}
		
		for (int i = 0; i < anchorPts.size(); i++) {
			AnchorPoint oldPt = oldAnchorPts.get(i);	
			AnchorPoint newPt = new AnchorPoint(oldPt.x + p.x, oldPt.y + p.y);
			anchorPts.set(i, newPt);
			isMoving = true;
		}
		updateBoundingBox();
	}
	
	public void resize(Point p, int direction) {
		if (oldAnchorPts == null || !isResizing) {
			createAnchorPtBuffer();
		}
		int xValue = 0;
		int yValue = 0;
		
		switch (direction) {
		case RectHandle.NORTH:
			yValue = getMaxY();
			break;
		case RectHandle.SOUTH:
			yValue = getMinY();
			break;
		case RectHandle.EAST:
			xValue = getMinX();
			break;
		case RectHandle.WEST:
			xValue = getMaxX();
			break;
		case RectHandle.NORTH_EAST:
			xValue = getMinX();
			yValue = getMaxY();
			break;
		case RectHandle.NORTH_WEST:
			xValue = getMaxX();
			yValue = getMaxY();
			break;
		case RectHandle.SOUTH_EAST:
			xValue = getMinX();
			yValue = getMinY();
			break;
		case RectHandle.SOUTH_WEST:
			xValue = getMaxX();
			yValue = getMinY();
			break;
		}
		
		double xDist = 0;
		double yDist = 0;
		double xScale = 0;
		double yScale = 0;
		
		for (int i = 0; i < oldAnchorPts.size(); i++) {
			AnchorPoint curPt = oldAnchorPts.get(i);
			xDist = Math.sqrt(Math.pow(curPt.x - xValue, 2));
			yDist = Math.sqrt(Math.pow(curPt.y - yValue, 2));
			xScale = (xDist / xValue) * p.x;
			yScale = (yDist / yValue) * p.y;
			
			System.out.println("[xDist: " + xDist + ", yDist: " + yDist + ", xScale: "
								+ xScale + ", yScale: " + yScale + ", p.x: " + p.x
								+ ", p.y: " + p.y);
			
			AnchorPoint newPt = new AnchorPoint(0, 0);
			switch (direction) {
			case RectHandle.NORTH:		// intentional fall through
			case RectHandle.SOUTH:
				newPt = new AnchorPoint(curPt.x, (int)(curPt.y + yScale));
				break;
			case RectHandle.EAST:		// intentional fall through
			case RectHandle.WEST:
				newPt = new AnchorPoint((int)(curPt.x + xScale), curPt.y);
				break;
			case RectHandle.NORTH_WEST:	// intentional fall through
			case RectHandle.NORTH_EAST:
			case RectHandle.SOUTH_WEST:
			case RectHandle.SOUTH_EAST:
				newPt = new AnchorPoint((int)(curPt.x + xScale), (int)(curPt.y + yScale));
				break;
			}
			anchorPts.set(i, newPt);
			isResizing = true;
			updateBoundingBox();
		}
	}
	
	/**
	 * Clears out the old anchor pt buffer (if possible) and updates the
	 * bounding box of the poly.
	 */
	public void update() {
		System.out.println("Poly updated");
		if (!isMoving && !isResizing) {
			oldAnchorPts = null;	// clear old anchorpts so that any new manipulations act on new pts
		}
		updateBoundingBox();
	}
	
	//********************************************************
	//* 				  UTILITY METHODS					 *
	//********************************************************
	
	/**
	 * Finds the extreme start and end pts of a polygon to determine how to 
	 * draw the bounding box.
	 */
	protected void updateBoundingBox() {		
		int minX = anchorPts.get(0).x;
		int minY = anchorPts.get(0).y;
		int maxX = anchorPts.get(0).x;
		int maxY = anchorPts.get(0).y;
		
		for (int i = 0; i < anchorPts.size(); i++) {
			if (anchorPts.get(i).x < minX) {
				minX = anchorPts.get(i).x;
			}
			
			if (anchorPts.get(i).x > maxX) {
				maxX = anchorPts.get(i).x;
			}
			
			if (anchorPts.get(i).y < minY) {
				minY = anchorPts.get(i).y;
			}
			
			if (anchorPts.get(i).y > maxY) {
				maxY = anchorPts.get(i).y;
			}
		}
		
		Point p1 = new Point(minX, minY);
		Point p2 = new Point(maxX, maxY);
		
		boundBox.setStartPt(p1);
		boundBox.setEndPt(p2);
		boundRect = new Rectangle(minX, minY, maxX - minX, maxY - minY);		
	}

	//********************************************************
	//* 				  ACCESSOR METHODS					 *
	//********************************************************
	
	public String toString() {
		String str = "Poly[nPts=" + anchorPts.size();
		for (int i = 0; i < anchorPts.size(); i++) {
			str += "" + anchorPts.get(i).toString() + ", ";
		}
		return str;
	}
	
	public Rectangle getBoundRect() {
		if (boundRect == null) {
			updateBoundingBox();
		}
		return boundRect;
	}
	
	public int getPtCount() {
		return anchorPts.size();
	}

	public boolean didClose() {
		return didClose;
	}
	
	/**
	 * 
	 * @return The maximum x-value of the poly
	 */
	public int getMaxX() {
		int maxX = oldAnchorPts.get(0).x;	// starting test pt
		
		for (int i = 0; i < oldAnchorPts.size(); i++) {
			AnchorPoint testPt = oldAnchorPts.get(i);
			
			if (testPt.x > maxX) {
				maxX = testPt.x;
			}
		}
		return maxX;
	}
	
	/**
	 * 
	 * @return The minimum x-value of the poly
	 */
	public int getMinX() {
		int minX = oldAnchorPts.get(0).x;	// starting test pt
		
		for (int i = 0; i < oldAnchorPts.size(); i++) {
			AnchorPoint testPt = oldAnchorPts.get(i);
			
			if (testPt.x < minX) {
				minX = testPt.x;
			}
		}
		return minX;
	}
	
	/**
	 * 
	 * @return The maximum y-value of the poly
	 */
	public int getMaxY() {
		int maxY = oldAnchorPts.get(0).y;	// starting test pt
		
		for (int i = 0; i < oldAnchorPts.size(); i++) {
			AnchorPoint testPt = oldAnchorPts.get(i);
			
			if (testPt.y > maxY) {
				maxY = testPt.y;
			}
		}
		return maxY;
	}
	
	/**
	 * 
	 * @return The minimum y-value of the poly
	 */
	public int getMinY() {
		int minY = oldAnchorPts.get(0).y;	// starting test pt
		
		for (int i = 0; i < oldAnchorPts.size(); i++) {
			AnchorPoint testPt = oldAnchorPts.get(i);
			
			if (testPt.y < minY) {
				minY = testPt.y;
			}
		}
		return minY;
	}
}
