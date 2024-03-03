package model;

import view.ShapeType;
import view.GUIElements.CustomCanvas;

import java.util.List;

public interface Shape {
    void addChild(Shape shape);

    List<Shape> getChildren();

    List<Point> getPoints();

    double getX();

    double getY();

    double getWidth();

    double getHeight();

    void draw(CustomCanvas gc);

    double calculateShapeLength();

    double calculateShapeArea();

    int getPriority();

    double calculateDistanceFromMouse(double x, double y);

    void setCoordinates(double x, double y);

    void addToShapeContainer(ShapeContainer shapeContainer);

    void removeChild(Shape shape);

    void removePoint(Point point);

    void delete(ShapeContainer shapeContainer);

    ShapeType getType();
}
