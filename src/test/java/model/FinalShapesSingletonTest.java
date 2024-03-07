package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FinalShapesSingletonTest {

    @Test
    void getInstance() {
        FinalShapesSingleton finalShapesSingleton1 = FinalShapesSingleton.getInstance();
        FinalShapesSingleton finalShapesSingleton2 = FinalShapesSingleton.getInstance();
        assertNotNull(finalShapesSingleton1);
        assertNotNull(finalShapesSingleton2);
        assertEquals(finalShapesSingleton1, finalShapesSingleton2);
        assertSame(finalShapesSingleton1, finalShapesSingleton2);
    }
}