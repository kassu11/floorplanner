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
import view.GUIElements.canvas.CustomCanvas;
import view.SettingsSingleton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.File;
import java.util.List;

import static java.util.Arrays.asList;
import static javafx.geometry.Pos.CENTER;

public class OptionsToolbar extends CustomToolbar {

    private final Controller controller;
    private CustomCanvas gc;
    private SettingsSingleton settings = SettingsSingleton.getInstance();
    private FileManager fileManager = FileManager.getInstance();
    private List<TextFieldWithLabel> textFields;

    public OptionsToolbar(Stage stage, Controller controller, CustomCanvas gc) {
        super(stage);
        this.controller = controller;
        this.gc = gc;
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
            gc.clear();
            controller.drawAllShapes(gc, Controller.SingletonType.FINAL);
            System.out.println("Grid size set to " + settings.getGridWidth() + "x" + settings.getGridHeight());
        });

    }

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
                    controller.drawAllShapes(gc, Controller.SingletonType.FINAL);
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

    public void showSettings() {
        Stage settingsWindow = new Stage();

        settingsWindow.setTitle(settings.getLocalizationString("settings"));
        CheckBox showLengths = new CheckBox(settings.getLocalizationString("showLengths"));
        showLengths.setSelected(settings.isDrawLengths());
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
            settings.setDrawLengths(showLengths.isSelected());
            settings.setDrawGrid(showGrid.isSelected());
            settings.setUnitsVisible(showUnits.isSelected());
            settings.setLocaleWithLocaleLanguage((languageSettings.getValue().getKey()));

            settings.setMeasurementUnit(measurementSettings.getValue());
            controller.drawAllShapes(gc, Controller.SingletonType.FINAL);
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

        VBox shapeSettingsLayout = new VBox(shapeLabel, showLengths, showAreas, showGrid, showUnits, saveButton);
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

    public void updateLocalization() {
        for (String key : getButtons().keySet()) {
            getButtons().get(key).setText(settings.getLocalizationString(key));
        }
        for (TextFieldWithLabel textField : textFields) {
            textField.getLabel().setText(settings.getLocalizationString(textField.getKey()));
        }
    }

    public void updateResolution(){
        for(TextFieldWithLabel textField : textFields){
            if(textField.getKey().equals("gridWidth")) textField.getTextField().setText(String.format("%.0f", settings.getGridWidth()));
            if(textField.getKey().equals("gridHeight")) textField.getTextField().setText(String.format("%.0f", settings.getGridHeight()));
        }
    }
}
