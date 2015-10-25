package menus;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import gfx.Canvas;
import managers.MenuManager;

public class MyMenu extends JMenuBar {
	private Canvas canvas;
	private MenuManager menuManager;
	
	// File Menu
	private JMenu fileMenu;
	private JMenuItem newItem;
	private JMenuItem openItem;
	private JMenuItem saveItem;
	private JMenuItem saveAsItem;
	private JMenuItem quitItem;
	
	// Edit Menu
	private JMenu editMenu;
	private JMenuItem undoItem;
	private JMenuItem redoItem;
	private JMenuItem cutItem;
	private JMenuItem copyItem;
	private JMenuItem pasteItem;
	private JMenuItem duplicateItem;
	
	// View Menu
	private JMenu viewMenu;
	private JMenuItem redrawItem;
	private JMenuItem filledItem;
	private JMenuItem wireframeItem;
	private JMenuItem bringFrontItem;
	private JMenuItem sendBackItem;
	
	// Tools Menu
	private JMenu toolsMenu;
	private JMenuItem getAreaItem;
	private JMenuItem reflectXItem;
	private JMenuItem reflectYItem;
	
	public MyMenu(Canvas canvas) {
		this.canvas = canvas;
		
		menuManager = canvas.getCanvasManager().getMenuManager();
		
		buildFileMenu();
		buildEditMenu();
		buildViewMenu();
		buildToolsMenu();
		
		add(fileMenu);
		add(editMenu);
		add(viewMenu);
		add(toolsMenu);
		
		addKeyListener(menuManager.getKeyboard());
		
		// Add Menus to the menubar
	}
	
	private void buildFileMenu() {
		fileMenu = new JMenu("File");
		newItem = new JMenuItem("New");
		openItem = new JMenuItem("Open");
		saveItem = new JMenuItem("Save");
		saveAsItem = new JMenuItem("Save As...");
		quitItem = new JMenuItem("Quit");
		
		fileMenu.add(newItem);
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		fileMenu.add(saveAsItem);
		fileMenu.addSeparator();
		fileMenu.add(quitItem);
		
		openItem.addActionListener( e -> {
			menuManager.readFile();
		});
		
		saveItem.addActionListener( e -> {
			menuManager.saveFile();
		});
	}
	
	private void buildEditMenu() {
		editMenu = new JMenu("Edit");
		undoItem = new JMenuItem("Undo");
		redoItem = new JMenuItem("Redo");
		cutItem = new JMenuItem("Cut");
		copyItem = new JMenuItem("Copy");
		pasteItem = new JMenuItem("Paste");
		duplicateItem = new JMenuItem("Duplicate");
		
		editMenu.add(undoItem);
		editMenu.add(redoItem);
		editMenu.add(cutItem);
		editMenu.add(copyItem);
		editMenu.add(pasteItem);
		editMenu.add(duplicateItem);
	}
	
	private void buildViewMenu() {
		viewMenu = new JMenu("View");
		redrawItem = new JMenuItem("Redraw");
		filledItem = new JMenuItem("Filled");
		wireframeItem = new JMenuItem("Wireframe");
		bringFrontItem = new JMenuItem("Bring Front");
		sendBackItem = new JMenuItem("Send Back");
		
		viewMenu.add(redrawItem);
		viewMenu.add(filledItem);
		viewMenu.add(wireframeItem);
		viewMenu.add(bringFrontItem);
		viewMenu.add(sendBackItem);
	}
	
	private void buildToolsMenu() {
		toolsMenu = new JMenu("Tools");
		getAreaItem = new JMenuItem("Get Area");
		reflectXItem = new JMenuItem("Reflect X");
		reflectYItem = new JMenuItem("Reflect Y");
		
		toolsMenu.add(getAreaItem);
		toolsMenu.add(reflectXItem);
		toolsMenu.add(reflectYItem);
	}

}
