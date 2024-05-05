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
    public static Paint SELECTED = LIGHT_BLUE;
    public static Paint HOVER = LIGHT_BLUE;
    public static Paint SELECTED_HOVER = PURPLE;
    public static Paint FINAL_NORMAL = BLACK;
    public static Paint PREVIEW_NORMAL = BLACK;
    private CanvasColors() {
    }

    public static void updateColorsByMode(ModeType mode) {
        HOVER = DARK_LIGHT_BLUE;
        SELECTED = LIGHT_BLUE;
        SELECTED_HOVER = LIGHT_BLUE;
        FINAL_NORMAL = BLACK;
        PREVIEW_NORMAL = BLACK;
        switch (mode) {
            case SELECT -> {
                PREVIEW_NORMAL = PURPLE;
            }
            case DRAW -> {
                SELECTED = GREEN;
                HOVER = DARK_GREEN;
                SELECTED_HOVER = DARK_GREEN;
                FINAL_NORMAL = BLACK;
            }
            case DELETE -> {
                HOVER = RED;
                FINAL_NORMAL = DARK_RED;
            }
            case ROTATE -> {
                SELECTED = YELLOW;
                HOVER = YELLOW;
                SELECTED_HOVER = YELLOW;
                FINAL_NORMAL = BLACK;
            }
            case AREA -> {
                SELECTED = YELLOW;
                HOVER = YELLOW;
                SELECTED_HOVER = YELLOW;
                FINAL_NORMAL = BLACK;
            }
        }
    }
}
