package test.jp.saka1029.cspint.sequential;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import jp.saka1029.cspint.sequential.Constraint;
import jp.saka1029.cspint.sequential.Domain;
import jp.saka1029.cspint.sequential.Predicate2;
import jp.saka1029.cspint.sequential.Predicate3;
import jp.saka1029.cspint.sequential.Problem;
import jp.saka1029.cspint.sequential.Solver;
import jp.saka1029.cspint.sequential.Variable;

class TestSolver {

    static Logger logger = Logger.getLogger(TestSolver.class.getName());

    @Test
    void testSimple() {
        Domain domain = Domain.range(1, 4);
        Problem problem = new Problem();
        Variable a = problem.variable("a", domain);
        Variable b = problem.variable("b", domain);
        Variable c = problem.variable("c", domain);
        Predicate2 diff = (x, y) -> x != y;
        Predicate3 equation = (x, y, z) -> x + y == z;
        Constraint ab = problem.constraint(diff, a, b);
        Constraint ac = problem.constraint(diff, a, c);
        Constraint bc = problem.constraint(diff, b, c);
        Constraint abc = problem.constraint(equation, a, b, c);
        Solver solver = new Solver();
        List<List<Constraint>> constraints = Solver.constraintLists(problem, problem.variables);
        assertEquals(3, problem.variables.size());
        assertEquals(3, constraints.size());
        assertEquals(0, constraints.get(0).size());
        assertEquals(1, constraints.get(1).size());
        assertEquals(Set.of(ab), new HashSet<>(constraints.get(1)));
        assertEquals(3, constraints.get(2).size());
        assertEquals(Set.of(ac, bc, abc), new HashSet<>(constraints.get(2)));
        List<Map<Variable, Integer>> actual = new ArrayList<>();
        solver.solve(problem, map -> actual.add(Map.copyOf(map)));
        List<Map<Variable, Integer>> expected = List.of(
            Map.of(a, 1, b, 2, c, 3),
            Map.of(a, 2, b, 1, c, 3)
        );
        assertEquals(expected, actual);
    }

    static int number(int... digits) {
        return IntStream.of(digits).reduce(0, (a, b) -> 10 * a + b);
    }

    /*
     * A, B, C, D, E, F, G, H, I:{1..9}
     * A < D < G
     * (A / BC) + (D / EF) + (G / HI) = 1
     */
    @Test
    public void test分数の和() {
        Problem p = new Problem();
        Domain digit = Domain.rangeClosed(1, 9);
        Variable A = p.variable("A", digit);
        Variable B = p.variable("B", digit);
        Variable C = p.variable("C", digit);
        Variable D = p.variable("D", digit);
        Variable E = p.variable("E", digit);
        Variable F = p.variable("F", digit);
        Variable G = p.variable("G", digit);
        Variable H = p.variable("H", digit);
        Variable I = p.variable("I", digit);
        p.allDifferent(A, B, C, D, E, F, G, H, I);
        p.constraint((a, b, c, d, e, f, g, h, i) -> {
            int bc = number(b, c), ef = number(e, f), hi = number(h, i);
            return a * ef * hi + d * bc * hi + g * bc * ef == bc * ef * hi;
        }, A, B, C, D, E, F, G, H, I);
        p.constraint((a, d, g) -> a < d && d < g, A, D, G);
        Solver s = new Solver();
        s.solve(p, a -> logger.info("answer: " + a));
        List<List<Constraint>> order = Solver.constraintLists(p, p.variables);
        for (int i = 0; i < order.size(); ++i)
            logger.info(p.variables.get(i) + ":" + order.get(i));
        logger.info("束縛回数: " + Arrays.toString(s.bindCount));
    }

    @Test
    public void test分数の和2() {
        Problem p = new Problem();
        Domain digit = Domain.rangeClosed(1, 9);
        String[] names = {"A", "B", "C", "D", "E", "F", "G", "H", "I"};
        Variable[] vars = Stream.of(names).map(n -> p.variable(n, digit)).toArray(Variable[]::new);
        p.allDifferent(vars);
        p.constraint(v -> {
            int bc = number(v[1], v[2]), ef = number(v[4], v[5]), hi = number(v[7], v[8]);
            return v[0] * ef * hi + v[3] * bc * hi + v[6] * bc * ef == bc * ef * hi;
        }, vars[0], vars[1], vars[2], vars[3], vars[4], vars[5], vars[6], vars[7], vars[8]);
        p.constraint((a, d, g) -> a < d && d < g, vars[0], vars[3], vars[6]);
        Solver s = new Solver();
        s.solve(p, a -> logger.info("answer: " + a));
        List<List<Constraint>> order = Solver.constraintLists(p, p.variables);
        for (int i = 0; i < order.size(); ++i)
            logger.info(p.variables.get(i) + ":" + order.get(i));
        logger.info("束縛回数: " + Arrays.toString(s.bindCount));
    }

}
