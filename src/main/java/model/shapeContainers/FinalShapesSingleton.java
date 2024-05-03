package model.shapeContainers;

/**
 * Singleton class for storing shapes
 */
public class FinalShapesSingleton extends ShapeContainer {
    /**
     * Constructor for the shapes singleton
     */
    private FinalShapesSingleton() {
        super();
    }
    /**
     * Helper class for the shapes singleton
     */
    private static class ShapesSingletonHelper {
        private static final FinalShapesSingleton INSTANCE = new FinalShapesSingleton();
    }
    /**
     * Returns the instance of the shapes singleton
     * @return instance of the shapes singleton
     */
    public static FinalShapesSingleton getInstance() {
        return ShapesSingletonHelper.INSTANCE;
    }

}
