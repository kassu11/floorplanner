package view.GUIElements;

import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.stage.Stage;
import view.ModeType;
import view.SettingsSingleton;
import view.ShapeType;

import java.util.HashMap;
import java.util.Objects;

public class DrawingToolbar extends ToolBar {
    private final ContextMenu modeMenu = new ContextMenu();
    private double cursorX, cursorY;
    private CustomMenuItem lineMode, rectangleMode, circleMode, multilineMode;
    private Stage stage;
    private HashMap<String, Button> buttons = new HashMap<>();

    public DrawingToolbar(Stage stage) {
        super();
        this.setOrientation(Orientation.VERTICAL);
        addButton(new Button("Select"));
        addButton(new Button("Mode"));
        addButton(new Button("Delete"));
        this.stage = stage;

        //Set up the mode menu
        this.lineMode = new CustomMenuItem("Line", ShapeType.LINE);
        this.rectangleMode = new CustomMenuItem("Rectangle", ShapeType.RECTANGLE);
        this.circleMode = new CustomMenuItem("Circle", ShapeType.CIRCLE);
        this.multilineMode = new CustomMenuItem("Multiline", ShapeType.MULTILINE);
        this.modeMenu.getItems().addAll(rectangleMode, circleMode, lineMode, multilineMode);
    }

    public void addButton(Button button) {
        this.getItems().add(button);
        this.getItems().add(new Separator());
        buttons.put(button.getText(), button);
    }

    public void setCursorCoordinates() {
        this.cursorX = stage.getX() + this.getLayoutX();
        this.cursorY = stage.getY() + this.getLayoutY();
    }

    public void changeMode(ModeType mode) {
        setCursorCoordinates();
        SettingsSingleton.setCurrentMode(mode);
        modeMenu.hide();
        if (mode == ModeType.DRAW) {
            modeMenu.setOnAction(event -> {
                SettingsSingleton.setCurrentShape(((CustomMenuItem) event.getTarget()).getShapeType());
                System.out.println(((CustomMenuItem) event.getTarget()).getShapeType());
            });
            modeMenu.show(this, cursorX + this.getWidth(), cursorY + buttons.get("Mode").getHeight());
        }
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
