package model;

import java.util.ArrayList;
import java.util.List;

public class ShapesSingleton {
    private static final List<Shape> shapes = new ArrayList<>();

    private ShapesSingleton() {
    }

    private static class ShapesSingletonHelper {
        private static final ShapesSingleton INSTANCE = new ShapesSingleton();
    }

    public static ShapesSingleton getInstance() {
        return ShapesSingletonHelper.INSTANCE;
    }

    public static List<Shape> getShapes() {
        return shapes;
    }

    public static void addShape(Shape shape) {
        shapes.add(shape);
    }

    public static void addAllShapes(List<Shape> shapes) {
        shapes.addAll(shapes);
    }

    public static void clearShapes() {
        shapes.clear();
    }

}
