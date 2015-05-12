package org.junit.samples;

import java.util.List;

import org.junit.experimental.runners.model.TestModelFactory;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.internal.runners.statements.Fail;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;
import org.junit.runners.AbstractRunner;
import org.junit.runners.model.RunnerScheduler;
import org.junit.runners.model.Statement;

public class ModelRunner extends AbstractRunner<TestMethod> {
    private final TestModelFactory factory;

    public ModelRunner(TestModelFactory factory) {
        this.factory = factory;
    }

    @Override
    protected List<TestMethod> getChildren() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Description describeChild(TestMethod child) {
        return child.getDescription();
    }

    @Override
    protected void runChild(TestMethod child, RunNotifier notifier) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected Statement withBeforeClasses(Statement statement) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Statement withAfterClasses(Statement statement) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected List<TestRule> classRules() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Description getDescription() {
        return factory.getDescription();
    }
//    private final TestModelFactory factory;
// 
//    private volatile RunnerScheduler scheduler = new RunnerScheduler() {
//        public void schedule(Runnable childStatement) {
//            childStatement.run();
//        }
//
//        public void finished() {
//            // do nothing
//        }
//    };
//
//    public ModelRunner(TestModelFactory factory) {
//        this.factory = factory;
//    }
//
//    @Override
//    public Description getDescription() {
//        return factory.getDescription();
//    }
//
//    @Override
//    public void run(RunNotifier notifier) {
//        EachTestNotifier testNotifier = new EachTestNotifier(notifier,
//                getDescription());
//        try {
//            Statement statement = classBlock(notifier);
//            statement.evaluate();
//        } catch (AssumptionViolatedException e) {
//            testNotifier.addFailedAssumption(e);
//        } catch (StoppedByUserException e) {
//            throw e;
//        } catch (Throwable e) {
//            testNotifier.addFailure(e);
//        }
//    }
////        EachTestNotifier eachNotifier = new EachTestNotifier(notifier, getDescription());
////        TestModel model;
////        try {
////            model = factory.createModel();
////        } catch (Throwable e) {
////            eachNotifier.addFailure(e);
////        }
//
//    private Statement classBlock(RunNotifier notifier) {
//        Statement statement = childrenInvoker(notifier);
////        if (!areAllChildrenIgnored()) {
////            statement = withBeforeClasses(statement);
////            statement = withAfterClasses(statement);
////            statement = withClassRules(statement);
////        }
//        return statement;
//    }
//
//    private Statement childrenInvoker(final RunNotifier notifier) {
//        return new Statement() {
//            @Override
//            public void evaluate() {
//                runChildren(notifier);
//            }
//        };
//    }
//
//    private void runChildren(final RunNotifier notifier) {
//        final RunnerScheduler currentScheduler = scheduler;
//        try {
//            for (final TestMethod each : getFilteredChildren()) {
//                currentScheduler.schedule(new Runnable() {
//                    public void run() {
//                        ModelRunner.this.runChild(each, notifier);
//                    }
//                });
//            }
//        } finally {
//            currentScheduler.finished();
//        }
//    }
//
//    private List<TestMethod> getFilteredChildren() {
//        
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    private void runChild(TestMethod method, RunNotifier notifier) {
//        Description description = method.getDescription();
//        if (isIgnored(method)) {
//            notifier.fireTestIgnored(description);
//        } else {
//            Statement statement;
//            try {
//                statement = methodBlock(method);
//            }
//            catch (Throwable ex) {
//                statement = new Fail(ex);
//            }
//            runLeaf(statement, description, notifier);
//        }
//    }
}
