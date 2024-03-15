package view.events;

import controller.Controller;
import model.shapes.Point;
import model.shapes.Shape;
import view.GUIElements.canvas.CustomCanvas;
import view.types.ShapeType;

public class DrawUtilities {
    public static void addShapesFirstPoint(Controller controller, double x, double y) {
        Point point = controller.getHoveredPoint();
        if (point == null) point = controller.createAbsolutePoint(x, y, Controller.SingletonType.FINAL);

        controller.getHistoryManager().addFirstPoint(point);

        controller.setLastPoint(point);
    }

    public static Shape addShapesLastPoint(Controller controller, double x, double y, ShapeType shapeType) {
        Point startPoint = controller.getLastPoint();
        Point endPoint = controller.getHoveredPoint();
        double fixedY = y;
        double fixedX = x;
        if(controller.isCtrlDown()) {
            double snappedAngle = ShapeMath.getSnapAngle(startPoint.getX(), startPoint.getY(), x, y);
            double radius = ShapeMath.getRadius(startPoint.getX(), startPoint.getY(), x, y);
            fixedX = ShapeMath.getSnapAngleX(startPoint.getX(), radius, snappedAngle);
            fixedY = ShapeMath.getSnapAngleY(startPoint.getY(), radius, snappedAngle);
        }
        if (endPoint == null) endPoint = controller.createAbsolutePoint(fixedX, fixedY, Controller.SingletonType.FINAL);

        Shape shape = controller.createShape(endPoint, startPoint, shapeType, Controller.SingletonType.FINAL);
        controller.setLastPoint(shapeType == ShapeType.MULTILINE ? endPoint : null);

        controller.getHistoryManager().addShape(shape);

        return shape;
    }

    public static void renderDrawingPreview(Controller controller, double x, double y, CustomCanvas gc) {
        Shape lastPoint = controller.getLastPoint();
        Shape hoveredShape = controller.getHoveredShape();
        double fixedY = y;
        double fixedX = x;
        if (hoveredShape != null){
            if (hoveredShape.getType() == ShapeType.LINE) {
                Point pointA = hoveredShape.getPoints().get(0);
                double hoveredDistance = hoveredShape.calculateDistanceFromMouse(x, y);
                double distanceFromPointA = pointA.calculateDistanceFromMouse(x, y);
                double angle = ShapeMath.calculateAngle(pointA, hoveredShape.getPoints().get(1));
                double radius = Math.hypot(distanceFromPointA, hoveredDistance);

                fixedY = pointA.getY() + radius * Math.sin(angle);
                fixedX = pointA.getX() + radius * Math.cos(angle);
                controller.createAbsolutePoint(fixedX, fixedY).draw(gc);
            }
        }

        if (lastPoint == null) return;

        if(controller.isCtrlDown()) {
            double snappedAngle = ShapeMath.getSnapAngle(lastPoint.getX(), lastPoint.getY(), x, y);
            double radius = ShapeMath.getRadius(lastPoint.getX(), lastPoint.getY(), x, y);
            fixedX = ShapeMath.getSnapAngleX(lastPoint.getX(), radius, snappedAngle);
            fixedY = ShapeMath.getSnapAngleY(lastPoint.getY(), radius, snappedAngle);
        }
        Point point = controller.createAbsolutePoint(fixedX, fixedY);
        if (hoveredShape != null && hoveredShape.getType() == ShapeType.POINT) point = controller.createAbsolutePoint(hoveredShape.getX(), hoveredShape.getY());
        Shape createdShape = controller.createShape(point, lastPoint.getX(), lastPoint.getY(), controller.getCurrentShape(), null);
        createdShape.draw(gc);
        createdShape.drawLength(gc);
    }
}
