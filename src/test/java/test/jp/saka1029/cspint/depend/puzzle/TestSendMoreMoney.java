package test.jp.saka1029.cspint.depend.puzzle;

import java.util.Arrays;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import jp.saka1029.cspint.depend.Derivation;
import jp.saka1029.cspint.depend.Domain;
import jp.saka1029.cspint.depend.Predicate;
import jp.saka1029.cspint.depend.Problem;
import jp.saka1029.cspint.depend.Solver;
import jp.saka1029.cspint.depend.Variable;

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
    void test() {
        Problem p = new Problem();
        Domain ZERO = Domain.of(0);
        Domain FIRST = Domain.rangeClosed(1, 9);
        Domain REST = Domain.rangeClosed(0, 9);
        Derivation ADD = a -> a[0] + a[1] + a[2] / 10;
        Derivation LOWER = a -> a[0] % 10;
        Predicate UP = a -> a[0] == a[1] / 10;
        Predicate LOW = a -> a[0] == a[1] % 10;
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
        Predicate NE = a -> a[0] != a[1];
        for (int i = 0, size = variables.length; i < size; ++i)
            for (int j = i + 1; j < size; ++j)
                p.constraint(NE, variables[i], variables[j]);
        Solver s = new Solver();
        s.solve(p, m -> logger.info("answer: " + m));
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
    public void test制約1個() {
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
            a -> number(a[0], a[1], a[2], a[3]) + number(a[4], a[5], a[6], a[1]), S, E, N, D, M, O, R);
        Variable Y = p.variable("Y", a -> digit(a[0], 0), SUM);
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
        s.solve(p, m -> logger.info("answer: " + m));
        logger.info("bind count: " + Arrays.toString(s.bindCount));
    }

}
