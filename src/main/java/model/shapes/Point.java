package model.shapes;

import model.shapeContainers.ShapeContainer;
import view.GUIElements.canvas.CustomCanvas;
import view.types.ShapeType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * Concrete class for handling points
 */
public class Point extends AbstractShape {
    /**
     * Width of the point
     * Height of the point
     */
    private double width = 15, height = 15;
    /**
     * Priority of the point
     */
    private int priority = 1;
    /**
     * List of dimensions
     */
    private List<Dimension> dimensions = new ArrayList<>();
    /**
     * Constructor for the point with coordinates
     * @param x X coordinate of the point
     * @param y Y coordinate of the point
     */
    public Point(double x, double y) {
        super(x, y);
    }
    /**
     * Returns the width of the point
     * @return width of the point
     */
    @Override
    public double getWidth() {
        return width;
    }
    /**
     * Returns the height of the point
     * @return height of the point
     */
    @Override
    public double getHeight() {
        return height;
    }
    /**
     * Draws the point
     * @param gc custom canvas
     */
    public void draw(CustomCanvas gc) {
        gc.fillOvalWithOutScaling(this.getX(), this.getY(), width, height);
    }
    /**
     * Calculates the length of the point
     * @return length of the point
     */
    @Override
    public double calculateShapeLength() {
        return 0;
    }
    /**
     * Calculates the area of the point
     * @return area of the point
     */
    @Override
    public double calculateShapeArea() {
        return 0;
    }
    /**
     * Calculates the distance from the mouse to the point
     * @param x X coordinate of the mouse
     * @param y Y coordinate of the mouse
     * @return distance from the mouse to the point
     */
    @Override
    public double calculateDistanceFromMouse(double x, double y) {
        double deltaX = this.getX() - x;
        double deltaY = this.getY() - y;

        return Math.hypot(deltaX, deltaY);
    }
    /**
     * Sets the coordinates of the point
     * @param x X coordinate
     * @param y Y coordinate
     */
    @Override
    public void setCoordinates(double x, double y) {
        this.setX(x);
        this.setY(y);
    }
    /**
     * Sets the coordinates of the point
     * @param coordinates coordinates of the point
     */
    public void setCoordinates(double[] coordinates) {
        this.setX(coordinates[0]);
        this.setY(coordinates[1]);
    }
    /**
     * Returns the priority of the point
     * @return priority of the point
     */
    @Override
    public int getPriority() {
        return priority;
    }
    /**
     * Adds the point to the shape container
     * @param shapeContainer shape container
     */
    @Override
    public void addToShapeContainer(ShapeContainer shapeContainer) {
        shapeContainer.addShape(this);
    }
    /**
     * Returns the type of the shape
     * @return type of the shape
     */
    @Override
    public ShapeType getType() {
        return ShapeType.POINT;
    }
    /**
     * Deletes the point from the shape container
     * @param shapeContainer shape container
     */
    @Override
    public void delete(ShapeContainer shapeContainer) {
        if (shapeContainer != null) shapeContainer.getShapes().remove(this);
        Iterator<Shape> iterator = getChildren().iterator();
        while (iterator.hasNext()) {
            Shape shape = iterator.next();
            iterator.remove();
            shape.removePoint(this);
            shape.delete(shapeContainer);
        }
    }
    /**
     * Adds a dimension to the list of dimensions
     * @param dimension dimension to be added
     */
    public void addDimension(Dimension dimension) {
        dimensions.add(dimension);
    }
}
