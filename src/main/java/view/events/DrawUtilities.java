package view.events;

import controller.Controller;
import model.Point;
import model.Shape;
import view.SettingsSingleton;
import view.ShapeType;

public class DrawUtilities {
    public static void addShapesFirstPoint(Controller controller, double x, double y) {
        Point point = SettingsSingleton.getHoveredPoint();
        if (point == null) point = controller.createAbsolutePoint(x, y, Controller.SingletonType.FINAL);

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
}
