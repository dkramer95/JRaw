package managers;

import gfx.Canvas;
import gfx.Decoration;
import input.Keyboard;
import input.Mouse;
import shapes.MyPath;
import shapes.MyPoly;
import shapes.MyShape;
import shapes.ShapeFactory;
import tools.Tool;

public class ShapeManager extends Manager {
	private MyShape activeShape;		// the active construction shape
	private boolean canUpdateCanvas;		
	private Tool activeShapeTool;
	
	public ShapeManager(Canvas canvas, Mouse mouse, Keyboard keyboard) {
		super(canvas, mouse, keyboard);
	}

	//********************************************************
	//* 				   SHAPE METHODS					 *
	//********************************************************
	
	public void init() {
		activeShape = null;
		initShape();
	}
	
	/**
	 * Initializes the canvas to accept a new shape based on the active shape tool
	 * that will be generated from mouse input. 
	 */
	private void initShape() {
		if (!canvas.isConstructing() || activeShape == null) {
			activeShape = ShapeFactory.createShapeFromTool(activeShapeTool);
			activeShape.setDecoration((Decoration)canvas.getDecoration().clone());
			canUpdateCanvas = true;
			canvas.setIsConstructing(true);
			canvas.setActiveShape(activeShape);
		}
	}
	
	/**
	 * Updates the active canvas shape if it exists, otherwise it initializes
	 * the canvas to have an active shape.
	 * @param event
	 */
	private void updateShape(int event) {
		if (canvas.isConstructing()) {
			switch (event) {
			case Mouse.EVENT_CLICK:
				activeShape.handleClick(mouse);
				break;
			case Mouse.EVENT_DRAG:
				activeShape.handleDrag(mouse);
				break;
			case Mouse.EVENT_MOVE:
				activeShape.handleMove(mouse);
				break;
			case Mouse.EVENT_RELEASE:
				activeShape.handleRelease(mouse);
				canvas.setIsConstructing(false);
				activeShape.setZIndex(canvas.getShapes().size());
				canvas.addShape(activeShape);
				initShape();
				break;
			}	
			canvas.setActiveShape(activeShape);
		} else {
			initShape();
		}
	}
	
	/**
	 * Handles special case for polygon as the construction is different than
	 * that of other shapes.
	 * @param event
	 */
	private void updatePoly(int event) {
		MyPoly poly = (MyPoly)activeShape;
		switch (event) {
		case Mouse.EVENT_CLICK:
			poly.handleClick(mouse);
			if (poly.didClose()) {
				canvas.addShape(poly);
				canvas.setIsConstructing(false);
				initShape();
				return;
			} else {
				canvas.setIsConstructing(true);
			}
			break;
		case Mouse.EVENT_MOVE:
			poly.handleMove(mouse);
			break;
		case Mouse.EVENT_RELEASE:
			poly.handleRelease(mouse);
			break;
		}
		canvas.setActiveShape(poly);
	}
	
	/**
	 * When a different tool is clicked, if it is not the same as the active shape,
	 * the active shape is added to the canvas and construction ends. Otherwise, if
	 * the user has clicked on the same button, nothing has changed.
	 */
	private void updateConstruction() {
		boolean diffShapeType = false;
		activeShape = canvas.getActiveShape();
		MyShape newShape = ShapeFactory.createShapeFromTool(activeShapeTool);
		diffShapeType = !(activeShape.getClass().equals(newShape.getClass()));	// is tool shape same as active shape?	
		
		if (diffShapeType) {	// the tool clicked isn't the same as what active shape was
			if (canvas.isConstructing()) {
				if (activeShape instanceof MyPath) {
					MyPath path = (MyPath)activeShape;
					activeShape = path;
				} else if (activeShape instanceof MyPoly) {
					MyPoly poly = (MyPoly)activeShape;
					if (poly.getPtCount() > 3) {	// close out poly if possible
						poly.closePoly();
						activeShape = poly;
					} else {
						activeShape = null;
						canvas.setIsConstructing(false);
						canvas.repaint();
						return;
					}
				}
				canvas.addShape(activeShape);
				canvas.setIsConstructing(false);
				canvas.repaint();
			}
		}
	}
	
	//********************************************************
	//* 				   MOUSE METHODS					 *
	//********************************************************
	
	public void handleMouse(int event) {
		if (activeShape instanceof MyPoly) {
			switch (event) {
			case Mouse.EVENT_CLICK:
			case Mouse.EVENT_MOVE:
			case Mouse.EVENT_RELEASE:
				updatePoly(event);
				break;
			}
		} else {
			updateShape(event);
		}
	}
	
	//********************************************************
	//* 				   KEYBOARD METHODS					 *
	//********************************************************

	public void handleKeyPress() {}
	
	public void handleKeyRelease() {}
	
	/**
	 * Updates the active shape tool of the canvas and checks to see if it
	 * is different than the active shape that is being constructed. If it is
	 * it will attempt to add that shape to the canvas, then change the type
	 * of active shape to the current shape type of the active shape tool.
	 * @param tool
	 */
	public void updateTool(Tool tool) {
		activeShapeTool = tool;
		initShape();
		updateConstruction();
	}

}
