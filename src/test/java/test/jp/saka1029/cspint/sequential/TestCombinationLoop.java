package test.jp.saka1029.cspint.sequential;

import java.util.Arrays;
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

    @Test
    void test() {
        recursive(values, a -> logger.info(Arrays.toString(a)));
    }

}
