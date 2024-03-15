package model.shapeContainers;

import model.shapes.Shape;

import java.util.ArrayList;
import java.util.List;

public abstract class ShapeContainer {
    private List<Shape> shapes;

    public ShapeContainer() {
        this.shapes = new ArrayList<>();
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public void addShape(Shape shape) {
        this.shapes.add(shape);
    }

    public void addAllShapes(List<Shape> shapes) {
        this.shapes.addAll(shapes);
    }

    public void clearShapes() {
        shapes.clear();
    }

    public void transferAllShapesTo(ShapeContainer shapeContainer) {
        shapeContainer.addAllShapes(shapes);
        clearShapes();
    }

    public void transferSingleShapeTo(Shape shape, ShapeContainer shapeContainer) {
        shapeContainer.addShape(shape);
        shapes.remove(shape);
    }
}
