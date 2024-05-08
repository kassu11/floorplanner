package controller;
// Can you update the documentation of this file to use @param and @return?
import dao.SettingsDao;
import entity.Settings;
import model.history.HistoryManager;
import model.shapeContainers.FinalShapesSingleton;
import model.shapeContainers.PreviewShapesSingleton;
import model.shapeContainers.ShapeContainer;
import model.shapes.*;
import view.GUI;
import view.GUIElements.canvas.CanvasColors;
import view.GUIElements.canvas.CanvasMath;
import view.GUIElements.canvas.CustomCanvas;
import view.SettingsSingleton;
import view.types.ModeType;
import view.types.ShapeDataType;
import view.types.ShapeType;

import java.util.ArrayList;
import java.util.List;

/**
 * The Controller class is responsible for handling the logic of the application.
 */
public class Controller {
    /** The GUI object */
    private GUI gui;
    /** The HistoryManager object */
    private HistoryManager historyManager;
    /** The CanvasMath object */
    private CanvasMath canvasMath;
    /** The ShapeContainer object for completed shapes*/
    private ShapeContainer finalShapes = FinalShapesSingleton.getInstance();
    /** The ShapeContainer object for preview shapes*/
    private ShapeContainer previewShapes = PreviewShapesSingleton.getInstance();
    /** The SettingsSingleton object */
    private SettingsSingleton settingsSingleton = SettingsSingleton.getInstance();
    /** The list of custom shapes */
    private List<Shape> customShapes = new ArrayList<>();
    /** The SettingsDao object */
    private SettingsDao settingsDao = new SettingsDao();
    /** The boolean value of whether the control key is pressed */
    private boolean ctrlIsDown = false;
    /** The current shape type */
    private ShapeType currentShape = ShapeType.LINE;
    /** The current mode type */
    private ModeType currentMode = ModeType.DRAW;
    /** The last point and hovered point variables*/
    private Point lastPoint, hoveredPoint;
    /** The selected and hovered shape variables */
    private Shape selectedShape, hoveredShape;
    /** The mouse x and y coordinates */
    private double mouseX, mouseY;
    /** The hover color and selected color variables */
    private String hoverColor, selectedColor;
    /** The SingletonType enum */
    /** The points of the area shape */
    private List<Point> areaShapes = new ArrayList<>();

    public enum SingletonType {
        FINAL, PREVIEW
    }

    /** The Controller constructor
     *
     * @param gui
     */
    public Controller(GUI gui) {
        this.gui = gui;
        this.canvasMath = new CanvasMath(this.gui.getCanvasContainer());
        this.historyManager = new HistoryManager(this);
        setCurrentMode(ModeType.DRAW);
        loadSettings();
    }

    /** The createShape method that creates a shape with a single point
     *
     * @param x
     * @param y
     * @param x1
     * @param y1
     * @param shapeType
     * @param singletonType
     * @return
     */
    public Shape createShape(double x, double y, double x1, double y1, ShapeType shapeType, SingletonType singletonType) {
        Point pointA = new Point(x, y);
        if (singletonType != null) getShapeContainer(singletonType).addShape(pointA);

        return this.createShape(pointA, x1, y1, shapeType, singletonType);
    }

    /** The createShape method that creates a shape with two points
     *
     * @param pointA
     * @param x1
     * @param y1
     * @param shapeType
     * @param singletonType
     * @return
     */
    public Shape createShape(Point pointA, double x1, double y1, ShapeType shapeType, SingletonType singletonType) {
        Point pointB = new Point(x1, y1);
        if (singletonType != null) getShapeContainer(singletonType).addShape(pointB);

        return this.createShape(pointA, pointB, shapeType, singletonType);
    }

