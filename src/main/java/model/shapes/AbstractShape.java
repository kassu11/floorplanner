package model.shapes;

import model.shapeContainers.ShapeContainer;
import view.GUIElements.canvas.CustomCanvas;
import view.events.ShapeMath;

import java.util.ArrayList;
import java.util.List;
/**
 * Abstract class for handling shapes
 */
public abstract class AbstractShape implements Shape {
    /**
     * Counter for the shape id
     */
    private static int idCounter = 1;
    /**
     * X coordinate of the shape
     * Y coordinate of the shape
     * Width of the shape
     * Height of the shape
     * Centroid X coordinate of the shape
     * Centroid Y coordinate of the shape
     * Selected X coordinate of the shape
     * Selected Y coordinate of the shape
     */
    private double x, y, width, height, centroidX, centroidY, selectedX, selectedY;
    /**
     * List of points
     */
    private List<Point> points = new ArrayList<>();
    /**
     * List of children shapes
     */
    private List<Shape> children = new ArrayList<>();
    /**
     * Parent shape
     */
    private Shape parentShape;
    /**
     * Priority of the shape
     * Id of the shape
     */
    private int priority, id;
    /**
     * Boolean value for selected
     */
    private boolean isSelected;
    /**
     * Constructor for the abstract shape with coordinates
     * @param x X coordinate of the shape
     * @param y Y coordinate of the shape
     */

    public AbstractShape(double x, double y) {
        this.x = ShapeMath.toFixed(x, 9);
        this.y = ShapeMath.toFixed(y, 9);
    }
    /**
     * Constructor for the abstract shape with a list of shapes and a list of points
     * @param shapes list of shapes
     * @param points list of points
     */
    public AbstractShape(List<Shape> shapes, List<Point> points) {
        this.points.addAll(points);
    }
    /**
     * Constructor for the abstract shape with 2 points
     * @param pointA point A
     * @param pointB point B
     */
    public AbstractShape(Point pointA, Point pointB) {
        points.add(pointA);
        points.add(pointB);
        updateDimensions();
        for (Point point : points) {
            point.addChild(this);
        }
    }
    /**
     * Constructor for the abstract shape with 3 points
     * @param pointA point A
     * @param pointB point B
     * @param pointC point C
     */
    public AbstractShape(Point pointA, Point pointB, Point pointC) {
        points.add(pointA);
        points.add(pointB);
        points.add(pointC);
        updateDimensions();
    }
    /**
     * Constructor for the abstract shape with 4 points
     * @param pointA point A
     * @param pointB point B
     * @param pointC point C
     * @param pointD point D
     */
    public AbstractShape(Point pointA, Point pointB, Point pointC, Point pointD) {
        points.add(pointA);
        points.add(pointB);
        points.add(pointC);
        points.add(pointD);
        updateDimensions();
        this.id = idCounter++;
    }

    /**
     * Calculates the dimensions of the shape
     * @param pointA point A
     * @param pointB point B
     */
    private Point calculateDimensions(Point a, Point b) {
        this.x = Math.min(a.getX(), b.getX());
        this.y = Math.min(a.getY(), b.getY());
        this.width = Math.max(a.getX(), b.getX()) - this.x;
        this.height = Math.max(a.getY(), b.getY()) - this.y;
        return b;
    }

    /**
     * Updates the dimensions of the shape
     */
    protected void updateDimensions() {
        points.stream().reduce(this::calculateDimensions);
        calculateCentroid();
    }

