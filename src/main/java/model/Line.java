package model;

import javafx.scene.text.Text;
import javafx.scene.transform.Affine;
import view.GUIElements.CustomCanvas;
import view.ShapeType;

public class Line extends AbstractShape {

    public Line(Point pointA, Point pointB) {
        super(pointA, pointB);
    }

    @Override
    public double calculateShapeLength() {
        double deltaX = this.getPoints().get(0).getX() - this.getPoints().get(1).getX();
        double deltaY = this.getPoints().get(0).getY() - this.getPoints().get(1).getY();
        double length = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        return length;
    }

    @Override
    public double calculateShapeArea() {
        System.out.println("Line has no area");
        return 0;
    }

    @Override
    public void draw(CustomCanvas gc) {
        Point pointA = this.getPoints().get(0);
        Point pointB = this.getPoints().get(1);
        gc.beginPath();
        gc.moveTo(pointA.getX(), pointA.getY());
        gc.lineTo(pointB.getX(), pointB.getY());
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

    @Override
    public ShapeType getType() {
        return ShapeType.LINE;
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

    public void delete(ShapeContainer shapeContainer) {
        if (shapeContainer != null) shapeContainer.getShapes().remove(this);

        for (int i = 0; i < getPoints().size(); i++) {
            Shape point = getPoints().get(i);
            getPoints().remove(i);
            point.removeChild(this);
            if (point.getChildren().isEmpty() && shapeContainer != null) shapeContainer.getShapes().remove(point);
            i--;
        }
    }

    public void drawLength(CustomCanvas gc) {
        Point pointA = this.getPoints().get(0);
        Point pointB = this.getPoints().get(1);

        String text = String.format("%.2f cm", calculateShapeLength());
        final Text textElement = new Text(text);

        double textOffset = textElement.getLayoutBounds().getWidth() / 2;
        Affine original = gc.getTransform();
        double deltaX = pointA.getX() - pointB.getX();
        double deltaY = pointA.getY() - pointB.getY();
        double radians = Math.atan2(deltaY, deltaX);
        this.calculateCentroid();

        gc.setTransform(radians, this.getCentroidX(), this.getCentroidY());
        gc.fillText(text, radians, this.getCentroidX(), this.getCentroidY(), textOffset);
        gc.setTransform(original);
    }
}
