package org.junit.runners;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.internal.runners.ErrorReportingRunner;
import org.junit.runner.Runner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.InvalidTestClassError;
import org.junit.runners.model.TestClass;
import org.junit.runners.parameterized.BlockJUnit4ClassRunnerWithParametersFactory;
import org.junit.runners.parameterized.ParametersRunnerFactory;
import org.junit.runners.parameterized.TestWithParameters;

/**
 * The custom runner <code>Parameterized</code> implements parameterized tests.
 * When running a parameterized test class, instances are created for the
 * cross-product of the test methods and the test data elements.
 * <p>
 * For example, to test the <code>+</code> operator, write:
 * <pre>
 * &#064;RunWith(Parameterized.class)
 * public class AdditionTest {
 *     &#064;Parameters(name = &quot;{index}: {0} + {1} = {2}&quot;)
 *     public static Iterable&lt;Object[]&gt; data() {
 *         return Arrays.asList(new Object[][] { { 0, 0, 0 }, { 1, 1, 2 },
 *                 { 3, 2, 5 }, { 4, 3, 7 } });
 *     }
 *
 *     private int firstSummand;
 *
 *     private int secondSummand;
 *
 *     private int sum;
 *
 *     public AdditionTest(int firstSummand, int secondSummand, int sum) {
 *         this.firstSummand = firstSummand;
 *         this.secondSummand = secondSummand;
 *         this.sum = sum;
 *     }
 *
 *     &#064;Test
 *     public void test() {
 *         assertEquals(sum, firstSummand + secondSummand);
 *     }
 * }
 * </pre>
 * <p>
 * Each instance of <code>AdditionTest</code> will be constructed using the
 * three-argument constructor and the data values in the
 * <code>&#064;Parameters</code> method.
 * <p>
 * In order that you can easily identify the individual tests, you may provide a
 * name for the <code>&#064;Parameters</code> annotation. This name is allowed
 * to contain placeholders, which are replaced at runtime. The placeholders are
 * <dl>
 * <dt>{index}</dt>
 * <dd>the current parameter index</dd>
 * <dt>{0}</dt>
 * <dd>the first parameter value</dd>
 * <dt>{1}</dt>
 * <dd>the second parameter value</dd>
 * <dt>...</dt>
 * <dd>...</dd>
 * </dl>
 * <p>
 * In the example given above, the <code>Parameterized</code> runner creates
 * names like <code>[2: 3 + 2 = 5]</code>. If you don't use the name parameter,
 * then the current parameter index is used as name.
 * <p>
 * You can also write:
 * <pre>
 * &#064;RunWith(Parameterized.class)
 * public class AdditionTest {
 *     &#064;Parameters(name = &quot;{index}: {0} + {1} = {2}&quot;)
 *     public static Iterable&lt;Object[]&gt; data() {
 *         return Arrays.asList(new Object[][] { { 0, 0, 0 }, { 1, 1, 2 },
 *                 { 3, 2, 5 }, { 4, 3, 7 } });
 *     }
 *
 *     &#064;Parameter(0)
 *     public int firstSummand;
 *
 *     &#064;Parameter(1)
 *     public int secondSummand;
 *
 *     &#064;Parameter(2)
 *     public int sum;
 *
 *     &#064;Test
 *     public void test() {
 *         assertEquals(sum, firstSummand + secondSummand);
 *     }
 * }
 * </pre>
 * <p>
 * Each instance of <code>AdditionTest</code> will be constructed with the default constructor
 * and fields annotated by <code>&#064;Parameter</code>  will be initialized
 * with the data values in the <code>&#064;Parameters</code> method.
 *
 * <p>
 * The parameters can be provided as an array, too:
 * 
 * <pre>
 * &#064;Parameters
 * public static Object[][] data() {
 * 	return new Object[][] { { 0, 0, 0 }, { 1, 1, 2 }, { 3, 2, 5 }, { 4, 3, 7 } } };
 * }
 * </pre>
 * 
 * <h3>Tests with single parameter</h3>
 * <p>
 * If your test needs a single parameter only, you don't have to wrap it with an
 * array. Instead you can provide an <code>Iterable</code> or an array of
 * objects.
 * <pre>
 * &#064;Parameters
 * public static Iterable&lt;? extends Object&gt; data() {
 * 	return Arrays.asList(&quot;first test&quot;, &quot;second test&quot;);
 * }
 * </pre>
 * <p>
 * or
 * <pre>
 * &#064;Parameters
 * public static Object[] data() {
 * 	return new Object[] { &quot;first test&quot;, &quot;second test&quot; };
 * }
 * </pre>
 *
 * <h3>Create different runners</h3>
 * <p>
 * By default the {@code Parameterized} runner creates a slightly modified
 * {@link BlockJUnit4ClassRunner} for each set of parameters. You can build an
 * own {@code Parameterized} runner that creates another runner for each set of
 * parameters. Therefore you have to build a {@link ParametersRunnerFactory}
 * that creates a runner for each {@link TestWithParameters}. (
 * {@code TestWithParameters} are bundling the parameters and the test name.)
 * The factory must have a public zero-arg constructor.
 *
 * <pre>
 * public class YourRunnerFactory implements ParametersRunnerFactory {
 *     public Runner createRunnerForTestWithParameters(TestWithParameters test)
 *             throws InitializationError {
 *         return YourRunner(test);
 *     }
 * }
 * </pre>
 * <p>
 * Use the {@link UseParametersRunnerFactory} to tell the {@code Parameterized}
 * runner that it should use your factory.
 *
 * <pre>
 * &#064;RunWith(Parameterized.class)
 * &#064;UseParametersRunnerFactory(YourRunnerFactory.class)
 * public class YourTest {
 *     ...
 * }
 * </pre>
 *
 * @since 4.0
 */
