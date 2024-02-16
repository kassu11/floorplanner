package model;

import java.util.ArrayList;
import java.util.List;

public class Point implements Shape {
    private double x, y;
    private double width = 5, height = 5;
    private List<Shape> children = new ArrayList<>();

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
        ShapesSingleton.addShape(this);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public List<Shape> getChildren() {
        return children;
    }

    @Override
    public List<Point> getPoints() {
        System.out.println("point doesnt have a point dummy!");
        return null;
    }

    @Override
    public void addChild(Shape shape) {
        this.children.add(shape);
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }
}
