package model.shapes;

import view.GUIElements.canvas.CustomCanvas;
import view.types.ShapeType;

import java.util.List;

public class CustomShape extends AbstractShape {

    public CustomShape(List<Shape> shapes, List<Point> points) {
        super(shapes, points);
    }

    @Override
    public void draw(CustomCanvas gc) {

    }

    @Override
    public double calculateShapeLength() {
        return 0;
    }

    @Override
    public double calculateShapeArea() {
        return 0;
    }

    @Override
    public ShapeType getType() {
        return ShapeType.CUSTOMSHAPE;
    }

}
