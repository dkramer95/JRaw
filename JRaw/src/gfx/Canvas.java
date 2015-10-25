package gfx;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import managers.CanvasManager;
import panels.SwatchPanel;
import shapes.MyShape;
import shapes.Selection;

/**
 * This class is responsible for providing every object with a graphics object
 * reference, to allow things to be rendered to the screen.
 * @author DavidKramer
 *
 */
public class Canvas extends JPanel {
	public static final int DRAW_WIREFRAME = 0;
	public static final int DRAW_FILLED = 1;
	
	private CanvasManager canvasManager;
	private Decoration decoration;
	private Selection selection;
	
	private JScrollPane scrollPane;
	private SwatchPanel swatchPanel;
	
	private int drawMode;
	private boolean isConstructing;					// are we currently dragging a shape?
	private boolean hasSelection;
	private MyShape activeShape;					// shape that is currently being constructed
	private ArrayList<MyShape> shapes;	
	
	public Canvas() {
		init();
	}
	
	private void init() {
		setFocusable(true);
		requestFocus();
		requestFocusInWindow();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		canvasManager = new CanvasManager(this);
		shapes = new ArrayList<>();
		
		isConstructing = false;
		
		decoration = new Decoration(Color.GRAY, 
						Decoration.DEFAULT_STROKE_COLOR, Decoration.DEFAULT_STROKE);
		decoration.setHasFill(true);	// default
		decoration.setHasStroke(true);	// default
		decoration.setStrokeWidth(5.0f);
		
		// Mouse input
		addMouseListener(canvasManager.getMouse());
		addMouseMotionListener(canvasManager.getMouse());
		addMouseWheelListener(canvasManager.getMouse());	
		
		// Keyboard input
		addKeyListener(canvasManager.getKeyboard());
		System.out.println("Has Focus ? " + hasFocus());
	}
	
	//********************************************************
	//* 				  DRAWING METHODS					 *
	//********************************************************
	
	public void paint(Graphics g) {
		render(g);
	}
	
	public void render(Graphics g) {
		g.clearRect(0, 0, getWidth(), getHeight());
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.BLACK);
		for (int i = 0; i < shapes.size(); i++) {
			MyShape shape = shapes.get(i);
			shape.render(g);
		}
		
		if (isConstructing && activeShape != null) {
			activeShape.render(g);
		}
		
		if (hasSelection) {
			selection.render(g);
		}
		g.dispose();
	}
	
	//********************************************************
	//* 				  MUTATOR METHODS					 *
	//********************************************************
	
	public void addShape(MyShape shape) {
		shapes.add(shape);
		System.out.println("Shape added");
	}
	
	public void setIsConstructing(boolean b) {
		isConstructing = b;
	}
	
	public void setActiveShape(MyShape shape) {
		activeShape = shape;
	}
	
	public void setHasSelection(boolean b) {
		hasSelection = b;
	}
	
	public void setSelection(Selection s) {
		selection = s;
	}
	
	public void setDecoration(Decoration d) {
		decoration = d;
	}
	
	public void setScrollPane(JScrollPane s) {
		scrollPane = s;
	}
	
	public void setSwatchPanel(SwatchPanel p) {
		swatchPanel = p;
	}
	
	//********************************************************
	//* 				  ACCESSOR METHODS					 *
	//********************************************************
	
	public boolean isConstructing() {
		return isConstructing;
	}
	
	public CanvasManager getCanvasManager() {
		return canvasManager;
	}
	
	public MyShape getActiveShape() {
		return activeShape;
	}
	
	public Selection getSelection() {
		return selection;
	}
	
	public ArrayList<MyShape> getShapes() {
		return shapes;
	}

	public boolean hasSelection() {
		return hasSelection;
	}
	
	public Decoration getDecoration() {
		return decoration;
	}
	
	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	public void resetDecoration() {
		decoration = new Decoration(Color.GRAY, 
						 Decoration.DEFAULT_STROKE_COLOR,
						 Decoration.DEFAULT_STROKE);
		decoration.setHasFill(true);	// default
		decoration.setHasStroke(true);	// default
		decoration.setStrokeWidth(5.0f);
	}
	
}
