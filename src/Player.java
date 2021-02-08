import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Player {
	
	double xCoord;
	double yCoord;
	double radius;
	Color colour;
	
	
	public Player(double x, double y, double radius, Color colour) {
		xCoord = x;
		yCoord = y;
		this.radius = radius;
		this.colour = colour;
		
	}
	
	public Circle drawPlayer() {
		Circle circle = new Circle(xCoord, yCoord, radius, colour);
		return circle;
	}
	
	public void xMovement(double x) {
		xCoord += x;
	}
	
	public void yMovement(double x) {
		yCoord += x;
	}

	public void setX (double x) {
		xCoord = x;
	}
	
	public void setY(double x) {
		yCoord = x;
	}
	
	public double getX() {
		return xCoord;
	}
	
	public double getY() {
		return yCoord;
	}
	
}
