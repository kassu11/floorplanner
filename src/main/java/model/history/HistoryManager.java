package model.history;

import controller.Controller;
import model.Point;
import model.Shape;
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
    private boolean undoRedo = false;

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

    private void undoAndRedo() {
        if(undoRedo) return;
        undoRedo = true;
        this.undo();
        this.redo();
        undoRedo = false;
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
            this.undoAndRedo();// Render the line draw mode
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

            this.undoAndRedo(); // Render the line draw mode
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

        HistoryHandler redo = () -> copySelectionCoordinates(copyShapes, selectionCoordinates, selectedShape);
        HistoryHandler undo = () -> setSelectedShapesToCoordinates(copyShapes, originalCoordinates);

        addEvent(redo, undo);
    }

    private void copySelectionCoordinates(Shape[] shapes, Double[] selectionCoordinates, Shape selectedShape) {
        SettingsSingleton.setCurrentMode(ModeType.SELECT);
        SettingsSingleton.setLastPoint(null);
        SettingsSingleton.setSelectedShape(selectedShape);
        controller.transferAllShapesTo(Controller.SingletonType.FINAL);
        for(int i = 0; i < shapes.length; i++) {
            shapes[i].setSelectedCoordinates(selectionCoordinates[i * 2], selectionCoordinates[i * 2 + 1]);
            controller.transferSingleShapeTo(shapes[i], Controller.SingletonType.PREVIEW);
        }
        SelectUtilities.moveSelectedArea(controller, SettingsSingleton.getMouseX(), SettingsSingleton.getMouseY());
    }

    private void setSelectedShapesToCoordinates(Shape[] shapes, Double[] originalCoordinates) {
        SettingsSingleton.setHoveredShape(null);
        SettingsSingleton.setSelectedShape(null);
        controller.transferAllShapesTo(Controller.SingletonType.FINAL);

        for(int i = 0; i < shapes.length; i++) {
            shapes[i].setCoordinates(originalCoordinates[i * 2], originalCoordinates[i * 2 + 1]);
        }
    }

    public void addToSelection(List<Shape> shapes) {
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

        HistoryHandler redo = () -> copySelectionCoordinates(copyShapes, selectionCoordinates, selectedShape);
        HistoryHandler undo = () -> {
            setSelectedShapesToCoordinates(copyShapes, originalCoordinates);
            this.undoAndRedo();
        };

        addEvent(redo, undo);
    }

    public void finalizeSelectionMerge(List<Shape> shapes, Point selectedPoint, Point hoveredPoint) {
        Shape[] copyShapes = shapes.toArray(new Shape[0]);
        Shape[] selectedShapes = selectedPoint.getChildren().toArray(new Shape[0]);
        Shape[] hoveredShapes = hoveredPoint.getChildren().toArray(new Shape[0]);
        Point[] oldSelectedLinks = new Point[selectedShapes.length * 2];
        Point[] oldHoveredLinks = new Point[hoveredShapes.length * 2];
        Double[] originalCoordinates = new Double[copyShapes.length * 2];
        for(int i = 0; i < copyShapes.length; i++) {
            originalCoordinates[i * 2] = copyShapes[i].getX();
            originalCoordinates[i * 2 + 1] = copyShapes[i].getY();
        }

        for(int i = 0; i < selectedShapes.length; i++) {
            oldSelectedLinks[i * 2] = selectedShapes[i].getPoints().get(0);
            oldSelectedLinks[i * 2 + 1] = selectedShapes[i].getPoints().get(1);
        }

        for(int i = 0; i < hoveredShapes.length; i++) {
            oldHoveredLinks[i * 2] = hoveredShapes[i].getPoints().get(0);
            oldHoveredLinks[i * 2 + 1] = hoveredShapes[i].getPoints().get(1);
        }

        HistoryHandler redo = () -> {
            controller.transferAllShapesTo(Controller.SingletonType.FINAL);
            SettingsSingleton.setSelectedShape(selectedPoint);
            SettingsSingleton.setHoveredShape(hoveredPoint);
            for (Shape copyShape : copyShapes) controller.transferSingleShapeTo(copyShape, Controller.SingletonType.PREVIEW);
            SelectUtilities.finalizeSelectedShapes(controller, null, 0, 0, false);
            for(int i = 0; i < copyShapes.length; i++) {
                copyShapes[i].setCoordinates(originalCoordinates[i * 2], originalCoordinates[i * 2 + 1]);
            }
        };

        HistoryHandler undo = () -> {
            SettingsSingleton.setSelectedShape(selectedPoint);
            SettingsSingleton.setHoveredShape(null);
            selectedPoint.getChildren().clear();
            hoveredPoint.getChildren().clear();
            controller.transferAllShapesTo(Controller.SingletonType.FINAL);
            if(!controller.getShapes(Controller.SingletonType.FINAL).contains(hoveredPoint)) controller.getShapes(Controller.SingletonType.FINAL).add(hoveredPoint);
            for(int i = 0; i < copyShapes.length; i++) {
                copyShapes[i].setCoordinates(originalCoordinates[i * 2], originalCoordinates[i * 2 + 1]);
                if (!controller.getShapes(Controller.SingletonType.FINAL).contains(copyShapes[i])) {
                    controller.getShapes(Controller.SingletonType.PREVIEW).add(copyShapes[i]);
                } else controller.transferSingleShapeTo(copyShapes[i], Controller.SingletonType.PREVIEW);
            }

            addMergedLinksBack(selectedPoint, selectedShapes, oldSelectedLinks);
            addMergedLinksBack(hoveredPoint, hoveredShapes, oldHoveredLinks);

            this.undoAndRedo();
        };

        addEvent(redo, undo);
    }

    private void addMergedLinksBack(Point point, Shape[] pointChildren, Point[] childrenLinks) {
        for(int i = 0; i < pointChildren.length; i++) {
            pointChildren[i].getPoints().clear();
            pointChildren[i].getPoints().add(childrenLinks[i * 2]);
            pointChildren[i].getPoints().add(childrenLinks[i * 2 + 1]);
            point.addChild(pointChildren[i]);
        }
    }

    public void finalizeSelection(List<Shape> shapes) {
        Shape selectedShape = SettingsSingleton.getSelectedShape();
        Shape[] copyShapes = shapes.toArray(new Shape[0]);
        Double[] originalCoordinates = new Double[copyShapes.length * 2];
        for(int i = 0; i < copyShapes.length; i++) {
            originalCoordinates[i * 2] = copyShapes[i].getX();
            originalCoordinates[i * 2 + 1] = copyShapes[i].getY();
        }

        HistoryHandler redo = () -> {
            SettingsSingleton.setSelectedShape(selectedShape);
            SettingsSingleton.setHoveredShape(null);
            controller.transferAllShapesTo(Controller.SingletonType.FINAL);
            for(int i = 0; i < copyShapes.length; i++) {
                copyShapes[i].setCoordinates(originalCoordinates[i * 2], originalCoordinates[i * 2 + 1]);
            }
        };
        HistoryHandler undo = this::undoAndRedo;

        addEvent(redo, undo);
    }
}
