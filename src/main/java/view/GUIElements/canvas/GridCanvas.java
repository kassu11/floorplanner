package view.GUIElements.canvas;

import javafx.scene.text.Font;
import javafx.scene.transform.Affine;
import model.shapes.Shape;
import view.SettingsSingleton;
public class GridCanvas extends CustomCanvas {
    private final SettingsSingleton settings = SettingsSingleton.getInstance();
    private final double minGapBetweenText = 75;
    private final double maxGapBetweenText = 250;
    private Font rulerFont = new Font("Arial", 11);
    private final Affine original = gc.getTransform();
    private final Affine rotate = new Affine();
    private final double rulerHeight = 15;
    private final double textPaddingBottom = 4;
    private final double textPaddingLeft = 3;
    private final double l1LineHeight = 10;
    private final double l2LineHeight = 3;
    private final double l3LineHeight = 6;
    public GridCanvas(double width, double height) {
        super(width, height);
        setLineWidth(1);
        gc.setStroke(CanvasColors.GRID_LINE);
        gc.setImageSmoothing(false);
    }

    public void drawGrid() {
        clear();
        gc.setFill(CanvasColors.GRID_BACKGROUND);
        double width = settings.getGridWidth();
        double height = settings.getGridHeight();
        fillRect(0, 0, width, height);
        if (settings.isGridEnabled()) {
            beginPath();
            int gridSize = settings.getGridSize();
            for (int i = 0; i < width; i += gridSize) {
                moveTo(i, 0);
                lineTo(i, height);
            }
            for (int i = 0; i < height; i += gridSize) {
                moveTo(0, i);
                lineTo(width, i);
            }
            stroke();
        }
    }

    @Override
    public void updateCanvasColors(Shape shape) {
        System.out.println("YOU SHOULD NOT UPDATE THE GRID CANVAS COLORS!");
    }
}
