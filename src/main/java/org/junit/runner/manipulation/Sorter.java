package org.junit.runner.manipulation;

import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.runner.Description;

/**
 * A <code>Sorter</code> orders tests. In general you will not need
 * to use a <code>Sorter</code> directly. Instead, use {@link org.junit.runner.Request#sortWith(Comparator)}.
 *
 * @since 4.0
 */
public class Sorter implements Comparator<Description> {
    /**
     * NULL is a <code>Sorter</code> that leaves elements in an undefined order
     */
    public static final Sorter NULL = new Sorter(new Comparator<Description>() {
        public int compare(Description o1, Description o2) {
            return 0;
        }
    });

    private final Comparator<Description> comparator;

    /**
     * Creates a <code>Sorter</code> that uses <code>comparator</code>
     * to sort tests
     *
     * @param comparator the {@link Comparator} to use when sorting tests
     */
    public Sorter(Comparator<Description> comparator) {
        this.comparator = comparator;
    }

    /**
     * Sorts the test in <code>runner</code> using <code>comparator</code>
     */
    public void apply(Object object) {
        if (object instanceof Sortable) {
            Sortable sortable = (Sortable) object;
            sortable.sort(this);
        }
    }

    public int compare(Description o1, Description o2) {
        return comparator.compare(o1, o2);
    }

    /**
     * Creates an {@link Sorter} that shuffles the items using the given
     * {@link Random} instance.
     */
    public static Sorter shuffledBy(Random random) {
        return new Sorter(new ShufflingComparator(random));
    }

    private static class ShufflingComparator implements Comparator<Description> {
        private final Random random;
        private final ConcurrentMap<Description, RandomValueHolder> map
                = new ConcurrentHashMap<Description, RandomValueHolder>();

        private static class RandomValueHolder implements Comparable<RandomValueHolder> {
            private static final Lock lock = new ReentrantLock();
            private final Random random;
            private volatile long[] randomValues;

            RandomValueHolder(Random random) {
                this.random = random;
            }

            long[] getRandomValues() {
                long[] value = randomValues; // volatile read
                if (value == null) {
                    lock.lock();
                    try {
                        value = randomValues;
                        if (value == null) {
                            value = new long[4];
                            for (int i = 0; i < value.length; i++) {
                                value[i] = random.nextLong();
                            }
                            randomValues = value;
                        }
                    } finally {
                        lock.unlock();
                    }
                }
                return value;
            }

            public int compareTo(RandomValueHolder other) {
                long[] thisRandomValues = getRandomValues();
                long[] otherRandomValues = other.getRandomValues();
                for (int i = 0; i < thisRandomValues.length; i++) {
                    int result = compare(thisRandomValues[i], otherRandomValues[i]);
                    if (result != 0) {
                        return result;
                    }
                }
                return 0;
            }

            private static int compare(long one, long two) {
                return (one < two) ? -1 : ((one == two) ? 0 : 1);
            }
        }

        ShufflingComparator(Random random) {
            this.random = random;
        }

        public int compare(Description o1, Description o2) {
            RandomValueHolder holder = new RandomValueHolder(random);
            RandomValueHolder h1 = map.putIfAbsent(o1, holder);
            if (h1 == null) {
                h1 = holder;
                holder = new RandomValueHolder(random);
            }
            RandomValueHolder h2 = map.putIfAbsent(o2, holder);
            if (h2 == null) {
                h2 = holder;
            }
            return h1.compareTo(h2);
        }
    }
}
