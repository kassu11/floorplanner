package view.GUIElements.canvas;

import model.shapes.Shape;

public class PreviewCanvas extends CustomCanvas {
    public PreviewCanvas(double width, double height) {
        super(width, height);
        setLineWidth(4);
    }

    @Override
    public void updateCanvasColors(Shape shape) {
        setStrokeColor(CanvasColors.PREVIEW_NORMAL);
        setFillColor(CanvasColors.PREVIEW_NORMAL);
    }
}
