package model;

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

}
