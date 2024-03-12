package model.shapeContainers;

public class PreviewShapesSingleton extends ShapeContainer {
    private PreviewShapesSingleton() {
        super();
    }

    private static class ShapesSingletonHelper {
        private static final PreviewShapesSingleton INSTANCE = new PreviewShapesSingleton();
    }

    public static PreviewShapesSingleton getInstance() {
        return PreviewShapesSingleton.ShapesSingletonHelper.INSTANCE;
    }

}
