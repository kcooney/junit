package org.junit.samples;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SimpleTestExecutorTest {

    @Test
    public void passes() {
        JUnitCore junitCore = new JUnitCore();
        FakeListener fakeListener = new FakeListener();
        junitCore.addListener(fakeListener);
        Result result = junitCore.run(new SimpleTestExecutor().createRunner());
        assertFalse(result.wasSuccessful());
 
        List<String> expectedMessages = new ArrayList<String>();
        expectedMessages.add("testRunStarted: org.junit.samples.SimpleTest");
        expectedMessages.add("testStarted: divideByZero(org.junit.samples.SimpleTest)");
        expectedMessages.add("testFailure: divideByZero(org.junit.samples.SimpleTest): / by zero");
        expectedMessages.add("testFinished: divideByZero(org.junit.samples.SimpleTest)");
        
        expectedMessages.add("testStarted: testEquals(org.junit.samples.SimpleTest)");
        expectedMessages.add("testFailure: testEquals(org.junit.samples.SimpleTest): Size expected:<12> but was:<13>");
        expectedMessages.add("testFinished: testEquals(org.junit.samples.SimpleTest)");
        expectedMessages.add("testRunFinished");
        
        assertEquals(expectedMessages, fakeListener.messages);
    }

    private static class FakeListener extends RunListener {
        public final List<String> messages = new ArrayList<String>();

        @Override
        public void testRunStarted(Description description) throws Exception {
            messages.add("testRunStarted: " + description);
        }

        @Override
        public void testRunFinished(Result result) throws Exception {
            messages.add("testRunFinished");
        }

        @Override
        public void testStarted(Description description) throws Exception {
            messages.add("testStarted: " + description);
        }

        @Override
        public void testFinished(Description description) throws Exception {
            messages.add("testFinished: " + description);
        }

        @Override
        public void testFailure(Failure failure) throws Exception {
            messages.add("testFailure: " + failure);
        }

        @Override
        public void testAssumptionFailure(Failure failure) {
            messages.add("testAssumptionFailure: " + failure);
        }

        @Override
        public void testIgnored(Description description) throws Exception {
            messages.add("testIgnored: " + description);
        }
    }
}
