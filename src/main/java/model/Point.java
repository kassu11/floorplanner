package model;

import java.util.ArrayList;
import java.util.List;

public class Point implements Shape {
    private double x, y;
    private List<Shape> children = new ArrayList<>();

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
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
    public void addChild(Shape shape) {
        this.children.add(shape);
    }
}
