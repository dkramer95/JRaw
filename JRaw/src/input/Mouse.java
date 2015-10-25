package input;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.SwingUtilities;

import managers.CanvasManager;

/**
 * This class keeps track of all the mouse input. This is used in conjunction with the canvas
 * manager class. All input recorded here is passed to canvas manager.
 * @author DavidKramer
 *
 */
public class Mouse implements MouseListener, MouseMotionListener, MouseWheelListener {
	private CanvasManager canvasManager;
	
	// Mouse Input Constants
	public static final int EVENT_CLICK = 0;
	public static final int EVENT_RIGHT_CLICK = 1;
	public static final int EVENT_DRAG = 2;
	public static final int EVENT_MOVE = 3;
	public static final int EVENT_RELEASE = 4;
	public static final int EVENT_SCROLL = 5;
	
	// Move direction constants
	public static final int MOVE_LEFT = 0;
	public static final int MOVE_RIGHT = 1;
	public static final int MOVE_UP = 2;
	public static final int MOVE_DOWN = 3;
	
	private Point clickPt;
	private Point movePt;
	private Point dragPt;
	private Point endPt;
	private Point directionPt;				// useful to keep track of direction we are moving
	
	private boolean isDragging;
	private boolean isPressed;
	private boolean didRightClick;
	private boolean isMeasuring;
	
	private int clickCount;
	private int wheelRotateCount;
	private int moveDirection;				// are we moving more x, or more y?
	
	public Mouse(CanvasManager canvasManager) {
		this.canvasManager = canvasManager;
		init();
	}
	
	//********************************************************
	//* 				    MOUSE METHODS					 *
	//********************************************************

	public void mouseDragged(MouseEvent e) {
		isDragging = true;
		isPressed = true;
		dragPt = e.getPoint();
		canvasManager.handleMouse(Mouse.EVENT_DRAG);
//		canvasManager.handleDrag();
//		updateMoveDirection();	// TODO move direction not quite working yet
	}

	public void mouseMoved(MouseEvent e) {
		isDragging = false;
		isPressed = false;
		movePt = e.getPoint();
		canvasManager.handleMouse(Mouse.EVENT_MOVE);
//		canvasManager.handleMove();
	}

	public void mousePressed(MouseEvent e) {
		isDragging = false;
		isPressed = true;
		clickPt = e.getPoint();
		clickCount = e.getClickCount();
		didRightClick = SwingUtilities.isRightMouseButton(e) ? true : false;
		
		if (didRightClick) {
			canvasManager.handleRightClick();	//TODO change this to handleInput	
		} else {
			canvasManager.handleMouse(Mouse.EVENT_CLICK);
		}
	}

	public void mouseReleased(MouseEvent e) {
		isDragging = false;
		isPressed = false;
		endPt = e.getPoint();
		canvasManager.handleMouse(Mouse.EVENT_RELEASE);
//		canvasManager.handleRelease();
	}
	
	//********************************************************
	//* 				 UNUSED MOUSE METHODS				 *
	//********************************************************
	
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseWheelMoved(MouseWheelEvent e) {}
	
	//********************************************************
	//* 				  UTILITY METHODS					 *
	//********************************************************
	
	/**
	 * Initializes points for the first time.
	 */
	private void init() {
		clickPt = new Point(0, 0);
		movePt = new Point(0, 0);
		dragPt = new Point(0, 0);
		endPt = new Point(0, 0);
	} 
	
	/**
	 * Updates the direction which we are moving the mouse
	 */
	private void updateMoveDirection() { // TODO this isn't working 100% yet.
		if (directionPt == null) {
			directionPt = (Point)dragPt.clone();
		}
		
		int dx = dragPt.x - directionPt.x;
		int dy = dragPt.y - directionPt.y;
		
		directionPt = new Point(dragPt.x + dx, dragPt.y + dy);
		System.out.println(directionPt);
		
		if (directionPt.x > directionPt.y) {
			moveDirection = MOVE_LEFT;
		} else if (directionPt.x < directionPt.y) {
			System.out.println("Moving On Y-Axis!");
			moveDirection = MOVE_RIGHT;
		}
		
		directionPt = new Point(0, 0);
	}
	
	//********************************************************
	//* 				  ACCESSOR METHODS					 *
	//********************************************************

	public Point getClickPt() {
		return clickPt;
	}

	public Point getMovePt() {
		return movePt;
	}

	public Point getDragPt() {
		return dragPt;
	}

	public Point getEndPt() {
		return endPt;
	}

	public boolean isDragging() {
		return isDragging;
	}

	public boolean isPressed() {
		return isPressed;
	}

	public boolean didRightClick() {
		return didRightClick;
	}
	
	public int getMoveDirection() {
		return moveDirection;
	}

	public int getClickCount() {
		return clickCount;
	}

	public int getWheelRotateCount() {
		return wheelRotateCount;
	}
	
}
