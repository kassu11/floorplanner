package view.events;

import controller.Controller;
import model.shapes.Point;
import model.shapes.Shape;

public class ShapeMath {
    private ShapeMath() {
    }

    public static double getSnapAngle(double startX, double startY, double endX, double endY) {
        double originalAngle = Math.atan2(endY - startY, endX - startX);
        double snappingAngle = Math.PI / 12;
        return (originalAngle >= 0 ? Math.round(originalAngle / snappingAngle) : Math.ceil(originalAngle / snappingAngle - 0.5)) * snappingAngle;
    }

    public static double[] getSnapCoordinates(Point point, double x, double y) {
        double snappedAngle = ShapeMath.getSnapAngle(point.getX(), point.getY(), x, y);
        double radius = Math.hypot(x - point.getX(), y - point.getY());
        return new double[]{point.getX() + Math.cos(snappedAngle) * radius, point.getY() + Math.sin(snappedAngle) * radius};
    }

    public static double[] getPointOnLine(Shape hoveredShape, double x, double y) {
        Point pointA = hoveredShape.getPoints().get(0);
        double hoveredDistance = hoveredShape.calculateDistanceFromMouse(x, y);
        double distanceFromPointA = pointA.calculateDistanceFromMouse(x, y);
        double angle = ShapeMath.calculateAngle(pointA, hoveredShape.getPoints().get(1));
        double radius = Math.hypot(distanceFromPointA, hoveredDistance);

        return new double[]{pointA.getX() + radius * Math.cos(angle), pointA.getY() + radius * Math.sin(angle)};
    }

    private static double calculateAngle(Point pointA, Point pointB) {
        return Math.atan2(pointB.getY() - pointA.getY(), pointB.getX() - pointA.getX());
    }

    public static Point createIntersectionPoint(Controller controller, Shape lineA, Shape lineB) {
        // Extracting coordinates for the first vector
        double x1 = lineA.getPoints().get(0).getX();
        double y1 = lineA.getPoints().get(0).getY();
        double x2 = lineA.getPoints().get(1).getX();
        double y2 = lineA.getPoints().get(1).getY();

        // Extracting coordinates for the second vector
        double x3 = lineB.getPoints().get(0).getX();
        double y3 = lineB.getPoints().get(0).getY();
        double x4 = lineB.getPoints().get(1).getX();
        double y4 = lineB.getPoints().get(1).getY();

        // Calculating slopes of the lines
        double slope1 = (y2 - y1) / (x2 - x1);
        double slope2 = (y4 - y3) / (x4 - x3);

        // Checking if lines are parallel
        if (slope1 == slope2) return null; // No intersection

        double divider = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);

        // Calculating intersection point
        double x = ((x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4)) / divider;
        double y = ((x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4)) / divider;

        Point point =  controller.createAbsolutePoint(x, y);

        if(isPointInsideHitbox(point, lineA) || isPointInsideHitbox(point, lineB)) return point;
        return null;
    }

    private static boolean isPointInsideHitbox(Point point, Shape line) {
        double x = point.getX();
        double y = point.getY();
        double x1 = line.getPoints().get(0).getX();
        double y1 = line.getPoints().get(0).getY();
        double x2 = line.getPoints().get(1).getX();
        double y2 = line.getPoints().get(1).getY();
        return !(x < Math.min(x1, x2) || x > Math.max(x1, x2) || y < Math.min(y1, y2) || y > Math.max(y1, y2));
    }

    public static double toFixed(double num, int decimal) {
        double power = Math.pow(10, decimal);
        return Math.round(num * power) / power;
    }
}
