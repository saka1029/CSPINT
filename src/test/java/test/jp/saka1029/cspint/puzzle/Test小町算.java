package test.jp.saka1029.cspint.puzzle;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import jp.saka1029.cspint.Domain;
import jp.saka1029.cspint.Problem;
import jp.saka1029.cspint.Solver;
import jp.saka1029.cspint.Variable;
import test.jp.saka1029.cspint.Common;

public class Test小町算 {

    static final Logger logger = Common.getLogger(Test小町算.class);

    static int calc(int[] digits, int[] ops) {
        int total = 0, term = 0;
        int op = 1;
        for (int i = 0, size = ops.length; i < size; ++i)
            if (ops[i] != 0) {
                total += op * term;
                op = ops[i];
                term = digits[i];
            } else
                term = term * 10 + digits[i];
        total += op * term;
        return total;
    }

    static String format(int[] digits, int[] ops) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0, size = ops.length; i < size; ++i) {
            switch (ops[i]) {
            case -1 : sb.append("-"); break;
            case 0 : break;
            case 1 : sb.append("+"); break;
            }
            sb.append(digits[i]);
        }
        return sb.toString();
    }

    @Test
    public void testCalc() {
        int[] digits = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        assertEquals(+1+2+3+4+5+6+7+8+9, calc(digits, new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1}));
        assertEquals(-1-2-3-4-5-6-7-8-9, calc(digits, new int[] {-1, -1, -1, -1, -1, -1, -1, -1, -1}));
        assertEquals(123456789, calc(digits, new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0}));
        assertEquals(-123456789, calc(digits, new int[] {-1, 0, 0, 0, 0, 0, 0, 0, 0}));
        assertEquals(123+456+789, calc(digits, new int[] {0, 0, 0, 1, 0, 0, 1, 0, 0}));
        assertEquals(12+34+56+78+9, calc(digits, new int[] {0, 0, 1, 0, 1, 0, 1, 0, 1}));
        assertEquals(-12+34+56+78+9, calc(digits, new int[] {-1, 0, 1, 0, 1, 0, 1, 0, 1}));
    }

    @Test
    public void test小町算() {
        int[] digits = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        Problem p = new Problem();
        Domain first = Domain.of(-1, 0);
        Domain rest = Domain.of(-1, 0, 1);
        Variable[] vars = new Variable[9];
        vars[0] = p.variable("@0", first);
        for (int i = 1; i < 9; ++i)
            vars[i] = p.variable("@" + i, rest);
        p.constraint(a -> calc(digits, a) == 100, vars);
        new Solver().solve(p, (c, m) -> logger.info(
            format(digits,
                Arrays.stream(vars).mapToInt(v -> m.get(v)).toArray())));
    }

}
