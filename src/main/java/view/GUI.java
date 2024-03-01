package view;

import controller.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Line;
import model.Point;
import model.Shape;
import view.GUIElements.CanvasContainer;
import view.GUIElements.CustomCanvas;
import view.GUIElements.OptionsToolbar;
import view.GUIElements.DrawingToolbar;
import view.events.EventCallback;
import view.events.KeyboardEvents;

import java.util.ArrayList;
import java.util.List;

import static javafx.scene.paint.Color.*;

public class GUI extends Application {

    Controller controller;
    Point lastPoint;
    private Point hoveredPoint;
    private Shape selectedShape, hoveredShape;
    private CanvasContainer canvasContainer;
    private int canvasWidth = 750;
    private int canvasHeight = 750;
    private double middleX, middleY, selectedX, selectedY;

    @Override
    public void init() {
        canvasContainer = new CanvasContainer(canvasWidth, canvasHeight);
        controller = new Controller(this);
    }

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();

        CustomCanvas gc = canvasContainer.getLayer(0);
        CustomCanvas previewGc = canvasContainer.getLayer(1);

        gc.setLineWidth(5);
        previewGc.setLineWidth(5);

        canvasContainer.setOnMouseClicked(event -> {
            if (event.getButton() != MouseButton.PRIMARY)
                return;
            Point endPoint = hoveredPoint;
            double mouseX = controller.getCanvasMath().relativeXtoAbsoluteX(event.getX());
            double mouseY = controller.getCanvasMath().relativeYtoAbsoluteY(event.getY());

            switch (SettingsSingleton.getCurrentMode()) {
                case DRAW -> {
                    if (lastPoint == null) {
                        if (hoveredPoint != null)
                            lastPoint = hoveredPoint;
                        else
                            lastPoint = controller.createAbsolutePoint(mouseX, mouseY, Controller.SingletonType.FINAL);
                    } else {
                        previewGc.clear();

                        if (endPoint == null)
                            endPoint = controller.createAbsolutePoint(mouseX, mouseY, Controller.SingletonType.FINAL);

                        Shape newShape = controller.createShape(lastPoint, endPoint,
                                SettingsSingleton.getCurrentShape(), Controller.SingletonType.FINAL);
                        newShape.draw(gc);
                        newShape.calculateShapeArea();
                        gc.clear();

                        controller.drawAllShapes(gc);

                        if (SettingsSingleton.isShapeType(ShapeType.MULTILINE))
                            lastPoint = endPoint;
                        else
                            lastPoint = null;
                    }
                }
                case SELECT -> {
                    if (hoveredShape != null && selectedShape == null) {
                        selectedShape = hoveredShape;
                        selectedX = mouseX;
                        selectedY = mouseY;
                    } else if (selectedShape != null) {
                        gc.clear();
                        if (selectedShape.getClass().equals(Point.class)) {
                            selectedShape.setCoordinates(mouseX, mouseY);
                            for (Shape shape : selectedShape.getChildren()) {
                                if (hoveredPoint != null) {
                                    for (Point point : shape.getPoints()) {
                                        if (point.equals(selectedShape)) {
                                            shape.getPoints().set(shape.getPoints().indexOf(point),
                                                    (Point) hoveredShape);
                                            hoveredPoint.addChild(shape);
                                            controller.removeShape(point);
                                            break;
                                        }
                                    }
                                }
                                shape.setCoordinates(shape.getX() + mouseX - selectedShape.getX(),
                                        shape.getY() + mouseY - selectedShape.getY());
                                shape.draw(gc);
                            }
                        }
                        if (selectedShape.getClass().equals(Line.class)) {
                            double deltaX = mouseX - selectedX;
                            double deltaY = mouseY - selectedY;

                            for (Point point : selectedShape.getPoints()) {
                                point.setCoordinates(point.getX() + deltaX, point.getY() + deltaY);
                            }
                        }
                        selectedShape = null;
                        controller.drawAllShapes(gc);
                        previewGc.clear();
                    }
                }
                case DELETE -> {
                    if (hoveredShape != null) {
                        controller.removeShape(hoveredShape);
                        if (hoveredShape.getClass().equals(Line.class)) {
                            for (Point point : hoveredShape.getPoints()) {
                                point.getChildren().remove(hoveredShape);
                            }
                        }
                        if (hoveredShape.getClass().equals(Point.class)) {
                            for (Shape shape : hoveredShape.getChildren()) {
                                for (int i = 0; i < shape.getPoints().size(); i++) {
                                    if (!shape.getPoints().get(i).equals(hoveredShape)) {
                                        shape.getPoints().get(i).getChildren().remove(shape);
                                        shape.getPoints().remove(i);
                                        i--;
                                    }
                                    controller.removeShape(shape);
                                }
                            }
                            controller.removeShape(hoveredShape);
                        }
                        hoveredShape = null;
                        selectedShape = null;
                        lastPoint = null;
                        gc.clear();
                        controller.drawAllShapes(gc);
                    }
                }
            }
        });
        // This is the preview drawing
        canvasContainer.setOnMouseMoved(event -> {
            previewGc.clear();
            hoveredShape = null;
            List<Shape> startingPoints = new ArrayList<>();
            hoveredPoint = null;
            double distanceCutOff = 15;
            double lowestDistance = distanceCutOff;

            double mouseX = controller.getCanvasMath().relativeXtoAbsoluteX(event.getX());
            double mouseY = controller.getCanvasMath().relativeYtoAbsoluteY(event.getY());

            for (Shape shape : controller.getShapes(Controller.SingletonType.FINAL)) {

                double distance = shape.calculateDistanceFromMouse(mouseX, mouseY);
                int bestPriority = hoveredShape != null ? hoveredShape.getPriority() : 0;
                if ((distance < lowestDistance && shape.getPriority() >= bestPriority)
                        || (distance < distanceCutOff && bestPriority < shape.getPriority())) {
                    if (shape == lastPoint)
                        continue;
                    lowestDistance = distance;
                    hoveredShape = shape;
                    if (shape.getClass().equals(Point.class))
                        hoveredPoint = (Point) shape;
                }
            }

            if (hoveredShape != null) {
                previewGc.setFill(RED);
                previewGc.setStroke(RED);
                hoveredShape.draw(previewGc);
            }

            if (selectedShape != null) {
                previewGc.setFill(BLUE);
                previewGc.setStroke(BLUE);
                selectedShape.draw(previewGc);
                if (selectedShape.getClass().equals(Point.class)) {
                    for (Shape shape : selectedShape.getChildren()) {
                        for (Point point : shape.getPoints()) {
                            if (!point.equals(selectedShape))
                                startingPoints.add(point);
                        }
                    }
                } else {
                    startingPoints.addAll(selectedShape.getPoints());
                }

            }

            previewGc.beginPath();

            if (SettingsSingleton.getCurrentMode() == ModeType.DRAW) {
                if (lastPoint == null)
                    return;
                Point point = controller.createRelativePoint(mouseX, mouseY, null);
                controller.createShape(point, lastPoint.getX(), lastPoint.getY(), SettingsSingleton.getCurrentShape(),
                        null).draw(previewGc);
            }
            if (SettingsSingleton.getCurrentMode() == ModeType.SELECT) {
                if (!startingPoints.isEmpty()) {
                    if (selectedShape.getClass().equals(Point.class)) {
                        for (Shape startingPoint : startingPoints) {
                            previewGc.moveTo(startingPoint.getX(), startingPoint.getY());
                            previewGc.lineTo(mouseX, mouseY);
                        }
                    }
                }

            }

            previewGc.stroke();
        });

