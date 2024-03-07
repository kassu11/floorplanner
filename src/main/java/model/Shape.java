package model;

import view.ShapeType;
import view.GUIElements.CustomCanvas;

import java.io.Serializable;
import java.util.List;

public interface Shape extends Serializable {
    void addChild(Shape shape);

    List<Shape> getChildren();

    List<Point> getPoints();

    double getX();

    double getY();

    double getCentroidX();

    double getCentroidY();

    double getWidth();

    double getHeight();

    void draw(CustomCanvas gc);

    double calculateShapeLength();

    double calculateShapeArea();

    void calculateCentroid();

    int getPriority();

    double calculateDistanceFromMouse(double x, double y);

    void setCoordinates(double x, double y);

    void addToShapeContainer(ShapeContainer shapeContainer);

    void removeChild(Shape shape);

    void removePoint(Point point);

    void delete(ShapeContainer shapeContainer);

    ShapeType getType();

    void drawLength(CustomCanvas gc);
}
