package xyz.epicebic.ebiclib.collection;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

/**
 * The WeightedRandom class allows random selection from a collection of items with different weights.
 * The probability of selecting an item is proportional to its weight.
 *
 * @param <T> the type of items stored in the weighted random.
 *
 * @author Jishuna
 */
public class WeightedRandom<T> {
    private final NavigableMap<Double, T> map = new TreeMap<>();
    private final Random random;
    private double total = 0;

    public WeightedRandom() {
        this(new Random());
    }

    public WeightedRandom(Random random) {
        this.random = random;
    }

    public WeightedRandom<T> add(double weight, T result) {
        if (weight <= 0)
            return this;

        total += weight;
        map.put(total, result);
        return this;
    }

    public T poll() {
        double value = random.nextDouble() * total;
        return map.higherEntry(value).getValue();
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    public int size() {
        return this.map.size();
    }

    public Set<Entry<Double, T>> getEntries() {
        double prev = 0;
        Set<Entry<Double, T>> entries = new LinkedHashSet<>();

        for (Entry<Double, T> entry : this.map.entrySet()) {
            entries.add(Map.entry(entry.getKey() - prev, entry.getValue()));
            prev = entry.getKey();
        }

        return entries;
    }
}