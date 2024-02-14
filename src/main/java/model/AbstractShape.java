package model;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractShape implements Shape {

    private double x, y, x1, y1;
    private List <Shape> children = new ArrayList<>();
    public AbstractShape(double x, double y, double x1, double y1) {
        this.x = x;
        this.y = y;
        this.x1 = x1;
        this.y1 = y1;
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

    public void addChild(Shape shape) {
        children.add(shape);
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
}