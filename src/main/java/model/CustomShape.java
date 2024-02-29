package model;

import javafx.scene.canvas.GraphicsContext;

import java.util.List;


public class CustomShape extends AbstractShape{

    public CustomShape(Point pointA, Point pointB) {
        super(pointA, pointB);
        addChild(new Line(pointA, pointB));
    }

    @Override
    public double calculateShapeLength() {
        return 0;
    }

    @Override
    public double calculateShapeArea() {
        return 0;
    }

    public void addChild(Shape shape) {
        super.addChild(shape);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.beginPath();
        gc.moveTo(this.getPoints().get(0).getX(), this.getPoints().get(0).getY());
        gc.lineTo(this.getPoints().get(1).getX(), this.getPoints().get(1).getY());
        gc.stroke();
        System.out.println("Drawing custom shape");
        System.out.println("Number of points: " + getPoints().size());
        System.out.println("Number of children: " + getChildren().size());
    }
}
