package view;

import controller.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.shapes.Point;
import model.shapes.Shape;
import view.GUIElements.canvas.CanvasContainer;
import view.GUIElements.canvas.CustomCanvas;
import view.GUIElements.toolbars.DrawingToolbar;
import view.GUIElements.toolbars.OptionsToolbar;
import view.events.DrawUtilities;
import view.events.KeyboardEvents;
import view.events.SelectUtilities;
import view.types.ModeType;
import view.types.ShapeType;

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
            Shape selectedShape = controller.getSelectedShape();
            Shape hoveredShape = controller.getHoveredShape();
            double mouseX = controller.getCanvasMath().relativeXtoAbsoluteX(event.getX());
            double mouseY = controller.getCanvasMath().relativeYtoAbsoluteY(event.getY());

            if (controller.getLastPoint() != null && controller.getLastPoint() == controller.getHoveredPoint())
                return;

            switch (controller.getCurrentMode()) {
                case DRAW -> {
                    if (controller.getLastPoint() == null) DrawUtilities.addShapesFirstPoint(controller, mouseX, mouseY);
                    else {
                        Shape newShape = DrawUtilities.addShapesLastPoint(controller, mouseX, mouseY, controller.getCurrentShape());

                        previewGc.clear();
                        controller.drawAllShapes(gc, Controller.SingletonType.FINAL);
                        newShape.calculateShapeArea();

//                        if (SettingsSingleton.isShapeType(ShapeType.MULTILINE)) {
//                            controller.addCustomShape(newShape);
//                            controller.checkIfConnected(newShape);
//                        }
                    }
                }
                case SELECT -> {
                    if (hoveredShape != null && (selectedShape == null || event.isShiftDown())) {
                        SelectUtilities.selectHoveredShape(controller, mouseX, mouseY);
                        controller.drawAllShapes(previewGc, Controller.SingletonType.PREVIEW);
                    } else if (selectedShape != null) {
                        SelectUtilities.finalizeSelectedShapes(controller, gc, mouseX, mouseY);
                        previewGc.clear();
                    }
                    controller.drawAllShapes(gc, Controller.SingletonType.FINAL);
                }
                case DELETE -> {
                    if (hoveredShape == null) return;
                    SelectUtilities.deleteShape(controller, hoveredShape);
                    controller.drawAllShapes(gc, Controller.SingletonType.FINAL);
                    previewGc.clear();
                }
                case ROTATE -> {
                    if (hoveredShape != null && (selectedShape == null || event.isShiftDown())) {
                        SelectUtilities.selectHoveredShape(controller, mouseX, mouseY);
                        controller.drawAllShapes(previewGc, Controller.SingletonType.PREVIEW);
                        controller.drawAllShapes(gc, Controller.SingletonType.FINAL);
                    } else if (selectedShape != null) {
                        SelectUtilities.finalizeSelectedRotation(controller, mouseX, mouseY);
                        controller.drawAllShapes(gc, Controller.SingletonType.FINAL);
                        previewGc.clear();
                    }

                    controller.drawAllShapes(previewGc, Controller.SingletonType.PREVIEW);

                }
            }
        });
        // This is the preview drawing
        canvasContainer.setOnMouseMoved(event -> {
            Shape selectedShape = controller.getSelectedShape();
            controller.setMousePosition(event.getX(), event.getY());
            Shape hoveredShape = null;
            controller.setHoveredShape(null);
            previewGc.clear();
            controller.setHoveredPoint(null);
            double distanceCutOff = controller.getCanvasMath().relativeDistance(15);
            double lowestDistance = distanceCutOff;

            double mouseX = controller.getCanvasMath().relativeXtoAbsoluteX(event.getX());
            double mouseY = controller.getCanvasMath().relativeYtoAbsoluteY(event.getY());

            for (Shape shape : controller.getShapes(Controller.SingletonType.FINAL)) {
                double distance = shape.calculateDistanceFromMouse(mouseX, mouseY);
                int bestPriority = hoveredShape != null ? hoveredShape.getPriority() : 0;
                if ((distance < lowestDistance && shape.getPriority() >= bestPriority) || (distance < distanceCutOff && bestPriority < shape.getPriority())) {
                    if (shape == controller.getLastPoint()) continue;
                    lowestDistance = distance;
                    hoveredShape = shape;
                    controller.setHoveredShape(hoveredShape);
                    if (shape.getType() == ShapeType.POINT) controller.setHoveredPoint((Point) shape);
                }
            }

            if (hoveredShape != null) {
                previewGc.setFillColor(controller.getHoverColor());
                previewGc.setStrokeColor(controller.getHoverColor());
                hoveredShape.draw(previewGc);
            }

            previewGc.setFillColor(controller.getSelectedColor());
            previewGc.setStrokeColor(controller.getSelectedColor());

            if (controller.getCurrentMode() == ModeType.DRAW) {
                DrawUtilities.renderDrawingPreview(controller, mouseX, mouseY, previewGc);
            } else if (controller.getCurrentMode() == ModeType.SELECT && selectedShape != null) {
                if (!event.isShiftDown()) SelectUtilities.moveSelectedArea(controller, mouseX, mouseY);
                else SelectUtilities.updateSelectionCoordinates(controller, mouseX, mouseY);

                controller.drawAllShapes(previewGc, Controller.SingletonType.PREVIEW);
            } else if (controller.getCurrentMode() == ModeType.ROTATE && selectedShape != null) {
                if (!event.isShiftDown()) SelectUtilities.rotateSelectedShape(controller, mouseX, mouseY);
                else SelectUtilities.updateSelectionCoordinates(controller, mouseX, mouseY);
                controller.drawAllShapes(previewGc, Controller.SingletonType.PREVIEW);
            }
        });

        canvasContainer.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.MIDDLE) {
                middleX = canvasContainer.getX() + event.getX();
                middleY = canvasContainer.getY() + event.getY();
                System.out.println("Middle X: " + middleX + " Middle Y: " + middleY);
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
            else canvasContainer.setZoom(Math.min(Math.pow(canvasContainer.getZoom(), 1.15) + .1, maxScale));

            double scale = canvasContainer.getZoom() / zoomLevel;
            double deltaX = (event.getX() * scale) - event.getX();
            double deltaY = (event.getY() * scale) - event.getY();

            canvasContainer.setX(canvasContainer.getX() * scale + deltaX);
            canvasContainer.setY(canvasContainer.getY() * scale + deltaY);
            controller.drawAllShapes(gc, Controller.SingletonType.FINAL);
            controller.drawAllShapes(previewGc, Controller.SingletonType.PREVIEW);
        });

        DrawingToolbar drawToolbar = new DrawingToolbar(controller, stage);
        drawToolbar.getButtons().get("Mode").setOnAction(event -> drawToolbar.changeMode(ModeType.DRAW));
        drawToolbar.getButtons().get("Select").setOnAction(event -> drawToolbar.changeMode(ModeType.SELECT));
        drawToolbar.getButtons().get("Delete").setOnAction(event -> drawToolbar.changeMode(ModeType.DELETE));
        drawToolbar.getButtons().get("Reset").setOnAction(event -> {
                controller.removeAllShapes();
                gc.getGrid().drawGrid();
            }
        );

        drawToolbar.getButtons().get("Rotate").setOnAction(event -> drawToolbar.changeMode(ModeType.ROTATE));
        System.out.println("CANVAS CONTAINER IS : " + canvasContainer.getLayer(0));
        OptionsToolbar optionBar = new OptionsToolbar(stage, controller, canvasContainer.getLayer(0));
        optionBar.getButtons().get("Settings").setOnAction(event -> optionBar.showSettings());
        optionBar.getButtons().get("File").setOnAction(event -> optionBar.showFile());

        root.setLeft(drawToolbar);
        root.setTop(optionBar);
        root.setCenter(canvasContainer);

        Scene view = new Scene(root, canvasWidth, canvasHeight);
        stage.setTitle("Floor Plan Creator");
        stage.setScene(view);
        stage.show();

        view.setOnKeyPressed(KeyboardEvents.onKeyPressed(previewGc, gc, controller)::handle);
        view.setOnKeyReleased(KeyboardEvents.onKeyReleased(previewGc, gc, controller)::handle);
    }

    public CanvasContainer getCanvasContainer() {
        return canvasContainer;
    }
}