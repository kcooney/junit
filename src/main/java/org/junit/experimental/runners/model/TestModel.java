package org.junit.experimental.runners.model;

import java.util.List;

import org.junit.rules.TestRule;
import org.junit.runners.model.Statement;
import org.junit.samples.TestMethod;

public interface TestModel {

    List<TestMethod> tests();
    List<Statement> befores();
    List<TestRule> rules();
}
