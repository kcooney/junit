package org.junit.samples;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.experimental.runners.model.TestModel;
import org.junit.experimental.runners.model.TestModelFactory;
import org.junit.internal.runners.statements.Fail;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.Statement;

public class SimpleTestExecutor extends BaseExecutor {
    private final Description description;
    private final Description divideByZeroDescription;
    private final Description testEqualsDescription;

    public SimpleTestExecutor() {
        divideByZeroDescription = Description.createTestDescription(SimpleTest.class, "divideByZero");;
        testEqualsDescription = Description.createTestDescription(SimpleTest.class, "testEquals");
        description = Description.createSuiteDescription(SimpleTest.class);
        description.addChild(testEqualsDescription);
        description.addChild(divideByZeroDescription);
    }

    private static class ModelFactory implements TestModelFactory {
        
        public Description getDescription() {
            Description description = Description.createSuiteDescription(SimpleTest.class);
            description.addChild(Description.createTestDescription(SimpleTest.class, "divideByZero"));
            description.addChild(Description.createTestDescription(SimpleTest.class, "testEquals"));
            return description;
        }

        public TestModel createModel() throws Throwable {
            return new Model(new SimpleTest());
        }
    }
 
    private static class Model implements TestModel {
        private final SimpleTest test;

        public Model(SimpleTest test) {
            this.test = test;
        }
        
        public List<TestRule> rules() {
            return Collections.<TestRule>singletonList(test.folder);
        }

        public List<Statement> befores() {
            return Collections.<Statement>singletonList(new Statement() {
                @Override public void evaluate() throws Throwable {
                    test.setUp();
                }});
        }

        public List<TestMethod> tests() {
            List<TestMethod> allTests = new ArrayList<TestMethod>(2);
            allTests.add(new TestMethod() {
                @Override public void evaluate() throws Throwable {
                    test.divideByZero();
                }

                public Description getDescription() {
                    return Description.createTestDescription(SimpleTest.class, "divideByZero");
                }});
            allTests.add(new TestMethod() {
                @Override public void evaluate() throws Throwable {
                    test.testEquals();
                }

                public Description getDescription() {
                    return Description.createTestDescription(SimpleTest.class, "testEquals");
                }});
            return allTests;
        }
    }

    private void runTestWithRules(SimpleTest test, Statement statement, RunNotifier runNotifier, Description testDescription) {
        statement = test.folder.apply(statement, testDescription);
        runTest(statement, runNotifier, testDescription);
    }

    private SimpleTest createTest(RunNotifier runNotifier, Description description) {
        try {
           return new SimpleTest();
        } catch (Throwable e) {
            runTest(new Fail(e), runNotifier, description);
            return null;
        }
    }
    
    public void runDivideByZero(RunNotifier runNotifier) {
        final SimpleTest test = createTest(runNotifier, divideByZeroDescription);
        if (test == null) {
            return;
        }
        
        Statement statement = new Statement() {
            @Override
            public void evaluate() throws Throwable {
                test.setUp();
                test.divideByZero();
            }
        };
        
        runTestWithRules(test, statement, runNotifier, divideByZeroDescription);
    }

    public void runTestEquals(RunNotifier runNotifier) {
        final SimpleTest test = createTest(runNotifier, testEqualsDescription);
        if (test == null) {
            return;
        }
        Statement statement = new Statement() {
            @Override
            public void evaluate() throws Throwable {
                test.setUp();
                test.testEquals();
            }
        };
        
        runTestWithRules(test, statement, runNotifier, testEqualsDescription);
    }

    public Runner createRunner() {
        return new ModelRunner(new ModelFactory());
//        return new Runner() {
//            @Override
//            public Description getDescription() {
//                return description;
//            }
//
//            @Override
//            public void run(RunNotifier runNotifer) {
//                runDivideByZero(runNotifer);
//                runTestEquals(runNotifer);
//            }
//        };
    }
}
