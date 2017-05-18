package org.junit.tests.manipulation;

import org.junit.runner.manipulation.Ordering;
import org.junit.runner.manipulation.Sorter;

/**
 * A sorter that orders tests reverse alphanumerically by test name.
 */
public final class ReverseAlphanumericSorter implements Ordering.Factory {

    public Ordering create(Ordering.Context context) {
        return new Sorter(Comparators.reverse(Comparators.alphanumeric()));
    }
}
