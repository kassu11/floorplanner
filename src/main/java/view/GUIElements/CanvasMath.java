package view.GUIElements;

public class CanvasMath {
    private CanvasContainer canvasContainer;

    public CanvasMath(CanvasContainer canvasContainer) {
        this.canvasContainer = canvasContainer;
    }

    public double relativeXtoAbsoluteX(double relativeX) {
        return canvasContainer.getX() + relativeX;
    }

    public double relativeYtoAbsoluteY(double relativeY) {
        return canvasContainer.getY() + relativeY;
    }
}
