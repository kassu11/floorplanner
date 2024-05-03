package model.shapes;

import view.GUIElements.canvas.CustomCanvas;
import view.types.ShapeType;

import java.util.List;
/**
 * Concrete class for handling custom shapes
 */
public class CustomShape extends AbstractShape {
    /**
     * Constructor for the custom shape with a list of shapes and a list of points
     * @param shapes list of shapes
     * @param points list of points
     */

    public CustomShape(List<Shape> shapes, List<Point> points) {
        super(shapes, points);
    }
    /**
     * Draws the custom shape
     * @param gc custom canvas
     */
    @Override
    public void draw(CustomCanvas gc) {

    }
    /**
     * Calculates the length of the custom shape
     * @return length of the custom shape
     */
    @Override
    public double calculateShapeLength() {
        return 0;
    }
    /**
     * Calculates the area of the custom shape
     * @return area of the custom shape
     */
    @Override
    public double calculateShapeArea() {
        return 0;
    }
    /**
     * Calculates the distance from the mouse to the custom shape
     * @param x X coordinate of the mouse
     * @param y Y coordinate of the mouse
     * @return distance from the mouse to the custom shape
     */
    @Override
    public ShapeType getType() {
        return ShapeType.CUSTOMSHAPE;
    }

}
