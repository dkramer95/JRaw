package gfx;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.io.Serializable;

import shapes.MyShape;
import shapes.Selection;

/**
 * This class defines the decoration or appearance of a shape, which consists of a 
 * fill color, stroke color, and the actual stroke itself. The fill color can
 * also become a gradient.
 * @author DavidKramer
 *
 */
public class Decoration implements Serializable, Cloneable {
	public static final Color DEFAULT_FILL_COLOR = Color.WHITE;
	public static final Color DEFAULT_STROKE_COLOR = Color.BLACK;
	public static final BasicStroke DEFAULT_STROKE = new BasicStroke(6.0f);
	
	private Color fillColor;
	transient private GradientPaint gradient;
	private Color strokeColor;
	transient private BasicStroke stroke;
	
	private boolean hasGradient;
	private boolean hasFill;
	private boolean hasStroke;
	
	private int cap;
	private int join;
	private float strokeWidth;
	
	public Decoration(Color fillColor, Color strokeColor, BasicStroke stroke) {
		this.fillColor = fillColor;
		this.strokeColor = strokeColor;
		this.stroke = stroke;
		cap = DEFAULT_STROKE.getEndCap();
		join = DEFAULT_STROKE.getLineJoin();
		strokeWidth = 6.0f;
	}
	
	public Decoration(GradientPaint gradient, Color strokeColor, BasicStroke stroke) {
		this.gradient = gradient;
		this.strokeColor = strokeColor;
		this.stroke = stroke;
		cap = DEFAULT_STROKE.getEndCap();
		join = DEFAULT_STROKE.getLineJoin();
		strokeWidth = 1.0f;
	}
	
	public Decoration() {
		this.gradient = new GradientPaint(0.0f, 0.0f, Color.RED, 1.0f, 1.0f, Color.BLUE);
		this.fillColor = DEFAULT_FILL_COLOR;
		this.strokeColor = DEFAULT_STROKE_COLOR;
		this.stroke = DEFAULT_STROKE;
	}
	
	//********************************************************
	//* 				STATIC UTILITY METHODS				 *
	//********************************************************
	
	/**
	 * Decorates all shapes contained in a selection with the specified decoration
	 * @param selection - active selection
	 * @param decoration - decoration context to apply to shapes
	 */
	public static void decorateSelection(Selection selection, Decoration decoration) {
		for (int i = 0; i < selection.getSelectedShapes().size(); i++) {
			MyShape shape = selection.getSelectedShapes().get(i);
			shape.setDecoration((Decoration)decoration.clone());	
		}
	}
	
	/**
	 * Decorates all shapes contained in a selection with the specified fill / stroke. 
	 * Generally fill / stroke should be false, otherwise the other decorateSelection
	 * method should be called because it will apply everything.
	 * @param selection - active selection
	 * @param decoration - decoration context to apply to shapes
	 * @param fill - if true, the fill style is applied
	 * @param stroke - if true, the stroke style is applied
	 */
	public static void decorateSelection(Selection selection, Decoration decoration, boolean fill, boolean stroke) {
		for (int i = 0; i < selection.getSelectedShapes().size(); i++) {
			MyShape shape = selection.getSelectedShapes().get(i);
			if (fill && stroke) {
				shape.setDecoration((Decoration)decoration.clone());	
			} else if (fill) {
				shape.getDecoration().setFillColor(decoration.getFillColor());
			} else if (stroke) {
				shape.getDecoration().setStroke(decoration.getStroke());
				shape.getDecoration().setStrokeColor(decoration.getStrokeColor());
			}
		}
	}
	
	//********************************************************
	//* 				  MUTATOR METHODS					 *
	//********************************************************
	
	public void setFillColor(Color c) {
		fillColor = c;
	}
	
	public void setStrokeColor(Color c) {
		strokeColor = c;
	}
	
	public void setStroke(BasicStroke stroke) {
		this.stroke = stroke;
	}
	
	public void setStrokeWidth(float f) {
		strokeWidth = f;
	}
	
	public void setHasFill(boolean b) {
		hasFill = b;
	}
	
	public void setHasGradient(boolean b) {
		hasGradient = b;
	}
	
	public void setHasStroke(boolean b) {
		hasStroke = b;
	}
	
	public void setGradient(GradientPaint g) {
		gradient = g;
	}
	
	public void setJoinType(int joinType) {
		this.join = joinType;
	}
	
	public void setCapType(int capType) {
		this.cap = capType;
	}
	
	//********************************************************
	//* 				  ACCESSOR METHODS					 *
	//********************************************************
	
	public Color getFillColor() {
		return fillColor;
	}
	
	public GradientPaint getGradient() {
		return gradient;
	}
	
	public Color getStrokeColor() {
		return strokeColor;
	}
	
	public BasicStroke getStroke() {
		return stroke = new BasicStroke(getStrokeWidth(), getCap(), getJoin());
//		return stroke;
	}
	
	public boolean hasFill() {
		return hasFill;
	}
	
	public boolean hasGradient() {
		return hasGradient;
	}
	
	public boolean hasStroke() {
		return hasStroke;
	}
	
	public int getJoin() {
		return join;
	}
	
	public int getCap() {
		return cap;
	}
	
	public float getStrokeWidth() {
		return strokeWidth;
	}
	
	/**
	 * Determines if the current object is equal to another decoration object
	 * @param d
	 * @return
	 */
	public boolean equals(Decoration d) {
		if (!this.getFillColor().equals(d.getFillColor())) {	// are fills same?
			return false;
		} else if (!this.hasFill() == d.hasFill()) {
			return false;
		} else if (!this.hasStroke() == d.hasStroke()) {
			return false;
		} else if (!this.getStrokeColor().equals(d.getStrokeColor())) { // is stroke color same?
			return false;
		} else if (this.getJoin() != d.getJoin()) { // is stroke same?
			return false;
		} else if (this.getCap() != d.getCap()) {
			return false;
		} else if (this.getStrokeWidth() != d.getStrokeWidth()) {
			return false;
		} else if (!this.getStroke().equals(getStroke())) {
			return false;
		} else {
			return true;
		}
	}
	
	public String toString() {
		String str = "Decoration[hasFill=" + hasFill + ", hasStroke=" + hasStroke 
				+ ", fillColor=" + fillColor + "strokeColor=" + strokeColor
				+ ", stroke=" + stroke + "]";
		return str;
	}
	
	/**
	 * Useful so that each shape object will have its own decoration, when referencing
	 * decoration objects from other classes. This ensures that each decoration will
	 * be unique to each shape.
	 */
	public Object clone() {
		try {
			return (Decoration)super.clone();
		} catch (CloneNotSupportedException e) {
			System.out.println("Clone not supported!");
		}
		return null;
	}
	
}
