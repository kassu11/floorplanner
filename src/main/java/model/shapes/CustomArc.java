package model.shapes;

import view.GUIElements.canvas.CustomCanvas;
import view.types.ShapeType;

public class CustomArc extends AbstractShape{
    private int angleLength, startAngle;
    private double height, centerX, centerY, radiusX, radiusY;
    public CustomArc(Point pointA, Point pointB, double height, int startAngle, int angleLength) {
        super(pointA, pointB);
        this.height = height;
        this.startAngle = startAngle;
        this.angleLength = angleLength;
        this.centerX = pointA.getX();
        this.centerY = pointA.getY();
        this.radiusX = height;
        this.radiusY = height;
    }
    @Override
    public void draw(CustomCanvas gc) {
        gc.beginPath();
        gc.arc(centerX, centerY, height, height, startAngle, angleLength);
        gc.stroke();
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
    public ShapeType getType() {
        return null;
    }

    public void setStartAngle(int startAngle) {
        this.startAngle = startAngle;
    }

    public void setAngleLength(int angleLength) {
        this.angleLength = angleLength;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getCenterX() {
        return centerX;
    }

    public void setCenterX(double centerX) {
        this.centerX = centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public void setCenterY(double centerY) {
        this.centerY = centerY;
    }

    public double getRadiusX() {
        return radiusX;
    }

    public void setRadiusX(double radiusX) {
        this.radiusX = radiusX;
    }

    public double getRadiusY() {
        return radiusY;
    }

    public void setRadiusY(double radiusY) {
        this.radiusY = radiusY;
    }
}
