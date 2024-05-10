package view.GUIElements.toolbars;

import controller.Controller;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.SVGPath;
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
        addButton(createIconButton(SvgConfig.CURSOR.getSvgName(), settings.getLocalizationString("select")), "select");
        getItems().add(new javafx.scene.control.Separator());       // Drawing modes start here
        addButton(createIconButton(SvgConfig.LINE.getSvgName(), settings.getLocalizationString("line")), "line");
        addButton(createIconButton(SvgConfig.RECTANGLE.getSvgName(), settings.getLocalizationString("rectangle")), "rectangle");
        addButton(createIconButton(SvgConfig.MULTILINE.getSvgName(), settings.getLocalizationString("multiline")), "multiline");
        addButton(createIconButton(SvgConfig.DOOR.getSvgName(), settings.getLocalizationString("door")), "door");
        getItems().add(new javafx.scene.control.Separator());       // Drawing modes end here
        addButton(createIconButton(SvgConfig.ERASER.getSvgName(), settings.getLocalizationString("delete")), "delete");
        addButton(createIconButton(SvgConfig.ROTATE.getSvgName(), settings.getLocalizationString("rotate")), "rotate");
        addButton(createIconButton(SvgConfig.AREA.getSvgName(), settings.getLocalizationString("area")), "area");
        getItems().add(new javafx.scene.control.Separator());
        addButton(createIconButton(SvgConfig.CLEAR.getSvgName(), settings.getLocalizationString("reset")), "reset");

        // Set up the mode menu
//        this.lineMode = new CustomMenuItem(settings.getLocalizationString("line"), ShapeType.LINE);
//        this.rectangleMode = new CustomMenuItem(settings.getLocalizationString("rectangle"), ShapeType.RECTANGLE);
//        this.circleMode = new CustomMenuItem(settings.getLocalizationString("circle"), ShapeType.CIRCLE);
//        this.multilineMode = new CustomMenuItem(settings.getLocalizationString("multiline"), ShapeType.MULTILINE);
//        this.doorMode = new CustomMenuItem(settings.getLocalizationString("door"), ShapeType.DOOR);
//        this.modeMenu.getItems().addAll(rectangleMode, circleMode, lineMode, multilineMode, doorMode);
    }
    /**
     * Changes the draw mode
     * @param mode mode
     */
    public void changeMode(ModeType mode) {
        setCursorCoordinates();
        controller.setCurrentMode(mode);
        modeMenu.hide();
//        if (mode == ModeType.DRAW) {
//            modeMenu.setOnAction(event -> controller.setCurrentShape(((CustomMenuItem) event.getTarget()).getShapeType()));
//            modeMenu.show(this, getCursorX() + this.getWidth(), getCursorY() + getButtons().get("mode").getHeight());
//        }
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
            if(!getButtons().get(key).getText().isEmpty()) {
                getButtons().get(key).setText(settings.getLocalizationString(key));
            } else {
                getButtons().get(key).setTooltip(new Tooltip(settings.getLocalizationString(key)));
            }
        }
    }

    public static Button createIconButton(String svg, String tooltip) {
        SVGPath path = new SVGPath();
        path.setContent(svg);
        Bounds bounds = path.getBoundsInLocal();

        double scaleFactor = 24 / Math.max(bounds.getWidth(), bounds.getHeight());
        path.setScaleX(scaleFactor);
        path.setScaleY(scaleFactor);
        path.getStyleClass().clear();
        path.getStyleClass().add("button-icon");

        Button button = new Button();
        button.setPickOnBounds(true);
        button.setTooltip(new Tooltip(tooltip));
        button.setGraphic(path);
        button.setAlignment(Pos.CENTER);
        button.getStyleClass().clear();
        button.getStyleClass().add("icon-button");

        return button;
    }

}
