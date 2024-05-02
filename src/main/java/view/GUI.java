package view;

import controller.Controller;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.shapes.Dimension;
import javafx.stage.StageStyle;
import model.shapes.Point;
import model.shapes.Shape;
import view.GUIElements.Ruler;
import view.GUIElements.canvas.CanvasContainer;
import view.GUIElements.canvas.CustomCanvas;
import view.GUIElements.toolbars.DrawingToolbar;
import view.GUIElements.toolbars.OptionsToolbar;
import view.events.AreaUtilities;
import view.events.DrawUtilities;
import view.events.KeyboardEvents;
import view.events.SelectUtilities;
import view.types.ModeType;
import view.types.ShapeType;
/**
 * GUI class for the main application
 */
public class GUI extends Application {
    /**
     * Controller
     */
    private Controller controller;
    /**
     * Canvas container
     */
    private CanvasContainer canvasContainer;
    /**
     * Canvas width
     */
    private int canvasWidth = 750;
    /**
     * Canvas height
     */
    private int canvasHeight = 750;
    /**
     * Middle x
     * Middle y
     */
    private double middleX, middleY;
    /**
     * Settings singleton
     */
    private SettingsSingleton settings = SettingsSingleton.getInstance();
    /**
     * Option bar
     */
    private OptionsToolbar optionBar;
    /**
     * Drawing toolbar
     */
    private DrawingToolbar drawToolbar;
    /**
     * Init method
     */

    @Override
    public void init() {
        canvasContainer = new CanvasContainer(canvasWidth, canvasHeight);
        controller = new Controller(this);
    }
    /**
     * Start method
     * @param stage stage
     */

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();

        CustomCanvas gc = canvasContainer.getLayer(0);
        CustomCanvas previewGc = canvasContainer.getLayer(1);

        gc.setLineWidth(5);
        previewGc.setLineWidth(5);

        Ruler xRuler = new Ruler(false);
        xRuler.setPadding(new Insets(0, 0, 0, 50));
        Ruler yRuler = new Ruler(true);

        if(settings.isGridEnabled()) gc.getGrid().drawGrid();

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
                        Shape newShape = DrawUtilities.addShapesLastPoint(controller, mouseX, mouseY, controller.getCurrentShapeType());

