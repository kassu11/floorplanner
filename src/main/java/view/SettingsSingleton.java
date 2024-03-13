package view;

import entity.Settings;

public class SettingsSingleton {
    private static boolean drawLengths = true;
    private static boolean isDrawGrid = true;
    private static double gridHeight = 750;
    private static double gridWidth = 750;
    private static int gridSize = 25;

    private SettingsSingleton() {
    }

    private static class SettingsSingletonHelper {
        private static final SettingsSingleton INSTANCE = new SettingsSingleton();
    }

    public static SettingsSingleton getInstance() {
        return SettingsSingletonHelper.INSTANCE;
    }
    public static boolean isDrawLengths() {
        return drawLengths;
    }

    public static void setDrawLengths(boolean drawLengths) {
        SettingsSingleton.drawLengths = drawLengths;
    }

    public static boolean isGridEnabled() {
        return isDrawGrid;
    }

    public static void setDrawGrid(boolean isDrawGrid) {
        SettingsSingleton.isDrawGrid = isDrawGrid;
    }

    public static double getGridHeight() {
        return gridHeight;
    }

    public static void setGridHeight(double gridHeight) {
        SettingsSingleton.gridHeight = gridHeight;
    }

    public static double getGridWidth() {
        return gridWidth;
    }

    public static void setGridWidth(double gridWidth) {
        SettingsSingleton.gridWidth = gridWidth;
    }

    public static int getGridSize() {
        return gridSize;
    }

    public static void setGridSize(int gridSize) {
        SettingsSingleton.gridSize = gridSize;
    }

    public void setSettings(Settings settings) {
        setDrawLengths(settings.isDrawLengths());
        setDrawGrid(settings.isDrawGrid());
        setGridHeight(settings.getGridHeight());
        setGridWidth(settings.getGridWidth());
        setGridSize(settings.getGridSize());
    }

    public Settings getSettings() {
        return new Settings(drawLengths, isDrawGrid, gridHeight, gridWidth, gridSize);
    }
}
