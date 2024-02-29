package view.GUIElements;

public class CanvasMath {
    private CanvasContainer canvasContainer;

    public CanvasMath(CanvasContainer canvasContainer) {
        this.canvasContainer = canvasContainer;
    }

    public double relativeXtoAbsoluteX(double relativeX) {
        return (canvasContainer.getX() + relativeX) / canvasContainer.getZoom();
    }

    public double relativeYtoAbsoluteY(double relativeY) {
        return (canvasContainer.getY() + relativeY) / canvasContainer.getZoom();
    }

    public double zoomRelativeX(double relativeX) {
        return relativeX * canvasContainer.getZoom();
    }

    public double zoomRelativeY(double relativeY) {
        return relativeY * canvasContainer.getZoom();
    }
}
