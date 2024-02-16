package model;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractShape implements Shape {

    private double x, y, x1, y1;
    private Point pointA, pointB;
    private List<Shape> children = new ArrayList<>();
    private Shape parentShape;

    public AbstractShape(Point pointA, Point pointB) {
        this.pointA = pointA;
        this.pointB = pointB;
    }

    public void addChild(Shape shape) {
        children.add(shape);
    }

    @Override
    public void draw() {

    }

    @Override
    public void resize() {

    }

    @Override
    public void move() {

    }

    @Override
    public void delete() {

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

    public double getX1() {
        return x1;
    }

    public void setX1(double x1) {
        this.x1 = x1;
    }

    public double getY1() {
        return y1;
    }

    public void setY1(double y1) {
        this.y1 = y1;
    }

    public Shape getParentShape() {
        return parentShape;
    }

    public void setParentShape(Shape parentShape) {
        this.parentShape = parentShape;
    }
}
