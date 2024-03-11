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

    public void addShape(Shape shape) {
        if(shape.getType() == ShapeType.LINE) this.addLine(shape);
        else this.addComplexShape(shape);
    }

    private void addLine(Shape shape) {
        Point[] points = shape.getPoints().toArray(new Point[0]);
        Point lastPoint = SettingsSingleton.getLastPoint();
        Point newPoint = null;
        for (Point point : points) if(this.assignShape(point)) newPoint = point;
        Point finalNewPoint = newPoint;

        HistoryHandler redo = () -> {
            for(Point point : points) {
                shape.getPoints().add(point);
                point.addChild(shape);
            }
            if(finalNewPoint != null) controller.getShapeContainer(Controller.SingletonType.FINAL).addShape(finalNewPoint);
            controller.getShapeContainer(Controller.SingletonType.FINAL).addShape(shape);
            SettingsSingleton.setLastPoint(lastPoint);
        };


        HistoryHandler undo = () -> {
            controller.removeShape(shape, Controller.SingletonType.FINAL);
            shape.getPoints().clear();
            for(Point point : points) point.removeChild(shape);

            if(finalNewPoint != null) controller.removeShape(finalNewPoint, Controller.SingletonType.FINAL);

            this.undo(); // Render the line draw mode
            this.redo();
        };

        addEvent(redo, undo);

    }

    private void addComplexShape(Shape shape) {
        Point[] points = shape.getPoints().toArray(new Point[0]);
        Shape[] shapes = shape.getChildren().toArray(new Shape[0]);
        Point[] childShapePoints = new Point[shapes.length * 2];
        Boolean[] isNewPoint = new Boolean[points.length];
        Point lastPoint = SettingsSingleton.getLastPoint();

        for (int i = 0; i < points.length; i++) isNewPoint[i] = this.assignShape(points[i]);
        for (int i = 0; i < shapes.length; i++) {
            this.assignShape(shapes[i]);
            childShapePoints[i * 2] = shapes[i].getPoints().get(0);
            childShapePoints[i * 2 + 1] = shapes[i].getPoints().get(1);
        }

        HistoryHandler redo = () -> {
            SettingsSingleton.setLastPoint(lastPoint);
            for(int i = 0; i < points.length; i++) {
                points[i].setParentShape(shape);
                if (isNewPoint[i]) controller.getShapeContainer(Controller.SingletonType.FINAL).addShape(points[i]);
                shape.getPoints().add(points[i]);
            }

            for(int i = 0; i < shapes.length; i++) {
                shapes[i].getPoints().add(childShapePoints[i * 2]);
                childShapePoints[i * 2].addChild(shapes[i]);
                shapes[i].getPoints().add(childShapePoints[i * 2 + 1]);
                childShapePoints[i * 2 + 1].addChild(shapes[i]);
                controller.getShapeContainer(Controller.SingletonType.FINAL).addShape(shapes[i]);
                shape.addChild(shapes[i]);
            }
        };

        HistoryHandler undo = () -> {
            shape.getPoints().clear();
            shape.getChildren().clear();
            for(Shape child : shapes) {
                for(Point point : child.getPoints()) point.removeChild(child);
                child.getPoints().clear();
                controller.removeShape(child, Controller.SingletonType.FINAL);
            }

            for(int i = 0; i < points.length; i++) {
                if(isNewPoint[i]) controller.removeShape(points[i], Controller.SingletonType.FINAL);
                points[i].removeChild(shape);
                points[i].setParentShape(null);
            }

            this.undo(); // Render the line draw mode
            this.redo();
        };

        addEvent(redo, undo);
    }

    public void startSelection(List<Shape> shapes) {
        Shape selectedShape = SettingsSingleton.getSelectedShape();
        Shape[] copyShapes = shapes.toArray(new Shape[0]);
        Double[] originalCoordinates = new Double[copyShapes.length * 2];
        Double[] selectionCoordinates = new Double[copyShapes.length * 2];
        for(int i = 0; i < copyShapes.length; i++) {
            originalCoordinates[i * 2] = copyShapes[i].getX();
            originalCoordinates[i * 2 + 1] = copyShapes[i].getY();
            selectionCoordinates[i * 2] = copyShapes[i].getSelectedX();
            selectionCoordinates[i * 2 + 1] = copyShapes[i].getSelectedY();
        }

        HistoryHandler redo = () -> {
            SettingsSingleton.setCurrentMode(ModeType.SELECT);
            SettingsSingleton.setLastPoint(null);
            SettingsSingleton.setSelectedShape(selectedShape);
            for(int i = 0; i < copyShapes.length; i++) {
                copyShapes[i].setSelectedCoordinates(selectionCoordinates[i * 2], selectionCoordinates[i * 2 + 1]);
                controller.transferSingleShapeTo(copyShapes[i], Controller.SingletonType.PREVIEW);
            }
            SelectUtilities.moveSelectedArea(controller, SettingsSingleton.getMouseX(), SettingsSingleton.getMouseY());
        };

        HistoryHandler undo = () -> {
            SettingsSingleton.setHoveredShape(null);
            SettingsSingleton.setSelectedShape(null);
            controller.transferAllShapesTo(Controller.SingletonType.FINAL);

            for(int i = 0; i < copyShapes.length; i++) {
                copyShapes[i].setCoordinates(originalCoordinates[i * 2], originalCoordinates[i * 2 + 1]);
            }
        };

        addEvent(redo, undo);
    }

    public void selectShape(Shape[] oldSelections, Shape shape) {
//        Shape selectedShape = SettingsSingleton.getSelectedShape();
//        List<Shape> newSelections = new ArrayList<>();
//
//        for(Shape oldShape : oldSelections) {
//            if (controller.getShapes(Controller.SingletonType.PREVIEW).contains(oldShape)) {
//                newSelections.add(oldShape);
//            }
//        }
//
//        Double[] selectedCoords = new Double[oldSelections.length * 2];
//        Double[] originCoords = new Double[oldSelections.length * 2];
//        double selectedX = shape.getSelectedX();
//        double selectedY = shape.getSelectedY();
//
//        for (int i = 0; i < oldSelections.length; i++) {
//            selectedCoords[i * 2] = oldSelections[i].getSelectedX();
//            selectedCoords[i * 2 + 1] = oldSelections[i].getSelectedY();
//            originCoords[i * 2] = oldSelections[i].getX();
//            originCoords[i * 2 + 1] = oldSelections[i].getY();
//        }
//
//        HistoryHandler redo = () -> {
//            SettingsSingleton.setHoveredShape(shape);
//            SettingsSingleton.setCurrentMode(ModeType.SELECT);
//            for (int i = 0; i < oldSelections.length; i++) {
//                System.out.println("Setting selected coordinates");
//                System.out.println(selectedCoords[i * 2] + "  " + selectedCoords[i * 2 + 1]);
//                oldSelections[i].setSelectedCoordinates(selectedCoords[i * 2], selectedCoords[i * 2 + 1]);
//            }
//            SelectUtilities.selectHoveredShape(controller, 0, 0, false);
//            shape.setSelectedCoordinates(selectedX, selectedY);
//            SelectUtilities.moveSelectedArea(controller, SettingsSingleton.getMouseX(), SettingsSingleton.getMouseY());
//        };
//        HistoryHandler undo = () -> {
//            SettingsSingleton.setHoveredShape(null);
//            SettingsSingleton.setCurrentMode(ModeType.SELECT);
//            SettingsSingleton.setSelectedShape(selectedShape);
//            controller.transferAllShapesTo(Controller.SingletonType.FINAL);
//            for (int i = 0; i < oldSelections.length; i++) {
//                oldSelections[i].setSelectedCoordinates(selectedCoords[i * 2], selectedCoords[i * 2 + 1]);
//                controller.transferSingleShapeTo(oldSelections[i], Controller.SingletonType.PREVIEW);
//            }
//            for(Shape selection : newSelections) {
//                SelectUtilities.selectHoveredShape(controller, 0, 0, false);
//            }
//            SelectUtilities.moveSelectedArea(controller, SettingsSingleton.getMouseX(), SettingsSingleton.getMouseY());
//            System.out.println(controller.getShapes(Controller.SingletonType.PREVIEW).size() + " " + oldSelections.length + " " + oldSelections.length);
//        };
//        addEvent(redo, undo);
    }

    public void finalizeSelection(Shape shape, double x1, double y1, double x2, double y2, CustomCanvas canvas) {
//        HistoryHandler redo = () -> {
//            SettingsSingleton.setCurrentMode(ModeType.SELECT);
//            SettingsSingleton.setSelectedShape(shape);
//            SettingsSingleton.setHoveredShape(null);
//            SelectUtilities.finalizeSelectedShapes(controller, canvas, x1, y1, false);
//        };
//        HistoryHandler undo = () -> {
//            SettingsSingleton.setHoveredShape(shape);
//            SettingsSingleton.setCurrentMode(ModeType.SELECT);
//            SelectUtilities.selectHoveredShape(controller, x1, y1, false);
//            SelectUtilities.moveSelectedArea(controller, x2, y2);
//        };
//        addEvent(redo, undo);
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