        canvasContainer.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.MIDDLE) {
                middleX = canvasContainer.getX() + event.getX();
                middleY = canvasContainer.getY() + event.getY();
            }
        });

        canvasContainer.setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.MIDDLE) {
                canvasContainer.setX(middleX - event.getX());
                canvasContainer.setY(middleY - event.getY());
                canvasContainer.clear();
                for (Shape shape : controller.getShapes(Controller.SingletonType.FINAL))
                    shape.draw(gc);
            }
        });

        final double minScale = .05, maxScale = 150.0;
        canvasContainer.setOnScroll(event -> {
            double zoomLevel = canvasContainer.getZoom();
            if (event.getDeltaY() < 0)
                canvasContainer.setZoom(Math.max(Math.pow(canvasContainer.getZoom(), 0.9) - .1, minScale));
            else
                canvasContainer.setZoom(Math.min(Math.pow(canvasContainer.getZoom(), 1.15) + .1, maxScale));

            double scale = canvasContainer.getZoom() / zoomLevel;
            double deltaX = (event.getX() * scale) - event.getX();
            double deltaY = (event.getY() * scale) - event.getY();

            canvasContainer.setX(canvasContainer.getX() * scale + deltaX);
            canvasContainer.setY(canvasContainer.getY() * scale + deltaY);
            canvasContainer.clear();
            controller.drawAllShapes(gc);
        });

        DrawingToolbar drawToolbar = new DrawingToolbar(stage);
        drawToolbar.getButtons().get("Mode").setOnAction(event -> drawToolbar.changeMode(ModeType.DRAW));
        drawToolbar.getButtons().get("Select").setOnAction(event -> drawToolbar.changeMode(ModeType.SELECT));
        drawToolbar.getButtons().get("Delete").setOnAction(event -> drawToolbar.changeMode(ModeType.DELETE));
        drawToolbar.getButtons().get("Reset").setOnAction(event -> controller.clearShapes());
        OptionsToolbar optionBar = new OptionsToolbar();

        root.setLeft(drawToolbar);
        root.setTop(optionBar);
        root.setCenter(canvasContainer);

        Scene view = new Scene(root, canvasWidth, canvasHeight);
        stage.setTitle("view.FPGUI");
        stage.setScene(view);
        stage.show();

        EventCallback resetLastPoint = () -> lastPoint = null;
        view.setOnKeyPressed(KeyboardEvents.onKeyPressed(previewGc, resetLastPoint)::handle);
    }

    public CanvasContainer getCanvasContainer() {
        return canvasContainer;
    }

}