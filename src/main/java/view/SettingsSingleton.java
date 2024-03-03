package view;

import model.Point;

public class SettingsSingleton {
    private static ShapeType currentShape = ShapeType.LINE;
    private static ModeType currentMode = ModeType.DRAW;
    private static Point lastPoint;
    private static Point hoveredPoint;

    private SettingsSingleton() {
    }

    private static class SettingsSingletonHelper {
        private static final SettingsSingleton INSTANCE = new SettingsSingleton();
    }

    public static SettingsSingleton getInstance() {
        return SettingsSingletonHelper.INSTANCE;
    }

    public static ShapeType getCurrentShape() {
        return currentShape;
    }

    public static void setCurrentShape(ShapeType shape) {
        currentShape = shape;
        System.out.println("Current shape: " + currentShape);
    }

    public static ModeType getCurrentMode() {
        return currentMode;
    }

    public static void setCurrentMode(ModeType currentMode) {
        SettingsSingleton.currentMode = currentMode;
    }

    public static boolean isShapeType(ShapeType shape) {
        return currentShape == shape;
    }

    public static void setLastPoint(Point lastPoint) {
        SettingsSingleton.lastPoint = lastPoint;
    }

    public static Point getLastPoint() {
        return lastPoint;
    }

    public static Point getHoveredPoint() {
        return hoveredPoint;
    }

    public static void setHoveredPoint(Point hoveredPoint) {
        SettingsSingleton.hoveredPoint = hoveredPoint;
    }
}
