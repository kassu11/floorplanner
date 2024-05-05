package view.GUIElements.canvas;

import model.shapes.Shape;

public class FinalCanvas extends CustomCanvas {
    public FinalCanvas(double width, double height) {
        super(width, height);
        setLineWidth(4);
    }

    @Override
    public void updateCanvasColors(Shape shape) {
        switch (shape.getShapeDataType()) {
            case NORMAL, AREA -> {
                setStrokeColor(CanvasColors.FINAL_NORMAL);
                setFillColor(CanvasColors.FINAL_NORMAL);
            }
            case HOVER -> {
                setStrokeColor(CanvasColors.HOVER);
                setFillColor(CanvasColors.HOVER);
            }
            case SELECTED -> {
                setStrokeColor(CanvasColors.SELECTED);
                setFillColor(CanvasColors.SELECTED);
            }
            case SELECTED_HOVER -> {
                setStrokeColor(CanvasColors.SELECTED_HOVER);
                setFillColor(CanvasColors.SELECTED_HOVER);
            }
        }
    }
}