                        previewGc.clear();
                        controller.drawAllShapes(gc, Controller.SingletonType.FINAL);
                        newShape.calculateShapeArea();

//                        if (controller.getCurrentShapeType() == ShapeType.MULTILINE) {
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
                case SIZING -> {
                    if(hoveredShape != null && hoveredShape.getType() == ShapeType.LINE) {
                        Dimension dimension = controller.setDimensionLine(hoveredShape,  Math.random() * 200 + 200);
                        controller.drawAllShapes(gc, Controller.SingletonType.FINAL);
                        dimension.draw(gc);
                    }
                }
                case AREA -> {
                    if(selectedShape != null && !event.isShiftDown()) {
                        controller.transferAllShapesTo(Controller.SingletonType.FINAL);
                        controller.drawAllShapes(gc, Controller.SingletonType.FINAL);
                    }
                    if (hoveredShape != null) {
                        SelectUtilities.selectHoveredShape(controller, mouseX, mouseY);
                        previewGc.clear();
                        AreaUtilities.drawArea(controller, previewGc);
                    }
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
            previewGc.drawRulerXpointer(event.getX());
            previewGc.drawRulerYpointer(event.getY());
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

            previewGc.setFillColor(controller.getSelectedColor());
            previewGc.setStrokeColor(controller.getSelectedColor());

            if (controller.getCurrentMode() == ModeType.DRAW) {
                DrawUtilities.renderDrawingPreview(controller, mouseX, mouseY, previewGc);
            } else if (controller.getCurrentMode() == ModeType.SELECT && selectedShape != null) {
                if (!event.isShiftDown()) SelectUtilities.moveSelectedArea(controller, mouseX, mouseY);
                else SelectUtilities.updateSelectionCoordinates(controller, mouseX, mouseY);

                controller.drawAllShapes(previewGc, Controller.SingletonType.PREVIEW);
            } else if(controller.getCurrentMode() == ModeType.AREA) {
                controller.drawAllShapes(previewGc, Controller.SingletonType.PREVIEW);
                AreaUtilities.drawArea(controller, previewGc);
            } else if (controller.getCurrentMode() == ModeType.ROTATE && selectedShape != null) {
                if (!event.isShiftDown()) SelectUtilities.rotateSelectedShape(controller, mouseX, mouseY);
                else SelectUtilities.updateSelectionCoordinates(controller, mouseX, mouseY);
                controller.drawAllShapes(previewGc, Controller.SingletonType.PREVIEW);
            }

            if (hoveredShape != null) {
//                if(hoveredShape.getType() == ShapeType.LINE && controller.getCurrentMode() == ModeType.SELECT && !event.isShiftDown()) return;
                previewGc.setFillColor(controller.getHoverColor());
                previewGc.setStrokeColor(controller.getHoverColor());
                hoveredShape.draw(previewGc);
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
            xRuler.updateRuler(zoomLevel);
            yRuler.updateRuler(zoomLevel);
            controller.drawAllShapes(gc, Controller.SingletonType.FINAL);
            controller.drawAllShapes(previewGc, Controller.SingletonType.PREVIEW);
        });

        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            settings.setGridWidth(newVal.intValue());
            optionBar.updateResolution();
            controller.drawAllShapes(gc, Controller.SingletonType.FINAL);
            canvasWidth = newVal.intValue();
            canvasContainer.resizeCanvas(canvasWidth, canvasHeight);
        });

        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            settings.setGridHeight(newVal.intValue());
            optionBar.updateResolution();
            controller.drawAllShapes(gc, Controller.SingletonType.FINAL);
            canvasHeight = newVal.intValue();
            canvasContainer.resizeCanvas(canvasWidth, canvasHeight);
        });



        drawToolbar = new DrawingToolbar(controller, stage);
        drawToolbar.getButtons().get("mode").setOnAction(event -> drawToolbar.changeMode(ModeType.DRAW));
        drawToolbar.getButtons().get("select").setOnAction(event -> drawToolbar.changeMode(ModeType.SELECT));
        drawToolbar.getButtons().get("delete").setOnAction(event -> drawToolbar.changeMode(ModeType.DELETE));
        drawToolbar.getButtons().get("area").setOnAction(event -> drawToolbar.changeMode(ModeType.AREA));
        drawToolbar.getButtons().get("reset").setOnAction(event -> {
                controller.removeAllShapes();
                gc.getGrid().drawGrid();
            }
        );

        drawToolbar.getButtons().get("rotate").setOnAction(event -> drawToolbar.changeMode(ModeType.ROTATE));
        System.out.println("CANVAS CONTAINER IS : " + canvasContainer.getLayer(0));

        optionBar = new OptionsToolbar(stage, controller, canvasContainer.getLayer(0));
        optionBar.getButtons().get("settings").setOnAction(event -> optionBar.showSettings());
        optionBar.getButtons().get("file").setOnAction(event -> optionBar.showFile());


        root.setLeft(drawToolbar);
        root.setTop(optionBar);

        BorderPane canvasBorder = new BorderPane();

        canvasBorder.setCenter(canvasContainer);
        //canvasBorder.setTop(xRuler);
        //canvasBorder.setLeft(yRuler);

        root.setCenter(canvasBorder);

        Scene view = new Scene(root, canvasWidth, canvasHeight);

        stage.setTitle("Floor Plan Creator");
        stage.setScene(view);
        stage.show();

        view.setOnKeyPressed(KeyboardEvents.onKeyPressed(previewGc, gc, controller)::handle);
        view.setOnKeyReleased(KeyboardEvents.onKeyReleased(previewGc, gc, controller)::handle);
    }
    /**
     * Returns the canvas container
     * @return canvas container
     */
    public CanvasContainer getCanvasContainer() {
        return canvasContainer;
    }
    /**
     * Updates the toolbar localization
     */
    public void updateToolbarLocalization() {
        drawToolbar.updateLocalization();
        optionBar.updateLocalization();
    }
}