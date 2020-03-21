package test.jp.saka1029.cspint.sequential;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import jp.saka1029.cspint.sequential.Answer;
import jp.saka1029.cspint.sequential.Domain;
import jp.saka1029.cspint.sequential.Problem;
import jp.saka1029.cspint.sequential.Solver;
import jp.saka1029.cspint.sequential.Variable;

class TestSendMoreMoney {

    Logger logger = Logger.getLogger(TestSendMoreMoney.class.getName());

    static int number(int... digits) {
        return IntStream.of(digits).reduce(0, (a, b) -> a * 10 + b);
    }

    // {C1=1, C2=1, C3=0, S=9, E=5, N=6, D=7, M=1, O=0, R=8, Y=2}
    static class AssertAnswer implements Answer {

        final Problem problem;

        AssertAnswer(Problem problem) {
            this.problem = problem;
        }

        int value(Map<Variable, Integer> result, String name) {
            return (int) result.get(problem.variable(name));
        }

        @Override
        public void answer(Map<Variable, Integer> result) {
            if (result.containsKey(problem.variable("C2"))) {
                assertEquals(1, (int) value(result, "C1"));
                assertEquals(1, (int) value(result, "C2"));
                assertEquals(0, (int) value(result, "C3"));
            }
            assertEquals(9, (int) value(result, "S"));
            assertEquals(5, (int) value(result, "E"));
            assertEquals(7, (int) value(result, "D"));
            assertEquals(1, (int) value(result, "M"));
            assertEquals(0, (int) value(result, "O"));
            assertEquals(8, (int) value(result, "R"));
            assertEquals(2, (int) value(result, "Y"));
        }

    };

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
        p.constraint(
            (s, e, n, d, m, o, r, y) -> number(s, e, n, d) + number(m, o, r, e) == number(m, o, n, e, y),
            S, E, N, D, M, O, R, Y);
        Solver solver = new Solver();
        solver.solve(p, new AssertAnswer(p));
    }

    static Problem digitConstraint() {
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
        // C3 C2 C1
        // S E N D
        // + M O R E
        // -------------
        // M O N E Y
        p.constraint((d, e, y, c1) -> d + e == 10 * c1 + y, D, E, Y, C1);
        p.constraint((n, r, e, c1, c2) -> n + r + c1 == 10 * c2 + e, N, R, E, C1, C2);
        p.constraint((e, o, n, c2, c3) -> e + o + c2 == 10 * c3 + n, E, O, N, C2, C3);
        p.constraint((s, m, o, c3) -> s + m + c3 == m * 10 + o, S, M, O, C3);
        return p;
    }

    @Test
    public void testDigitConstraintSimple() {
        Problem p = digitConstraint();
        Solver solver = new Solver();
        solver.solve(p, new AssertAnswer(p));
    }

    @Test
    public void testDigitConstraintRightToLeft() {
        Problem p = digitConstraint();
        List<Variable> resolvingOrder = List.of(
            p.variable("D"), p.variable("E"), p.variable("Y"), p.variable("C1"),
            p.variable("N"), p.variable("R"), p.variable("C2"),
            p.variable("O"), p.variable("C3"),
            p.variable("S"), p.variable("M"));
        Solver solver = new Solver();
        solver.solve(p, resolvingOrder, new AssertAnswer(p));
    }

}
