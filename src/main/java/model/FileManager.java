package model;

import model.shapeContainers.FinalShapesSingleton;
import model.shapes.Shape;

import java.io.*;
import java.util.List;

/**
 * Class for handling file management
 */

public class FileManager {

    /**
     * Current file to be handled
     */
    private File currentFile;

    /**
     * Final shapes singleton instance to be used for file management operations
     */
    FinalShapesSingleton finalShapesSingleton = FinalShapesSingleton.getInstance();

    /**
     * Constructor for the file manager
     */
    private FileManager() {
    }

    /**
     * Helper class for the file manager singleton
     */
    private static class FileManagerSingletonHelper {
        private static final FileManager INSTANCE = new FileManager();
    }

    /**
     * Returns the file manager instance
     * @return file manager instance
     */
    public static FileManager getInstance() {
        return FileManager.FileManagerSingletonHelper.INSTANCE;
    }

/**
     * Sets the current file
     * @param currentFile current file to be set
     */
    public void setCurrentFile(File currentFile) {
        if (currentFile == null) throw new NullPointerException("File is null");
        this.currentFile = currentFile;
    }

    /**
     * Checks the file format
     * @param file takes a file as a parameter
     * @return boolean value of the current file depends on last four characters ".ser"
     */
    public boolean checkFileFormat(String file) {
        String lastFour = file.substring(file.length() - 4);
        return lastFour.equals(".ser");
    }

    /**
     * Adds file format to the file
     * @param file takes a file as a parameter
     * @return file with ".ser" format
     */
    public String addFileFormat(String file) {
        if (checkFileFormat(file)) return file;
        return file + ".ser";
    }

    /**
     * Exports the floor plan
     */
    public void exportFloorPlan() {
        try (FileOutputStream fileOut = new FileOutputStream(currentFile);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)){
            objectOut.writeObject(finalShapesSingleton.getShapes());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Imports the floor plan
     */
    public void importFloorPlan() {
        try (FileInputStream fileIn = new FileInputStream(currentFile);
             ObjectInputStream objectIn = new ObjectInputStream(fileIn)){
            List<Shape> shapes = (List<Shape>) objectIn.readObject();
            finalShapesSingleton.clearShapes();
            finalShapesSingleton.addAllShapes(shapes);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
