package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import managers.CanvasManager;

/**
 * This class listens to keyboard input and keeps track of the last key pressed,
 * as well as if any modifier keys have been pressed.
 * @author DavidKramer
 *
 */
public class Keyboard implements KeyListener {
	private CanvasManager canvasManager;
	
	// Modifier Key Flags
	private boolean shiftFlag;
	private boolean ctrlFlag;
	private boolean altFlag;
	
	private int lastKey;
	
	public Keyboard(CanvasManager canvasManager) {
		this.canvasManager = canvasManager;
	}
	
	//********************************************************
	//* 				  KEYBOARD METHODS					 *
	//********************************************************
	
	public void keyPressed(KeyEvent e) {
		
		//Fix this later so that it grabs modifiers, not key code
		shiftFlag = e.getKeyCode() == KeyEvent.VK_SHIFT ? true : false;
		ctrlFlag = e.getKeyCode() == 157 ? true : false;	// TODO - WORK AROUND FOR CMD KEY (ON MAC). CHANGE TO CTRL
		altFlag = e.getKeyCode() == KeyEvent.VK_ALT ? true : false;
		
		lastKey = e.getKeyCode();
		canvasManager.handleKeyPress();
	}
	
	/**
	 * Clears out modifier key presses.
	 */
	public void keyReleased(KeyEvent e) {
		shiftFlag = false;
		ctrlFlag = false;
		altFlag = false;
		
		canvasManager.handleKeyRelease();
	}
	
	// Unused methods
	public void keyTyped(KeyEvent e) {}
	
	//********************************************************
	//* 				  ACCESSOR METHODS					 *
	//********************************************************
	
	public int getLastKey() {
		return lastKey;
	}
	
	public boolean getShiftFlag() {
		return shiftFlag;
	}
	
	public boolean getCtrlFlag() {
		return ctrlFlag;
	}
	
	public boolean getAltFlag() {
		return altFlag;
	}
}
