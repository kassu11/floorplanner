package view.GUIElements.toolbars;

import controller.Controller;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.FileManager;
import view.GUIElements.ComboBoxItem;
import view.GUIElements.canvas.CanvasContainer;
import view.SettingsSingleton;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.File;
import java.util.List;

import static java.util.Arrays.asList;
import static javafx.geometry.Pos.CENTER;
/**
 * Options toolbar class
 */
public class OptionsToolbar extends CustomToolbar {
    /**
     * Controller
     */
    private final Controller controller;
    /**
     * Custom canvas container
     */
    private CanvasContainer canvasContainer;
    /**
     * Settings singleton
     */
    private SettingsSingleton settings = SettingsSingleton.getInstance();
    /**
     * File manager
     */
    private FileManager fileManager = FileManager.getInstance();
    /**
     * List of text fields
     */
    private List<TextFieldWithLabel> textFields;
    /**
     * Constructor for the options toolbar
     * @param stage stage
     * @param controller controller
     * @param canvasContainer custom canvas container
     */

    public OptionsToolbar(Stage stage, Controller controller, CanvasContainer canvasContainer) {
        super(stage);
        this.controller = controller;
        this.canvasContainer = canvasContainer;
        this.setOrientation(Orientation.HORIZONTAL);
        addButton(new Button(settings.getLocalizationString("file")), "file");
        addButton(new Button(settings.getLocalizationString("settings")), "settings");

        TextFieldWithLabel gridWidth = new TextFieldWithLabel("gridWidth", String.format("%.0f", settings.getGridWidth()));
        TextFieldWithLabel gridHeight = new TextFieldWithLabel("gridHeight", String.format("%.0f", settings.getGridHeight()));
        TextFieldWithLabel gridSize = new TextFieldWithLabel("gridSize", String.format("%d", settings.getGridSize()));

        textFields = asList(gridWidth, gridHeight, gridSize);

        for (TextFieldWithLabel textField : textFields) {
            textField.setMaxWidth(100);
            if (textFields.indexOf(textField) != 0) this.getItems().add(new Separator());
            this.getItems().add(textField);
        }

        this.getItems().add(new Separator());

        addButton(new Button(settings.getLocalizationString("gridSizeButton")), "gridSizeButton");

        getButton("gridSizeButton").setOnAction(e -> {
            settings.setGridWidth(Double.parseDouble(gridWidth.getTextField().getText()));
            settings.setGridHeight(Double.parseDouble(gridHeight.getTextField().getText()));
            settings.setGridSize(Integer.parseInt(gridSize.getTextField().getText()));
            canvasContainer.clear();
            canvasContainer.updateAllCanvasLayers(controller);
            System.out.println("Grid size set to " + settings.getGridWidth() + "x" + settings.getGridHeight());
        });

    }
    /**
     * Shows the file window
     */
    public void showFile() {
        Stage fileWindow = new Stage();

        fileWindow.setTitle("File");
        Button exportButton = new Button(settings.getLocalizationString("saveFloorplan"));
        Button importButton = new Button(settings.getLocalizationString("loadFloorplan"));

        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Filter all ser files", "ser");
        fileChooser.setDialogTitle("File Explorer");
        fileChooser.setFileFilter(filter);
        fileChooser.setCurrentDirectory(new File("./src/main/resources/"));

        exportButton.setOnAction(e -> {
            int response = fileChooser.showSaveDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                File selectedFile = new File(fileManager.addFileFormat(fileChooser.getSelectedFile().getAbsolutePath()));
                fileManager.setCurrentFile(selectedFile);
                fileManager.exportFloorPlan();
                fileWindow.close();
            }
        });

