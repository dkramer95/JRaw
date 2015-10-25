package shapes;

import tools.Tool;

/**
 * Factory class for creating objects of MyShape.
 * @author DavidKramer
 *
 */
public class ShapeFactory {

	/**
	 * Returns a new instance of a shape based on the active tool.
	 * @param tool The tool to create a shape from
	 * @return a shape based on the type of tool
	 */
	public static MyShape createShapeFromTool(Tool tool) {
		switch (tool.getToolID()) {
		case Tool.TOOL_RECT:
			return new MyRect();
		case Tool.TOOL_CIRCLE:
			return new MyCircle();
		case Tool.TOOL_LINE:
			return new MyLine();
		case Tool.TOOL_POLY:
			return new MyPoly();
		case Tool.TOOL_PEN:
			return new MyPath();
		default:  // illegal tool
			throw new IllegalArgumentException("Invalid tool. Must be of type shape!");
		}
	}
}
