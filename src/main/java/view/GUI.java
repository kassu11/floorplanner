package view;

import controller.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Point;
import model.Shape;
import view.GUIElements.CanvasContainer;
import view.GUIElements.CustomCanvas;
import view.GUIElements.OptionsToolbar;
import view.GUIElements.DrawingToolbar;
import view.events.DrawUtilities;
import view.events.KeyboardEvents;
import view.events.SelectUtilities;

import static javafx.scene.paint.Color.*;

public class GUI extends Application {

    Controller controller;
    private CanvasContainer canvasContainer;
    private int canvasWidth = 750;
    private int canvasHeight = 750;
    private double middleX, middleY;

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
            if (event.getButton() != MouseButton.PRIMARY) return;
            // Point endPoint = ;
            Shape selectedShape = SettingsSingleton.getSelectedShape();
            Shape hoveredShape = SettingsSingleton.getHoveredShape();
            double mouseX = controller.getCanvasMath().relativeXtoAbsoluteX(event.getX());
            double mouseY = controller.getCanvasMath().relativeYtoAbsoluteY(event.getY());

            if (SettingsSingleton.getLastPoint() != null && SettingsSingleton.getLastPoint() == SettingsSingleton.getHoveredPoint()) return;

            switch (SettingsSingleton.getCurrentMode()) {
                case DRAW -> {
                    if (SettingsSingleton.getLastPoint() == null)
                        DrawUtilities.addShapesFirstPoint(controller, mouseX, mouseY);
                    else {
                        Shape newShape = DrawUtilities.addShapesLastPoint(controller, mouseX, mouseY, SettingsSingleton.getCurrentShape());

                        previewGc.clear();
                        controller.drawAllShapes(gc, Controller.SingletonType.FINAL);
                        newShape.calculateShapeArea();

                        if (SettingsSingleton.isShapeType(ShapeType.MULTILINE)) {
                            controller.addCustomShape(newShape);
                            controller.checkIfConnected(newShape);
                        }
                    }
                }
                case SELECT -> {
                    if (hoveredShape != null && selectedShape == null || event.isShiftDown()) {
                        SelectUtilities.selectHoveredShape(controller, mouseX, mouseY);
                        controller.drawAllShapes(previewGc, Controller.SingletonType.PREVIEW);

                    } else if (selectedShape != null && !event.isShiftDown()) {
                        SelectUtilities.finalizeSelectedShapes(controller, gc, mouseX, mouseY);
                        previewGc.clear();
                    }
                    controller.drawAllShapes(gc, Controller.SingletonType.FINAL);
                }
                case DELETE -> {
                    if (hoveredShape == null) return;
                    controller.deleteShape(hoveredShape, Controller.SingletonType.FINAL);

                    SettingsSingleton.setHoveredShape(null);
                    SettingsSingleton.setSelectedShape(null);
                    SettingsSingleton.setLastPoint(null);
                    controller.drawAllShapes(gc, Controller.SingletonType.FINAL);
                }
            }
        });
        // This is the preview drawing
        canvasContainer.setOnMouseMoved(event -> {
            Shape selectedShape = SettingsSingleton.getSelectedShape();
            Shape hoveredShape = null;
            SettingsSingleton.setHoveredShape(null);
            previewGc.clear();
            SettingsSingleton.setHoveredPoint(null);
            double distanceCutOff = 15;
            double lowestDistance = distanceCutOff;

            double mouseX = controller.getCanvasMath().relativeXtoAbsoluteX(event.getX());
            double mouseY = controller.getCanvasMath().relativeYtoAbsoluteY(event.getY());

            for (Shape shape : controller.getShapes(Controller.SingletonType.FINAL)) {

                double distance = shape.calculateDistanceFromMouse(mouseX, mouseY);
                int bestPriority = hoveredShape != null ? hoveredShape.getPriority() : 0;
                if ((distance < lowestDistance && shape.getPriority() >= bestPriority) || (distance < distanceCutOff && bestPriority < shape.getPriority())) {
                    if (shape == SettingsSingleton.getLastPoint()) continue;
                    lowestDistance = distance;
                    hoveredShape = shape;
                    SettingsSingleton.setHoveredShape(hoveredShape);
                    if (shape.getClass().equals(Point.class)) SettingsSingleton.setHoveredPoint((Point) shape);
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
            }

            previewGc.beginPath();

            if (SettingsSingleton.getCurrentMode() == ModeType.DRAW) {
                if (SettingsSingleton.getLastPoint() == null) return;
                Shape lastpoint = SettingsSingleton.getLastPoint();
                Point point = controller.createAbsolutePoint(mouseX, mouseY, null);
                controller.createShape(point, lastpoint.getX(), lastpoint.getY(), SettingsSingleton.getCurrentShape(), null).draw(previewGc);
            } else if (SettingsSingleton.getCurrentMode() == ModeType.SELECT && selectedShape != null) {
                if(!event.isShiftDown()) SelectUtilities.moveSelectedArea(controller, mouseX, mouseY);

                controller.drawAllShapes(previewGc, Controller.SingletonType.PREVIEW);
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
                controller.drawAllShapes(gc, Controller.SingletonType.FINAL);
                controller.drawAllShapes(previewGc, Controller.SingletonType.PREVIEW);
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
            controller.drawAllShapes(gc, Controller.SingletonType.FINAL);
            controller.drawAllShapes(previewGc, Controller.SingletonType.PREVIEW);
        });

        DrawingToolbar drawToolbar = new DrawingToolbar(stage);
        drawToolbar.getButtons().get("Mode").setOnAction(event -> drawToolbar.changeMode(ModeType.DRAW));
        drawToolbar.getButtons().get("Select").setOnAction(event -> drawToolbar.changeMode(ModeType.SELECT));
        drawToolbar.getButtons().get("Delete").setOnAction(event -> drawToolbar.changeMode(ModeType.DELETE));
        drawToolbar.getButtons().get("Reset").setOnAction(event -> controller.removeAllShapes());
        OptionsToolbar optionBar = new OptionsToolbar();

        root.setLeft(drawToolbar);
        root.setTop(optionBar);
        root.setCenter(canvasContainer);

        Scene view = new Scene(root, canvasWidth, canvasHeight);
        stage.setTitle("view.FPGUI");
        stage.setScene(view);
        stage.show();

        view.setOnKeyPressed(KeyboardEvents.onKeyPressed(previewGc)::handle);

        test(value -> System.out.println(value));
        test2(previewGc, w -> {
            w.clear();
            System.out.println(w);
        });
    }

    public CanvasContainer getCanvasContainer() {
        return canvasContainer;
    }

    public void test(CustomCallback callback) {
        callback.set("Hello World");
    }

    public void test2(CustomCanvas canvas, CustomCallback2 callback) {
        callback.set(canvas);
    }

}

@FunctionalInterface
interface CustomCallback {
    void set(String value);
}

@FunctionalInterface
interface CustomCallback2 {
    void set(CustomCanvas value);
}