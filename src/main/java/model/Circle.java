package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.ArcType;

public class Circle  extends AbstractShape{
    public Circle(Point pointA, Point pointB) {
        super(pointA, pointB);
        ShapesSingleton.addShape(this);
    }

    @Override
    public double calculateShapeLength() {
        return 0;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.beginPath();
        gc.strokeArc(this.getX(), this.getY(), Math.abs(this.getWidth()), Math.abs(this.getHeight()), 0, 360, ArcType.OPEN);
    }
}
