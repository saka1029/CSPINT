package test.jp.saka1029.cspint.depend.puzzle;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import jp.saka1029.cspint.depend.Derivation1;
import jp.saka1029.cspint.depend.Derivation3;
import jp.saka1029.cspint.depend.Domain;
import jp.saka1029.cspint.depend.Predicate;
import jp.saka1029.cspint.depend.Predicate2;
import jp.saka1029.cspint.depend.Problem;
import jp.saka1029.cspint.depend.Solver;
import jp.saka1029.cspint.depend.Variable;
import test.jp.saka1029.cspint.Common;

class TestSendMoreMoney {

    static final Logger logger = Logger.getLogger(TestSendMoreMoney.class.getName());

    /**
     *     S  E  N  D
     *  +  M  O  R  E
     *  --------------
     *  M  O  N  E  Y
     * S5 S4 S3 S2 S1 S0
     *
     */
    @Test
    void test各桁ごとに導出() {
        logger.info(Common.methodName());
        Problem p = new Problem();
        Domain ZERO = Domain.of(0);
        Domain FIRST = Domain.rangeClosed(1, 9);
        Domain REST = Domain.rangeClosed(0, 9);
        Derivation3 ADD = (a, b, c) -> a + b + c / 10;
        Derivation1 LOWER = a -> a % 10;
        Predicate2 UP = (a, b) -> a == b / 10;
        Predicate2 LOW = (a, b) -> a == b % 10;
    /*
     *     S  E  N  D
     *  +  M  O  R  E
     *  --------------
     *  M  O  N  E  Y
     * S5 S4 S3 S2 S1 Z
     */
        Variable Z = p.variable("Z", ZERO);
        Variable D = p.variable("D", REST);
        Variable E = p.variable("E", REST);
        Variable S1 = p.variable("S1", ADD, D, E, Z); Variable Y = p.variable("Y", LOWER, S1);
        Variable N = p.variable("N", REST);
        Variable R = p.variable("R", REST);
        Variable S2 = p.variable("S2", ADD, N, R, S1); p.constraint(LOW, E, S2);
        Variable O = p.variable("O", REST);
        Variable S3 = p.variable("S3", ADD, E, O, S2); p.constraint(LOW, N, S3);
        Variable S = p.variable("S", FIRST);
        Variable M = p.variable("M", FIRST);
        Variable S4 = p.variable("S4", ADD, S, M, S3); p.constraint(LOW, O, S4);
        p.constraint(UP, M, S4);
        Variable[] variables = {S, E, N, D, M, O, R, Y};
        Predicate2 NE = (a, b) -> a != b;
        for (int i = 0, size = variables.length; i < size; ++i)
            for (int j = i + 1; j < size; ++j)
                p.constraint(NE, variables[i], variables[j]);
        Solver s = new Solver();
        assertEquals(1, s.solve(p, m -> logger.info("answer: " + m)));
        logger.info("bind count: " + Arrays.toString(s.bindCount));
    }

    static int number(int... digits) {
        return Arrays.stream(digits).reduce(0, (a, b) -> 10 * a + b);
    }

    static int digit(int number, int n) {
        for (int i = 0; i < n; ++i)
            number /= 10;
        return number % 10;
    }

    static Predicate digit(int n) {
        return a -> digit(a[0], n) == a[1];
    }

    @Test
    public void testまとめて導出() {
        logger.info(Common.methodName());
        Problem p = new Problem();
        Domain FIRST = Domain.rangeClosed(1, 9);
        Domain REST = Domain.rangeClosed(0, 9);
        Variable S = p.variable("S", FIRST); // 0
        Variable E = p.variable("E", REST);  // 1
        Variable N = p.variable("N", REST);  // 2
        Variable D = p.variable("D", REST);  // 3
        Variable M = p.variable("M", FIRST); // 4
        Variable O = p.variable("O", REST);  // 5
        Variable R = p.variable("R", REST);  // 6
//        Variable Y = p.variable("Y", REST);  // 7
        Variable SUM = p.variable("SUM",
            (s, e, n, d, m, o, r) -> number(s, e, n, d) + number(m, o, r, e)
        , S, E, N, D, M, O, R);
        Variable Y = p.variable("Y", a -> digit(a, 0), SUM);
        p.constraint(digit(1), SUM, E);
        p.constraint(digit(2), SUM, N);
        p.constraint(digit(3), SUM, O);
        p.constraint(digit(4), SUM, M);
        Variable[] variables = {S, E, N, D, M, O, R, Y};
        Predicate NE = a -> a[0] != a[1];
        for (int i = 0, size = variables.length; i < size; ++i)
            for (int j = i + 1; j < size; ++j)
                p.constraint(NE, variables[i], variables[j]);
        Solver s = new Solver();
        assertEquals(1, s.solve(p, m -> logger.info("answer: " + m)));
        logger.info("bind count: " + Arrays.toString(s.bindCount));
    }

}
