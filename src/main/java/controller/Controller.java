package controller;

import dao.SettingsDao;
import entity.Settings;
import javafx.scene.paint.Color;
import model.history.HistoryManager;
import model.shapeContainers.FinalShapesSingleton;
import model.shapeContainers.PreviewShapesSingleton;
import model.shapeContainers.ShapeContainer;
import model.shapes.*;
import view.GUI;
import view.GUIElements.canvas.CanvasMath;
import view.GUIElements.canvas.CustomCanvas;
import view.SettingsSingleton;
import view.types.ModeType;
import view.types.ShapeType;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    private GUI gui;
    private HistoryManager historyManager;
    private CanvasMath canvasMath;
    private ShapeContainer finalShapes = FinalShapesSingleton.getInstance();
    private ShapeContainer previewShapes = PreviewShapesSingleton.getInstance();
    private SettingsSingleton settingsSingleton = SettingsSingleton.getInstance();
    private List<Shape> customShapes = new ArrayList<>();
    private SettingsDao settingsDao = new SettingsDao();
    private boolean ctrlIsDown = false;
    private ShapeType currentShape = ShapeType.LINE;
    private ModeType currentMode = ModeType.DRAW;
    private Point lastPoint, hoveredPoint;
    private Shape selectedShape, hoveredShape;
    private double mouseX, mouseY;
    private String hoverColor, selectedColor;

    public enum SingletonType {
        FINAL, PREVIEW
    }

    public Controller(GUI gui) {
        this.gui = gui;
        this.canvasMath = new CanvasMath(this.gui.getCanvasContainer());
        this.historyManager = new HistoryManager(this);
        setCurrentMode(ModeType.DRAW);
        loadSettings();
    }

    public Shape createShape(double x, double y, double x1, double y1, ShapeType shapeType, SingletonType singletonType) {
        Point pointA = new Point(x, y);
        if (singletonType != null) getShapeContainer(singletonType).addShape(pointA);

        return this.createShape(pointA, x1, y1, shapeType, singletonType);
    }

    public Shape createShape(Point pointA, double x1, double y1, ShapeType shapeType, SingletonType singletonType) {
        Point pointB = new Point(x1, y1);
        if (singletonType != null) getShapeContainer(singletonType).addShape(pointB);

        return this.createShape(pointA, pointB, shapeType, singletonType);
    }

    public Shape createShape(Point pointA, Point pointB, ShapeType shapeType, SingletonType singletonType) {
        // TODO: Refactor this to use a factory

        Shape shape = switch (shapeType) {
            case LINE, MULTILINE -> new Line(pointA, pointB);
            case RECTANGLE -> {
                Point pointC = createAbsolutePoint(pointB.getX(), pointA.getY(), singletonType);
                Point pointD = createAbsolutePoint(pointA.getX(), pointB.getY(), singletonType);
                Rectangle rectangle = new Rectangle(pointA, pointB, pointC, pointD);
                if (singletonType != null) getShapeContainer(singletonType).addAllShapes(rectangle.getChildren());

                singletonType = null;
                yield rectangle;
            }
            case CIRCLE -> new Circle(pointA, pointB);
            default -> null;
        };
        if (singletonType != null) shape.addToShapeContainer(getShapeContainer(singletonType));
        return shape;
    }

    public void transferAllShapesTo(SingletonType type) {
        switch (type) {
            case PREVIEW -> finalShapes.transferAllShapesTo(previewShapes);
            case FINAL -> previewShapes.transferAllShapesTo(finalShapes);
        }
    }

    public void transferSingleShapeTo(Shape shape, SingletonType type) {
        switch (type) {
            case PREVIEW -> finalShapes.transferSingleShapeTo(shape, previewShapes);
            case FINAL -> previewShapes.transferSingleShapeTo(shape, finalShapes);
        }
    }

    public void drawAllShapes(CustomCanvas customCanvas, SingletonType type) {
        customCanvas.clear();
        if(settingsSingleton.isGridEnabled() && type == SingletonType.FINAL) customCanvas.getGrid().drawGrid();
        if(settingsSingleton.isUnitsVisible()){
            customCanvas.drawRulerX();
            customCanvas.drawRulerY();
        }
        customCanvas.setLineWidth(4);
        customCanvas.setFill(Color.BLACK);
        for (Shape shape : getShapeContainer(type).getShapes()) {
            shape.draw(customCanvas);

            if (settingsSingleton.isUnitsVisible()) {
                shape.drawLength(customCanvas, settingsSingleton.getMeasurementUnit(), settingsSingleton.getMeasurementModifier());
            }
        }
    }

    public void removeShape(Shape shape, SingletonType type) {
        getShapeContainer(type).getShapes().remove(shape);
    }

    public void deleteShape(Shape shape, SingletonType type) {
        shape.delete(getShapeContainer(type));
        this.setHoveredShape(null);
        this.setSelectedShape(null);
        this.setLastPoint(null);
    }

    public List<Shape> getShapes(SingletonType type) {
        return getShapeContainer(type).getShapes();
    }

    public Point createRelativePoint(double x, double y) {
        return createRelativePoint(x, y, null);
    }

    public Point createRelativePoint(double x, double y, SingletonType singletonType) {
        Point point = new Point(canvasMath.relativeXtoAbsoluteX(x), canvasMath.relativeYtoAbsoluteY(y));
        if (singletonType != null) getShapeContainer(singletonType).addShape(point);
        return point;
    }

    public Point createAbsolutePoint(double x, double y) {
        return createAbsolutePoint(x, y, null);
    }

    public Point createAbsolutePoint(double x, double y, SingletonType singletonType) {
        Point point = new Point(x, y);
        if (singletonType != null) getShapeContainer(singletonType).addShape(point);
        return point;
    }

    public void saveSettings() {
        if(settingsDao.find(1) == null) {
            Settings settings = new Settings(settingsSingleton.isDrawLengths(), settingsSingleton.isGridEnabled(),
                    settingsSingleton.getGridHeight(), settingsSingleton.getGridWidth(), settingsSingleton.getGridSize(),
                    settingsSingleton.getLocale().getLanguage(), settingsSingleton.getMeasurementUnit());

            settingsDao.persist(settings);
            System.out.println("Settings saved!");
        } else {
            Settings settings = settingsSingleton.getInstance().getSettings();
            settingsDao.find(1).setSettings(settings);
            settingsDao.update(settingsDao.find(1));
        }
    }

    public void loadSettings() {
        Settings settings = settingsDao.find(1);
        if(settings != null) {
            settingsSingleton.setSettings(settings);
        }
        else {
            settingsDao.persist(settingsSingleton.getSettings());
        }
    }

    public CanvasMath getCanvasMath() {
        return canvasMath;
    }

    public void addCustomShape(Shape newShape) {
        customShapes.add(newShape);
        System.out.println("Added line to custom shapes!");
        System.out.println("X: " + newShape.getPoints().get(0).getX() + " Y: " + newShape.getPoints().get(0).getY());
        System.out.println("X: " + newShape.getPoints().get(1).getX() + " Y: " + newShape.getPoints().get(1).getY());
    }

    public void checkIfConnected(Shape newShape) {
        if (customShapes.isEmpty()) {
            System.out.println("Custom shapes is empty");
            return;
        }
        List<Point> points = new ArrayList<>();
        for (Shape shape : customShapes) {
            for (Point point : shape.getPoints()) {
                if (!points.contains(point)) points.add(point);
            }
        }
        Point customPointB = newShape.getPoints().get(1);
        for (Shape shape : customShapes) {
            Point pointA = shape.getPoints().get(0);
            if (pointA.equals(customPointB)) {
                new CustomShape(customShapes, points);
                customShapes.clear();
                return;
            }
        }
    }

    public void removeAllShapes() {
        finalShapes.clearShapes();
        previewShapes.clearShapes();
        gui.getCanvasContainer().clear();
        historyManager.reset();
    }

    public ShapeContainer getShapeContainer(SingletonType type) {
        if (type == null) return null;
        return switch (type) {
            case FINAL -> finalShapes;
            case PREVIEW -> previewShapes;
        };
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }





    public ShapeType getCurrentShapeType() {
        return currentShape;
    }

    public void setCurrentShape(ShapeType shape) {
        currentShape = shape;
    }

    public ModeType getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(ModeType currentMode) {
        if(currentMode == ModeType.DRAW) {
            this.selectedColor = "#00d415";
            this.hoverColor = "#00d415";
        } else if(currentMode == ModeType.SELECT || currentMode == ModeType.ROTATE) {
            this.selectedColor = "#036ffc";
            this.hoverColor = "#78b0fa";
        } else if(currentMode == ModeType.DELETE) {
            this.selectedColor = "#000000";
            this.hoverColor = "#ff0000";
        } else if(currentMode == ModeType.AREA) {
            this.selectedColor = "#4269f54a";
            this.hoverColor = "#78b0fa";
        } else {
            this.selectedColor = "#000000";
            this.hoverColor = "#000000";
        }
        this.currentMode = currentMode;
    }

    public void updateToolbarLocalization() {
        gui.updateToolbarLocalization();
    }
    public void setLastPoint(Point lastPoint) {
        this.lastPoint = lastPoint;
    }

    public Point getLastPoint() {
        return lastPoint;
    }

    public Point getHoveredPoint() {
        return hoveredPoint;
    }

    public void setHoveredPoint(Point hoveredPoint) {
        this.hoveredPoint = hoveredPoint;
    }

    public Shape getSelectedShape() {
        return selectedShape;
    }

    public void setSelectedShape(Shape selectedShape) {
        this.selectedShape = selectedShape;
    }

    public Shape getHoveredShape() {
        return hoveredShape;
    }

    public void setHoveredShape(Shape hoveredShape) {
        this.hoveredShape = hoveredShape;
    }

    public double getMouseX() {
        return mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }

    public void setMousePosition(double mouseX, double mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    public boolean isCtrlDown() {
        return ctrlIsDown;
    }

    public void setCtrlDown(boolean shiftIsDown) {
        this.ctrlIsDown = shiftIsDown;
    }


    public String getHoverColor() {
        return hoverColor;
    }

    public String getSelectedColor() {
        return selectedColor;
    }

    public Dimension setDimensionLine(Shape shape, double distance) {
        Dimension dimension = new Dimension();
        dimension.setDistance(distance);
        dimension.addShape(shape);
        dimension.resize();
        if(shape.getType() == ShapeType.LINE) {
            ((Line) shape).addDimension(dimension);
        }
        return dimension;
    }
}
