package view.GUIElements.canvas;

import javafx.scene.paint.Paint;
import view.types.ModeType;

public final class CanvasColors {
    public static final Paint LIGHT_BLUE = Paint.valueOf("#38d1ff");
    public static final Paint DARK_LIGHT_BLUE = Paint.valueOf("#00b2e8");
    public static final Paint PURPLE = Paint.valueOf("#ad03fc");
    public static final Paint RED = Paint.valueOf("#ff474e");
    public static final Paint DARK_RED = Paint.valueOf("#260001");
    public static final Paint GREEN = Paint.valueOf("#21ff19");
    public static final Paint DARK_GREEN = Paint.valueOf("#07c400");
    public static final Paint BLACK = Paint.valueOf("#000000");
    public static final Paint YELLOW = Paint.valueOf("#fcba03");
    public static final Paint GRAY = Paint.valueOf("#c9c9c9");
    public static final Paint LIGHT_GRAY = Paint.valueOf("#353535");
    public static final Paint DARK_GRAY = Paint.valueOf("#1d1e1e");
    public static final Paint WHITE = Paint.valueOf("#ffffff");
    public static final Paint GRID_LINE = GRAY;
    public static final Paint RULER_TEXT = WHITE;
    public static final Paint RULER_CORNER = BLACK;
    public static final Paint RULER_OUTER = LIGHT_GRAY;
    public static final Paint RULER_INNER = BLACK;
    public static final Paint GRID_BACKGROUND = WHITE;
    public static final Paint RULER = RED;
    public static Paint SELECTED = LIGHT_BLUE;
    public static Paint HOVER = LIGHT_BLUE;
    public static Paint SELECTED_HOVER = PURPLE;
    public static Paint FINAL_NORMAL = BLACK;
    public static Paint PREVIEW_NORMAL = BLACK;
    private CanvasColors() {
    }

    public static void updateColorsByMode(ModeType mode) {
        SELECTED = SELECTED_HOVER = LIGHT_BLUE;
        FINAL_NORMAL = PREVIEW_NORMAL = HOVER = BLACK;
        switch (mode) {
            case SELECT -> HOVER = PREVIEW_NORMAL = LIGHT_BLUE;
            case DRAW -> PREVIEW_NORMAL = GREEN;
            case DELETE -> {
                HOVER = PREVIEW_NORMAL = RED;
                FINAL_NORMAL = DARK_RED;
            }
            case ROTATE -> {
                HOVER = SELECTED_HOVER = SELECTED = PREVIEW_NORMAL = YELLOW;
            }
            case AREA -> {
                SELECTED = RED;
                HOVER = PURPLE;
                SELECTED_HOVER = PURPLE;
                PREVIEW_NORMAL = PURPLE;
            }
        }
    }
}
