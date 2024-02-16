package view.GUIElements;

import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.stage.Stage;
import view.ShapeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class DrawingToolbar extends ToolBar {
    private final ContextMenu modeMenu = new ContextMenu();
    private double cursorX, cursorY;
    private Stage stage;
    private HashMap<String, Button> buttons = new HashMap<>();

    public DrawingToolbar(Stage stage) {
        super();
        this.setOrientation(Orientation.VERTICAL);
        addButton(new Button("Select"));
        addButton(new Button("Mode"));
        addButton(new Button("Delete"));
        this.stage = stage;
    }

    public void addButton(Button button) {
        this.getItems().add(button);
        this.getItems().add(new Separator());
        buttons.put(button.getText(),button);
    }

    public void setCursorCoordinates(){
        this.cursorX = stage.getX() + this.getLayoutX();
        this.cursorY = stage.getY() + this.getLayoutY();
    }

    public ShapeType changeMode(ShapeType currentMode) {
        setCursorCoordinates();
        AtomicReference<ShapeType> newMode = new AtomicReference<>(currentMode);
        CustomMenuItem lineMode = new CustomMenuItem("Line", ShapeType.LINE);
        CustomMenuItem rectangleMode = new CustomMenuItem("Rectangle", ShapeType.RECTANGLE);
        CustomMenuItem circleMode = new CustomMenuItem("Circle", ShapeType.CIRCLE);

        modeMenu.setOnAction(event -> {
            newMode.set(((CustomMenuItem) event.getTarget()).getShapeType());
            System.out.println(((CustomMenuItem) event.getTarget()).getShapeType());
        });

        switch(currentMode) {
            case LINE -> modeMenu.getItems().addAll(rectangleMode, circleMode);
            case RECTANGLE -> modeMenu.getItems().addAll(lineMode, circleMode);
            case CIRCLE -> modeMenu.getItems().addAll(lineMode, rectangleMode);
        }

        modeMenu.show(this, cursorX + this.getWidth(), cursorY + buttons.get("Mode").getHeight());
        return newMode.get();
    }


    public void setCursorX(double cursorX) {
        this.cursorX = cursorX;
    }

    public void setCursorY(double cursorY) {
        this.cursorY = cursorY;
    }

    public HashMap<String, Button> getButtons() {
        return buttons;
    }
}
