package controller;

import dao.SettingsDao;
import entity.Settings;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.GUI;
import view.SettingsSingleton;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ControllerDBTest {

    private static Controller controller;
    private static Settings originalSettings;
    private static SettingsDao settingsDao = new SettingsDao();

    @BeforeAll
    static void saveOrigSettings() {
        originalSettings = SettingsSingleton.getInstance().getSettings();
    }
    @BeforeEach
    void setUp() {
        SettingsSingleton.getInstance().setSettings(new Settings(true, true, 750, 750, 25, "en"));
        GUI gui = new GUI();
        controller = new Controller(gui);
    }

    @AfterAll
    static void restoreOrigSettings() {
        settingsDao.persist(originalSettings);
        SettingsSingleton.getInstance().setSettings(originalSettings);
    }
    @Test
    void saveSettings() {
        Settings settings = new Settings(true, false, 700, 700, 20, "fi");
        SettingsSingleton.getInstance().setSettings(settings);
        controller.saveSettings();
        controller.loadSettings();
        Settings singletonSettings = SettingsSingleton.getInstance().getSettings();

        assertEquals(settings.getGridHeight(), singletonSettings.getGridHeight(), "Should return the same settings");
        assertEquals(settings.getGridWidth(), singletonSettings.getGridWidth(), "Should return the same settings");
        assertEquals(settings.getGridSize(), singletonSettings.getGridSize(), "Should return the same settings");
        assertEquals(settings.isDrawGrid(), singletonSettings.isDrawGrid(), "Should return the same settings");
        assertEquals(settings.isDrawLengths(), singletonSettings.isDrawLengths(), "Should return the same settings");
        assertEquals(settings.getLocale(), singletonSettings.getLocale(), "Should return the same settings");

        Settings updatedSettings = new Settings(false, true, 500, 500, 10, "en");
        SettingsSingleton.getInstance().setSettings(updatedSettings);
        controller.saveSettings();
        controller.loadSettings();
        singletonSettings = SettingsSingleton.getInstance().getSettings();

        assertEquals(updatedSettings.getGridHeight(), singletonSettings.getGridHeight(), "Should return the same settings");
        assertEquals(updatedSettings.getGridWidth(), singletonSettings.getGridWidth(), "Should return the same settings");
        assertEquals(updatedSettings.getGridSize(), singletonSettings.getGridSize(), "Should return the same settings");
        assertEquals(updatedSettings.isDrawGrid(), singletonSettings.isDrawGrid(), "Should return the same settings");
        assertEquals(updatedSettings.isDrawLengths(), singletonSettings.isDrawLengths(), "Should return the same settings");
        assertEquals(updatedSettings.getLocale(), singletonSettings.getLocale(), "Should return the same settings");
    }

    @Test
    void loadSettings() {
        controller.loadSettings();
        Settings singletonSettings = SettingsSingleton.getInstance().getSettings();
        assertEquals(originalSettings.getGridHeight(), singletonSettings.getGridHeight(), "Should return the same original settings");
        assertEquals(originalSettings.getGridWidth(), singletonSettings.getGridWidth(), "Should return the same original settings");
        assertEquals(originalSettings.getGridSize(), singletonSettings.getGridSize(), "Should return the same original settings");
        assertEquals(originalSettings.isDrawGrid(), singletonSettings.isDrawGrid(), "Should return the same original settings");
        assertEquals(originalSettings.isDrawLengths(), singletonSettings.isDrawLengths(), "Should return the same original settings");
        assertEquals(originalSettings.getLocale(), singletonSettings.getLocale(), "Should return the same original settings");

        Settings settings = new Settings(true, false, 700, 700, 20, "fi");
        SettingsSingleton.getInstance().setSettings(settings);
        controller.saveSettings();
        controller.loadSettings();
        singletonSettings = SettingsSingleton.getInstance().getSettings();
        assertEquals(settings.getGridHeight(), singletonSettings.getGridHeight(), "Should return the same default settings");
        assertEquals(settings.getGridWidth(), singletonSettings.getGridWidth(), "Should return the same default settings");
        assertEquals(settings.getGridSize(), singletonSettings.getGridSize(), "Should return the same default settings");
        assertEquals(settings.isDrawGrid(), singletonSettings.isDrawGrid(), "Should return the same default settings");
        assertEquals(settings.isDrawLengths(), singletonSettings.isDrawLengths(), "Should return the same default settings");
        assertEquals(settings.getLocale(), singletonSettings.getLocale(), "Should return the same default settings");
    }
}
