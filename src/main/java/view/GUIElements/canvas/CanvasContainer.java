package view.GUIElements.canvas;

import controller.Controller;
import javafx.scene.layout.GridPane;
/**
 * Class for handling the canvas container
 */
public class CanvasContainer extends GridPane {
    /**
     * List of canvases
     */
    private final CustomCanvas[] layers = new CustomCanvas[4];
    /**
     * X coordinate
     * Y coordinate
     * Zoom level
     */
    private double x, y, zoom;
    /**
     * Constructor for the canvas container
     * @param width width of the canvas
     * @param height height of the canvas
     */
    public CanvasContainer(double width, double height) {
        super();
        x = 0;
        y = 0;
        zoom = 1;


        layers[0] = new RulerHandsCanvas(width, height);
        layers[1] = new GridCanvas(width, height);
        layers[2] = new FinalCanvas(width, height);
        layers[3] = new PreviewCanvas(width, height);

        for (CustomCanvas canvas : layers) add(canvas, 0, 0);
        setZoom(1);
    }

    public void updateAllCanvasLayers(Controller controller) {
        controller.drawAllShapes(layers[1], Controller.SingletonType.FINAL);
        controller.drawAllShapes(layers[2], Controller.SingletonType.PREVIEW);
        ((GridCanvas)layers[0]).drawGrid();
    }

    /**
     * Returns the layer at the index
     * @param index index
     * @return layer
     */
    public CustomCanvas getLayer(int index) {
        return layers[index];
    }
    /**
     * Resizes the canvas
     * @param width width
     * @param height height
     */
    public void resizeCanvas(double width, double height) {
        for (CustomCanvas layer : layers) {
            layer.resizeCanvas(width, height);
        }
    }
    /**
     * Clears the canvas
     */
    public void clear() {
        for (CustomCanvas layer : layers) {
            layer.clear();
        }
    }
    /**
     * Sets the X coordinate
     * @param x X coordinate
     */
    public void setX(double x) {
        this.x = x;
        for (CustomCanvas layer : layers) {
            layer.setX(x);
        }
    }
    /**
     * Sets the Y coordinate
     * @param y Y coordinate
     */
    public void setY(double y) {
        this.y = y;
        for (CustomCanvas layer : layers) {
            layer.setY(y);
        }
    }
    /**
     * Sets the zoom level
     * @param zoom zoom level
     */
    public void setZoom(double zoom) {
        this.zoom = zoom;
        for (CustomCanvas layer : layers) {
            layer.setZoom(zoom);
        }
    }
    /**
     * Returns the X coordinate
     * @return X coordinate
     */
    public double getX() {
        return x;
    }
    /**
     * Returns the Y coordinate
     * @return Y coordinate
     */
    public double getY() {
        return y;
    }
    /**
     * Returns the zoom level
     * @return zoom level
     */
    public double getZoom() {
        return zoom;
    }
}
