package model.history;

import controller.Controller;
import model.Shape;

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
        shapeReferences.put(shape.getId(), shape);
    }

    public void createShape(Shape shape) {
        List<HistoryEvent> createEvents = new ArrayList<>();
        assignShape(shape);
        shape.getPoints().forEach(point -> {
            if(!shapeReferences.containsKey(point.getId())) {
                assignShape(point);
            }
        });

        HistoryHandler redo = () -> {
            controller.getShapeContainer(Controller.SingletonType.FINAL).addShape(shape);
        };
    }

    public void moveShape(Shape shape, double x1, double y1, double x2, double y2) {
        HistoryHandler redo = () -> shape.setCoordinates(x1, y1);
        HistoryHandler undo = () -> shape.setCoordinates(x2, y2);
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

