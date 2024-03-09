package view.events;

import com.sun.nio.sctp.SctpSocketOption;
import controller.Controller;
import model.Point;
import model.Shape;
import view.GUIElements.CustomCanvas;
import view.ModeType;
import view.SettingsSingleton;
import view.ShapeType;

public class DrawUtilities {
    public static void addShapesFirstPoint(Controller controller, double x, double y) {
        Point point = SettingsSingleton.getHoveredPoint();
        if (point == null) point = controller.createAbsolutePoint(x, y, Controller.SingletonType.FINAL);

        controller.getHistoryManager().addFirstPoint(point);

        SettingsSingleton.setLastPoint(point);
    }

    public static Shape addShapesLastPoint(Controller controller, double x, double y, ShapeType shapeType) {
        Point startPoint = SettingsSingleton.getLastPoint();
        Point endPoint = SettingsSingleton.getHoveredPoint();
        if (endPoint == null) endPoint = controller.createAbsolutePoint(x, y, Controller.SingletonType.FINAL);

        Shape shape = controller.createShape(endPoint, startPoint, shapeType, Controller.SingletonType.FINAL);
        SettingsSingleton.setLastPoint(shapeType == ShapeType.MULTILINE ? endPoint : null);

        return shape;
    }

    public static void renderDrawingPreview(Controller controller, double x, double y, CustomCanvas gc) {
        Shape lastPoint = SettingsSingleton.getLastPoint();
        Shape hoveredShape = SettingsSingleton.getHoveredShape();
        if (lastPoint == null) return;
        System.out.println("Rendering drawing preview: " + x + " " + y);
        Point point = controller.createAbsolutePoint(x, y);
        if (hoveredShape != null && hoveredShape.getType() == ShapeType.POINT) point = controller.createAbsolutePoint(hoveredShape.getX(), hoveredShape.getY());
        Shape createdShape = controller.createShape(point, lastPoint.getX(), lastPoint.getY(), SettingsSingleton.getCurrentShape(), null);
        createdShape.draw(gc);
        createdShape.drawLength(gc);
    }
}
