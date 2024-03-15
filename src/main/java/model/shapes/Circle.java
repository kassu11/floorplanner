package model.shapes;

import javafx.scene.shape.ArcType;
import view.GUIElements.canvas.CustomCanvas;
import view.types.ShapeType;

public class Circle extends AbstractShape {

    double length;
    double area;

    public Circle(Point pointA, Point pointB) {
        super(pointA, pointB);
    }

    @Override
    public double calculateShapeLength() {
        double a = this.getWidth() / 2;
        double b = this.getHeight() / 2;
        length = Math.PI * (a + b);
        System.out.println("Length of circle: " + length);
        return length;
    }

    @Override
    public double calculateShapeArea() {
        double a = this.getWidth() / 2;
        double b = this.getHeight() / 2;
        area = Math.PI * a * b;
        System.out.println("Area of circle: " + area);
        return area;
    }

    @Override
    public void draw(CustomCanvas gc) {
        gc.beginPath();
        gc.strokeArc(this.getX(), this.getY(), Math.abs(this.getWidth()), Math.abs(this.getHeight()), 0, 360, ArcType.OPEN);
    }

    public double calculateDistanceFromMouse(double x, double y) {
        double deltaX = this.getX() - x + this.getWidth() / 2;
        double deltaY = this.getY() - y + this.getHeight() / 2;

        return Math.hypot(deltaX, deltaY);
    }

    @Override
    public ShapeType getType() {
        return ShapeType.CIRCLE;
    }
}
