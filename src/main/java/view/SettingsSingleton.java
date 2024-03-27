package view;

import entity.Settings;
import org.springframework.core.io.Resource;

import java.util.Locale;
import java.util.ResourceBundle;

public class SettingsSingleton {
    private static boolean drawLengths = true;
    private static boolean isDrawGrid = true;
    private static double gridHeight = 750;
    private static double gridWidth = 750;
    private static int gridSize = 25;
    private static Locale locale = LocaleConfig.values()[0].getLocale();

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
        setLocaleWithString(settings.getLocale());
    }

    public ResourceBundle getLocalization() {
        return ResourceBundle.getBundle("localization", locale);
    }

    public String getLocalizationString(String key) {
        return getLocalization().getString(key);
    }

    public static ResourceBundle getLocalizationWithLocale(Locale locale) {
        return ResourceBundle.getBundle("localization", locale);
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

    public static String getLocaleFullName() {
        switch(locale.getLanguage()) {
            case "en":
                return "English";
            case "fi":
                return "Suomi";
            case "ja":
                return "日本語";
            default:
                return "English";
        }
    }

    public static Locale getLocale() {
        return locale;
    }

    public static LocaleConfig[] getAllLocalization() {
        return LocaleConfig.values();
    }

    private Locale getLocaleWithString(String language) {
        switch (language) {
            case "en":
                return new Locale("en", "US");
            case "fi":
                return new Locale("fi", "FI");
            case "ja":
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
        return new Settings(drawLengths, isDrawGrid, gridHeight, gridWidth, gridSize, locale.getLanguage());
    }
}
