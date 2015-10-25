package managers;

import gfx.Canvas;
import input.Keyboard;
import input.Mouse;

/**
 * Base class for all managers. All managers must manage something and handle
 * input received from the keyboard and mouse
 * @author DavidKramer
 *
 */
public abstract class Manager {
	protected Canvas canvas;					// the drawing canvas object
	protected Mouse mouse;
	protected Keyboard keyboard;
	
	public Manager() {}
	
	/**
	 * Constructs a new manager and gives it access to the canvas and mouse/keyboard inputs
	 * @param canvas - The drawing canvas
	 * @param mouse - The mouse input listener
	 * @param keyboard - The keyboard input listener
	 */
	public Manager(Canvas canvas, Mouse mouse, Keyboard keyboard) {
		this.canvas = canvas;
		this.mouse = mouse;
		this.keyboard = keyboard;
	}
	
	//********************************************************
	//* 				   MOUSE METHODS					 *
	//********************************************************
	
	public abstract void handleMouse(int event);
	
	//********************************************************
	//* 				  KEYBOARD METHODS					 *
	//********************************************************
	
	public abstract void handleKeyPress();
	public abstract void handleKeyRelease();
	
	//********************************************************
	//* 				   ACCESSOR METHODS					 *
	//********************************************************
	
	public Mouse getMouse() {
		return mouse;
	}
	
	public Keyboard getKeyboard() {
		return keyboard;
	}
}
