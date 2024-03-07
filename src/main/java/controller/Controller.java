package controller;

import model.*;
import model.history.HistoryManager;
import view.GUI;
import view.GUIElements.CanvasMath;
import view.GUIElements.CustomCanvas;
import view.SettingsSingleton;
import view.ShapeType;

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

    public static void main(String[] args) {
        GUI.launch(GUI.class);
    }

    public enum SingletonType {
        FINAL, PREVIEW
    }

    public Controller(GUI gui) {
        this.gui = gui;
        this.canvasMath = new CanvasMath(this.gui.getCanvasContainer());
        this.historyManager = new HistoryManager(this);
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
        for (Shape shape : getShapeContainer(type).getShapes()) {
            shape.draw(customCanvas);
            if (settingsSingleton.isDrawLengths()) {
                shape.drawLength(customCanvas);
            }
        }
    }

    public void removeShape(Shape shape, SingletonType type) {
        getShapeContainer(type).getShapes().remove(shape);
    }

    public void deleteShape(Shape shape, SingletonType type) {
        shape.delete(getShapeContainer(type));
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

}
