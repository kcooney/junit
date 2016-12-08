package org.junit.runners.parameterized;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

import org.junit.runners.model.HierarchicalStore;
import org.junit.runners.model.Store;
import org.junit.runners.model.TestClass;

/**
 * A {@code TestWithParameters} keeps the data together that are needed for
 * creating a runner for a single data set of a parameterized test. It has a
 * name, the test class and a list of parameters.
 * 
 * @since 4.12
 */
public class TestWithParameters {
    private final String name;

    private final TestClass testClass;

    private final List<Object> parameters;

    private final Store store;

    public TestWithParameters(String name, TestClass testClass,
            List<Object> parameters) {
        this(name, testClass, parameters, new HierarchicalStore());
    }
 
    /** @since 4.13 */
    public TestWithParameters(String name, TestClass testClass,
            List<Object> parameters,
            Store store) {
        this.name = notNull(name, "The name is missing.");
        this.testClass = notNull(testClass, "The test class is missing.");
        notNull(parameters, "The parameters are missing.");
        this.parameters = unmodifiableList(new ArrayList<Object>(parameters));
        this.store = notNull(store, "The store is missing.");
    }

    public String getName() {
        return name;
    }

    public TestClass getTestClass() {
        return testClass;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    /** @since 4.13 */
    Store getStore() {
        return store;
    }

    @Override
    public int hashCode() {
        int prime = 14747;
        int result = prime + name.hashCode();
        result = prime * result + testClass.hashCode();
        return prime * result + parameters.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        TestWithParameters other = (TestWithParameters) obj;
        return name.equals(other.name)
                && parameters.equals(other.parameters)
                && testClass.equals(other.testClass);
    }

    @Override
    public String toString() {
        return testClass.getName() + " '" + name + "' with parameters "
                + parameters;
    }

    private static <T> T notNull(T value, String message) {
        if (value == null) {
            throw new NullPointerException(message);
        }
        return value;
    }
}
