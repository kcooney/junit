package org.junit.experimental.runners.model;

import org.junit.runner.Description;

public interface TestModelFactory {

    Description getDescription();
    TestModel createModel() throws Throwable;
}
