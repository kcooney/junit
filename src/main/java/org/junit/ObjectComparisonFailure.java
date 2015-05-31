package org.junit;

/**
 * A version of {@link ComparisonFailure} that can be used for comparison failures
 * between objects that are not strings. Unlike {@code ComparisonFailure}, the
 * comparison message is not compacted (for backwards compatibility).
 *
 * @since 4.13
 */
class ObjectComparisonFailure extends ComparisonFailure {
    private static final long serialVersionUID = 1L;

    private final String rawMessage;

    /**
     * Constructs a comparison failure.
     *
     * @param message the identifying message or null
     * @param expected the expected value
     * @param actual the actual value
     */
    public <T> ObjectComparisonFailure(String message, T expected, T actual) {
        super(message, nullableToString(expected), nullableToString(actual));
        rawMessage = message;
    }
 
    private static String nullableToString(Object value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    /**
     * Returns {@code "expected:<expected> but was:<actual>}, prepending the
     * user-defined message if available.
     */
    @Override
    public String getMessage() {
        return Assert.format(rawMessage, getExpected(), getActual());
    }
}
