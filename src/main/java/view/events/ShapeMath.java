package view.events;

import controller.Controller;
import model.shapes.Point;
import model.shapes.Shape;
/**
 * Class for handling shape math
 */
public class ShapeMath {
    /**
     * Private constructor to prevent instantiation
     */
    private ShapeMath() {
    }
    /**
     * Returns the snapped angle
     * @param startX start X coordinate
     * @param startY start Y coordinate
     * @param endX end X coordinate
     * @param endY end Y coordinate
     * @return snapped angle
     */
    public static double getSnapAngle(double startX, double startY, double endX, double endY) {
        double originalAngle = Math.atan2(endY - startY, endX - startX);
        double snappingAngle = Math.PI / 12;
        return (originalAngle >= 0 ? Math.round(originalAngle / snappingAngle) : Math.ceil(originalAngle / snappingAngle - 0.5)) * snappingAngle;
    }
    /**
     * Returns the snapped coordinates
     * @param point point
     * @param x X coordinate
     * @param y Y coordinate
     * @return snapped coordinates
     */
    public static double[] getSnapCoordinates(Point point, double x, double y) {
        double snappedAngle = ShapeMath.getSnapAngle(point.getX(), point.getY(), x, y);
        double radius = Math.hypot(x - point.getX(), y - point.getY());
        return new double[]{point.getX() + Math.cos(snappedAngle) * radius, point.getY() + Math.sin(snappedAngle) * radius};
    }
    /**
     * Returns the point on the line
     * @param hoveredShape hovered shape
     * @param x X coordinate
     * @param y Y coordinate
     * @return double array of the point on the line
     */
    public static double[] getPointOnLine(Shape hoveredShape, double x, double y) {
        Point pointA = hoveredShape.getPoints().get(0);
        double hoveredDistance = hoveredShape.calculateDistanceFromMouse(x, y);
        double distanceFromPointA = pointA.calculateDistanceFromMouse(x, y);
        double angle = ShapeMath.calculateAngle(pointA, hoveredShape.getPoints().get(1));
        double radius = Math.hypot(distanceFromPointA, hoveredDistance);

        return new double[]{pointA.getX() + radius * Math.cos(angle), pointA.getY() + radius * Math.sin(angle)};
    }
    /**
     * Calculates the angle between two points
     * @param pointA point A
     * @param pointB point B
     * @return angle between two points
     */
    private static double calculateAngle(Point pointA, Point pointB) {
        return Math.atan2(pointB.getY() - pointA.getY(), pointB.getX() - pointA.getX());
    }
    /**
     * Creates an intersection point
     * @param controller controller
     * @param lineA line A
     * @param lineB line B
     * @return intersection point
     * @return null if no intersection
     */
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
    /**
     * Checks if a point is inside a hitbox
     * @param point point
     * @param line line
     * @return true if point is inside hitbox
     */
    private static boolean isPointInsideHitbox(Point point, Shape line) {
        double x = point.getX();
        double y = point.getY();
        double x1 = line.getPoints().get(0).getX();
        double y1 = line.getPoints().get(0).getY();
        double x2 = line.getPoints().get(1).getX();
        double y2 = line.getPoints().get(1).getY();
        return !(x < Math.min(x1, x2) || x > Math.max(x1, x2) || y < Math.min(y1, y2) || y > Math.max(y1, y2));
    }
    /**
     * Rounds a number to a specific number of decimals
     * @param num number
     * @param decimal number of decimals
     * @return rounded number
     */
    public static double toFixed(double num, int decimal) {
        double power = Math.pow(10, decimal);
        return Math.round(num * power) / power;
    }
}
