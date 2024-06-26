package model.history;

import controller.Controller;
import model.shapes.Point;
import model.shapes.Shape;
import view.types.ModeType;
import view.types.ShapeType;
import view.events.SelectUtilities;

import java.util.ArrayList;
import java.util.List;
/**
 * Class for managing history events
 */
public class HistoryManager {
    /**
     * List of history events
     */
    private List<HistoryEvent> events = new ArrayList<>();
    /**
     * Index of the history events
     */
    private int index = 0;
    /**
     * Controller for the history manager
     */
    private Controller controller;
    /**
     * Boolean for undo and redo
     * Prevents the recursive undo and redo calls from undoAndRedo method
     */
    private boolean undoRedo = false;
    /**
     * Constructor for the history manager
     * @param controller controller for the history manager
     */

    public HistoryManager(Controller controller) {
        this.controller = controller;
    }
    /**
     * Adds an event to the history manager
     * @param redo redo handler
     * @param undo undo handler
     */
    public void addEvent(HistoryHandler redo, HistoryHandler undo) {
        if (index < events.size()) {
            events = events.subList(0, index);
        }
        HistoryEvent event = new HistoryEvent(redo, undo);
        events.add(event);
        index++;
    }
    /**
     * Resets the history manager
     */
    public void reset() {
        events.clear();
        index = 0;
    }
    /**
     * Handles the undo event
     */
    public void undo() {
        if (index > 0) {
            index--;
            events.get(index).undo();
        }
    }
    /**
     * Handles the redo event
     */
    public void redo() {
        if (index < events.size()) {
            events.get(index).redo();
            index++;
        }
    }
    /**
     * Handles the undo and redo event
     */
    private void undoAndRedo() {
        if(undoRedo) return;
        undoRedo = true;
        this.undo();
        this.redo();
        undoRedo = false;
    }
    /**
     * Tests if shape has been added to history
     * If not adds it to history
     * @param shape shape to test
     */
    private boolean isNewToHistory(Shape shape) {
        if (shape.getAddedToHistory()) return false;
        shape.addToHistory();
        return true;
    }
    /**
     * Adds the first point to the history manager
     * @param point point to add
     */
    public void addFirstPoint(Point point) {
        boolean isNewPoint = this.isNewToHistory(point);
        ShapeType mode = controller.getCurrentShapeType();

        HistoryHandler redo = () -> {
            if (isNewPoint) controller.getShapeContainer(Controller.SingletonType.FINAL).addShape(point);
            controller.setLastPoint(point);
            controller.setCurrentMode(ModeType.DRAW);
            controller.setCurrentShape(mode);
        };

        HistoryHandler undo = () -> {
            if (isNewPoint) controller.removeShape(point, Controller.SingletonType.FINAL);
            controller.setLastPoint(null);
            controller.setCurrentShape(mode);
        };

        addEvent(redo, undo);
    }
    /**
     * Adds a shape to the history manager
     * @param shape shape to add
     */
    public void addShape(Shape shape) {
        if(shape.getType() == ShapeType.LINE) this.addLine(shape);
        else this.addComplexShape(shape);
    }
    /**
     * Adds a line to the history manager
     * @param shape shape to add
     */
    private void addLine(Shape shape) {
        Point[] points = shape.getPoints().toArray(new Point[0]);
        Point lastPoint = controller.getLastPoint();
        Point newPoint = null;
        for (Point point : points) if(this.isNewToHistory(point)) newPoint = point;
        Point finalNewPoint = newPoint;

        HistoryHandler redo = () -> {
            for(Point point : points) {
                shape.getPoints().add(point);
                point.addChild(shape);
            }
            if(finalNewPoint != null) controller.getShapeContainer(Controller.SingletonType.FINAL).addShape(finalNewPoint);
            controller.getShapeContainer(Controller.SingletonType.FINAL).addShape(shape);
            controller.setLastPoint(lastPoint);
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
    /**
     * Adds a complex shape to the history manager
     * @param shape shape to add
     */
    private void addComplexShape(Shape shape) {
        Point[] points = shape.getPoints().toArray(new Point[0]);
        Shape[] shapes = shape.getChildren().toArray(new Shape[0]);
        Point[] childShapePoints = new Point[shapes.length * 2];
        Boolean[] isNewPoint = new Boolean[points.length];
        Point lastPoint = controller.getLastPoint();

        for (int i = 0; i < points.length; i++) isNewPoint[i] = this.isNewToHistory(points[i]);
        for (int i = 0; i < shapes.length; i++) {
            this.isNewToHistory(shapes[i]);
            childShapePoints[i * 2] = shapes[i].getPoints().get(0);
            childShapePoints[i * 2 + 1] = shapes[i].getPoints().get(1);
        }

        HistoryHandler redo = () -> {
            controller.setLastPoint(lastPoint);
            for(int i = 0; i < points.length; i++) {
                points[i].setParentShape(shape);
                if (Boolean.TRUE.equals(isNewPoint[i])) controller.getShapeContainer(Controller.SingletonType.FINAL).addShape(points[i]);
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
                if(Boolean.TRUE.equals(isNewPoint[i])) controller.removeShape(points[i], Controller.SingletonType.FINAL);
                points[i].removeChild(shape);
                points[i].setParentShape(null);
            }

            this.undoAndRedo(); // Render the line draw mode
        };

        addEvent(redo, undo);
    }
    /**
     * Undo the first shape selection
     * @param shapes shapes that are selected
     */
    public void startSelection(List<Shape> shapes) {
        ModeType currentMode = controller.getCurrentMode();
        Shape selectedShape = controller.getSelectedShape();
        Shape[] copyShapes = shapes.toArray(new Shape[0]);
        Double[] originalCoordinates = new Double[copyShapes.length * 2];
        Double[] selectionCoordinates = new Double[copyShapes.length * 2];
        for(int i = 0; i < copyShapes.length; i++) {
            originalCoordinates[i * 2] = copyShapes[i].getX();
            originalCoordinates[i * 2 + 1] = copyShapes[i].getY();
            selectionCoordinates[i * 2] = copyShapes[i].getSelectedX();
            selectionCoordinates[i * 2 + 1] = copyShapes[i].getSelectedY();
        }

        HistoryHandler redo = () -> copySelectionCoordinates(copyShapes, selectionCoordinates, selectedShape, currentMode);
        HistoryHandler undo = () -> setSelectedShapesToCoordinates(copyShapes, originalCoordinates, currentMode);

        addEvent(redo, undo);
    }
    /**
     * Copies coordinates of the selected shapes
     * @param shapes shapes to copy
     */
    private void copySelectionCoordinates(Shape[] shapes, Double[] selectionCoordinates, Shape selectedShape, ModeType currentMode) {
        controller.setCurrentMode(currentMode);
        controller.setLastPoint(null);
        controller.setSelectedShape(selectedShape);
        controller.transferAllShapesTo(Controller.SingletonType.FINAL);
        for (int i = 0; i < shapes.length; i++) {
            shapes[i].setSelectedCoordinates(selectionCoordinates[i * 2], selectionCoordinates[i * 2 + 1]);
            controller.transferSingleShapeTo(shapes[i], Controller.SingletonType.PREVIEW);
        }
        if (currentMode == ModeType.SELECT) SelectUtilities.moveSelectedArea(controller, controller.getMouseX(), controller.getMouseY());
    }
    /**
     * Sets selected shapes to coordinates
     * @param shapes shapes to set
     */
    private void setSelectedShapesToCoordinates(Shape[] shapes, Double[] originalCoordinates, ModeType currentMode) {
        controller.setHoveredShape(null);
        controller.setSelectedShape(null);
        controller.setCurrentMode(currentMode);
        controller.transferAllShapesTo(Controller.SingletonType.FINAL);

        for(int i = 0; i < shapes.length; i++) {
            shapes[i].setCoordinates(originalCoordinates[i * 2], originalCoordinates[i * 2 + 1]);
        }
    }
    /**
     * Adds to the selection
     * @param shapes shapes to add
     */
    public void addToSelection(List<Shape> shapes) {
        ModeType currentMode = controller.getCurrentMode();
        Shape selectedShape = controller.getSelectedShape();
        Shape[] copyShapes = shapes.toArray(new Shape[0]);
        Double[] originalCoordinates = new Double[copyShapes.length * 2];
        Double[] selectionCoordinates = new Double[copyShapes.length * 2];
        for(int i = 0; i < copyShapes.length; i++) {
            originalCoordinates[i * 2] = copyShapes[i].getX();
            originalCoordinates[i * 2 + 1] = copyShapes[i].getY();
            selectionCoordinates[i * 2] = copyShapes[i].getSelectedX();
            selectionCoordinates[i * 2 + 1] = copyShapes[i].getSelectedY();
        }

        HistoryHandler redo = () -> copySelectionCoordinates(copyShapes, selectionCoordinates, selectedShape, currentMode);
        HistoryHandler undo = () -> {
            setSelectedShapesToCoordinates(copyShapes, originalCoordinates, currentMode);
            this.undoAndRedo();
        };

        addEvent(redo, undo);
    }
    /**
     * Finalizes the selection merge
     * @param shapes shapes to finalize
     */
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
            controller.setSelectedShape(selectedPoint);
            controller.setHoveredShape(hoveredPoint);
            for (Shape copyShape : copyShapes) controller.transferSingleShapeTo(copyShape, Controller.SingletonType.PREVIEW);
            SelectUtilities.finalizeSelectedShapes(controller, null, 0, 0, false);
            for(int i = 0; i < copyShapes.length; i++) {
                copyShapes[i].setCoordinates(originalCoordinates[i * 2], originalCoordinates[i * 2 + 1]);
            }
        };

        HistoryHandler undo = () -> {
            controller.setSelectedShape(selectedPoint);
            controller.setHoveredShape(null);
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
    /**
     * Adds merged links back
     * @param point point to add
     * @param pointChildren children of the point
     * @param childrenLinks links of the children
     */
    private void addMergedLinksBack(Point point, Shape[] pointChildren, Point[] childrenLinks) {
        for(int i = 0; i < pointChildren.length; i++) {
            pointChildren[i].getPoints().clear();
            pointChildren[i].getPoints().add(childrenLinks[i * 2]);
            pointChildren[i].getPoints().add(childrenLinks[i * 2 + 1]);
            point.addChild(pointChildren[i]);
        }
    }
    /**
     * Finalizes the selection
     * @param shapes shapes to finalize
     */
    public void finalizeSelection(List<Shape> shapes) {
        Shape selectedShape = controller.getSelectedShape();
        Shape[] copyShapes = shapes.toArray(new Shape[0]);
        Double[] originalCoordinates = new Double[copyShapes.length * 2];
        for(int i = 0; i < copyShapes.length; i++) {
            originalCoordinates[i * 2] = copyShapes[i].getX();
            originalCoordinates[i * 2 + 1] = copyShapes[i].getY();
        }

        HistoryHandler redo = () -> {
            controller.setSelectedShape(selectedShape);
            controller.setHoveredShape(null);
            controller.transferAllShapesTo(Controller.SingletonType.FINAL);
            for(int i = 0; i < copyShapes.length; i++) {
                copyShapes[i].setCoordinates(originalCoordinates[i * 2], originalCoordinates[i * 2 + 1]);
            }
        };
        HistoryHandler undo = this::undoAndRedo;

        addEvent(redo, undo);
    }
    /**
     * Deletes a shape
     * @param shape shape to delete
     */
    public void deleteShape(Shape shape) {
        Point[] points = shape.getPoints().toArray(new Point[0]);
        Shape[] children = shape.getChildren().toArray(new Shape[0]);
        Point[] childrenOppositePoints = new Point[children.length * 2];
        for(int i = 0; i < children.length; i++) {
            Point oppositePoint = children[i].getPoints().getFirst();
            if(oppositePoint == shape) oppositePoint = children[i].getPoints().getLast();
            childrenOppositePoints[i] = oppositePoint;
        }

        HistoryHandler redo = () -> {
            controller.transferAllShapesTo(Controller.SingletonType.FINAL);
            controller.deleteShape(shape, Controller.SingletonType.FINAL);
        };

        HistoryHandler undo = () -> {
            for(int i = 0; i < children.length; i++) {
                if(childrenOppositePoints[i].getChildren().isEmpty()) controller.getShapeContainer(Controller.SingletonType.FINAL).addShape(childrenOppositePoints[i]);
                controller.getShapeContainer(Controller.SingletonType.FINAL).addShape(children[i]);
                children[i].getPoints().add((Point) shape);
                children[i].getPoints().add(childrenOppositePoints[i]);
                shape.addChild(children[i]);
                childrenOppositePoints[i].addChild(children[i]);
            }

            for(Point point : points) {
                if(point.getChildren().isEmpty()) controller.getShapeContainer(Controller.SingletonType.FINAL).addShape(point);
                shape.getPoints().add(point);
                point.addChild(shape);
            }

            controller.getShapeContainer(Controller.SingletonType.FINAL).addShape(shape);
        };

        addEvent(redo, undo);
    }
}
