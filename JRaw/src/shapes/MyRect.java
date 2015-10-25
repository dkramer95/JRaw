package shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import input.Mouse;

/**
 * This class represents a basic rectangle.
 * @author DavidKramer
 *
 */
public class MyRect extends MyShape {
	
	public MyRect() {
		boundBox = new BoundingBox(startPt, endPt);
		decoration.setStrokeColor(Color.BLACK);
		decoration.setStroke(new BasicStroke(6.0f));
		decoration.setHasFill(true);
		decoration.setHasStroke(true);
	}
	
	//********************************************************
	//* 				  DRAWING METHODS					 *
	//********************************************************
	
	public void render(Graphics g) {
		final Graphics2D g2d = (Graphics2D)g.create();
		
		int startX = (int)(startPt.x * SCALE_FACTOR);
		int startY = (int)(startPt.y * SCALE_FACTOR);
		int width = (int)(getWidth() * SCALE_FACTOR);
		int height = (int)(getHeight() * SCALE_FACTOR);
		
		try {
			if (decoration.hasStroke()) {
				g2d.setColor(decoration.getStrokeColor());
				g2d.setStroke(decoration.getStroke());
//				g2d.drawRect(startPt.x, startPt.y, getWidth(), getHeight());
			
				
				g2d.drawRect(startX, startY, width, height);
			}
			
//			if (decoration.hasGradient()) { // GRADIENT DEMO!!
//				GradientPaint paintTest = new GradientPaint(0, 0, Color.RED, getWidth(), getHeight(), Color.ORANGE);
//			    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//				int w = getWidth();
//				int h = getHeight();
//		        Color color1 = Color.BLACK;
//		        Color color2 = Color.RED;
//		        GradientPaint gp = new GradientPaint(0, 0, color1, 0, h * 2, color2);
//		        g2d.setPaint(gp);
//				g2d.fillRect(startPt.x, startPt.y, getWidth(), getHeight());
//			} else if (decoration.hasFill()) {
//				g2d.setColor(decoration.getFillColor());
//				g2d.fillRect(startPt.x, startPt.y, getWidth(), getHeight());
//			}
			
			if (decoration.hasFill()) {
				g2d.setColor(decoration.getFillColor());
//				g2d.fillRect(startPt.x, startPt.y, getWidth(), getHeight());
				g2d.fillRect(startX, startY, width, height);
			}
			
			if (boundBox.isVisible() && isSelected) {
				boundBox.render(g);	
			}
			
		} finally {
			g2d.dispose();
		}
	}

	//********************************************************
	//* 				    MOUSE METHODS					 *
	//********************************************************
	
	public void handleClick(Mouse mouse) {
		if (!isSelected) {
			if (getBoundRect().contains(mouse.getClickPt())) {
				isSelected = true;
				canMove = true;
			}
		} else if (isSelected) {
			if (getBoundRect().contains(mouse.getClickPt())) {
				if (mouse.getClickCount() == 2) {
					isSelected = false;
					canMove = false;
				}
			} else {
				isSelected = false;
				canMove = false;
			}
		}
//		System.out.println("Bounding Box Start: " + boundBox.getStartPt() + ", End : " + boundBox.getEndPt());
		isDragging = false;
	}

	public void handleRelease(Mouse mouse) {
		isDragging = false;
		isMoving = false;
		isResizing = false;
		System.out.println("Mouse released!");
		if (getWidth() > 0 && getHeight() > 0) { 
			canMove = true;
		}
		update();
	}
	
	public void handleMove(Mouse mouse) {
		if (boundBox.isVisible()) {
			boundBox.handleMove(mouse);
			canResize = boundBox.getActiveHandle() != null ? true : false;
		}
		isDragging = false;
	}

	public void handleDrag(Mouse mouse) {
		Point p = getOffsetPt(mouse);
		if (!isDragging) {
			if (canResize) {
				resize(p, boundBox.getActiveHandle().getResizeDirection());
			} else if (canMove) {
				move(p);
			} else { // we haven't dragged out the shape yet
				dragShape(mouse);	
			}
		} else if (isDragging) {
			dragShape(mouse);
		}
	}
	
	//********************************************************
	//* 				  UTILITY METHODS					 *
	//********************************************************
	
	private void dragShape(Mouse mouse) {
		isDragging = true;
		startPt = mouse.getClickPt();
		endPt = mouse.getDragPt();
		checkPoints();
	}
	
	private void constrainXPts() {
		
	}

} 
