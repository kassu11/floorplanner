package view.events;

import controller.Controller;
import model.shapes.Point;
import model.shapes.Shape;
import view.GUIElements.canvas.CustomCanvas;
import view.types.ShapeType;

public class DrawUtilities {
    public static void addShapesFirstPoint(Controller controller, double x, double y) {
        Point point = controller.getHoveredPoint();
        Shape hoveredShape = controller.getHoveredShape();
        double fixedY = y;
        double fixedX = x;
        if (hoveredShape != null && hoveredShape.getType() == ShapeType.LINE) {
            Point pointA = hoveredShape.getPoints().get(0);
            double hoveredDistance = hoveredShape.calculateDistanceFromMouse(x, y);
            double distanceFromPointA = pointA.calculateDistanceFromMouse(x, y);
            double angle = ShapeMath.calculateAngle(pointA, hoveredShape.getPoints().get(1));
            double radius = Math.hypot(distanceFromPointA, hoveredDistance);

            fixedY = pointA.getY() + radius * Math.sin(angle);
            fixedX = pointA.getX() + radius * Math.cos(angle);
        }
        if (point == null) point = controller.createAbsolutePoint(fixedX, fixedY, Controller.SingletonType.FINAL);

        controller.getHistoryManager().addFirstPoint(point);

        controller.setLastPoint(point);
    }

    public static Shape addShapesLastPoint(Controller controller, double x, double y, ShapeType shapeType) {
        Point startPoint = controller.getLastPoint();
        Point endPoint = controller.getHoveredPoint();
        Shape hoveredShape = controller.getHoveredShape();
        double fixedY = y;
        double fixedX = x;

        if(controller.isCtrlDown()) {
            double snappedAngle = ShapeMath.getSnapAngle(startPoint.getX(), startPoint.getY(), x, y);
            double radius = ShapeMath.getRadius(startPoint.getX(), startPoint.getY(), x, y);
            fixedX = ShapeMath.getSnapAngleX(startPoint.getX(), radius, snappedAngle);
            fixedY = ShapeMath.getSnapAngleY(startPoint.getY(), radius, snappedAngle);
        }

        if (hoveredShape != null && hoveredShape.getType() == ShapeType.LINE) {
            if(!controller.isCtrlDown()) {
                Point pointA = hoveredShape.getPoints().get(0);
                double hoveredDistance = hoveredShape.calculateDistanceFromMouse(fixedX, fixedY);
                double distanceFromPointA = pointA.calculateDistanceFromMouse(fixedX, fixedY);
                double angle = ShapeMath.calculateAngle(pointA, hoveredShape.getPoints().get(1));
                double radius = Math.hypot(distanceFromPointA, hoveredDistance);

                fixedY = pointA.getY() + radius * Math.sin(angle);
                fixedX = pointA.getX() + radius * Math.cos(angle);
            } else {
                Shape line = controller.createShape(fixedX, fixedY, startPoint.getX(), startPoint.getY(), ShapeType.LINE, null);
                Point intersection = ShapeMath.createIntersectionPoint(controller, line, hoveredShape);
                if (intersection != null) {
                    fixedX = intersection.getX();
                    fixedY = intersection.getY();
                }
            }
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

        if(lastPoint != null && controller.isCtrlDown()) {
            double snappedAngle = ShapeMath.getSnapAngle(lastPoint.getX(), lastPoint.getY(), x, y);
            double radius = ShapeMath.getRadius(lastPoint.getX(), lastPoint.getY(), x, y);
            fixedX = ShapeMath.getSnapAngleX(lastPoint.getX(), radius, snappedAngle);
            fixedY = ShapeMath.getSnapAngleY(lastPoint.getY(), radius, snappedAngle);
        }

        if (hoveredShape != null && hoveredShape.getType() == ShapeType.LINE) {
            if (lastPoint == null || !controller.isCtrlDown()) {
                Point pointA = hoveredShape.getPoints().get(0);
                double hoveredDistance = hoveredShape.calculateDistanceFromMouse(fixedX, fixedY);
                double distanceFromPointA = pointA.calculateDistanceFromMouse(fixedX, fixedY);
                double angle = ShapeMath.calculateAngle(pointA, hoveredShape.getPoints().get(1));
                double radius = Math.hypot(distanceFromPointA, hoveredDistance);

                fixedY = pointA.getY() + radius * Math.sin(angle);
                fixedX = pointA.getX() + radius * Math.cos(angle);
            } else {
                Shape line = controller.createShape(fixedX, fixedY, lastPoint.getX(), lastPoint.getY(), ShapeType.LINE, null);
                Point intersection = ShapeMath.createIntersectionPoint(controller, line, hoveredShape);
                if (intersection != null) {
                    fixedX = intersection.getX();
                    fixedY = intersection.getY();
                }
            }
            controller.createAbsolutePoint(fixedX, fixedY).draw(gc);
        }


        if (lastPoint == null) return;


        Point point = controller.createAbsolutePoint(fixedX, fixedY);
        if (hoveredShape != null && hoveredShape.getType() == ShapeType.POINT) point = controller.createAbsolutePoint(hoveredShape.getX(), hoveredShape.getY());
        Shape createdShape = controller.createShape(point, lastPoint.getX(), lastPoint.getY(), controller.getCurrentShape(), null);
        createdShape.draw(gc);
        createdShape.drawLength(gc);
    }
}
