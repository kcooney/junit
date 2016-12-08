package org.junit.internal.builders;

import org.junit.runner.Runner;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.RunnerBuilder;

public class JUnit4Builder extends RunnerBuilder {
    private final RunnerBuilder builder;

    public JUnit4Builder() {
        builder = null;
    }

    /** @since 4.13 */
    public JUnit4Builder(RunnerBuilder builder) {
        this.builder = builder;
    }

    @Override
    public Runner runnerForClass(Class<?> testClass) throws Throwable {
        if (builder == null) {
            return new BlockJUnit4ClassRunner(testClass);
        }
        return new BlockJUnit4ClassRunner(testClass, builder);
    }
}