/* Bao Nguyen
 * Brain Juice
 */

package util;

import java.util.ArrayList;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Sprite {
	private GraphicsContext gc;
	private Image image;
	private String currentPath;
	
	private double width;
	private double height;

	private double x;
	private double y;
	
	private BoundingBox bounds;

	private ArrayList<String> framePaths;
	
	private boolean exists = true;

	public Sprite(GraphicsContext graphicsContext)	{
		gc = graphicsContext;
		
		width = 10;
		height = 10;
		
		x = 0;
		y = 0;

		framePaths = new ArrayList<String>();
		
		exists = true;
	}
	
	public double getWidth() {
		return width;
	}
	
	public void setWidth(double width) {
		this.width = width;
	}
	
	public double getHeight() {
		return height;
	}
	
	public void setHeight(double height) {
		this.height = height;
	}
	
	public int getFrameIndex() {
		return framePaths.indexOf(currentPath);
	}
	
	public String getFramePath() {
		return currentPath;
	}
	
	public void addFrame(String imagePath) {
		currentPath = imagePath;		
		framePaths.add(imagePath);
	}

	public void setFrame(int index) {
		image = new Image("file:" + framePaths.get(index));
		currentPath = framePaths.get(index);
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}

	public void setPosition(double x, double y)	{
		this.x = x;
		this.y = y;
	}

	public Bounds getBounds() {
		return bounds;
	}

	public void display()	{
		gc.drawImage(image, x, y, width, height);
		bounds = new BoundingBox(x, y, width, height);
	}
	
	public void display(double x, double y, double width, double height) {
		gc.drawImage(image, x, y, width, height);
		bounds = new BoundingBox(x, y, width, height);
	}
	
	public boolean exists() {
		return exists;
	}
	
	public void setExists(boolean value) {
		exists = value;
	}
}
