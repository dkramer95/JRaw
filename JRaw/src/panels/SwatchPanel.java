package panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import buttons.SwatchButton;
import gfx.Canvas;
import gfx.Decoration;

public class SwatchPanel extends JPanel {
	public static final int MAX_SWATCHES = 20;
	
	private ArrayList<SwatchButton> swatches;
	private JScrollPane scrollPane;
	private Canvas canvas;
	
	public SwatchPanel(Canvas canvas) {
		this.canvas = canvas;
		setPreferredSize(new Dimension(1280, 28));
		setLayout(new FlowLayout(FlowLayout.LEFT));
		swatches = new ArrayList<>();
		add(Box.createHorizontalStrut(45));
		createSwatches();
	}
	
	/**
	 * Generates random color value swatches and adds them to the swatch panel.
	 */
	private void createSwatches() {
		Random rng = new Random();
		
		int swatchCount = 54;
		
		ArrayList<Color> colors = new ArrayList<>();
		colors.add(Color.RED);
		colors.add(Color.GREEN);
		colors.add(Color.BLUE);
		colors.add(Color.CYAN);
		colors.add(Color.MAGENTA);
		colors.add(Color.YELLOW);
		colors.add(Color.BLACK);
		colors.add(Color.LIGHT_GRAY);
		colors.add(Color.DARK_GRAY);
		colors.add(Color.ORANGE);
		colors.add(Color.PINK);
		
		for (int i = 0; i < colors.size(); i++) {			
			Color c = colors.get(i);
			SwatchButton button = new SwatchButton(canvas);
			
			Decoration d = new Decoration();
			d.setHasFill(true);
			d.setFillColor(c);
			
			button.setDecoration((Decoration)d.clone());
			button.getDecoration().setFillColor(d.getFillColor());
			swatches.add(button);
		}
		
		for (int i = 0; i < swatches.size(); i++) {
			add(swatches.get(i));
		}
		
	}
	
	
	public void addSwatch() {}	//TODO - add swatches to swatch panel
	
}
