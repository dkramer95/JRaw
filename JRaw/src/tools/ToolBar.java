package tools;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JToolBar;

import buttons.FillStrokeButton;
import gfx.Canvas;
import managers.CanvasManager;

/**
 * This class contains all of the available tools in one panel that is on the left side 
 * of the screen. Users can toggle between each tool and it will update the status
 * in the canvas manager to allow the user to perform different actions.
 * @author DavidKramer
 *
 */
public class ToolBar extends JToolBar {
	private Canvas canvas;
	private CanvasManager canvasManager;
	
	private Tool activeTool;
	private ArrayList<Tool> tools;
	
	private Tool selectTool;
	private Tool rectTool;
	private Tool circleTool;
	private Tool polyTool;
	private Tool lineTool;
	private Tool penTool;
	private Tool dimensionTool;
	private Tool zoomTool;
	
	private FillStrokeButton fillStrokeBtn;
	
	public ToolBar(Canvas canvas) {
		this.canvas = canvas;
		canvasManager = this.canvas.getCanvasManager();
		canvasManager.setToolBar(this);
		tools = new ArrayList<>();
		initTools();
		
		setBackground(new Color(209, 210, 212));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setFloatable(false);
		setFocusable(false);
	}
	
	/**
	 * Creates all of the available tools and adds them to the toolbar. By default, the first
	 * tool (which is the selection tool) is the active tool.
	 */
	private void initTools() {
		selectTool = new Tool(this, Tool.TOOL_SELECT, "res/icons/select_32_32.gif");
		rectTool = new Tool(this, Tool.TOOL_RECT, "res/icons/rect2_32_32.gif");
		circleTool = new Tool(this, Tool.TOOL_CIRCLE, "res/icons/ellipse_32_32.gif");
		polyTool = new Tool(this, Tool.TOOL_POLY, "res/icons/polygon_32_32.gif");
		lineTool = new Tool(this, Tool.TOOL_LINE, "res/icons/line_32_32.gif");
		penTool = new Tool(this, Tool.TOOL_PEN, "res/icons/pen_32_32.gif");
		dimensionTool = new Tool(this, Tool.TOOL_DIMENSION, "res/icons/dimension_32_32.gif");
		zoomTool = new Tool(this, Tool.TOOL_ZOOM, "res/icons/zoom_32_32.gif");
		
		polyTool.setCursor("res/cursors/penCursor.png", "pen");
		
		// TODO These tools don't work yet! REMOVE WHEN IMPLEMENTED!
//		penTool.setEnabled(false);
		dimensionTool.setEnabled(false);
//		zoomTool.setEnabled(false);
		
		selectTool.setToolTipText("Click on shapes or drag a rectangular marquee");
		tools.add(selectTool);
		tools.add(rectTool);
		tools.add(circleTool);
		tools.add(polyTool);
		tools.add(lineTool);
		tools.add(penTool);
		tools.add(dimensionTool);
		tools.add(zoomTool);
		
		fillStrokeBtn = new FillStrokeButton(canvas);
		
		for (int i = 0; i < tools.size(); i++) {
			Tool tool = tools.get(i);
			tool.setActive(false);
			add(tool);
//			add(Box.createVerticalStrut(1));
		}
		
		add(Box.createVerticalGlue());
		add(Box.createVerticalStrut(50));
		add(fillStrokeBtn);
		add(Box.createVerticalStrut(50));
		
		tools.get(0).setActive(true);
		canvasManager.setActiveTool(tools.get(0));
	}
	
	//********************************************************
	//* 				  UTILITY METHODS					 *
	//********************************************************
	
	private void updateTools() {
		for (int i = 0; i < tools.size(); i++) {
			Tool tool = tools.get(i);
			
			if (!tool.equals(activeTool)) {
				tool.setActive(false);
			} else {
				tool.setActive(true);
			}
		}
		canvasManager.setActiveTool(activeTool);
		canvasManager.updateCursor(activeTool.getCursor());
	}

	
	//********************************************************
	//* 				  MUTATOR METHODS					 *
	//********************************************************
	
	public void setActiveTool(Tool tool) {
		activeTool = tool;
		updateTools();
	}
	
	//********************************************************
	//* 				  ACCESSOR METHODS					 *
	//********************************************************
	
	public Canvas getCanvas() {
		return canvas;
	}
	
	public FillStrokeButton getFillStrokeBtn() {
		return fillStrokeBtn;
	}
	
	public ArrayList<Tool> getTools() {
		return tools;
	}

}
