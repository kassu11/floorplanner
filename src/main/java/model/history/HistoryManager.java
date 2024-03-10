package model.history;

import controller.Controller;
import model.Point;
import model.Shape;
import view.GUIElements.CustomCanvas;
import view.ModeType;
import view.SettingsSingleton;
import view.ShapeType;
import view.events.SelectUtilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryManager {
    private Map<Integer, Shape> shapeReferences = new HashMap<>();
    private List<HistoryEvent> events = new ArrayList<>();
    private int index = 0;
    private Controller controller;

    public HistoryManager(Controller controller) {
        this.controller = controller;
    }

    public void addEvent(HistoryHandler redo, HistoryHandler undo) {
        if (index < events.size()) {
            events = events.subList(0, index);
        }
        HistoryEvent event = new HistoryEvent(redo, undo);
        events.add(event);
        index++;
    }

    public void reset() {
        events.clear();
        index = 0;
    }

    public void undo() {
        if (index > 0) {
            index--;
            events.get(index).undo();
        }
    }

    public void redo() {
        if (index < events.size()) {
            events.get(index).redo();
            index++;
        }
    }

    private boolean assignShape(Shape shape) {
        if (this.shapeReferences.containsKey(shape.getId())) return false;
        shape.assignId();
        this.shapeReferences.put(shape.getId(), shape);
        return true;
    }

    public void addShape(Shape shape) {
        Point[] points = shape.getPoints().toArray(new Point[0]);
        Shape[] shapes = shape.getChildren().toArray(new Shape[0]);
        Boolean[] isNewPoint = new Boolean[points.length];
        Map<Point, List<Shape>> pointToShape = new HashMap<>();
        Point lastPoint = SettingsSingleton.getLastPoint();

        for (int i = 0; i < points.length; i++)
            isNewPoint[i] = this.assignShape(points[i]);
        for (Shape value : shapes)
            this.assignShape(value);

        HistoryHandler redo = () -> {
            controller.getShapeContainer(Controller.SingletonType.FINAL).addShape(shape);
            SettingsSingleton.setLastPoint(lastPoint);
            for (int i = 0; i < points.length; i++) {
                if (isNewPoint[i])
                    controller.getShapeContainer(Controller.SingletonType.FINAL).addShape(points[i]);
                else if (pointToShape.containsKey(points[i])) {
                    for (Shape childShape : pointToShape.get(points[i]))
                        points[i].addChild(childShape);
                    pointToShape.remove(points[i]);
                }
                if (shape.getType() != ShapeType.LINE) points[i].addChild(shape);
                shape.getPoints().add(points[i]);
            }
            for (Shape value : shapes) {
                controller.getShapeContainer(Controller.SingletonType.FINAL).addShape(value);
                shape.getChildren().add(value);
            }
        };

        HistoryHandler undo = () -> {
            controller.removeShape(shape, Controller.SingletonType.FINAL);
            shape.getPoints().clear();
            shape.getChildren().clear();
            for (int i = 0; i < points.length; i++) {
                if (isNewPoint[i])
                    controller.removeShape(points[i], Controller.SingletonType.FINAL);
                else {
                    List<Shape> containsShapes = new ArrayList<>();
                    for (Shape childShape : shapes) {
                        if (!points[i].getChildren().contains(childShape)) continue;
                        points[i].removeChild(childShape);
                        containsShapes.add(childShape);
                    }
                    if (!containsShapes.isEmpty()) pointToShape.put(points[i], containsShapes);
                }
                points[i].removeChild(shape);
            }

            for (Shape value : shapes)
                controller.removeShape(value, Controller.SingletonType.FINAL);

            this.undo(); // Render the line draw mode
            this.redo();
        };

        addEvent(redo, undo);
    }

    public void addFirstPoint(Point point) {
        boolean isNewPoint = this.assignShape(point);
        ShapeType mode = SettingsSingleton.getCurrentShape();

        HistoryHandler redo = () -> {
            if (isNewPoint) controller.getShapeContainer(Controller.SingletonType.FINAL).addShape(point);
            SettingsSingleton.setLastPoint(point);
            SettingsSingleton.setCurrentMode(ModeType.DRAW);
            SettingsSingleton.setCurrentShape(mode);
        };

        HistoryHandler undo = () -> {
            if (isNewPoint) controller.removeShape(point, Controller.SingletonType.FINAL);
            SettingsSingleton.setLastPoint(null);
            SettingsSingleton.setCurrentShape(mode);
        };

        addEvent(redo, undo);
    }

    public void selectShape(Shape[] oldSelections, Shape shape) {
        Shape selectedShape = SettingsSingleton.getSelectedShape();
        List<Shape> newSelections = new ArrayList<>();

        for(Shape oldShape : oldSelections) {
            if (controller.getShapes(Controller.SingletonType.PREVIEW).contains(oldShape)) {
                newSelections.add(oldShape);
            }
        }

        Double[] selectedCoords = new Double[oldSelections.length * 2];
        Double[] originCoords = new Double[oldSelections.length * 2];
        double selectedX = shape.getSelectedX();
        double selectedY = shape.getSelectedY();

        for (int i = 0; i < oldSelections.length; i++) {
            selectedCoords[i * 2] = oldSelections[i].getSelectedX();
            selectedCoords[i * 2 + 1] = oldSelections[i].getSelectedY();
            originCoords[i * 2] = oldSelections[i].getX();
            originCoords[i * 2 + 1] = oldSelections[i].getY();
        }

        HistoryHandler redo = () -> {
            SettingsSingleton.setHoveredShape(shape);
            SettingsSingleton.setCurrentMode(ModeType.SELECT);
            for (int i = 0; i < oldSelections.length; i++) {
                System.out.println("Setting selected coordinates");
                System.out.println(selectedCoords[i * 2] + "  " + selectedCoords[i * 2 + 1]);
                oldSelections[i].setSelectedCoordinates(selectedCoords[i * 2], selectedCoords[i * 2 + 1]);
            }
            SelectUtilities.selectHoveredShape(controller, 0, 0, false);
            shape.setSelectedCoordinates(selectedX, selectedY);
            SelectUtilities.moveSelectedArea(controller, SettingsSingleton.getMouseX(), SettingsSingleton.getMouseY());
        };
        HistoryHandler undo = () -> {
            SettingsSingleton.setHoveredShape(null);
            SettingsSingleton.setCurrentMode(ModeType.SELECT);
            SettingsSingleton.setSelectedShape(selectedShape);
            controller.transferAllShapesTo(Controller.SingletonType.FINAL);
            for (int i = 0; i < oldSelections.length; i++) {
                oldSelections[i].setSelectedCoordinates(selectedCoords[i * 2], selectedCoords[i * 2 + 1]);
                controller.transferSingleShapeTo(oldSelections[i], Controller.SingletonType.PREVIEW);
            }
            for(Shape selection : newSelections) {
                SelectUtilities.selectHoveredShape(controller, 0, 0, false);
            }
            SelectUtilities.moveSelectedArea(controller, SettingsSingleton.getMouseX(), SettingsSingleton.getMouseY());
            System.out.println(controller.getShapes(Controller.SingletonType.PREVIEW).size() + " " + oldSelections.length + " " + oldSelections.length);
        };
        addEvent(redo, undo);
    }

    public void finalizeSelection(Shape shape, double x1, double y1, double x2, double y2, CustomCanvas canvas) {
        HistoryHandler redo = () -> {
            SettingsSingleton.setCurrentMode(ModeType.SELECT);
            SettingsSingleton.setSelectedShape(shape);
            SettingsSingleton.setHoveredShape(null);
            SelectUtilities.finalizeSelectedShapes(controller, canvas, x1, y1, false);
        };
        HistoryHandler undo = () -> {
            SettingsSingleton.setHoveredShape(shape);
            SettingsSingleton.setCurrentMode(ModeType.SELECT);
            SelectUtilities.selectHoveredShape(controller, x1, y1, false);
            SelectUtilities.moveSelectedArea(controller, x2, y2);
        };
        addEvent(redo, undo);
    }

    public void moveArea(List<Shape> shapes, double deltaX, double deltaY) {
        HistoryHandler redo = () -> {
            for (Shape shape : shapes) {
                shape.setCoordinates(shape.getX() + deltaX, shape.getY() + deltaY);
            }
        };
        HistoryHandler undo = () -> {
            for (Shape shape : shapes) {
                shape.setCoordinates(shape.getX() - deltaX, shape.getY() - deltaY);
            }
        };
        addEvent(redo, undo);
    }
}