        importButton.setOnAction(e -> {
            int response = fileChooser.showOpenDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                File selectedFile = new File(fileChooser.getSelectedFile().getAbsolutePath());
                if (fileManager.checkFileFormat(selectedFile.getName())) {
                    fileManager.setCurrentFile(selectedFile);
                    fileManager.importFloorPlan();
                    fileWindow.close();
                    canvasContainer.updateAllCanvasLayers(controller);
                }
            }
        });

        Insets defaultInsets = new Insets(10, 10, 10, 10);

        VBox fileLayout = new VBox(exportButton, importButton);
        fileLayout.setPadding(defaultInsets);
        fileLayout.setSpacing(10);
        fileLayout.setAlignment(CENTER);

        Scene fileScene = new Scene(fileLayout, 200, 100);

        fileWindow.setScene(fileScene);
        fileWindow.show();
    }
    /**
     * Shows the settings window
     */
    public void showSettings() {
        Stage settingsWindow = new Stage();

        settingsWindow.setTitle(settings.getLocalizationString("settings"));
        CheckBox showAreas = new CheckBox(settings.getLocalizationString("showAreas"));
        Label shapeLabel = new Label(settings.getLocalizationString("shapeSettings"));

        ComboBox<ComboBoxItem> languageSettings = new ComboBox<>();
        ComboBoxItem defaultItem = new ComboBoxItem(settings.getLocale().getLanguage(), settings.getLocalizationString("languageName"));
        languageSettings.setValue(defaultItem);

        ComboBox<String> measurementSettings = new ComboBox<>();
        measurementSettings.setValue(settings.getMeasurementUnit());
        for(String measurement : settings.getMeasurementUnits().keySet()){
            measurementSettings.getItems().add(measurement);
        }

        Label otherSettingsLabel = new Label(settings.getLocalizationString("otherSettings"));
        CheckBox showGrid = new CheckBox(settings.getLocalizationString("showGrid"));
        showGrid.setSelected(settings.isGridEnabled());
        CheckBox showUnits = new CheckBox(settings.getLocalizationString("showUnits"));
        showUnits.setSelected(settings.isUnitsVisible());
        Button saveButton = new Button(settings.getLocalizationString("save"));

        saveButton.setOnAction(e -> {
            settings.setDrawGrid(showGrid.isSelected());
            settings.setUnitsVisible(showUnits.isSelected());
            settings.setLocaleWithLocaleLanguage((languageSettings.getValue().getKey()));
            settings.setUnitsVisible(showUnits.isSelected());
            settings.setMeasurementUnit(measurementSettings.getValue());
            canvasContainer.updateAllCanvasLayers(controller);
            controller.saveSettings();
            controller.updateToolbarLocalization();
            settingsWindow.close();
        });

        List<ComboBoxItem> comboItems = Arrays.stream(settings.getAllLocalization())
                .map(locale -> new ComboBoxItem(locale.getLocale().getLanguage(), settings.getLocalizationWithLocale(locale.getLocale()).getString("languageName"))).toList();
        languageSettings.getItems().addAll(comboItems);

        Insets defaultInsets = new Insets(10, 10, 10, 10);

        HBox languageSettingsLayout = new HBox(new Label(settings.getLocalizationString("language")), languageSettings);
        languageSettingsLayout.setSpacing(10);

        HBox measurementSettingsLayout = new HBox(new Label(settings.getLocalizationString("measurementUnit")), measurementSettings);
        measurementSettingsLayout.setSpacing(10);

        VBox shapeSettingsLayout = new VBox(shapeLabel , showAreas, showGrid, showUnits, saveButton);
        shapeSettingsLayout.setPadding(defaultInsets);
        shapeSettingsLayout.setSpacing(10);

        VBox otherSettingsLayout = new VBox(otherSettingsLabel, languageSettingsLayout, measurementSettingsLayout);
        otherSettingsLayout.setPadding(defaultInsets);
        otherSettingsLayout.setSpacing(10);

        HBox settingsLayout = new HBox(shapeSettingsLayout, otherSettingsLayout);
        settingsLayout.setPadding(defaultInsets);
        settingsLayout.setSpacing(30);

        Scene settingsScene = new Scene(settingsLayout, 400, 400);

        settingsWindow.setScene(settingsScene);
        settingsWindow.show();
    }

    /**
     * Updates the localization of all the text in the toolbar
     */
    public void updateLocalization() {
        for (String key : getButtons().keySet()) {
            getButtons().get(key).setText(settings.getLocalizationString(key));
        }
        for (TextFieldWithLabel textField : textFields) {
            textField.getLabel().setText(settings.getLocalizationString(textField.getKey()));
        }
    }
    /**
     * Updates the resolution
     */
    public void updateResolution(){
        for(TextFieldWithLabel textField : textFields){
            if(textField.getKey().equals("gridWidth")) textField.getTextField().setText(String.format("%.0f", settings.getGridWidth()));
            if(textField.getKey().equals("gridHeight")) textField.getTextField().setText(String.format("%.0f", settings.getGridHeight()));
        }
    }
}
