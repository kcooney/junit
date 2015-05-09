package org.junit.experimental.runners.junit5;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;

public class JUnit5 extends BlockJUnit4ClassRunner {

    public JUnit5(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected TestClass createTestClass(Class<?> testClass) {
        return new MetaAnnotationAwareTestClass(testClass);
    }
}
