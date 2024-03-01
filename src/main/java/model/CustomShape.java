package model;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;


public class CustomShape implements Shape{


    private List<Shape> children;
    public CustomShape(List<Shape> shapes) {
        this.children = new ArrayList<>();
        children.addAll(shapes);
        ShapesSingleton.addShape(this);
        System.out.println("Added custom shape to shapes singleton");
    }

    @Override
    public double calculateShapeLength() {
        return 0;
    }

    @Override
    public double calculateShapeArea() {
        return 0;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public double calculateDistanceFromMouse(double x, double y) {
        return 0;
    }

    @Override
    public void addChild(Shape shape) {

    }

    @Override
    public List<Shape> getChildren() {
        return null;
    }

    @Override
    public List<Point> getPoints() {
        return null;
    }

    @Override
    public double getX() {
        return 0;
    }

    @Override
    public double getY() {
        return 0;
    }

    @Override
    public double getWidth() {
        return 0;
    }

    @Override
    public double getHeight() {
        return 0;
    }

    @Override
    public void draw(GraphicsContext gc) {
    }
}
