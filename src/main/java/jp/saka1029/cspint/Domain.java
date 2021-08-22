package jp.saka1029.cspint;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Domain {

    final int[] values;

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

    public static Domain rangeClosed(int start, int end) {
        return range(start, end + 1);
    }

    public int size() {
        return values.length;
    }

    public int get(int index) {
        return values[index];
    }
    
    public IntStream stream() {
        return IntStream.of(values);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Domain
            && Arrays.equals(((Domain)obj).values, values);
    }

    @Override
    public String toString() {
        return Arrays.toString(values);
    }
}
