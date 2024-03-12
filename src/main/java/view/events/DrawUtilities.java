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
        double fixedY = y;
        double fixedX = x;
        if(controller.isCtrlDown()) {
            double originalAngle = Math.atan2(y - startPoint.getY(), x - startPoint.getX());
            double snappingAngle = Math.PI / 12;
            double snappedAngle = (originalAngle >= 0 ? Math.round(originalAngle / snappingAngle) : Math.ceil(originalAngle / snappingAngle - 0.5)) * snappingAngle;
            double radius = Math.hypot(x - startPoint.getX(), y - startPoint.getY());
            fixedX = startPoint.getX() + Math.cos(snappedAngle) * radius;
            fixedY = startPoint.getY() + Math.sin(snappedAngle) * radius;
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
        if (lastPoint == null) return;
        double fixedY = y;
        double fixedX = x;
        if(controller.isCtrlDown()) {
            double originalAngle = Math.atan2(y - lastPoint.getY(), x - lastPoint.getX());
            double snappingAngle = Math.PI / 12;
            double snappedAngle = (originalAngle >= 0 ? Math.round(originalAngle / snappingAngle) : Math.ceil(originalAngle / snappingAngle - 0.5)) * snappingAngle;
            double radius = Math.hypot(x - lastPoint.getX(), y - lastPoint.getY());
            fixedX = lastPoint.getX() + Math.cos(snappedAngle) * radius;
            fixedY = lastPoint.getY() + Math.sin(snappedAngle) * radius;
        }
        Point point = controller.createAbsolutePoint(fixedX, fixedY);
        if (hoveredShape != null && hoveredShape.getType() == ShapeType.POINT) point = controller.createAbsolutePoint(hoveredShape.getX(), hoveredShape.getY());
        Shape createdShape = controller.createShape(point, lastPoint.getX(), lastPoint.getY(), controller.getCurrentShape(), null);
        createdShape.draw(gc);
        createdShape.drawLength(gc);
    }
}
