package shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import input.Mouse;

public class MyPath extends MyPoly {
	private ArrayList<MySpline> curves;
	private MySpline activeSpline;
	private boolean isDraggingCurve;
	private boolean drawTest;
	private AnchorPoint c1;
	private AnchorPoint c2;
	public MyPath() {
		curves = new ArrayList<>();
	}
	
	public void render(Graphics g) {
//		if (isConstructing) {
//			beginPt.render(g);
//			g.drawLine(beginPt.x, beginPt.y, activePt.x, activePt.y);
//			if (activeSpline != null) {
//				activeSpline.render(g);
//			}
//		}
//		
//		for (int i = 0; i < curves.size(); i++) {
//			MySpline spline = curves.get(i);
//			spline.render(g);
//		}
		final Graphics2D g2d = (Graphics2D)g.create();
		try {
			g2d.setColor(Color.BLUE);
			for (int i = 0; i < anchorPts.size(); i++) {
				AnchorPoint curPt = anchorPts.get(i);
				if (i == anchorPts.size() - 1) {
					if (drawTest) {
						g2d.setColor(Color.BLUE);
						c1.render(g);
						c2.render(g);
						g2d.drawLine(c1.x, c1.y, c2.x, c2.y);
						activeSpline.render(g);
						activeSpline.getP0().render(g);
						activeSpline.getP3().render(g);
						return;
					}
				}
				if (!(i > curves.size() - 1)) {
					curves.get(i).render(g);
				}
				curPt.render(g);
//				if (isDraggingCurve) {
//					activeSpline.render(g);
//				}
//				if (i > curves.size() - 1) {
//					return;
//				}
//				MySpline spline = curves.get(i);
//				spline.render(g);
			}	
		} finally {
			g2d.dispose();
		}
	}
	
	//********************************************************
	//* 			       MOUSE METHODS				     *
	//********************************************************
	
	public void handleDrag(Mouse mouse) {
		if (isConstructing) {
			if (activeSpline == null) {
				activeSpline = new MySpline();
			}
			Point offsetPt = getOffsetPt(mouse);	//offset of mouse movement
			
			AnchorPoint p0 = anchorPts.get(anchorPts.size() - 2);
			AnchorPoint p3 = anchorPts.get(anchorPts.size() - 1);
			System.out.println("Last Pt: " + lastPt);
			drawTest = true;
			AnchorPoint lastPt = anchorPts.get(anchorPts.size() - 1);
			c1 = new AnchorPoint(p0.x + offsetPt.x, p0.y + offsetPt.y);
			c2 = new AnchorPoint(p3.x - offsetPt.x, p3.y - offsetPt.y);
			activeSpline.setP0(p0);
			activeSpline.setC1(new ControlAnchorPoint(c1));
			activeSpline.setC2(new ControlAnchorPoint(c2));
			activeSpline.setP3(new AnchorPoint(p3.x, p3.y));
			// p0 is pt before last
			// p3 is last pt
			isDraggingCurve = true;
		}
	}
	
	public void handleRelease(Mouse mouse) {
		if (isDraggingCurve) {
			activeSpline.setP3((AnchorPoint)activePt.clone());	// make a copy of active pt
			isDraggingCurve = false;
			curves.add(activeSpline);
			activeSpline = new MySpline();
		}
	}
	
	public void addCurve(MySpline curve) {
		curves.add(curve);
	}
}
