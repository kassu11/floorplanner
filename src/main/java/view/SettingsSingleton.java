package view;

import model.Point;
import model.Shape;

public class SettingsSingleton {
    private static ShapeType currentShape = ShapeType.LINE;
    private static ModeType currentMode = ModeType.DRAW;
    private static Point lastPoint, hoveredPoint;
    private static Shape selectedShape, hoveredShape;
    private static double middleX, middleY, selectedX, selectedY;
    private static boolean drawLengths = true;

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

    public static Shape getSelectedShape() {
        return selectedShape;
    }

    public static void setSelectedShape(Shape selectedShape) {
        SettingsSingleton.selectedShape = selectedShape;
    }

    public static Shape getHoveredShape() {
        return hoveredShape;
    }

    public static void setHoveredShape(Shape hoveredShape) {
        SettingsSingleton.hoveredShape = hoveredShape;
    }

    public static double getMiddleX() {
        return middleX;
    }

    public static void setMiddleX(double middleX) {
        SettingsSingleton.middleX = middleX;
    }

    public static double getMiddleY() {
        return SettingsSingleton.middleY;
    }

    public static void setMiddleY(double middleY) {
        SettingsSingleton.middleY = middleY;
    }

    public static double getSelectedX() {
        return SettingsSingleton.selectedX;
    }

    public static void setSelectedX(double selectedX) {
        SettingsSingleton.selectedX = selectedX;
    }

    public static double getSelectedY() {
        return SettingsSingleton.selectedY;
    }

    public static void setSelectedY(double selectedY) {
        SettingsSingleton.selectedY = selectedY;
    }

    public static boolean isDrawLengths() {
        return drawLengths;
    }

    public static void setDrawLengths(boolean drawLengths) {
        SettingsSingleton.drawLengths = drawLengths;
    }
}
