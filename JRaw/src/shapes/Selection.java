package shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;

import input.Mouse;

/**
 * This class allows the user to drag with their mouse a rectangular selection marquee 
 * that will check to see if it contains any shapes that are on the canvas. This allows
 * users to toggle shapes and modify their properties after they have already been
 * constructed.
 * @author DavidKramer
 *
 */
public class Selection extends MyRect {
	private ArrayList<MyShape> selectedShapes;
	private ArrayList<MyShape> clonedShapes;			// temporary array stores cloned shapes
	private ArrayList<MyShape> canvasShapes;
	
	private boolean didClear;
	private boolean hasShapes;
	private boolean isCloning;
	private int shapeCount;
	
	public Selection() {
		float dash[] = { 3.0f };
		BasicStroke stroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, dash, 0.0f);
		decoration.setStrokeColor(Color.GRAY);
		decoration.setStroke(stroke);
		canvasShapes = new ArrayList<>();
		selectedShapes = new ArrayList<>();
		clonedShapes = new ArrayList<>();
	}
	
	//********************************************************
	//* 				  DRAWING METHODS					 *
	//********************************************************
	
	public void render(Graphics g) {
		final Graphics2D g2d = (Graphics2D)g.create();
		try {
			if(!didClear) {
				g2d.setColor(decoration.getStrokeColor());
				g2d.setStroke(decoration.getStroke());
				g2d.drawRect(startPt.x, startPt.y, getWidth(), getHeight());
				boundBox.render(g);	
			}	
		} finally {
			g2d.dispose();
		}
	}
	
	//********************************************************
	//* 				   MOUSE METHODS					 *
	//********************************************************
	
	public void handleClick(Mouse mouse) {
		if (!hasShapes) {	// no shapes to begin with, check if click pt hits any shapes
			checkShapes(mouse.getClickPt());
			System.out.println("Checking shapes!");
			if (hasShapes) {
				canMove = true; // we can then move
			}
		}
		if (hasShapes) {
//			checkShapes(mouse.getClickPt()); TODO check this better. (this kind of works) but improve it!
			canResize = boundBox.checkHandleHover(mouse.getClickPt()) ? true : false;
			if (canResize) { 
				return; 
			}
			if (getBoundRect().contains(mouse.getClickPt())) {
				return;
			} else {
				clearSelection();
			}
		} else {
			clearSelection();
		} 
	}
	
	public void handleRelease(Mouse mouse) {
		if (isDragging) {
			checkShapes();
			if (!hasShapes) {
				clearSelection();
			}
			updateBoundingBox();
			canMove = true;
		} else if (isMoving || isResizing) {
			updateShapes();
			updateBoundingBox();
		}
		
		if (isCloning) {
//			selectedShapes = clonedShapes;
			clonedShapes.clear();	
		}
		
		// clear out 
		isDragging = false;
		isMoving = false;
		isResizing = false;
		isCloning = false;
	}
	
	/**
	 * Checks to see if the users mouse is hovered over a resize handle
	 * on the bounding box. If it is, it enables the selection to 
	 * be resized.
	 */
	public void handleMove(Mouse mouse) {
		if (hasShapes) {
			canResize = boundBox.checkHandleHover(mouse.getMovePt()) ? true : false;
		}
	}
	
	/**
	 * Drags out a new selection if we haven't already done so. Otherwise it checks to see
	 * if we should resize or move. Also handles cloning ability, if it is valid to do so.
	 */
	public void handleDrag(Mouse mouse) {
		if (hasShapes) {
			if (canResize) {
				Point p = getOffsetPt(mouse);
				resizeShapes(p, boundBox.getActiveHandle().getResizeDirection());
			} else if (canMove) {
				Point p = getOffsetPt(mouse);
				if (isCloning) {
					cloneSelectedShapes(p); // clone and move shapes
					updateBoundingBox();
					isMoving = true;
					return;	// we don't want to move the rest of shapes, if we're cloning!
				}
				moveSelectedShapes(p); 
				updateBoundingBox();
				isMoving = true;
			}
		} else {
			dragSelection(mouse);
		}
	}
		
	//********************************************************
	//* 				  SELECTION METHODS					 *
	//********************************************************
	
	/**
	 * Clears out the selection.
	 */
	public void clearSelection() {
		for (int i = 0; i < canvasShapes.size(); i++) {
			MyShape shape = canvasShapes.get(i);
			
			shape.setIsSelected(false);
			shape.getBoundingBox().setVisible(false);
		}
		System.out.println("Selection Cleared!");
		hasShapes = false;
		selectedShapes.clear();
		didClear = true;
	}
	
	/**
	 * Drags out a selection area and checks to make sure that start and end pts
	 * are correctly set.
	 * @param mouse
	 */
	private void dragSelection(Mouse mouse) {
		isDragging = true;
		startPt = mouse.getClickPt();
		endPt = mouse.getDragPt();
		checkPoints();
	}
	
	//********************************************************
	//* 				   SHAPE METHODS					 *
	//********************************************************
	
	/**
	 * Moves all shapes that are contained within the selection.
	 * @param p
	 */
	private void moveSelectedShapes(Point p) {
		for (int i = 0; i < selectedShapes.size(); i++) {
			MyShape shape = selectedShapes.get(i);
			shape.move(p);
		}
	}
	
	/**
	 * Moves all shapes that are contained within the selection.
	 * @param p
	 */
	private void cloneSelectedShapes(Point p) {
		if (clonedShapes.size() == 0) { // we haven't cloned anything yet
			for (int i = 0; i < selectedShapes.size(); i++) { // add selected shapes to cloned shapes
				MyShape shape = selectedShapes.get(i);
				MyShape cloneShape = (MyShape)shape.clone();
				clonedShapes.add(cloneShape);
				canvasShapes.add(cloneShape);	// then add to canvas!
			}
		} else {
			for (int i = 0; i < clonedShapes.size(); i++) { // move cloned shapes
				MyShape cloneShape = clonedShapes.get(i);
				cloneShape.move(p);
			}
		}
	}
	
	/**
	 * Resizes all shapes that are selected, based on the outer
	 * bounding box hovered handle.
	 * @param p
	 * @param direction
	 */
	private void resizeShapes(Point p, int direction) {
		isResizing = true;
		for (int i = 0; i < selectedShapes.size(); i++) {
			MyShape shape = selectedShapes.get(i);
			shape.resize(p, direction);
		}
		updateBoundingBox();
	}
	
	/**
	 * Updates shapes that were cloned and/or selected.
	 */
	private void updateShapes() {
		if (isCloning) {
			for (int i = 0; i < clonedShapes.size(); i++) {
				MyShape cloneShape = clonedShapes.get(i);
				cloneShape.setIsMoving(false);
				cloneShape.setIsResizing(false);
				cloneShape.update();
			}
		}
		
		for (int i = 0; i < selectedShapes.size(); i++) {
			MyShape shape = selectedShapes.get(i);
			shape.setIsMoving(false);
			shape.setIsResizing(false);
			shape.update();
		}
	}
	
	//********************************************************
	//* 				  UTILITY METHODS					 *
	//********************************************************
	
	protected void updateBoundingBox() {
		if (hasShapes) {
			ArrayList<Integer> xPts = new ArrayList<>();
			ArrayList<Integer> yPts = new ArrayList<>();
			
			if (isCloning & clonedShapes.size() > 0) {  // update bounding box based on cloned shapes
				for (int i = 0; i < clonedShapes.size(); i++) {
					System.out.println("Checking cloned shapes!");
					xPts.add(clonedShapes.get(i).getStartPt().x);
					xPts.add(clonedShapes.get(i).getEndPt().x);
					yPts.add(clonedShapes.get(i).getStartPt().y);
					yPts.add(clonedShapes.get(i).getEndPt().y);
				}
			} else {
				for (int i = 0; i < selectedShapes.size(); i++) {
					System.out.println("Checking selected shapes");
					if (selectedShapes.get(i) instanceof MyPoly) {	// use bound box from poly rather than start/end pts
						Rectangle r = selectedShapes.get(i).getBoundRect();
						xPts.add(r.x);
						xPts.add(r.width + r.x);
						yPts.add(r.y);
						yPts.add(r.height + r.y);
					} else {
						xPts.add(selectedShapes.get(i).getStartPt().x);
						xPts.add(selectedShapes.get(i).getEndPt().x);
						yPts.add(selectedShapes.get(i).getStartPt().y);
						yPts.add(selectedShapes.get(i).getEndPt().y);	
					}
				}	
			}
			
			Integer minX = Collections.min(xPts);
			Integer maxX = Collections.max(xPts);
			Integer minY = Collections.min(yPts);
			Integer maxY = Collections.max(yPts);
			
			System.out.println("MinX " + minX + "MinY " + minY + "MaxX" + maxX + "MaxY " + maxY);
			
			Point newStartPt = new Point(minX, minY);
			Point newEndPt = new Point(maxX, maxY);
			
			startPt = newStartPt;
			endPt = newEndPt;
			decoration.setStrokeColor(Color.BLUE);
			decoration.setStroke(new BasicStroke(2.0f));
			boundBox.setStartPt(newStartPt);
			boundBox.setEndPt(newEndPt);	
			boundBox.setVisible(true);
		}
	}
	
	/**
	 * Checks to see if any canvas shapes are contained within the boundaries of
	 * the active selection. If they are, they are added to the selection shapes
	 * array.
	 */
	private void checkShapes() {
		for (int i = 0; i < canvasShapes.size(); i++) {
			MyShape shape = canvasShapes.get(i);
			
			if (getBoundRect().contains(shape.getBoundRect())) { // one or more shapes contained in dragged area
				selectedShapes.add(shape);
				shape.getBoundingBox().setVisible(false);
				shape.setIsSelected(true);
			} else {
				shape.setIsSelected(false);
			}
		}
		hasShapes = selectedShapes.size() > 0 ? true : false;
	}
	
	/**
	 * Checks to see if any canvas shapes contain Point p (generally from a mouse click pt).
	 * If it does, is added to the selection shape array. This method stops searching once
	 * one shape that contains the Point p has been found. This is to prevent selecting
	 * shapes that overlap.
	 * @param p
	 */
	private void checkShapes(Point p) {
		if (canvasShapes.size() > 0) { // there has to be shapes to check!
		for (int i = canvasShapes.size() - 1; i >= 0; i--) { // reversed to find the highest z-index shape first
			MyShape shape = canvasShapes.get(i);
			if (shape.getBoundRect().contains(p)) {
				selectedShapes.add(shape);
				shape.getBoundingBox().setVisible(false);
				shape.setIsSelected(true);
				hasShapes = true;
				break;	// once we have found one shape, break out (so we don't select overlapping shapes)
			} else {
				shape.setIsSelected(false);
				shape.getBoundingBox().setVisible(false);
			}
		}
		updateBoundingBox();	
		}
	}
	
	/**
	 * Deletes all shapes that are contained in the active selection.
	 */
	public void deleteSelectedShapes() {
		for (int i = 0; i < selectedShapes.size(); i++) {
			MyShape shape = selectedShapes.get(i);
			canvasShapes.remove(shape);
			shape = null;
		}
		clearSelection();
	}
	
	public void sendShapesFront() {	//TODO implement send shapes to front
		System.out.println("Should send shapes to front but isn't implemented yet!");
		int[] shapeIndexes = new int[selectedShapes.size()];
		
		for (int i = 0; i < selectedShapes.size(); i++) {
			shapeIndexes[i] = selectedShapes.get(i).getZIndex();
		}
		
		for (int i : shapeIndexes) {
			System.out.println("Shape indexes: " + i);
		}
	}
	
	//********************************************************
	//* 				  MUTATOR METHODS					 *
	//********************************************************
	
	public void setCanvasShapes(ArrayList<MyShape> shapes) {
		canvasShapes = shapes;
	}
	
	public void setIsCloning(boolean b) {
		isCloning = true;
	}
	
	//********************************************************
	//* 				  ACCESSOR METHODS					 *
	//********************************************************
	
	public boolean didClear() {
		return didClear;
	}
	
	public boolean isCloning() {
		return isCloning;
	}
	
	public ArrayList<MyShape> getSelectedShapes() {
		return selectedShapes;
	}
	
	public int getShapeCount() {
		return shapeCount = selectedShapes.size();
	}
	
}
