package managers;

import java.awt.event.KeyEvent;

import gfx.Canvas;
import input.Keyboard;
import input.Mouse;
import utils.JRawIO;

public class MenuManager extends Manager {

	public MenuManager(Canvas canvas, Mouse mouse, Keyboard keyboard) {
		super(canvas, mouse, keyboard);
		// TODO Auto-generated constructor stub
	}
	
	public void handleMouse(int event) {
		
	}

	public void handleClick() {
	}

	public void handleRightClick() {
	}

	public void handleDrag() {
	}

	public void handleMove() {
	}

	public void handleRelease() {
	}

	public void handleKeyPress() {
		System.out.println("Ctrl: ? " + keyboard.getCtrlFlag());
		if (keyboard.getCtrlFlag()) {
			switch (keyboard.getLastKey()) {
			case KeyEvent.VK_N:
				System.out.println("Make new document");
				break;
			}
		}
	}
	
	public void handleKeyRelease() {
	}
	
	
	//********************************************************
	//* 			  	MENU HANDLING METHODS				 *
	//********************************************************

	public void saveFile() {
		System.out.println("Attempting to save file!");
//		JRawIO fWriter = new JRawIO(canvas);
		JRawIO io = new JRawIO(canvas);
		io.showSaveDialog();
	}
	
	public void readFile() {
		System.out.println("Attemtping to read file!");
		JRawIO io = new JRawIO(canvas);
		io.showOpenDialog();
	}
}
