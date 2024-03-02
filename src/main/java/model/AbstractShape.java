package model;

import view.GUIElements.CustomCanvas;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractShape implements Shape {

    private double x, y;
    private double width, height;
    private List<Point> points = new ArrayList<>();
    private List<Shape> children = new ArrayList<>();
    private Shape parentShape;
    private int priority;

    public AbstractShape(List<Shape> shapes, List<Point> points) {
        this.points.addAll(points);
    }

    public AbstractShape(Point pointA, Point pointB) {
        points.add(pointA);
        points.add(pointB);
        updateDimensions();
        for (Point point : points) {
            point.addChild(this);
        }
    }

    public AbstractShape(Point pointA, Point pointB, Point pointC) {
        points.add(pointA);
        points.add(pointB);
        points.add(pointC);
        updateDimensions();
    }

    public AbstractShape(Point pointA, Point pointB, Point pointC, Point pointD) {
        points.add(pointA);
        points.add(pointB);
        points.add(pointC);
        points.add(pointD);
        updateDimensions();
    }

    private Point calculateDimensions(Point a, Point b) {
        this.x = Math.min(a.getX(), b.getX());
        this.y = Math.min(a.getY(), b.getY());
        this.width = Math.max(a.getX(), b.getX()) - this.x;
        this.height = Math.max(a.getY(), b.getY()) - this.y;
        return b;
    }

    private void updateDimensions() {
        points.stream().reduce(this::calculateDimensions);
    }

    public void addChild(Shape shape) {
        children.add(shape);
    }

    public List<Point> getPoints() {
        return points;
    }

    public void removeChild(Shape shape) {
        children.remove(shape);
    }

    public List<Shape> getChildren() {
        return children;
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

    public Shape getParentShape() {
        return parentShape;
    }

    public abstract void draw(CustomCanvas gc);

    public void setParentShape(Shape parentShape) {
        this.parentShape = parentShape;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public int getPriority() {
        return priority;
    }

    public double calculateDistanceFromMouse(double x, double y) {
        double deltaX = this.getX() - x + this.getWidth() / 2;
        double deltaY = this.getY() - y + this.getHeight() / 2;

        return Math.hypot(deltaX, deltaY);
    }

    public void setCoordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void addToShapeContainer(ShapeContainer shapeContainer) {
        shapeContainer.addShape(this);
    }
}
