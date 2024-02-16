package model;

import javafx.scene.canvas.GraphicsContext;

public class Rectangle extends AbstractShape{

    public Rectangle(Point pointA, Point pointB, Point pointC, Point pointD) {
        super(pointA, pointB, pointC, pointD);
        addChild(new Line(pointA, pointD));
        addChild(new Line(pointD, pointB));
        addChild(new Line(pointB, pointC));
        addChild(new Line(pointC, pointA));
    }

    public void addChild(Shape shape) {
        super.addChild(shape);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.beginPath();
        gc.moveTo(this.getPoints().get(0).getX(), this.getPoints().get(0).getY());
        gc.lineTo(this.getPoints().get(1).getX(), this.getPoints().get(1).getY());
        gc.lineTo(this.getPoints().get(2).getX(), this.getPoints().get(2).getY());
        gc.lineTo(this.getPoints().get(3).getX(), this.getPoints().get(3).getY());
        gc.lineTo(this.getPoints().get(0).getX(), this.getPoints().get(0).getY());
        gc.stroke();
    }

}
