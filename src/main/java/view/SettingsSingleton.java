package view;

public class SettingsSingleton {
    private static ShapeType currentShape = ShapeType.LINE;

    private static ModeType currentMode = ModeType.DRAW;

    private SettingsSingleton() {
    }

    private static class SettingsSingletonHelper {
        private static final SettingsSingleton INSTANCE = new SettingsSingleton();
    }

    public static SettingsSingleton getInstance() {
        return SettingsSingletonHelper.INSTANCE;
    }

    public static ShapeType getCurrentShape() {
        return currentShape;
    }

    public static void setCurrentShape(ShapeType shape) {
        currentShape = shape;
        System.out.println("Current shape: " + currentShape);
    }

    public static ModeType getCurrentMode() {
        return currentMode;
    }

    public static void setCurrentMode(ModeType currentMode) {
        SettingsSingleton.currentMode = currentMode;
    }

    public static boolean isShapeType(ShapeType shape) {
        return currentShape == shape;
    }
}
