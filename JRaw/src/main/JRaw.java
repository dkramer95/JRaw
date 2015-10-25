package main;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import gfx.Canvas;
import menus.MyMenu;
import panels.SwatchPanel;
import tools.ToolBar;

public class JRaw extends JFrame {
	public static final String TITLE = "JRaw Version 2.0.1a by David Kramer";
	private static final Dimension MAX_SIZE = new Dimension(5000, 5000);	// arbitrary value, but gives enough of a
																			// blank canvas to scroll around
	private JScrollPane scrollPane;
	private SwatchPanel swatchPanel;
	private Canvas canvas;
	private ToolBar toolBar;
	private MyMenu menu;
	
	public JRaw() {
		init();
	}
	
	/**
	 * Initializes all the components for the application.
	 */
	private void init() {
		canvas = new Canvas();
		canvas.setPreferredSize(MAX_SIZE);
		canvas.setMaximumSize(MAX_SIZE);
		
		toolBar = new ToolBar(canvas);
		swatchPanel = new SwatchPanel(canvas);
		menu = new MyMenu(canvas);
		
		createScrollPane();
		addComponents();
		
		setTitle(TITLE);
		setSize(new Dimension(1280, 720));
		setLocationRelativeTo(null);
		setJMenuBar(menu);
		addKeyListener(canvas.getCanvasManager().getKeyboard());
																
		setFocusable(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	/**
	 * Creates the scrollable area for the canvas so that the user can move 
	 * around if objects are outside of the visible view. 
	 */
	private void createScrollPane() {
		// scroll pane for moving around canvas view
		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setViewportView(canvas);
		scrollPane.getViewport().setPreferredSize(canvas.getPreferredSize());
		scrollPane.revalidate();
		scrollPane.invalidate();
		scrollPane.repaint();
		scrollPane.setWheelScrollingEnabled(true);
		scrollPane.setOpaque(false);
		canvas.setScrollPane(scrollPane);
		canvas.setSwatchPanel(swatchPanel);
	}
	
	/**
	 * Adds components to the main window.
	 */
	private void addComponents() {
		setLayout(new BorderLayout());
		add(toolBar, BorderLayout.WEST);
		add(swatchPanel, BorderLayout.SOUTH);
		add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * Main method that starts the application.
	 * @param args
	 */
	public static void main(String[] args) {
		JRaw app = new JRaw();
	}
}