    /** The createShape method that creates a given shape with two points based on what the user has selected
     *
     * @param pointA
     * @param pointB
     * @param shapeType
     * @param singletonType
     * @return
     */
    public Shape createShape(Point pointA, Point pointB, ShapeType shapeType, SingletonType singletonType) {

        Shape shape = switch (shapeType) {
            case LINE, MULTILINE -> new Line(pointA, pointB);
            case RECTANGLE -> {
                Point pointC = createAbsolutePoint(pointB.getX(), pointA.getY(), singletonType);
                Point pointD = createAbsolutePoint(pointA.getX(), pointB.getY(), singletonType);
                Rectangle rectangle = new Rectangle(pointA, pointB, pointC, pointD);
                if (singletonType != null) getShapeContainer(singletonType).addAllShapes(rectangle.getChildren());

                singletonType = null;
                yield rectangle;
            }
            case CIRCLE -> new Circle(pointA, pointB);
            default -> null;
        };
        if (singletonType != null) {
            assert shape != null;
            shape.addToShapeContainer(getShapeContainer(singletonType));
        }
        return shape;
    }

    /** The transferAllShapesTo method that transfers all shapes to a given ShapeContainer object
     *
     * @param type
     */
    public void transferAllShapesTo(SingletonType type) {
        switch (type) {
            case PREVIEW -> finalShapes.transferAllShapesTo(previewShapes);
            case FINAL -> previewShapes.transferAllShapesTo(finalShapes);
        }
    }

    /** The transferSingleShapeTo method that transfers a single shape to a given ShapeContainer object
     *
     * @param shape
     * @param type
     */
    public void transferSingleShapeTo(Shape shape, SingletonType type) {
        switch (type) {
            case PREVIEW -> finalShapes.transferSingleShapeTo(shape, previewShapes);
            case FINAL -> previewShapes.transferSingleShapeTo(shape, finalShapes);
        }
    }

    /** The drawAllShapes method that draws all shapes on the canvas. This method also is responsible for drawing the measurement units and the grid
     *
     * @param customCanvas
     * @param type
     */
    public void drawAllShapes(CustomCanvas customCanvas, SingletonType type) {
        customCanvas.clear();
        for (Shape shape : getShapeContainer(type).getShapes()) {
            shape.draw(customCanvas);

            if (settingsSingleton.isUnitsVisible()) {
                shape.drawLength(customCanvas, settingsSingleton.getMeasurementUnit(), settingsSingleton.getMeasurementModifier());
            }
        }
    }

    /** The removeShape method that removes a shape from a given ShapeContainer object
     *
     * @param shape
     * @param type
     */
    public void removeShape(Shape shape, SingletonType type) {
        getShapeContainer(type).getShapes().remove(shape);
    }

    /** The deleteShape method that deletes a shape from a given type
     *
     * @param shape
     * @param type
     */
    public void deleteShape(Shape shape, SingletonType type) {
        shape.delete(getShapeContainer(type));
        this.setHoveredShape(null);
        this.setSelectedShape(null);
        this.setLastPoint(null);
    }

    /** The getShapes method that returns a list of shapes based on the given SingletonType
     *
     * @param type
     * @return List<Shape>
     */
    public List<Shape> getShapes(SingletonType type) {
        return getShapeContainer(type).getShapes();
    }

    /** The createRelativePoint method that creates a point based on the relative x and y coordinates
     *
     * @param x
     * @param y
     * @return Point
     */
    public Point createRelativePoint(double x, double y) {
        return createRelativePoint(x, y, null);
    }

    /** The createRelativePoint method that creates a point based on the relative x and y coordinates and a given SingletonType to save the shapes into the correct ShapeContainer object
     *
     * @param x
     * @param y
     * @param singletonType
     * @return Point
     */
    public Point createRelativePoint(double x, double y, SingletonType singletonType) {
        Point point = new Point(canvasMath.relativeXtoAbsoluteX(x), canvasMath.relativeYtoAbsoluteY(y));
        if (singletonType != null) getShapeContainer(singletonType).addShape(point);
        return point;
    }

    /** The createAbsolutePoint method that creates a point based on the absolute x and y coordinates
     *
     * @param x
     * @param y
     * @return Point
     */
    public Point createAbsolutePoint(double x, double y) {
        return createAbsolutePoint(x, y, null);
    }

    /** The createAbsolutePoint method that creates a point based on the absolute x and y coordinates and a given SingletonType to save the shapes into the correct ShapeContainer object
     *
     * @param x
     * @param y
     * @param singletonType
     * @return point
     */
    public Point createAbsolutePoint(double x, double y, SingletonType singletonType) {
        Point point = new Point(x, y);
        if (singletonType != null) getShapeContainer(singletonType).addShape(point);
        return point;
    }

