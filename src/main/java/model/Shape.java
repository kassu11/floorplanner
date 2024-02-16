package model;

import javafx.scene.canvas.GraphicsContext;

import java.util.List;

public interface Shape {
    void addChild(Shape shape);
    List<Shape> getChildren();
    List<Point> getPoints();
    double getX();
    double getY();

    double getWidth();

    double getHeight();

    void draw(GraphicsContext gc);
}
