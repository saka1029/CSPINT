package test.jp.saka1029.cspint.sequential;

import java.util.logging.Logger;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import jp.saka1029.cspint.sequential.Constraint;
import jp.saka1029.cspint.sequential.Domain;
import jp.saka1029.cspint.sequential.Problem;
import jp.saka1029.cspint.sequential.Solver;
import jp.saka1029.cspint.sequential.Variable;

class TestSendMoreMoney {

    Logger logger = Logger.getLogger(TestSendMoreMoney.class.getName());

    static int number(int... digits) {
        return IntStream.of(digits).reduce(0, (a, b) -> a * 10 + b);
    }

    @Test
    public void testSingleConstraint() {
        Domain zero = Domain.range(0, 10);
        Domain one = Domain.range(1, 10);
        Problem p = new Problem();
        Variable S = p.variable("S", one);
        Variable E = p.variable("E", zero);
        Variable N = p.variable("N", zero);
        Variable D = p.variable("D", zero);
        Variable M = p.variable("M", one);
        Variable O = p.variable("O", zero);
        Variable R = p.variable("R", zero);
        Variable Y = p.variable("Y", zero);
        Variable[] variables = {S, E, N, D, M, O, R, Y};
        for (Variable x : variables)
            for (Variable y : variables)
                if (x.name.compareTo(y.name) < 0)
                    p.constraint((a, b) -> a != b, x, y);
        Constraint c = p.constraint(
            (s, e, n, d, m, o, r, y) -> number(s, e, n, d) + number(m, o, r, e) == number(m, o, n, e, y),
            S, E, N, D, M, O, R, Y);
        Solver solver = new Solver();
        solver.solve(p, a -> logger.info("" + a));
    }

    @Test
    public void testDigitConstraint() {
        Domain zero = Domain.range(0, 10);
        Domain one = Domain.range(1, 10);
        Domain carry = Domain.of(0, 1);
        Problem p = new Problem();
        Variable C1 = p.variable("C1", carry);
        Variable C2 = p.variable("C2", carry);
        Variable C3 = p.variable("C3", carry);
        Variable S = p.variable("S", one);
        Variable E = p.variable("E", zero);
        Variable N = p.variable("N", zero);
        Variable D = p.variable("D", zero);
        Variable M = p.variable("M", one);
        Variable O = p.variable("O", zero);
        Variable R = p.variable("R", zero);
        Variable Y = p.variable("Y", zero);
        Variable[] variables = {S, E, N, D, M, O, R, Y};
        for (Variable x : variables)
            for (Variable y : variables)
                if (x.name.compareTo(y.name) < 0)
                    p.constraint((a, b) -> a != b, x, y);
        //   C3 C2 C1
        //    S  E  N  D
        //  + M  O  R  E
        // -------------
        // M  O  N  E  Y
        p.constraint((d, e, y, c1) -> d + e == 10 * c1 + y, D, E, Y, C1);
        p.constraint((n, r, e, c1, c2) -> n + r + c1 == 10 * c2 + e, N, R, E, C1, C2);
        p.constraint((e, o, n, c2, c3) -> e + o + c2 == 10 * c3 + n, E, O, N, C2, C3);
        p.constraint((s, m, o, c3) -> s + m + c3 == m * 10 + o, S, M, O, C3);
        Solver solver = new Solver();
        solver.solve(p, a -> logger.info("" + a));
    }

}
