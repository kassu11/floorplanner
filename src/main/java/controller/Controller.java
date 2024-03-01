package controller;

import model.*;
import view.GUI;
import view.GUIElements.CanvasMath;
import view.GUIElements.CustomCanvas;
import view.ShapeType;

import java.util.List;

public class Controller {
    private GUI gui;
    private CanvasMath canvasMath;
    private ShapeContainer finalShapes = FinalShapesSingleton.getInstance();
    private ShapeContainer previewShapes = PreviewShapesSingleton.getInstance();
    public enum SingletonType {
        FINAL, PREVIEW
    }

    public Controller(GUI gui) {
        this.gui = gui;
        this.canvasMath = new CanvasMath(this.gui.getCanvasContainer());
    }

    public Shape createShape(double x, double y, double x1, double y1, ShapeType shapeType, SingletonType singletonType) {
        Point pointA = new Point(x, y);
        Point pointB = new Point(x1, y1);

        return this.createShape(pointA, pointB, shapeType, singletonType);
    }

    public Shape createShape(Point pointA, double x1, double y1, ShapeType shapeType, SingletonType singletonType) {
        Point pointB = new Point(x1, y1);
        return this.createShape(pointA, pointB, shapeType, singletonType);
    }

    public Shape createShape(Point pointA, Point pointB, ShapeType shapeType, SingletonType singletonType) {
        // TODO: Refactor this to use a factory

        Shape shape =  switch (shapeType) {
            case LINE, MULTILINE -> new Line(pointA, pointB);
            case RECTANGLE -> {
                Point pointC = new Point(pointB.getX(), pointA.getY());
                Point pointD = new Point(pointA.getX(), pointB.getY());
                yield new Rectangle(pointA, pointB, pointC, pointD);
            }
            case CIRCLE -> new Circle(pointA, pointB);
        };
        if(singletonType != null) shape.addToShapeContainer(getShapeContainer(singletonType));
        return shape;
    }



    public void transferAllShapes(SingletonType type){
        switch(type){
            case PREVIEW -> finalShapes.transferAllShapesTo(previewShapes);
            case FINAL -> previewShapes.transferAllShapesTo(finalShapes);
        }
    }

    public void transferSingleShape(Shape shape, SingletonType type){
        switch(type){
            case PREVIEW -> finalShapes.transferSingleShapeTo(shape, previewShapes);
            case FINAL -> previewShapes.transferSingleShapeTo(shape, finalShapes);
        }
    }

    public void drawAllShapes(CustomCanvas customCanvas) {
        for (Shape shape : finalShapes.getShapes()) {
            shape.draw(customCanvas);
        }
    }

    public void removeShape(Shape shape) {
        finalShapes.getShapes().remove(shape);
    }

    public List<Shape> getShapes(SingletonType type) {
        return getShapeContainer(type).getShapes();
    }

    public Point createRelativePoint(double x, double y, SingletonType singletonType) {
        Point point = new Point(canvasMath.relativeXtoAbsoluteX(x), canvasMath.relativeYtoAbsoluteY(y));
        if(singletonType != null) getShapeContainer(singletonType).addShape(point);
        return point;
    }

    public Point createAbsolutePoint(double x, double y, SingletonType singletonType) {
        Point point = new Point(x, y);
        if(singletonType != null) getShapeContainer(singletonType).addShape(point);
        return point;
    }

    public CanvasMath getCanvasMath() {
        return canvasMath;
    }

    public void clearShapes() {
        finalShapes.clearShapes();
        gui.getCanvasContainer().clear();
    }

    public ShapeContainer getShapeContainer(SingletonType type){
        if(type == null) return null;
        return switch(type){
            case FINAL -> finalShapes;
            case PREVIEW -> previewShapes;
        };
    }

    public static void main(String[] args) {
        GUI.launch(GUI.class);
    }
}
