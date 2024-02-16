package model;

public class Line extends AbstractShape{


    public Line(Point pointA, Point pointB) {
        super(pointA, pointB);
        ShapesSingleton.addShape(this);
    }


}