public class Parameterized extends Suite {
    /**
     * Annotation for a method which provides parameters to be injected into the
     * test class constructor by <code>Parameterized</code>. The method has to
     * be public and static.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Parameters {
        /**
         * Optional pattern to derive the test's name from the parameters. Use
         * numbers in braces to refer to the parameters or the additional data
         * as follows:
         * <pre>
         * {index} - the current parameter index
         * {0} - the first parameter value
         * {1} - the second parameter value
         * etc...
         * </pre>
         * <p>
         * Default value is "{index}" for compatibility with previous JUnit
         * versions.
         *
         * @return {@link MessageFormat} pattern string, except the index
         *         placeholder.
         * @see MessageFormat
         */
        String name() default "{index}";
    }

    /**
     * Annotation for fields of the test class which will be initialized by the
     * method annotated by <code>Parameters</code>.
     * By using directly this annotation, the test class constructor isn't needed.
     * Index range must start at 0.
     * Default value is 0.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Parameter {
        /**
         * Method that returns the index of the parameter in the array
         * returned by the method annotated by <code>Parameters</code>.
         * Index range must start at 0.
         * Default value is 0.
         *
         * @return the index of the parameter.
         */
        int value() default 0;
    }

    /**
     * Add this annotation to your test class if you want to generate a special
     * runner. You have to specify a {@link ParametersRunnerFactory} class that
     * creates such runners. The factory must have a public zero-arg
     * constructor.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    @Target(ElementType.TYPE)
    public @interface UseParametersRunnerFactory {
        /**
         * @return a {@link ParametersRunnerFactory} class (must have a default
         *         constructor)
         */
        Class<? extends ParametersRunnerFactory> value() default BlockJUnit4ClassRunnerWithParametersFactory.class;
    }

    /**
     * Annotation for {@code public static void} methods which should be executed before
     * evaluating tests with a particular parameter.
     *
     * @see org.junit.BeforeClass
     * @see org.junit.Before
     * @since 4.13
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface BeforeParam {
    }

    /**
     * Annotation for {@code public static void} methods which should be executed after
     * evaluating tests with a particular parameter.
     *
     * @see org.junit.AfterClass
     * @see org.junit.After
     * @since 4.13
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface AfterParam {
    }

    /**
     * Only called reflectively. Do not use programmatically.
     */
    public Parameterized(Class<?> klass) throws Throwable {
        this(klass, new RunnersFactory(klass));
    }

    private Parameterized(Class<?> klass, RunnersFactory runnersFactory)
            throws InitializationError {
        super(klass, runnersFactory.createRunners());
        validateBeforeParamAndAfterParamMethods(runnersFactory.parameterCount);
    }

    private void validateBeforeParamAndAfterParamMethods(Integer parameterCount)
            throws InvalidTestClassError {
        List<Throwable> errors = new ArrayList<Throwable>();
        validatePublicStaticVoidMethods(Parameterized.BeforeParam.class, parameterCount, errors);
        validatePublicStaticVoidMethods(Parameterized.AfterParam.class, parameterCount, errors);
        if (!errors.isEmpty()) {
            throw new InvalidTestClassError(getTestClass().getJavaClass(), errors);
        }
    }

    private void validatePublicStaticVoidMethods(
            Class<? extends Annotation> annotation, Integer parameterCount,
            List<Throwable> errors) {
        List<FrameworkMethod> methods = getTestClass().getAnnotatedMethods(annotation);
        for (FrameworkMethod fm : methods) {
            fm.validatePublicVoid(true, errors);
            if (parameterCount != null) {
                int methodParameterCount = fm.getMethod().getParameterTypes().length;
                if (methodParameterCount != 0 && methodParameterCount != parameterCount) {
                    errors.add(new Exception("Method " + fm.getName()
                            + "() should have 0 or " + parameterCount + " parameter(s)"));
                }
            }
        }
    }

    private static class RunnersFactory {
        private static final ParametersRunnerFactory DEFAULT_FACTORY = new BlockJUnit4ClassRunnerWithParametersFactory();

        private final TestClass testClass;
        private final List<Object> allParameters;
        private final int parameterCount;
        private final Parameters parametersAnnotation;
        private final ParametersRunnerFactory runnerFactory;

        private RunnersFactory(Class<?> klass) throws Throwable {
            testClass = new TestClass(klass);
            allParameters = allParameters(testClass);
            parameterCount =
                    allParameters.isEmpty() ? 0 : normalizeParameters(allParameters.get(0)).length;
            parametersAnnotation = getParametersMethod(testClass).getAnnotation(Parameters.class);
            runnerFactory = getParametersRunnerFactory(testClass);
        }

        public List<Runner> createRunners() {
            return Collections.unmodifiableList(createRunnersForParameters(
                    allParameters, parametersAnnotation.name(), runnerFactory));
        }

        private static ParametersRunnerFactory getParametersRunnerFactory(TestClass testClass)
                throws InstantiationException, IllegalAccessException {
            UseParametersRunnerFactory annotation = testClass
                    .getAnnotation(UseParametersRunnerFactory.class);
            if (annotation == null) {
                return DEFAULT_FACTORY;
            } else {
                Class<? extends ParametersRunnerFactory> factoryClass = annotation
                        .value();
                return factoryClass.newInstance();
            }
        }

        private TestWithParameters createTestWithNotNormalizedParameters(
                String pattern, int index, Object parametersOrSingleParameter)
                throws MessageFormatException {
            Object[] parameters = normalizeParameters(parametersOrSingleParameter);
            return createTestWithParameters(testClass, pattern, index, parameters);
        }

        private static Object[] normalizeParameters(Object parametersOrSingleParameter) {
            return (parametersOrSingleParameter instanceof Object[]) ? (Object[]) parametersOrSingleParameter
                    : new Object[] { parametersOrSingleParameter };
        }

        @SuppressWarnings("unchecked")
        private static List<Object> allParameters(TestClass testClass) throws Throwable {
            Object parameters = getParametersMethod(testClass).invokeExplosively(null);
            if (parameters instanceof List) {
                return (List<Object>) parameters;
            } else if (parameters instanceof Collection) {
                return new ArrayList<Object>((Collection<Object>) parameters);
            } else if (parameters instanceof Iterable) {
                List<Object> result = new ArrayList<Object>();
                for (Object entry : ((Iterable<Object>) parameters)) {
                    result.add(entry);
                }
                return result;
            } else if (parameters instanceof Object[]) {
                return Arrays.asList((Object[]) parameters);
            } else {
                throw parametersMethodReturnedWrongType(testClass);
            }
        }

        private static FrameworkMethod getParametersMethod(TestClass testClass) throws Exception {
            List<FrameworkMethod> methods = testClass
                    .getAnnotatedMethods(Parameters.class);
            for (FrameworkMethod each : methods) {
                if (each.isStatic() && each.isPublic()) {
                    return each;
                }
            }

            throw new Exception("No public static parameters method on class "
                    + testClass.getName());
        }

        private List<Runner> createRunnersForParameters(
                Iterable<Object> allParameters, String namePattern,
                ParametersRunnerFactory runnerFactory) {
            int i = 0;
            List<Runner> runners = new ArrayList<Runner>();
            for (Object parametersOfSingleTest : allParameters) {
                try {
                    TestWithParameters test = createTestWithNotNormalizedParameters(
                            namePattern, i++, parametersOfSingleTest);
                    runners.add(createRunnerForTestWithParameters(runnerFactory, test));
                } catch (MessageFormatException e) {
                    runners.add(new ErrorReportingRunner(testClass.getJavaClass(), e));
                }
            }
            return runners;
        }

        private Runner createRunnerForTestWithParameters(
                ParametersRunnerFactory runnerFactory, TestWithParameters test) {
            try {
                return runnerFactory.createRunnerForTestWithParameters(test);
            } catch (Throwable e) {
                return new ErrorReportingRunner(testClass.getJavaClass(), e);
            }
        }

        private static Exception parametersMethodReturnedWrongType(TestClass testClass) throws Exception {
            String className = testClass.getName();
            String methodName = getParametersMethod(testClass).getName();
            String message = MessageFormat.format(
                    "{0}.{1}() must return an Iterable of arrays.", className,
                    methodName);
            return new Exception(message);
        }

        private static class MessageFormatException extends Exception {
            private static final long serialVersionUID = 1L;

            MessageFormatException(String message, IllegalArgumentException cause) {
                super(message, cause);
            }
        }
        private TestWithParameters createTestWithParameters(
                TestClass testClass, String pattern, int index,
                Object[] parameters) throws MessageFormatException {
            String finalPattern = pattern.replaceAll("\\{index\\}",
                    Integer.toString(index));
            String name;
            try {
                name = MessageFormat.format(finalPattern, parameters);
            } catch (IllegalArgumentException e) {
                throw new MessageFormatException("Could not format [" + pattern + "]", e);
            }
            return new TestWithParameters("[" + name + "]", testClass,
                    Arrays.asList(parameters));
        }
    }
}
