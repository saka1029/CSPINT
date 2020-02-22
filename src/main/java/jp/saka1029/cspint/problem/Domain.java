package jp.saka1029.cspint.problem;

import java.util.Arrays;
import java.util.Iterator;

public class Domain implements Iterable<Integer> {

    private final int[] values;

    private Domain(int[] values) {
        this.values = values;
    }

    public static Domain of(int... values) {
        return new Domain(Arrays.copyOf(values, values.length));
    }

    public static Domain range(int start, int end) {
        if (end < start) throw new IllegalArgumentException("start must <= end");
        int[] values = new int[end - start];
        for (int i = 0, v = start; v < end; ++i, ++v)
            values[i] = v;
        return new Domain(values);
    }

    public int size() {
        return values.length;
    }

    public int get(int index) {
        return values[index];
    }

    @Override
    public Iterator<Integer> iterator() {
        return Arrays.stream(values).iterator();
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Domain)) return false;
        Domain o = (Domain)obj;
        return Arrays.equals(values, o.values);
    }

    @Override
    public String toString() {
        return Arrays.toString(values);
    }
}
