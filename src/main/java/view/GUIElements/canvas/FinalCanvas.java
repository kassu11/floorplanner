package view.GUIElements.canvas;

import model.shapes.Shape;
import view.types.ShapeDataType;

public class FinalCanvas extends CustomCanvas {
    public FinalCanvas(double width, double height) {
        super(width, height);
        setLineWidth(4);
    }

    @Override
    public void updateCanvasColors(Shape shape) {
        if (shape == null) return;
        if (shape.containsShapeDataType(ShapeDataType.NORMAL)) {
            setStrokeColor(CanvasColors.FINAL_NORMAL);
            setFillColor(CanvasColors.FINAL_NORMAL);
        } else if (shape.containsShapeDataType(ShapeDataType.AREA)) {
            setStrokeColor(CanvasColors.YELLOW);
            setFillColor(CanvasColors.YELLOW);
        } else if (shape.containsShapeDataType(ShapeDataType.HOVER)) {
            setStrokeColor(CanvasColors.HOVER);
            setFillColor(CanvasColors.HOVER);
        } else if (shape.containsShapeDataType(ShapeDataType.SELECTED)) {
            setStrokeColor(CanvasColors.SELECTED);
            setFillColor(CanvasColors.SELECTED);
        }
    }
}
