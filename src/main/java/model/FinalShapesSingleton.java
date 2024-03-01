package model;

import java.util.List;

public class FinalShapesSingleton extends ShapeContainer{

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
