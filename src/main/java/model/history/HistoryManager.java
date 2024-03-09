package model.history;

import controller.Controller;
import model.Point;
import model.Shape;
import view.GUIElements.CustomCanvas;
import view.ModeType;
import view.SettingsSingleton;
import view.events.DrawUtilities;
import view.events.SelectUtilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HistoryManager {
    private HashMap<Integer, Shape> shapeReferences = new HashMap<>();
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

    private void assignShape(Shape shape) {
        shape.assignId();
        this.shapeReferences.put(shape.getId(), shape);
    }

    public void createShape(Shape shape) {
        List<HistoryEvent> createEvents = new ArrayList<>();
        assignShape(shape);
        shape.getPoints().forEach(point -> {
            if (!shapeReferences.containsKey(point.getId())) {
                assignShape(point);
            }
        });

        HistoryHandler redo = () -> {
            controller.getShapeContainer(Controller.SingletonType.FINAL).addShape(shape);
        };
    }

    public void addFirstPoint(Point point) {
        boolean isNewPoint = !shapeReferences.containsKey(point.getId());
        this.assignShape(point);
        HistoryHandler redo = () -> {
            if(isNewPoint) controller.getShapeContainer(Controller.SingletonType.FINAL).addShape(point);
            SettingsSingleton.setLastPoint(point);
            SettingsSingleton.setCurrentMode(ModeType.DRAW);
        };

        HistoryHandler undo = () -> {
            if (isNewPoint) controller.removeShape(point, Controller.SingletonType.FINAL);
            SettingsSingleton.setLastPoint(null);
        };

        addEvent(redo, undo);
    }

    public void selectShape(Shape shape, double x1, double y1) {
        HistoryHandler redo = () -> {
            SettingsSingleton.setHoveredShape(shape);
            SettingsSingleton.setCurrentMode(ModeType.SELECT);
            SelectUtilities.selectHoveredShape(controller, x1, y1, false);
        };
        HistoryHandler undo = () -> {
            SettingsSingleton.setHoveredShape(null);
            SettingsSingleton.setCurrentMode(ModeType.SELECT);
            SelectUtilities.unselectHoveredShape(controller);
            System.out.println("Unselecting shape");
            System.out.println("x1: " + x1 + " y1: " + y1);
//            SelectUtilities.moveSelectedArea(controller, x1, y1);
            shape.setCoordinates(x1, y1);
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
