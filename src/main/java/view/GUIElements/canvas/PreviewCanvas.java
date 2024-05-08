package view.GUIElements.canvas;

import model.shapes.Shape;
import view.types.ShapeDataType;

public class PreviewCanvas extends CustomCanvas {
    public PreviewCanvas(double width, double height) {
        super(width, height);
        setLineWidth(5);
    }

    @Override
    public void updateCanvasColors(Shape shape) {
        setStrokeColor(CanvasColors.PREVIEW_NORMAL);
        setFillColor(CanvasColors.PREVIEW_NORMAL);
    }
}