    /**
     * Returns the selected X coordinate
     * @return selected X coordinate
     */
    public double getSelectedX() {
        return selectedX;
    }
    /**
     * Sets the selected coordinates
     * @param x X coordinate
     * @param y Y coordinate
     */
    public void setSelectedCoordinates(double x, double y) {
        this.selectedX = x;
        this.selectedY = y;
    }
    /**
     * Updates the selected coordinates
     */
    public void updateSelectedCoordinates() {
        this.selectedX = this.x;
        this.selectedY = this.y;
    }
    /**
     * Returns the selected Y coordinate
     * @return selected Y coordinate
     */
    public double getSelectedY() {
        return selectedY;
    }
    /**
     * Assigns an id to the shape
     */
    public void assignId() {
        if (this.id == 0) this.id = idCounter++;
    }
    /**
     * Assigns an id to the shape
     * @param id id
     */
    public void assignId(int id) {
        this.id = id;
    }
    /**
     * Returns the id of the shape
     * @return id
     */
    public int getId() {
        return this.id;
    }
    /**
     * Calculates the centroid of the shape
     */
    public void calculateCentroid() {
        double sumX = 0;
        double sumY = 0;
        for (Point point : points) {
            sumX += point.getX();
            sumY += point.getY();
        }
        centroidX = sumX / points.size();
        centroidY = sumY / points.size();
    }
    /**
     * Adds a child shape to the shape
     * @param shape child shape
     */
    public void addChild(Shape shape) {
        children.add(shape);
    }
    /**
     * Gets the list of points
     * @return points list of points
     */
    public List<Point> getPoints() {
        return points;
    }
    /**
     * Removes a child point from the shape
     * @param Point point
     */
    public void removeChild(Shape shape) {
        children.remove(shape);
    }

    /**
     * Removes a point from the shape
     * @param Point point
     */
    public void removePoint(Point point) {
        boolean removed = points.remove(point);
        if (removed) point.removeChild(this);
    }
    /**
     * Returns the list of children
     * @return list of children
     */
    public List<Shape> getChildren() {
        return children;
    }
    /**
     * Returns the X coordinate of the shape
     * @return X coordinate
     */
    public double getX() {
        return x;
    }
    /**
     * Sets the X coordinate of the shape
     * @param x X coordinate
     */
    public void setX(double x) {
        this.x = ShapeMath.toFixed(x, 9);
    }
    /**
     * Returns the Y coordinate of the shape
     * @return Y coordinate
     */
    public double getY() {
        return y;
    }
    /**
     * Sets the Y coordinate of the shape
     * @param y Y coordinate
     */
    public void setY(double y) {
        this.y = ShapeMath.toFixed(y, 9);
    }
    /**
     * Returns the centroid X coordinate of the shape
     * @return centroid X coordinate
     */
    public double getCentroidX() {
        return centroidX;
    }
    /**
     * Returns the centroid Y coordinate of the shape
     * @return centroid Y coordinate
     */
    public double getCentroidY() {
        return centroidY;
    }
    /**
     * Returns the parent shape
     * @return parentShape
     */
    public Shape getParentShape() {
        return parentShape;
    }
    /**
     * Draws the shape, implementations vary based on shape type
     * @param CustomCanvas gc
     */
    public abstract void draw(CustomCanvas gc);
    /**
     * Sets the parent shape
     * @param parentShape parent shape
     */
    public void setParentShape(Shape parentShape) {
        this.parentShape = parentShape;
    }
    /**
     * Returns the width of the shape
     * @return width of the shape
     */
    public double getWidth() {
        return width;
    }
    /**
     * Returns the height of the shape
     * @return height of the shape
     */
    public double getHeight() {
        return height;
    }
    /**
     * Returns the priority of the shape
     * @return priority of the shape
     */
    public int getPriority() {
        return priority;
    }
    /**
     * Calculates the distance from the mouse
     * @param x X coordinate
     * @param y Y coordinate
     * @return distance from the mouse
     */
    public double calculateDistanceFromMouse(double x, double y) {
        double deltaX = this.getX() - x + this.getWidth() / 2;
        double deltaY = this.getY() - y + this.getHeight() / 2;

        return Math.hypot(deltaX, deltaY);
    }
    /**
     * Sets the coordinates of the shape
     * @param x X coordinate
     * @param y Y coordinate
     */
    public void setCoordinates(double x, double y) {
        this.x = x;
        this.y = y;
        calculateCentroid();
    }
    /**
     * Adds the shape to the shape container
     * @param shapeContainer shape container
     */
    public void addToShapeContainer(ShapeContainer shapeContainer) {
        shapeContainer.addShape(this);
    }
    /**
     * Deletes the shape from the shape container
     * @param shapeContainer shape container
     */
    public void delete(ShapeContainer shapeContainer) {
    }
    /**
     * Returns the type of the shape
     * @return type of the shape
     */
    public void drawLength(CustomCanvas gc, String unit, double modifier) {
    }
    /**
     * Sets the selected boolean value
     * @param selected selected
     */
    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    /**
     * Returns the selected boolean value
     * @return selected boolean value
     */
    public boolean isSelected() {
        return isSelected;
    }
}
