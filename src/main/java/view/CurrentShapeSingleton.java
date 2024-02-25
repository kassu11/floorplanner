package view;

public class CurrentShapeSingleton {
    private static ShapeType currentShape = ShapeType.LINE;

    private CurrentShapeSingleton() {
    }

    private static class CurrentShapeSingletonHelper {
        private static final CurrentShapeSingleton INSTANCE = new CurrentShapeSingleton();
    }

    public static CurrentShapeSingleton getInstance() {
        return CurrentShapeSingletonHelper.INSTANCE;
    }

    public static ShapeType getCurrentShape() {
        return currentShape;
    }

    public static void setCurrentShape(ShapeType shape) {
        currentShape = shape;
        System.out.println("Current shape: " + currentShape);
    }

    public static boolean isShapeType(ShapeType shape) {
        return currentShape == shape;
    }
}
