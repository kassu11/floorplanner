package model.shapeContainers;
/**
 * Singleton class for storing preview shapes
 */
public class PreviewShapesSingleton extends ShapeContainer {
    /**
     * Constructor for the preview shapes singleton
     */
    private PreviewShapesSingleton() {
        super();
    }
    /**
     * Helper class for the preview shapes singleton
     */
    private static class ShapesSingletonHelper {
        private static final PreviewShapesSingleton INSTANCE = new PreviewShapesSingleton();
    }
    /**
     * Returns the instance of the preview shapes singleton
     * @return instance of the preview shapes singleton
     */
    public static PreviewShapesSingleton getInstance() {
        return PreviewShapesSingleton.ShapesSingletonHelper.INSTANCE;
    }

}
