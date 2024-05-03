package model.shapes;

import model.shapeContainers.ShapeContainer;
import view.types.ShapeDataType;
import view.types.ShapeType;
import view.GUIElements.canvas.CustomCanvas;

import java.io.Serializable;
import java.util.List;
/**
 * Interface for handling shapes
 */
public interface Shape extends Serializable {
    /**
     * Adds a child to the shape
     * @param shape child to be added
     */
    void addChild(Shape shape);
    /**
     * Returns the list of children
     * @return list of children
     */
    List<Shape> getChildren();
    /**
     * Returns the list of points
     * @return list of points
     */
    List<Point> getPoints();
    /**
     * Returns the X coordinate of the shape
     * @return X coordinate of the shape
     */
    double getX();
    /**
     * Returns the Y coordinate of the shape
     * @return Y coordinate of the shape
     */
    double getY();
    /**
     * Returns the centroid X coordinate of the shape
     * @return centroid X coordinate of the shape
     */
    double getCentroidX();
    /**
     * Returns the centroid Y coordinate of the shape
     * @return centroid Y coordinate of the shape
     */
    double getCentroidY();
    /**
     * Returns the width of the shape
     * @return width of the shape
     */
    double getWidth();
    /**
     * Returns the height of the shape
     * @return height of the shape
     */
    double getHeight();

    /**
     * Draws the shape
     * @param gc custom canvas
     */
    void draw(CustomCanvas gc);
    /**
     * Calculates the length of the shape
     * @return length of the shape
     */
    double calculateShapeLength();
    /**
     * Calculates the area of the shape
     * @return area of the shape
     */
    double calculateShapeArea();
    /**
     * Calculates the centroid of the shape
     */
    void calculateCentroid();
    /**
     * Returns the priority of the shape
     * @return priority of the shape
     */
    int getPriority();
    /**
     * Calculates the distance from the mouse
     * @param x X coordinate
     * @param y Y coordinate
     */
    double calculateDistanceFromMouse(double x, double y);
    /**
     * Sets the coordinates of the shape
     * @param x X coordinate
     * @param y Y coordinate
     */
    void setCoordinates(double x, double y);
    /**
     * Adds the shape to a shape container
     * @param shapeContainer shape container
     */
    void addToShapeContainer(ShapeContainer shapeContainer);

    /**
     * Removes a child from the shape
     * @param shape
     */
    void removeChild(Shape shape);
    /**
     * Removes a point from the shape
     * @param point
     */
    void removePoint(Point point);
    /**
     * Deletes the shape from the shape container
     * @param shapeContainer shape container
     */
    void delete(ShapeContainer shapeContainer);
    /**
     * Returns the type of the shape
     * @return type of the shape
     */
    ShapeType getType();
    /**
     * Draws the length of the shape
     * @param gc custom canvas
     */
    void drawLength(CustomCanvas gc, String unit, double modifier);

    /**
     * Assigns an ID to the shape
     */
    public void addToHistory();
    /**
     * Returns the ID of the shape
     * @return ID of the shape
     */
    public boolean getAddedToHistory();
    /**
     * Returns the selected X coordinate
     * @return
     */
    public double getSelectedX();
    /**
     * Sets the selected coordinates
     * @param x X coordinate
     * @param y Y coordinate
     */
    public void setSelectedCoordinates(double x, double y);
    /**
     * Updates the selected coordinates
     */
    public void updateSelectedCoordinates();
    /**
     * Returns the selected Y coordinate
     * @return
     */
    public double getSelectedY();
    /**
     * Sets the selected state of the shape
     * @param selected selected state
     */
    public void setSelected(boolean selected);
    /**
     * Returns the selected state of the shape
     * @return selected state of the shape
     */
    public boolean isSelected();
    ShapeDataType getShapeDataType();

    /**
     * Sets the shape data type
     * @param shapeDataType
     */
    void setShapeDataType(ShapeDataType shapeDataType);
}
