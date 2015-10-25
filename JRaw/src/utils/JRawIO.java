package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import gfx.Canvas;
import shapes.MyShape;

public class JRawIO {
	private Canvas canvas;
	
	private JFileChooser chooser;
	private FileNameExtensionFilter filter;
	
	public JRawIO(Canvas canvas) {
		this.canvas = canvas;
		chooser = new JFileChooser();
		filter = new FileNameExtensionFilter("Jraw", "jraw");
		chooser.setFileFilter(filter);
	}
	
	public void showOpenDialog() {
		int openValue = chooser.showOpenDialog(null);
		
		if (openValue == JFileChooser.APPROVE_OPTION) {
			System.out.println("File: " + chooser.getSelectedFile().getAbsolutePath() + ".jraw");
			String filename = chooser.getSelectedFile().getAbsolutePath();
			readFile(filename);
		} else {
			JOptionPane.showMessageDialog(null, "Open terminated!");
		}
	}

	public void showSaveDialog() {
		int saveValue = chooser.showSaveDialog(null);
		
		if (saveValue == JFileChooser.APPROVE_OPTION) {
			System.out.println("File: " + chooser.getSelectedFile().getAbsolutePath() + ".jraw");
			String filename = chooser.getSelectedFile().getAbsolutePath() + ".jraw";
			writeFile(filename);
		} else {
			JOptionPane.showMessageDialog(null, "Save terminated! No changes made!");
		}
	}
	
	public void writeFile(String filename) {
		try {
			System.out.println("Write file filename: " + filename);
			File f = new File(filename);
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(filename));
			ArrayList<MyShape> canvasShapes = canvas.getShapes();
			os.writeObject(canvasShapes);
			os.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Done writing!");
	}
	
	public void readFile(String filename) {
		try {
			ObjectInputStream is = new ObjectInputStream(new FileInputStream(filename));
			ArrayList<MyShape> shapes = new ArrayList<>();
			shapes = (ArrayList<MyShape>)is.readObject();
			System.out.println("Shapes: " + shapes);
			
			for (int i = 0; i < shapes.size(); i++) {
				canvas.addShape(shapes.get(i));
			}
			canvas.repaint();
			
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		} 
		
		System.out.println("Done reading!");
	}
}
