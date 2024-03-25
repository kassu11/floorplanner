package view.GUIElements.toolbars;

import controller.Controller;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.stage.Stage;
import view.SettingsSingleton;
import view.types.ModeType;
import view.types.ShapeType;

public class DrawingToolbar extends CustomToolbar {
    private final ContextMenu modeMenu = new ContextMenu();
    private CustomMenuItem lineMode, rectangleMode, circleMode, multilineMode;
    private Controller controller;
    private SettingsSingleton settings = SettingsSingleton.getInstance();

    public DrawingToolbar(Controller controller, Stage stage) {
        super(stage);
        this.controller = controller;
        this.setOrientation(Orientation.VERTICAL);
        addButton(new Button(settings.getLocalizationString("select")), "select");
        addButton(new Button(settings.getLocalizationString("mode")), "mode");
        addButton(new Button(settings.getLocalizationString("delete")), "delete");
        addButton(new Button(settings.getLocalizationString("rotate")), "rotate");
        addButton(new Button(settings.getLocalizationString("reset")), "reset");

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
            modeMenu.show(this, getCursorX() + this.getWidth(), getCursorY() + getButtons().get("mode").getHeight());
        }
    }

    public void updateLocalization(){
        for(String key : getButtons().keySet()){
            getButtons().get(key).setText(settings.getLocalizationString(key));
        }
        lineMode.setText(settings.getLocalizationString("line"));
        rectangleMode.setText(settings.getLocalizationString("rectangle"));
        circleMode.setText(settings.getLocalizationString("circle"));
        multilineMode.setText(settings.getLocalizationString("multiline"));
    }

}
