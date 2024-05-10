package view.GUIElements.canvas;

import model.shapes.Shape;
import view.types.ShapeDataType;
/**
 * Final canvas class where all the drawn shapes are displayed
 */
public class FinalCanvas extends CustomCanvas {
    public FinalCanvas(double width, double height) {
        super(width, height);
        setLineWidth(5);
    }
    /**
     * Updates the canvas colors
     * @param shape shape
     */
    @Override
    public void updateCanvasColors(Shape shape) {
        if (shape == null) return;
        if (shape.containsShapeDataType(ShapeDataType.NORMAL)) {
            setStrokeColor(CanvasColors.FINAL_NORMAL);
            setFillColor(CanvasColors.FINAL_NORMAL);
        } else if (shape.containsShapeDataType(ShapeDataType.HOVER)) {
            System.out.println(shape);
            setStrokeColor(CanvasColors.HOVER);
            setFillColor(CanvasColors.HOVER);
        } else if (shape.containsShapeDataType(ShapeDataType.SELECTED)) {
            setStrokeColor(CanvasColors.SELECTED);
            setFillColor(CanvasColors.SELECTED);
        } else if (shape.containsShapeDataType(ShapeDataType.AREA)) {
            setStrokeColor(CanvasColors.YELLOW);
            setFillColor(CanvasColors.YELLOW);
        }
    }
}
