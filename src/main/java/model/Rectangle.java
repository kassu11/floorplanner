package model;

public class Rectangle extends AbstractShape{

    public Rectangle(Point pointA, Point pointB) {
        super(pointA, pointB);
        Point pointC = new Point(pointA.getX(), pointB.getY());
        Point pointD = new Point(pointB.getX(), pointA.getY());

        addChild(new Line(pointA, pointD));
        addChild(new Line(pointD, pointB));
        addChild(new Line(pointB, pointC));
        addChild(new Line(pointC, pointA));
    }

    public void addChild(Shape shape) {
        super.addChild(shape);
    }

}