    /** The saveSettings method that creates a new Settings object if a previous one is not found, and updates the existing settings if they are found in the database
     *
     */
    public void saveSettings() {
        if(settingsDao.find(1) == null) {
            Settings settings = new Settings(settingsSingleton.isGridEnabled(),
                    settingsSingleton.getGridHeight(), settingsSingleton.getGridWidth(), settingsSingleton.getGridSize(),
                    settingsSingleton.getLocale().getLanguage(), settingsSingleton.getMeasurementUnit());

            settingsDao.persist(settings);
            System.out.println("Settings saved!");
        } else {
            Settings settings = SettingsSingleton.getInstance().getSettings();
            settingsDao.find(1).setSettings(settings);
            settingsDao.update(settingsDao.find(1));
        }
    }

    /** The loadSettings method that loads the settings from the database
     *
     */
    public void loadSettings() {
        Settings settings = settingsDao.find(1);
        if(settings != null) {
            settingsSingleton.setSettings(settings);
        }
        else {
            settingsDao.persist(settingsSingleton.getSettings());
        }
    }

    /** The getCanvasMath method that returns the CanvasMath object
     *
     * @return CanvasMath
     */
    public CanvasMath getCanvasMath() {
        return canvasMath;
    }
    /** The addCustomShape method that adds a custom shape to the list of custom shapes*/
    public void addCustomShape(Shape newShape) {
        customShapes.add(newShape);
        System.out.println("Added line to custom shapes!");
        System.out.println("X: " + newShape.getPoints().get(0).getX() + " Y: " + newShape.getPoints().get(0).getY());
        System.out.println("X: " + newShape.getPoints().get(1).getX() + " Y: " + newShape.getPoints().get(1).getY());
    }

    /** The checkIfConnected method that checks if the custom shapes are connected
     *
     * @param newShape
     */
    public void checkIfConnected(Shape newShape) {
        if (customShapes.isEmpty()) {
            System.out.println("Custom shapes is empty");
            return;
        }
        List<Point> points = new ArrayList<>();
        for (Shape shape : customShapes) {
            for (Point point : shape.getPoints()) {
                if (!points.contains(point)) points.add(point);
            }
        }
        Point customPointB = newShape.getPoints().get(1);
        for (Shape shape : customShapes) {
            Point pointA = shape.getPoints().get(0);
            if (pointA.equals(customPointB)) {
                new CustomShape(customShapes, points);
                customShapes.clear();
                return;
            }
        }
    }

    /** The removeAllShapes method that removes all shapes from the canvas
     *
     */
    public void resetApplication() {
        finalShapes.clearShapes();
        previewShapes.clearShapes();
        gui.getCanvasContainer().updateAllCanvasLayers(this);
        areaShapes.clear();
        historyManager.reset();
    }

    /** The getShapeContainer method that returns a ShapeContainer object based on the given SingletonType
     *
     * @param type
     * @return ShapeContainer
     */
    public ShapeContainer getShapeContainer(SingletonType type) {
        if (type == null) return null;
        return switch (type) {
            case FINAL -> finalShapes;
            case PREVIEW -> previewShapes;
        };
    }

    /** The getHistoryManager method that returns the HistoryManager object
     *
     * @return
     */
    public HistoryManager getHistoryManager() {
        return historyManager;
    }
    /** The getCurrentShapeType method that returns the current shape type*/
    public ShapeType getCurrentShapeType() {
        return currentShape;
    }
    /** The setCurrentShape method that sets the current shape type*/
    public void setCurrentShape(ShapeType shape) {
        currentShape = shape;
    }
    /** The getCurrentMode method that returns the current mode type*/
    public ModeType getCurrentMode() {
        return currentMode;
    }
    /** The setCurrentMode method that sets the current mode type*/
    public void setCurrentMode(ModeType currentMode) {
//        String noColor = "#000000";
//        switch (currentMode) {
//            case DRAW -> {
//                this.selectedColor = "#00d415";
//                this.hoverColor = "#00d415";
//            }
//            case SELECT, ROTATE -> {
//                this.selectedColor = "#036ffc";
//                this.hoverColor = "#78b0fa";
//            }
//            case DELETE -> {
//                this.selectedColor = noColor;
//                this.hoverColor = "#ff0000";
//            }
//            case AREA -> {
//                this.selectedColor = "#4269f54a";
//                this.hoverColor = "#78b0fa";
//            }
//            default -> {
//                this.selectedColor = noColor;
//                this.hoverColor = noColor;
//            }
//        }
        this.currentMode = currentMode;
        CanvasColors.updateColorsByMode(currentMode);
    }

