package model;

import model.shapeContainers.PreviewShapesSingleton;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PreviewShapesSingletonTest {

    @Test
    void getInstance() {
        PreviewShapesSingleton previewShapesSingleton1 = PreviewShapesSingleton.getInstance();
        PreviewShapesSingleton previewShapesSingleton2 = PreviewShapesSingleton.getInstance();
        assertNotNull(previewShapesSingleton1);
        assertNotNull(previewShapesSingleton2);
        assertEquals(previewShapesSingleton1, previewShapesSingleton2);
        assertSame(previewShapesSingleton1, previewShapesSingleton2);
    }
}