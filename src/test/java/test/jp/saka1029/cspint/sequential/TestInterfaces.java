package test.jp.saka1029.cspint.sequential;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TestInterfaces {

    interface Op {
        int apply(int... a);
    }
    interface Op3 {
        int apply(int a, int b, int c);
    }
    interface Op2 {
        int apply(int a, int b);
        default Op3 and(Op2 and) {
            return (a, b, c) -> apply(apply(a, b), c);
        }
    }

    interface Pr1 {
        boolean test(int a);
    }

    interface Pr2 {
        boolean test(int a, int b);
    }

    @Test
    void test() {
        Op2 add = (a, b) -> a + b;
        Op3 add3 = add.and(add);
        assertEquals(6, add3.apply(1, 2, 3));
        Pr2 eq = (a, b) -> a == b;
    }

}
