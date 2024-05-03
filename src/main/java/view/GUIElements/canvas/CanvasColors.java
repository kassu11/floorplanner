package view.GUIElements.canvas;

import controller.Controller;
import javafx.scene.paint.Paint;
import view.types.ModeType;

public final class CanvasColors {
    public static final Paint BLUE = Paint.valueOf("#4269f5d4");
    public static final Paint PURPLE = Paint.valueOf("#ad03fc");
    public static final Paint GREEN = Paint.valueOf("#00d415");
    public static final Paint BLACK = Paint.valueOf("#000000");
    public static final Paint YELLOW = Paint.valueOf("#fcba03");
    public static Paint SELECTED = BLUE;
    public static Paint HOVER = BLUE;
    public static Paint SELECTED_HOVER = PURPLE;
    public static Paint NORMAL = BLACK;
    private CanvasColors() {
    }

    public static void updateColorsByMode(ModeType mode) {
        switch (mode) {
            case SELECT -> {
                SELECTED = PURPLE;
                HOVER = PURPLE;
                SELECTED_HOVER = YELLOW;
                NORMAL = BLACK;
            }
            case DRAW -> {
                SELECTED = BLUE;
                HOVER = BLUE;
                SELECTED_HOVER = PURPLE;
                NORMAL = BLACK;
            }
            case DELETE -> {
                SELECTED = YELLOW;
                HOVER = YELLOW;
                SELECTED_HOVER = YELLOW;
                NORMAL = BLACK;
            }
            case ROTATE -> {
                SELECTED = YELLOW;
                HOVER = YELLOW;
                SELECTED_HOVER = YELLOW;
                NORMAL = BLACK;
            }
            case AREA -> {
                SELECTED = YELLOW;
                HOVER = YELLOW;
                SELECTED_HOVER = YELLOW;
                NORMAL = BLACK;
            }
        }
    }
}
