package view;

import entity.Settings;

import java.util.Locale;
import java.util.ResourceBundle;

public class SettingsSingleton {
    private static boolean drawLengths = true;
    private static boolean isDrawGrid = true;
    private static boolean rulerVisible = true;
    private static double gridHeight = 750;
    private static double gridWidth = 750;
    private static int gridSize = 50;
    private static Locale locale = new Locale("en", "US");

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

    public static boolean isRulerVisible() {
        return rulerVisible;
    }

    public static void setRulerVisible(boolean rulerVisible) {
        SettingsSingleton.rulerVisible = rulerVisible;
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
        setLocaleWithString(settings.getLocale());
    }

    public ResourceBundle getLocalization() {
        return ResourceBundle.getBundle("localization", locale);
    }

    public String getLocalizationString(String key) {
        return getLocalization().getString(key);
    }

    public static String getLocaleSimpleName() {
        switch(locale.getLanguage()) {
            case "en":
                return "ENG";
            case "fi":
                return "FIN";
            case "ja":
                return "JPN";
            default:
                return "ENG";
        }
    }

    public static Locale getLocale() {
        return locale;
    }

    private Locale getLocaleWithString(String language) {
        switch (language) {
            case "ENG":
                return new Locale("en", "US");
            case "FIN":
                return new Locale("fi", "FI");
            case "JPN":
                return new Locale("ja", "JP");
            default:
                return new Locale("en", "US");
        }
    }

    public static void setLocale(Locale locale) {
        SettingsSingleton.locale = locale;
    }

    public static void setLocaleWithString(String localeSimpleName) {
        SettingsSingleton.locale = getInstance().getLocaleWithString(localeSimpleName);
    }

    public Settings getSettings() {
        return new Settings(drawLengths, isDrawGrid, gridHeight, gridWidth, gridSize, getLocaleSimpleName());
    }
}
