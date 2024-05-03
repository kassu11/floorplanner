package model.shapeContainers;

import model.shapes.Shape;

import java.util.ArrayList;
import java.util.List;
/**
 * Abstract class for handling shape containers
 */
public abstract class ShapeContainer {
    /**
     * List of shapes
     */
    private List<Shape> shapes;
    /**
     * Constructor for the shape container
     */
    protected ShapeContainer() {
        this.shapes = new ArrayList<>();
    }
    /**
     * Returns the list of shapes
     * @return list of shapes
     */
    public List<Shape> getShapes() {
        return shapes;
    }
    /**
     * Adds a shape to the list of shapes
     * @param shape shape to be added
     */
    public void addShape(Shape shape) {
        this.shapes.add(shape);
    }
    /**
     * Adds a list of shapes to the list of shapes
     * @param shapes list of shapes to be added
     */
    public void addAllShapes(List<Shape> shapes) {
        this.shapes.addAll(shapes);
    }
    /**
     * Removes a shape from the list of shapes
     * @param shape shape to be removed
     */
    public void clearShapes() {
        shapes.clear();
    }
    /**
     * Removes a shape from the list of shapes
     * @param shape shape to be removed
     */
    public void transferAllShapesTo(ShapeContainer shapeContainer) {
        shapeContainer.addAllShapes(shapes);
        clearShapes();
    }
    /**
     * Removes a shape from the list of shapes
     * @param shape shape to be removed
     */
    public void transferSingleShapeTo(Shape shape, ShapeContainer shapeContainer) {
        shapeContainer.addShape(shape);
        shapes.remove(shape);
    }
}
