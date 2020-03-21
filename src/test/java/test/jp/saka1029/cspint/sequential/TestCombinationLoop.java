package test.jp.saka1029.cspint.sequential;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

class TestCombinationLoop {

    static Logger logger = Logger.getLogger(TestCombinationLoop.class.toString());

    static int[][] values = {
        {0, 1, 2},
        {0, 10},
        {0, 100, 200},
    };

    interface Callback {
        void answer(int[] values);
    }

    static void recursive(int[][] values, Callback callback) {
        int size = values.length;
        new Object() {
            int[] result = new int[values.length];
            void run(int i) {
                if (i >= size)
                    callback.answer(result);
                else
                    for (int j = 0; j < values[i].length; ++j) {
                        result[i] = values[i][j];
                        run(i + 1);
                    }
            }
        }.run(0);
    }

    int[][] expected = {
        {0, 0, 0},
        {0, 0, 100},
        {0, 0, 200},
        {0, 10, 0},
        {0, 10, 100},
        {0, 10, 200},
        {1, 0, 0},
        {1, 0, 100},
        {1, 0, 200},
        {1, 10, 0},
        {1, 10, 100},
        {1, 10, 200},
        {2, 0, 0},
        {2, 0, 100},
        {2, 0, 200},
        {2, 10, 0},
        {2, 10, 100},
        {2, 10, 200},
    };

    @Test
    void test() {
        List<int[]> list = new ArrayList<>();
        recursive(values, a -> list.add(Arrays.copyOf(a, a.length)));
        int[][] actual = list.toArray(new int[0][0]);
        assertArrayEquals(expected, actual);
    }

}
