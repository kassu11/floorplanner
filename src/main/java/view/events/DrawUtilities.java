package view.events;

import controller.Controller;
import model.shapes.Point;
import model.shapes.Shape;
import view.GUIElements.canvas.CustomCanvas;
import view.SettingsSingleton;
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
        if (endPoint == null) endPoint = controller.createAbsolutePoint(x, y, Controller.SingletonType.FINAL);

        Shape shape = controller.createShape(endPoint, startPoint, shapeType, Controller.SingletonType.FINAL);
        controller.setLastPoint(shapeType == ShapeType.MULTILINE ? endPoint : null);

        controller.getHistoryManager().addShape(shape);

        return shape;
    }

    public static void renderDrawingPreview(Controller controller, double x, double y, CustomCanvas gc) {
        Shape lastPoint = controller.getLastPoint();
        Shape hoveredShape = controller.getHoveredShape();
        if (lastPoint == null) return;
        Point point = controller.createAbsolutePoint(x, y);
        if (hoveredShape != null && hoveredShape.getType() == ShapeType.POINT) point = controller.createAbsolutePoint(hoveredShape.getX(), hoveredShape.getY());
        Shape createdShape = controller.createShape(point, lastPoint.getX(), lastPoint.getY(), controller.getCurrentShape(), null);
        createdShape.draw(gc);
        createdShape.drawLength(gc);
    }
}
