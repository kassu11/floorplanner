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
        Point mousePoint = controller.createAbsolutePoint(x, y);

        if (hoveredShape != null && hoveredShape.getType() == ShapeType.LINE) {
            mousePoint.setCoordinates(ShapeMath.getPointOnLine(hoveredShape, x, y));
        }
        if (point == null) point = controller.createAbsolutePoint(mousePoint.getX(), mousePoint.getY(), Controller.SingletonType.FINAL);

        controller.getHistoryManager().addFirstPoint(point);
        controller.setLastPoint(point);
    }

    public static Shape addShapesLastPoint(Controller controller, double x, double y, ShapeType shapeType) {
        Point startPoint = controller.getLastPoint();
        Point endPoint = controller.getHoveredPoint();
        Shape hoveredShape = controller.getHoveredShape();
        Point mousePoint = controller.createAbsolutePoint(x, y);

        if(controller.isCtrlDown()) {
            mousePoint.setCoordinates(ShapeMath.getSnapCoordinates(startPoint, x, y));
        }

        if (hoveredShape != null && hoveredShape.getType() == ShapeType.LINE) {
            if(!controller.isCtrlDown()) {
                mousePoint.setCoordinates(ShapeMath.getPointOnLine(hoveredShape, x, y));
            } else {
                Shape line = controller.createShape(mousePoint.getX(), mousePoint.getY(), startPoint.getX(), startPoint.getY(), ShapeType.LINE, null);
                Point intersection = ShapeMath.createIntersectionPoint(controller, line, hoveredShape);
                if (intersection != null) mousePoint = intersection;
            }
        }
        if (endPoint == null) endPoint = controller.createAbsolutePoint(mousePoint.getX(), mousePoint.getY(), Controller.SingletonType.FINAL);

        Shape shape = controller.createShape(endPoint, startPoint, shapeType, Controller.SingletonType.FINAL);
        if(shapeType != ShapeType.MULTILINE) controller.setLastPoint(null);

        controller.getHistoryManager().addShape(shape);

        return shape;
    }

    public static void renderDrawingPreview(Controller controller, double x, double y, CustomCanvas gc) {
        Point lastPoint = controller.getLastPoint();
        Shape hoveredShape = controller.getHoveredShape();
        Point mousePoint = controller.createAbsolutePoint(x, y);

        if(lastPoint != null && controller.isCtrlDown()) {
            mousePoint.setCoordinates(ShapeMath.getSnapCoordinates(lastPoint, x, y));
        }

        if (hoveredShape != null && hoveredShape.getType() == ShapeType.LINE) {
            if (lastPoint == null || !controller.isCtrlDown()) {
                mousePoint.setCoordinates(ShapeMath.getPointOnLine(hoveredShape, mousePoint.getX(), mousePoint.getY()));
            } else {
                Shape line = controller.createShape(mousePoint.getX(), mousePoint.getY(), lastPoint.getX(), lastPoint.getY(), ShapeType.LINE, null);
                Point intersection = ShapeMath.createIntersectionPoint(controller, line, hoveredShape);
                if (intersection != null) mousePoint = intersection;
            }
            mousePoint.draw(gc);
        }


        if (lastPoint == null) return;

        if (hoveredShape != null && hoveredShape.getType() == ShapeType.POINT) mousePoint.setCoordinates(hoveredShape.getX(), hoveredShape.getY());
        Shape createdShape = controller.createShape(mousePoint, lastPoint.getX(), lastPoint.getY(), controller.getCurrentShape(), null);
        createdShape.draw(gc);
        createdShape.drawLength(gc);
    }
}
