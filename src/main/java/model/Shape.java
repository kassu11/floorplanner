package model;

import java.util.List;

public interface Shape {
    void addChild(Shape shape);
    List<Shape> getChildren();
    double getX();
    double getY();
}
