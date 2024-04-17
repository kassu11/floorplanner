package model;

import controller.Controller;
import model.shapes.Point;
import model.shapes.Shape;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.GUI;
import view.events.DrawUtilities;
import view.types.ShapeType;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileManagerTest {
    private static Controller controller;
    private static FileManager fileManager;

    @BeforeAll
    static void setUp() {
        GUI gui = new GUI();
        controller = new Controller(gui);
        fileManager = FileManager.getInstance();

        DrawUtilities.addShapesFirstPoint(controller, -10, -10);
        DrawUtilities.addShapesLastPoint(controller, -20, -20, ShapeType.MULTILINE);
        DrawUtilities.addShapesLastPoint(controller, -30, -30, ShapeType.MULTILINE);
        DrawUtilities.addShapesLastPoint(controller, -40, -40, ShapeType.MULTILINE);

        File testFile = new File("./src/main/resources/testFiles.ser");
        fileManager.setCurrentFile(testFile);
        fileManager.exportFloorPlan();
    }

    @BeforeEach
    void reset() {
        controller.getShapeContainer(Controller.SingletonType.FINAL).clearShapes();
        controller.getShapeContainer(Controller.SingletonType.PREVIEW).clearShapes();
        controller.getHistoryManager().reset();
    }

    @Test
    void createNewFloorplan() {
        DrawUtilities.addShapesFirstPoint(controller, 100, 100);
        Point point = DrawUtilities.addShapesLastPoint(controller, 300, 100, ShapeType.MULTILINE).getPoints().get(0);
        DrawUtilities.addShapesLastPoint(controller, 300, 300, ShapeType.MULTILINE);
        DrawUtilities.addShapesLastPoint(controller, 100, 300, ShapeType.MULTILINE);
        controller.setHoveredPoint(point);
        DrawUtilities.addShapesLastPoint(controller, 100, 100, ShapeType.MULTILINE);

        assertEquals(8, controller.getShapes(Controller.SingletonType.FINAL).size());

        File testFile = new File("./src/main/resources/testFiles2.ser");
        fileManager.setCurrentFile(testFile);
        fileManager.exportFloorPlan();

        reset();
        fileManager.importFloorPlan();

        assertEquals(8, controller.getShapes(Controller.SingletonType.FINAL).size());
    }

    @Test
    void loadFromSave() {
        File testFile = new File("./src/main/resources/testFiles.ser");
        fileManager.setCurrentFile(testFile);
        fileManager.importFloorPlan();
        assertEquals(7, controller.getShapes(Controller.SingletonType.FINAL).size());
    }

    @Test
    void checkFileFormat() {
        assertTrue(fileManager.checkFileFormat("testFiles.ser"));
        assertFalse(fileManager.checkFileFormat("testFiles.txt"));
    }

    @Test
    void addFileFormat() {
        assertEquals("testFiles.ser", fileManager.addFileFormat("testFiles"));
        assertEquals("testFiles.se.ser", fileManager.addFileFormat("testFiles.se"));
        assertEquals("testFiles.ser", fileManager.addFileFormat("testFiles.ser"));
    }
}