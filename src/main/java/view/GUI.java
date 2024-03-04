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
import view.events.KeyboardEvents;

import static javafx.scene.paint.Color.*;

public class GUI extends Application {

    Controller controller;
    private CanvasContainer canvasContainer;
    private int canvasWidth = 750;
    private int canvasHeight = 750;
    private double middleX, middleY, selectedX, selectedY;

    private double initialMouseX, initialMouseY;

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
            Point endPoint = SettingsSingleton.getHoveredPoint();
            Shape selectedShape = SettingsSingleton.getSelectedShape();
            Shape hoveredShape = SettingsSingleton.getHoveredShape();
            double mouseX = controller.getCanvasMath().relativeXtoAbsoluteX(event.getX());
            double mouseY = controller.getCanvasMath().relativeYtoAbsoluteY(event.getY());

            if (SettingsSingleton.getLastPoint() != null && SettingsSingleton.getLastPoint() == endPoint) return;

            switch (SettingsSingleton.getCurrentMode()) {
                case DRAW -> {
                    if (SettingsSingleton.getLastPoint() == null) {
                        if (SettingsSingleton.getHoveredPoint() != null)
                            SettingsSingleton.setLastPoint(SettingsSingleton.getHoveredPoint());
                        else
                            SettingsSingleton.setLastPoint(controller.createAbsolutePoint(mouseX, mouseY, Controller.SingletonType.FINAL));
                    } else {
                        previewGc.clear();

                        if (endPoint == null) endPoint = controller.createAbsolutePoint(mouseX, mouseY, Controller.SingletonType.FINAL);

                        Shape newShape = controller.createShape(SettingsSingleton.getLastPoint(), endPoint, SettingsSingleton.getCurrentShape(), Controller.SingletonType.FINAL);
                        newShape.draw(gc);
                        newShape.calculateShapeArea();

                        controller.drawAllShapes(gc, Controller.SingletonType.FINAL);

                        if (SettingsSingleton.isShapeType(ShapeType.MULTILINE)) {
                            SettingsSingleton.setLastPoint(endPoint);
                            controller.addCustomShape(newShape);
                            controller.checkIfConnected(newShape);
                        } else
                            SettingsSingleton.setLastPoint(null);
                    }
                }
                case SELECT -> {
                    if (hoveredShape != null && selectedShape == null) {
                        selectedShape = hoveredShape;
                        SettingsSingleton.setSelectedShape(selectedShape);
                        selectedX = mouseX;
                        selectedY = mouseY;
                        controller.transferSingleShapeTo(selectedShape, Controller.SingletonType.PREVIEW);
                        if (selectedShape.getClass().equals(Line.class)) {
                            for (Point point : selectedShape.getPoints()) {
                                if (!controller.getShapeContainer(Controller.SingletonType.PREVIEW).getShapes().contains(point))
                                    controller.transferSingleShapeTo(point, Controller.SingletonType.PREVIEW);
                                for (Shape shape : point.getChildren()) {
                                    if (!controller.getShapeContainer(Controller.SingletonType.PREVIEW).getShapes().contains(shape))
                                        controller.transferSingleShapeTo(shape, Controller.SingletonType.PREVIEW);
                                }
                            }
                        }
                        if (selectedShape.getClass().equals(Point.class)) {
                            for (Shape shape : selectedShape.getChildren()) {
                                controller.transferSingleShapeTo(shape, Controller.SingletonType.PREVIEW);
                            }
                        }
                        controller.drawAllShapes(previewGc, Controller.SingletonType.PREVIEW);
                        controller.drawAllShapes(gc, Controller.SingletonType.FINAL);
                    } else if (selectedShape != null) {
                        if (selectedShape.getClass().equals(Point.class)) {
                            selectedShape.setCoordinates(mouseX, mouseY);
                            for (Shape shape : selectedShape.getChildren()) {
                                if (SettingsSingleton.getHoveredPoint() != null && SettingsSingleton.getHoveredPoint() != selectedShape) {
                                    for (Point point : shape.getPoints()) {
                                        if (point.equals(selectedShape)) {
                                            shape.getPoints().set(shape.getPoints().indexOf(point), (Point) hoveredShape);
                                            SettingsSingleton.getHoveredPoint().addChild(shape);
                                            controller.removeShape(point, Controller.SingletonType.PREVIEW);
                                            break;
                                        }
                                    }
                                }
                                shape.setCoordinates(shape.getX() + mouseX - selectedShape.getX(), shape.getY() + mouseY - selectedShape.getY());
                                shape.draw(gc);
                            }
                        }
                        SettingsSingleton.setSelectedShape(null);
                        controller.transferAllShapesTo(Controller.SingletonType.FINAL);
                        controller.drawAllShapes(gc, Controller.SingletonType.FINAL);
                        previewGc.clear();
                    }
                }
                case DELETE -> {
                    if (hoveredShape == null) return;

                    controller.deleteShape(hoveredShape, Controller.SingletonType.FINAL);

                    SettingsSingleton.setHoveredShape(null);
                    SettingsSingleton.setSelectedShape(null);
                    SettingsSingleton.setLastPoint(null);
                    controller.drawAllShapes(gc, Controller.SingletonType.FINAL);

                }
                case ROTATE -> {
                    if (hoveredShape != null && selectedShape == null) {
                        System.out.println("No selected shape");
                        selectedShape = hoveredShape;
                        SettingsSingleton.setSelectedShape(selectedShape);
                        selectedShape.calculateCentroid();
                        selectedX = mouseX;
                        selectedY = mouseY;
                        controller.transferSingleShapeTo(selectedShape, Controller.SingletonType.PREVIEW);
                        if (selectedShape.getClass().equals(Line.class)) {
                            for (Point point : selectedShape.getPoints()) {
                                if (!controller.getShapeContainer(Controller.SingletonType.PREVIEW).getShapes().contains(point))
                                    controller.transferSingleShapeTo(point, Controller.SingletonType.PREVIEW);
                                for (Shape shape : point.getChildren()) {
                                    if (!controller.getShapeContainer(Controller.SingletonType.PREVIEW).getShapes().contains(shape))
                                        controller.transferSingleShapeTo(shape, Controller.SingletonType.PREVIEW);
                                }
                            }
                        }
                        if (selectedShape.getClass().equals(Point.class)) return;
                        controller.drawAllShapes(previewGc, Controller.SingletonType.PREVIEW);
                        controller.drawAllShapes(gc, Controller.SingletonType.FINAL);
                    } else if (selectedShape != null) {
                        if (!selectedShape.getClass().equals(Point.class)) {
                            double centroidX = selectedShape.getCentroidX();
                            double centroidY = selectedShape.getCentroidY();
                            double vectorX1 = selectedX - centroidX;
                            double vectorY1 = selectedY - centroidY;
                            double vectorX2 = mouseX - centroidX;
                            double vectorY2 = mouseY - centroidY;
                            double dotProduct = vectorX1 * vectorX2 + vectorY1 * vectorY2;
                            double determinant = vectorX1 * vectorY2 - vectorY1 * vectorX2;
                            double angle = Math.atan2(determinant, dotProduct);

                            for (Point point : selectedShape.getPoints()) {
                                double radians = Math.sqrt(Math.pow(point.getX() - centroidX, 2) + Math.pow(point.getY() - centroidY, 2));
                                if (centroidX == point.getX()) {
                                    if (point.getY() - centroidY < 0) point.setCoordinates(centroidX, centroidY - radians);
                                    else point.setCoordinates(centroidX, centroidY + radians);
                                } else {
                                    double pointAngle = Math.atan2(point.getY() - centroidY, point.getX() - centroidX);
                                    double newAngle = (pointAngle + angle) % (2 * Math.PI);
                                    point.setCoordinates(centroidX + radians * Math.cos(newAngle), centroidY + radians * Math.sin(newAngle));
                                }
                            }
                        }
                        SettingsSingleton.setSelectedShape(null);
                        controller.transferAllShapesTo(Controller.SingletonType.FINAL);
                        controller.drawAllShapes(gc, Controller.SingletonType.FINAL);
                        previewGc.clear();
                    }
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
                Point point = controller.createAbsolutePoint(mouseX, mouseY, null);
                controller.createShape(point, SettingsSingleton.getLastPoint().getX(), SettingsSingleton.getLastPoint().getY(), SettingsSingleton.getCurrentShape(), null)
                        .draw(previewGc);
            }
            if (SettingsSingleton.getCurrentMode() == ModeType.SELECT) {
                if (selectedShape != null) {
                    if (selectedShape.getClass().equals(Point.class)) {
                        for (Shape previewShape : controller.getShapes(Controller.SingletonType.PREVIEW)) {
                            if (previewShape.getClass().equals(Point.class)) previewShape.setCoordinates(mouseX, mouseY);
                        }
                    }
                    if (selectedShape.getClass().equals(Line.class)) {
                        double deltaX = mouseX - selectedX;
                        double deltaY = mouseY - selectedY;
                        for (Point point : selectedShape.getPoints()) {
                            point.setCoordinates(point.getX() + deltaX, point.getY() + deltaY);
                        }
                    }
                    selectedX = mouseX;
                    selectedY = mouseY;
                    controller.drawAllShapes(previewGc, Controller.SingletonType.PREVIEW);
                }
            }
            if (SettingsSingleton.getCurrentMode() == ModeType.ROTATE) {
                if (selectedShape != null && !selectedShape.getClass().equals(Point.class)) {
                    double centroidX = selectedShape.getCentroidX();
                    double centroidY = selectedShape.getCentroidY();
                    double vectorX1 = selectedX - centroidX;
                    double vectorY1 = selectedY - centroidY;
                    double vectorX2 = mouseX - centroidX;
                    double vectorY2 = mouseY - centroidY;
                    double dotProduct = vectorX1 * vectorX2 + vectorY1 * vectorY2;
                    double determinant = vectorX1 * vectorY2 - vectorY1 * vectorX2;
                    double angle = Math.atan2(determinant, dotProduct);
                    System.out.println("Angle: " + angle);
                    for (Point point : selectedShape.getPoints()) {
                        double radians = Math.sqrt(Math.pow(point.getX() - centroidX, 2) + Math.pow(point.getY() - centroidY, 2));
                        if (centroidX == point.getX()) {
                            if (point.getY() - centroidY < 0) point.setCoordinates(centroidX, centroidY - radians);
                            else point.setCoordinates(centroidX, centroidY + radians);
                        } else {
                            double pointAngle = Math.atan2(point.getY() - centroidY, point.getX() - centroidX);
                            System.out.println("Point angle: " + pointAngle);
                            double newAngle = (pointAngle + angle) % (2 * Math.PI);
                            System.out.println("New angle: " + newAngle);
                            point.setCoordinates(centroidX + radians * Math.cos(newAngle), centroidY + radians * Math.sin(newAngle));
                        }
                    }
                    selectedX = mouseX;
                    selectedY = mouseY;
                    controller.drawAllShapes(previewGc, Controller.SingletonType.PREVIEW);
                }
            }
            previewGc.stroke();
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
        drawToolbar.getButtons().get("Rotate").setOnAction(event -> drawToolbar.changeMode(ModeType.ROTATE));
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