    /** The updateToolbarLocalization method that updates the localization of all the toolbar texts
     *
     */
    public void updateToolbarLocalization() {
        gui.updateToolbarLocalization();
    }
    /** The setLastPoint method that sets the last point to the given point*/
    public void setLastPoint(Point lastPoint) {
        this.lastPoint = lastPoint;
    }
    /** The getLastPoint method that returns the last point*/
    public Point getLastPoint() {
        return lastPoint;
    }
    /** The getHoveredPoint method that returns the hovered point*/
    public Point getHoveredPoint() {
        return hoveredPoint;
    }
    /** The setHoveredPoint method that sets the hovered point to the given point*/
    public void setHoveredPoint(Point hoveredPoint) {
        this.hoveredPoint = hoveredPoint;
    }
    /** The getSelectedShape method that returns the selected shape*/
    public Shape getSelectedShape() {
        return selectedShape;
    }
    /** The setSelectedShape method that sets the selected shape to the given shape*/
    public void setSelectedShape(Shape selectedShape) {
        if(this.selectedShape != null) this.selectedShape.removeShapeDataType(ShapeDataType.SELECTED);
        if(selectedShape != null) selectedShape.addShapeDataType(ShapeDataType.SELECTED);

        this.selectedShape = selectedShape;
    }
    /** The getHoveredShape method that returns the hovered shape*/
    public Shape getHoveredShape() {
        return hoveredShape;
    }
    /** The setHoveredShape method that sets the hovered shape to the given shape*/
    public void setHoveredShape(Shape hoveredShape) {
        if (this.hoveredShape != null) this.hoveredShape.removeShapeDataType(ShapeDataType.HOVER);
        if(hoveredShape != null) hoveredShape.addShapeDataType(ShapeDataType.HOVER);
        this.hoveredShape = hoveredShape;
    }
    /** The getMouseX method that returns the mouse x coordinate*/
    public double getMouseX() {
        return mouseX;
    }
    /** The getMouseY method that returns the mouse y coordinate*/
    public double getMouseY() {
        return mouseY;
    }
    /** The setMousePosition method that sets the mouse x and y coordinates*/
    public void setMousePosition(double mouseX, double mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }
    /** The isCtrlDown method that returns a boolean value of whether the control key is pressed*/
    public boolean isCtrlDown() {
        return ctrlIsDown;
    }
    /** The setCtrlDown method that sets the control key to the given boolean value*/
    public void setCtrlDown(boolean shiftIsDown) {
        this.ctrlIsDown = shiftIsDown;
    }
    /** The getHoverColor method that returns the hover color*/
    public String getHoverColor() {
        return hoverColor;
    }
    /** The getSelectedColor method that returns the selected color*/
    public String getSelectedColor() {
        return selectedColor;
    }

    /** The setDimensionLine method that sets the dimension line to the given shape and distance
     *
     * @param shape
     * @param distance
     * @return Dimension
     */
    public Dimension setDimensionLine(Shape shape, double distance) {
        Dimension dimension = new Dimension();
        dimension.setDistance(distance);
        dimension.setShape(shape);
        dimension.resize();
        if(shape.getType() == ShapeType.LINE) {
            ((Line) shape).addDimension(dimension);
        }
        return dimension;
    }

    /**
     * The getAreaShapes method that returns the list of area shapes
     * @return areaShapes
     */
    public List<Point> getAreaShapes() {
        return areaShapes;
    }
}
