package model;

import javafx.scene.canvas.GraphicsContext;

public class Line extends AbstractShape{


    public Line(Point pointA, Point pointB) {
        super(pointA, pointB);
        ShapesSingleton.addShape(this);
    }

    @Override
    public double calculateShapeLength() {
        double deltax = this.getPoints().get(0).getX() - this.getPoints().get(1).getX();
        double deltay = this.getPoints().get(0).getY() - this.getPoints().get(1).getY();
        double length = Math.sqrt(deltax * deltax + deltay * deltay);
        System.out.println("Length of line: " + length);
        return length;
    }

    @Override
    public double calculateShapeArea() {
        System.out.println("Line has no area");
        return 0;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.beginPath();
        gc.moveTo(this.getPoints().get(0).getX(), this.getPoints().get(0).getY());
        gc.lineTo(this.getPoints().get(1).getX(), this.getPoints().get(1).getY());
        gc.stroke();
    }
}
