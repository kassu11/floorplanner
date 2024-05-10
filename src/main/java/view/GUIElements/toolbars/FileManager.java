package view.GUIElements.toolbars;

import controller.Controller;
import javafx.scene.control.ContextMenu;
import view.GUIElements.canvas.CanvasContainer;
import view.SettingsSingleton;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class FileManager {
    /**
     * File manager
     */
    private SettingsSingleton settings = SettingsSingleton.getInstance();
    private model.FileManager fileManager = model.FileManager.getInstance();
    private ContextMenu selectionMenu = new ContextMenu();
    private CanvasContainer canvasContainer;
    private Controller controller;
    private double x, y;
    /**
     * Shows the file window
     */
    public FileManager(CanvasContainer canvasContainer, Controller controller) {
        this.canvasContainer = canvasContainer;
        this.controller = controller;
        selectionMenu.getItems().add(new CustomMenuItem(settings.getLocalizationString("saveFloorplan")));
        selectionMenu.getItems().add(new CustomMenuItem(settings.getLocalizationString("loadFloorplan")));
    }
    /**
     * Shows the file window
     */
    public void showFile() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Filter all ser files", "ser");
        fileChooser.setDialogTitle("File Explorer");
        fileChooser.setFileFilter(filter);
        fileChooser.setCurrentDirectory(new File("./src/main/resources/"));

        selectionMenu.getItems().getFirst().setOnAction(e -> exportFloorPlan(fileChooser));
        selectionMenu.getItems().getLast().setOnAction(e -> importFloorPlan(fileChooser));
        selectionMenu.show(canvasContainer, x, y);

    }
    /**
     * Exports the floor plan
     * @param fileChooser file chooser
     */
    public void exportFloorPlan(JFileChooser fileChooser) {
        int response = fileChooser.showSaveDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            File selectedFile = new File(fileManager.addFileFormat(fileChooser.getSelectedFile().getAbsolutePath()));
            fileManager.setCurrentFile(selectedFile);
            fileManager.exportFloorPlan();
        }
    }
    /**
     * Imports the floor plan
     * @param fileChooser file chooser
     */
    public void importFloorPlan(JFileChooser fileChooser) {
        int response = fileChooser.showOpenDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            File selectedFile = new File(fileChooser.getSelectedFile().getAbsolutePath());
            if (fileManager.checkFileFormat(selectedFile.getName())) {
                fileManager.setCurrentFile(selectedFile);
                fileManager.importFloorPlan();
                canvasContainer.updateAllCanvasLayers(controller);
            }
        }
    }
    /**
     * Sets the cursor coordinates
     * @param x x
     * @param y y
     */
    public void setCursorCoordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

}
