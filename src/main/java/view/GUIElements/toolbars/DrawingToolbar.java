package view.GUIElements.toolbars;

import controller.Controller;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.stage.Stage;
import view.types.ModeType;
import view.types.ShapeType;

public class DrawingToolbar extends CustomToolbar {
    private final ContextMenu modeMenu = new ContextMenu();
    private CustomMenuItem lineMode, rectangleMode, circleMode, multilineMode;
    private Controller controller;

    public DrawingToolbar(Controller controller, Stage stage) {
        super(stage);
        this.controller = controller;
        this.setOrientation(Orientation.VERTICAL);
        addButton(new Button("Select"));
        addButton(new Button("Mode"));
        addButton(new Button("Delete"));
        addButton(new Button("Rotate"));
        addButton(new Button("Dimension"));
        addButton(new Button("Reset"));

        // Set up the mode menu
        this.lineMode = new CustomMenuItem("Line", ShapeType.LINE);
        this.rectangleMode = new CustomMenuItem("Rectangle", ShapeType.RECTANGLE);
        this.circleMode = new CustomMenuItem("Circle", ShapeType.CIRCLE);
        this.multilineMode = new CustomMenuItem("Multiline", ShapeType.MULTILINE);
        this.modeMenu.getItems().addAll(rectangleMode, circleMode, lineMode, multilineMode);
    }

    public void changeMode(ModeType mode) {
        setCursorCoordinates();
        controller.setCurrentMode(mode);
        modeMenu.hide();
        if (mode == ModeType.DRAW) {
            modeMenu.setOnAction(event -> {
                controller.setCurrentShape(((CustomMenuItem) event.getTarget()).getShapeType());
                System.out.println(((CustomMenuItem) event.getTarget()).getShapeType());
            });
            modeMenu.show(this, getCursorX() + this.getWidth(), getCursorY() + getButtons().get("Mode").getHeight());
        }
    }

}
