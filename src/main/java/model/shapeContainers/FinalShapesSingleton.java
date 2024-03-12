package model.shapeContainers;


public class FinalShapesSingleton extends ShapeContainer {

    private FinalShapesSingleton() {
        super();
    }

    private static class ShapesSingletonHelper {
        private static final FinalShapesSingleton INSTANCE = new FinalShapesSingleton();
    }

    public static FinalShapesSingleton getInstance() {
        return ShapesSingletonHelper.INSTANCE;
    }

}
