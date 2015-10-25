package managers;

import java.awt.event.KeyEvent;

import gfx.Canvas;
import input.Keyboard;
import input.Mouse;
import shapes.MyShape;
import shapes.Selection;

/**
 * This class manages the ability to create shape selections.
 * @author DavidKramer
 *
 */
public class SelectionManager extends Manager {
	private boolean canSelect;		// can we make a selection?
	private boolean hasSelection;
	private Selection selection;	// the canvas selection
	
	public SelectionManager(Canvas canvas, Mouse mouse, Keyboard keyboard) {
		super(canvas, mouse, keyboard);
		canSelect = true;
	}

	private void updateSelection(int event) {
		if (!canvas.hasSelection()) { // no selection, make one
			selection = new Selection();
			canvas.setHasSelection(true);
			canvas.setSelection(selection);
		} else {
			selection.setCanvasShapes(canvas.getShapes());	// reference to canvas shapes so we can check them
			switch (event) {
			case Mouse.EVENT_CLICK:
				selection.handleClick(mouse);
				break;
			case Mouse.EVENT_MOVE:
				selection.handleMove(mouse);
				break;
			case Mouse.EVENT_DRAG:
				selection.handleDrag(mouse);
				break;
			case Mouse.EVENT_RELEASE:
				selection.handleRelease(mouse);
				break;
			}
			
			if (selection.didClear()) {
				canvas.setHasSelection(false);
			} else {
				canvas.setSelection(selection);
			}
		}
		canvas.repaint();	
	}
	
	private void deleteSelection() {
		if (canvas.hasSelection()) {
			selection = canvas.getSelection();
			for (int i = 0; i < canvas.getShapes().size(); i++) {
				MyShape shape = canvas.getShapes().get(i);
				
				if (shape.isSelected()) {
					canvas.getShapes().remove(shape);
				}
			}
			selection.getSelectedShapes().clear();
			canvas.setHasSelection(false);
			canvas.repaint();
		}
	}
	
	//********************************************************
	//* 				   MOUSE METHODS					 *
	//********************************************************
	
	public void handleMouse(int event) {
		updateSelection(event);
	}
	
	public void handleClick() {	
		System.out.println("Handling click from selection manager");
		updateSelection(Mouse.EVENT_CLICK);	
	}

	public void handleRightClick() {
	}

	public void handleDrag() {
		updateSelection(Mouse.EVENT_DRAG);	
	}

	public void handleMove() {
		updateSelection(Mouse.EVENT_MOVE);
	}

	public void handleRelease() {
		updateSelection(Mouse.EVENT_RELEASE);
	}
	
	//********************************************************
	//* 				  KEYBOARD METHODS					 *
	//********************************************************
	
	public void handleKeyPress() {	
		if (keyboard.getAltFlag()) {
			selection.setIsCloning(true);
		}
		
		switch (keyboard.getLastKey()) {
		case KeyEvent.VK_DELETE:
			deleteSelection();
		}
	}

	public void handleKeyRelease() {
		if (!keyboard.getAltFlag()) {
			if (canvas.hasSelection()) {
				selection = canvas.getSelection();
				selection.setIsCloning(false);
			}
		}
	}
	
	//********************************************************
	//* 				  MUTATOR METHODS					 *
	//********************************************************
	
	/**
	 * Very important! This manager can't do anything unless it is given permission to 
	 * do so via the canSelect flag. This should be true, if the user changes the active
	 * tool to Tool.SELECT_TOOL. This will allow processing of input with respect to
	 * selections to occur.
	 * @param b
	 */
	public void setCanSelect(boolean b) {
		canSelect = b;
	}
}
