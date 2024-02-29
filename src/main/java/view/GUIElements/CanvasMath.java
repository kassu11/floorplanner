package view.GUIElements;

public class CanvasMath {
    public static double canvasXtoShapeX(double x, double canvasWidth, double shapeWidth) {
        return x * shapeWidth / canvasWidth;
    }
}
