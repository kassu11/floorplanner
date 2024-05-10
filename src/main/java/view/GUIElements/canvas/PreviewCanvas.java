package view.GUIElements.canvas;

import model.shapes.Shape;
/**
 * Preview canvas class where the preview of the shape is displayed
 */
public class PreviewCanvas extends CustomCanvas {
    /**
     * Constructor for the preview canvas
     * @param width width
     * @param height height
     */
    public PreviewCanvas(double width, double height) {
        super(width, height);
        setLineWidth(5);
    }
    /**
     * Updates the canvas colors
     * @param shape shape
     */

    @Override
    public void updateCanvasColors(Shape shape) {
        setStrokeColor(CanvasColors.PREVIEW_NORMAL);
        setFillColor(CanvasColors.PREVIEW_NORMAL);
    }
}
