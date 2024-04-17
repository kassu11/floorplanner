package model.shapes;

import model.shapeContainers.ShapeContainer;
import view.types.ShapeType;
import view.GUIElements.canvas.CustomCanvas;

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

    void drawLength(CustomCanvas gc, String unit, double modifier);
    public void assignId();
    public void assignId(int id);
    public int getId();
    public double getSelectedX();

    public void setSelectedCoordinates(double x, double y);

    public void updateSelectedCoordinates();

    public double getSelectedY();

    public void setSelected(boolean selected);

    public boolean isSelected();
}
