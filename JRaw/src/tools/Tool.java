package tools;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.Border;

/**
 * The tool class allows you to create various different shape creation tools that will 
 * be available on the left side toolbar. 
 * @author DavidKramer
 *
 */
public class Tool extends JButton {

	// Shape Tool ID Constants
	public static final int TOOL_RECT = 1;
	public static final int TOOL_CIRCLE = 2;
	public static final int TOOL_POLY = 3;
	public static final int TOOL_LINE = 4;
	public static final int TOOL_PEN = 5;
	public static final int TOOL_DIMENSION = 6;
	
	// Navigation Tool ID Constants
	public static final int TOOL_SELECT = 10;
	public static final int TOOL_HAND = 11;
	public static final int TOOL_ZOOM = 12;
	
	// Decoration Tool ID Constants
	public static final int TOOL_DECOR_FILL = 20;
	public static final int TOOL_DECOR_STROKE = 21;
	
	// Default Button Styles
	public static final Border BORDER_INACTIVE = BorderFactory.createRaisedBevelBorder();
	public static final Border BORDER_ACTIVE = BorderFactory.createLoweredBevelBorder();
	
	protected boolean isActive;
	protected int toolID;
	protected Cursor cursor;			// the mouse cursor associated with the tool (if any)
	
	private ToolBar toolBar;
	
	public Tool(ToolBar toolBar, int toolID, String iconPath) {
		this.toolBar = toolBar;
		this.toolID = toolID;
		setIcon(new ImageIcon(iconPath));
		setBorder(BORDER_ACTIVE);
		setFocusable(false);
		
		addActionListener(e -> {
			isActive = true;
			this.toolBar.setActiveTool(this);
		});
	}
	
	//********************************************************
	//* 				MUTATOR METHODS						 *
	//********************************************************
	
	public void setActive(boolean active) {
		isActive = active;
		
		if (isActive) {
			setBorder(BORDER_ACTIVE);
		} else {
			setBorder(BORDER_INACTIVE);
		}
	}
	
	/**
	 * Creates and sets the cursor associated with the tool from a path and name.
	 * @param path path to resource file
	 * @param name name of cursor
	 */
	public void setCursor(String path, String name) {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image img = tk.createImage("path");
		Cursor c = tk.createCustomCursor(img, new Point(7, 0), name);
	}
	
	//********************************************************
	//* 				ACCESSOR METHODS					 *
	//********************************************************
	
	public boolean isActive() {
		return isActive;
	}
	
	public int getToolID() {
		return toolID;
	}
	
	public Cursor getCursor() {
		return cursor;
	}
	
	public String toString() {
		switch(toolID) {
		case TOOL_RECT:
			return "Rect";
		case TOOL_CIRCLE:
			return "Circle";
		case TOOL_POLY:
			return "Poly";
		case TOOL_LINE:
			return "Line";
		case TOOL_PEN:
			return "Pen";
		case TOOL_DIMENSION:
			return "Dimension";
		case TOOL_SELECT:
			return "Select";
		case TOOL_HAND:
			return "Hand";
		case TOOL_ZOOM:
			return "Zoom";
		}
		return "";
	}
}
