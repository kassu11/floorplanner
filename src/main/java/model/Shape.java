package model;

import java.util.List;

public interface Shape {
    void addChild(Shape shape);
    List<Shape> getChildren();
    List<Point> getPoints();
    double getX();
    double getY();

    double getWidth();

    double getHeight();


}
