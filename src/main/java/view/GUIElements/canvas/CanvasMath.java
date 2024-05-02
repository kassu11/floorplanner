package view.GUIElements.canvas;
/**
 * Class for handling canvas math
 */
public class CanvasMath {
    /**
     * Canvas container
     */
    private CanvasContainer canvasContainer;
    /**
     * Constructor for the canvas math
     * @param canvasContainer canvas container
     */
    public CanvasMath(CanvasContainer canvasContainer) {
        this.canvasContainer = canvasContainer;
    }
    /**
     * Converts the relative X coordinate to an absolute X coordinate
     * @param relativeY relative X coordinate
     * @return absolute X coordinate
     */
    public double relativeXtoAbsoluteX(double relativeX) {
        return (canvasContainer.getX() + relativeX) / canvasContainer.getZoom();
    }
    /**
     * Converts the relative Y coordinate to an absolute Y coordinate
     * @param relativeY relative Y coordinate
     * @return absolute Y coordinate
     */
    public double relativeYtoAbsoluteY(double relativeY) {
        return (canvasContainer.getY() + relativeY) / canvasContainer.getZoom();
    }
    /**
     * Calculates the relative distance based on the zoom level
     * @param distance distance
     * @return relative distance
     */
    public double relativeDistance(double distance) {
        return distance / canvasContainer.getZoom();
    }
}
