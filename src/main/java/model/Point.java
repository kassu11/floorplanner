package model;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class Point implements Shape {
    private double x, y;
    private double width = 25, height = 25;
    private List<Shape> children = new ArrayList<>();
    private int priority = 1;

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

    public void draw(GraphicsContext gc) {
        gc.fillOval(this.getX() - this.getHeight() / 2, this.getY() - this.getWidth() / 2, this.getWidth(), this.getHeight());
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
        return priority;
    }

    public double calculateDistanceFromMouse(double x, double y) {
        double deltaX = this.getX() - x;
        double deltaY = this.getY() - y;

        return Math.hypot(deltaX, deltaY);
    }

    @Override
    public void setCoordinates(double x, double y) {
        this.setX(x);
        this.setY(y);
    }
}
