package view;

import model.Point;
import model.Shape;

public class SettingsSingleton {
    private static ShapeType currentShape = ShapeType.LINE;
    private static ModeType currentMode = ModeType.DRAW;
    private static Point lastPoint, hoveredPoint;
    private static Shape selectedShape, hoveredShape;
    private static double middleX, middleY, selectedX, selectedY, mouseX, mouseY;
    private static boolean drawLengths = true;
    private static boolean isDrawGrid = true;
    private static double gridHeight = 750;
    private static double gridWidth = 750;
    private static int gridSize = 25;

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

    public static boolean isGridEnabled() {
        return isDrawGrid;
    }

    public static void setDrawGrid(boolean isDrawGrid) {
        SettingsSingleton.isDrawGrid = isDrawGrid;
    }

    public static double getGridHeight() {
        return gridHeight;
    }

    public static void setGridHeight(double gridHeight) {
        SettingsSingleton.gridHeight = gridHeight;
    }

    public static double getGridWidth() {
        return gridWidth;
    }

    public static void setGridWidth(double gridWidth) {
        SettingsSingleton.gridWidth = gridWidth;
    }

    public static int getGridSize() {
        return gridSize;
    }

    public static void setGridSize(int gridSize) {
        SettingsSingleton.gridSize = gridSize;
    }

    public static double getMouseX() {
        return mouseX;
    }

    public static double getMouseY() {
        return mouseY;
    }

    public static void setMousePosition(double mouseX, double mouseY) {
        SettingsSingleton.mouseX = mouseX;
        SettingsSingleton.mouseY = mouseY;
    }
}
