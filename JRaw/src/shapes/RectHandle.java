package shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class RectHandle extends Rectangle {

	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	public static final int OFFSET = WIDTH / 2;
	
	public static final Color COLOR = Color.BLUE;
	
	// Resize Directions Based On Mouse Cursor
	public static final int NORTH = Cursor.N_RESIZE_CURSOR;
	public static final int SOUTH = Cursor.S_RESIZE_CURSOR;
	public static final int EAST = Cursor.E_RESIZE_CURSOR;
	public static final int WEST = Cursor.W_RESIZE_CURSOR;
	public static final int NORTH_EAST = Cursor.NE_RESIZE_CURSOR;
	public static final int NORTH_WEST = Cursor.NW_RESIZE_CURSOR;
	public static final int SOUTH_EAST = Cursor.SE_RESIZE_CURSOR;
	public static final int SOUTH_WEST = Cursor.SW_RESIZE_CURSOR;
	
	private Cursor resizeCursor;
	private boolean isHovered;
	private int resizeDirection;
	
	public RectHandle(int startX, int startY, int resizeDirection) {
		this.x = startX - OFFSET;
		this.y = startY - OFFSET;
		this.resizeDirection = resizeDirection;
		
		resizeCursor = new Cursor(this.resizeDirection);
		setBounds(this.x, this.y, WIDTH, HEIGHT);
	}
	
	//********************************************************
	//* 				  DRAWING METHODS					 *
	//********************************************************
	
	public void render(Graphics g) {
		final Graphics2D g2d = (Graphics2D)g.create();
		try {
			g2d.setColor(COLOR);
			g2d.setStroke(new BasicStroke(2.0f));
			g2d.drawRect(x, y, WIDTH, HEIGHT);	// outer rectangle outline
			g2d.setColor(Color.WHITE);
			g2d.fillRect(x, y, WIDTH, HEIGHT);	// inner rectangle
			g2d.setColor(COLOR);
			g2d.fillRect(x + 1, y + 1, WIDTH - 2, HEIGHT - 2);
		} finally {
			g2d.dispose();
		}
	}
	
	//********************************************************
	//* 				  ACCESSOR METHODS					 *
	//********************************************************
	
	public boolean checkHover(Point p) {
		return isHovered = contains(p) ? true : false;
	}
	
	public boolean isHovered() {
		return isHovered;
	}
	
	public Cursor getResizeCursor() {
		return resizeCursor;
	}
	
	public int getResizeDirection() {
		return resizeDirection;
	}
}
