package view.GUIElements.toolbars;

import controller.Controller;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.stage.Stage;
import view.SettingsSingleton;
import view.types.ModeType;
import view.types.ShapeType;
/**
 * Drawing toolbar class
 */
public class DrawingToolbar extends CustomToolbar {
    /**
     * Mode menu
     */
    private final ContextMenu modeMenu = new ContextMenu();
    /**
     * Line mode
     * Rectangle mode
     * Circle mode
     * Multiline mode
     */
    private CustomMenuItem lineMode, rectangleMode, circleMode, multilineMode, doorMode;
    /**
     * Controller
     */
    private Controller controller;
    /**
     * Settings singleton
     */
    private SettingsSingleton settings = SettingsSingleton.getInstance();
    /**
     * Constructor for the drawing toolbar
     * @param controller controller
     * @param stage stage
     */
    public DrawingToolbar(Controller controller, Stage stage) {
        super(stage);
        this.controller = controller;
        this.setOrientation(Orientation.VERTICAL);
        addButton(new Button(settings.getLocalizationString("select")), "select");
        addButton(new Button(settings.getLocalizationString("mode")), "mode");
        addButton(new Button(settings.getLocalizationString("delete")), "delete");
        addButton(new Button(settings.getLocalizationString("rotate")), "rotate");
        addButton(new Button(settings.getLocalizationString("reset")), "reset");
        addButton(new Button(settings.getLocalizationString("area")), "area");

        // Set up the mode menu
        this.lineMode = new CustomMenuItem(settings.getLocalizationString("line"), ShapeType.LINE);
        this.rectangleMode = new CustomMenuItem(settings.getLocalizationString("rectangle"), ShapeType.RECTANGLE);
        this.circleMode = new CustomMenuItem(settings.getLocalizationString("circle"), ShapeType.CIRCLE);
        this.multilineMode = new CustomMenuItem(settings.getLocalizationString("multiline"), ShapeType.MULTILINE);
        this.doorMode = new CustomMenuItem(settings.getLocalizationString("door"), ShapeType.DOOR);
        this.modeMenu.getItems().addAll(rectangleMode, circleMode, lineMode, multilineMode, doorMode);
    }
    /**
     * Changes the draw mode
     * @param mode mode
     */
    public void changeMode(ModeType mode) {
        setCursorCoordinates();
        controller.setCurrentMode(mode);
        modeMenu.hide();
        if (mode == ModeType.DRAW) {
            modeMenu.setOnAction(event -> controller.setCurrentShape(((CustomMenuItem) event.getTarget()).getShapeType()));
            modeMenu.show(this, getCursorX() + this.getWidth(), getCursorY() + getButtons().get("mode").getHeight());
        }
        if (mode != ModeType.AREA) {
            controller.getAreaShapes().forEach(shape -> controller.removeShape(shape, Controller.SingletonType.FINAL));
            controller.getAreaShapes().clear();
        }

    }
    /**
     * Updates the localization of all the text in the toolbar
     */
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
