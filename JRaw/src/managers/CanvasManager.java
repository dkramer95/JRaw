package managers;

import java.awt.Cursor;
import java.awt.event.KeyEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import dialogs.FillStrokeDialog;
import gfx.Canvas;
import input.Keyboard;
import input.Mouse;
import shapes.MyShape;
import tools.Tool;
import tools.ToolBar;

/**
 * This class is the heart of the application. This keeps track of the state of the canvas
 * and allows for the user to toggle between different tools to modify the canvas, based
 * on the current context. 
 * @author DavidKramer
 *
 */
public class CanvasManager extends Manager {	
	private Tool activeTool;
	private ToolBar toolBar;
	
	// Secondary managers for other operations 
	private ShapeManager shapeManager;
	private SelectionManager selectionManager;
	private MenuManager menuManager;
	
	public CanvasManager(Canvas canvas) {
		this.canvas = canvas;
		mouse = new Mouse(this);
		keyboard = new Keyboard(this);
		shapeManager = new ShapeManager(canvas, mouse, keyboard);
		selectionManager = new SelectionManager(canvas, mouse, keyboard);
		menuManager = new MenuManager(canvas, mouse, keyboard);
	}
	
	//********************************************************
	//* 				   MOUSE METHODS					 *
	//********************************************************
	
	public void handleRightClick() {
		JPopupMenu menu = new JPopupMenu();
		JMenuItem clearGuidesItem = new JMenuItem("Clear Shapes");
		JMenuItem bringToFrontItem = new JMenuItem("Bring To Front");
		clearGuidesItem.addActionListener(e -> {
			canvas.getShapes().clear();
			canvas.setHasSelection(false);
		});
		
		bringToFrontItem.addActionListener( e -> {
			if (canvas.hasSelection()) {
				canvas.getSelection().sendShapesFront();
				canvas.repaint();
			}
		});
		
		menu.add(clearGuidesItem);
		menu.add(bringToFrontItem);
		menu.show(canvas, mouse.getClickPt().x, mouse.getClickPt().y);
	}
	
	public void handleMouse(int event) {
		Manager activeManager = null;
		switch (activeTool.getToolID()) {
		case Tool.TOOL_SELECT:
			activeManager = selectionManager;
			break;
		case Tool.TOOL_RECT:
		case Tool.TOOL_CIRCLE:
		case Tool.TOOL_POLY:
		case Tool.TOOL_LINE:
		case Tool.TOOL_PEN:
			activeManager = shapeManager;
			break;
		}
		
		if (activeManager != null) {
			activeManager.handleMouse(event);	
		}
		canvas.repaint();
	}
	
	
	//********************************************************
	//* 				  KEYBOARD METHODS					 *
	//********************************************************
	
	public void handleKeyPress() { //TODO handle this better later. This is a rough keyboard shortcut manager
		menuManager.handleKeyPress();
		System.out.println("Key Pressed!");
		canvas.requestFocus();
		
		if (keyboard.getAltFlag()) {
			canvas.getSelection().setIsCloning(true); 
		}
		
		switch (keyboard.getLastKey()) {
		case KeyEvent.VK_DELETE:	//TODO delete shapes in selection  // FIX ME (it doesn't delete shapes properly)
			if (canvas.hasSelection()) {
				canvas.getSelection().deleteSelectedShapes();
				MyShape activeShape = canvas.getActiveShape();
				activeShape = null;
				canvas.repaint();
			}
			break;
		case KeyEvent.VK_M:			// change to rectangle tool
			toolBar.setActiveTool(toolBar.getTools().get(1));
			break;
		case KeyEvent.VK_L:
			toolBar.setActiveTool(toolBar.getTools().get(2));
			break;
		case KeyEvent.VK_V:			// change to selection tool
			toolBar.setActiveTool(toolBar.getTools().get(0));
			break;
		case KeyEvent.VK_D:			// change to default fill
			canvas.resetDecoration();
			toolBar.getFillStrokeBtn().repaint();
			break;
		case KeyEvent.VK_1:			// init fill/stroke dialog and show fill tab
			FillStrokeDialog d = new FillStrokeDialog(canvas);
			d.getTabPane().setSelectedIndex(0);
			break;
		case KeyEvent.VK_2:			// init fill/stroke dialog and show stroke tab
			d = new FillStrokeDialog(canvas);
			d.getTabPane().setSelectedIndex(1);
			break;
		}
		canvas.repaint();
	}
	
	public void handleKeyRelease() {
		if (!keyboard.getAltFlag()) {
			if (canvas.hasSelection()) {
				canvas.getSelection().setIsCloning(false);	
			}
		}
	}
	
	//********************************************************
	//* 				   MUTATOR METHODS					 *
	//********************************************************

	public void setActiveTool(Tool tool) {
		System.out.println("Active Tool: " + tool);
		
		if (canvas.hasSelection()) {	// clear out selection if we change tools
			canvas.getSelection().clearSelection();
			canvas.setHasSelection(false);
			canvas.repaint();
		}
		activeTool = tool;
		
		switch (tool.getToolID()) {	// TODO handle this better.
		case Tool.TOOL_RECT:		// intentional fall through
		case Tool.TOOL_CIRCLE:
		case Tool.TOOL_POLY:
		case Tool.TOOL_LINE:
		case Tool.TOOL_PEN:
			shapeManager.updateTool(activeTool);
			break;
		}
	}
	
	public void updateCursor(Cursor cursor) {
		canvas.setCursor(cursor);
	}
	
	public void setToolBar(ToolBar toolBar) {
		this.toolBar = toolBar;
	}
	
	//********************************************************
	//* 				   ACCESSOR METHODS					 *
	//********************************************************
	
	public Mouse getMouse() {
		return mouse;
	}
	
	public Keyboard getKeyboard() {
		return keyboard;
	}
	
	public ToolBar getToolBar() {
		return toolBar;
	}

	public MenuManager getMenuManager() {
		return menuManager;
	}
}
