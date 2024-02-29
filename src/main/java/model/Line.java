package model;

import javafx.scene.canvas.GraphicsContext;

public class Line extends AbstractShape{


    public Line(Point pointA, Point pointB) {
        super(pointA, pointB);
        ShapesSingleton.addShape(this);
    }

    @Override
    public double calculateShapeLength() {
        double deltaX = this.getPoints().get(0).getX() - this.getPoints().get(1).getX();
        double deltaY = this.getPoints().get(0).getY() - this.getPoints().get(1).getY();
        double length = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        System.out.println("Length of line: " + length);
        return length;
    }

    @Override
    public double calculateShapeArea() {
        System.out.println("Line has no area");
        return 0;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.beginPath();
        gc.moveTo(this.getPoints().get(0).getX(), this.getPoints().get(0).getY());
        gc.lineTo(this.getPoints().get(1).getX(), this.getPoints().get(1).getY());
        gc.stroke();
    }

    public double calculateDistanceFromMouse(double x, double y) {
        double x1 = this.getPoints().get(1).getX();
        double x2 = this.getPoints().get(0).getX();
        double y1 = this.getPoints().get(1).getY();
        double y2 = this.getPoints().get(0).getY();

        if (x1 == x2) {
            if (betweenLinesWithoutSlope(x, y, x1, x2, y1, y2)) return getDistanceWithoutSlope(x, x1);
        } else {
            double slope = (y2 - y1) / (x2 - x1);
            if (betweenLines(x, y, x1, x2, y1, y2, slope)) return getDistance(x, y, x1, y1, slope);
        }

        return 1000; // High default value
    }

    private static double getDistance(double mouseX, double mouseY, double x1, double y1, double slope) {
        double b = y1 - slope * x1;
        return Math.abs(slope * mouseX - mouseY + b) / Math.sqrt(Math.pow(slope, 2) + 1);
    }

    private static double getDistanceWithoutSlope(double mouseX, double x1) {
        return Math.abs(mouseX - x1);
    }

    private boolean betweenLines(double mouseX, double mouseY, double x1, double x2, double y1, double y2, double slope) {
        double lineLength = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        double perpendicularSlope;

        if (slope == 0) {
            return !(getDistanceWithoutSlope(mouseX, x1) > lineLength || getDistanceWithoutSlope(mouseX, x2) > lineLength);
        }

        perpendicularSlope = -1 / slope;
        return !(getDistance(mouseX, mouseY, x1, y1, perpendicularSlope) > lineLength || getDistance(mouseX, mouseY, x2, y2, perpendicularSlope) > lineLength);
    }

    private boolean betweenLinesWithoutSlope(double mouseX, double mouseY, double x1, double x2, double y1, double y2) {
        double lineLength = Math.abs(y2 - y1);
        return !(getDistance(mouseX, mouseY, x1, y1, 0) > lineLength || getDistance(mouseX, mouseY, x2, y2, 0) > lineLength);
    }


}
