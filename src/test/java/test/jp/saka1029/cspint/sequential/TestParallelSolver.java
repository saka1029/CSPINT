package test.jp.saka1029.cspint.sequential;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import jp.saka1029.cspint.sequential.Constraint;
import jp.saka1029.cspint.sequential.Domain;
import jp.saka1029.cspint.sequential.ParallelSolver;
import jp.saka1029.cspint.sequential.Predicate2;
import jp.saka1029.cspint.sequential.Predicate3;
import jp.saka1029.cspint.sequential.Problem;
import jp.saka1029.cspint.sequential.SequentialSolver;
import jp.saka1029.cspint.sequential.Solver;
import jp.saka1029.cspint.sequential.Variable;

class TestParallelSolver {

    static Logger logger = Logger.getLogger(TestParallelSolver.class.getName());

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
        Solver solver = new ParallelSolver();
        List<List<Constraint>> constraints = SequentialSolver.constraintOrder(problem, problem.variables);
        assertEquals(3, problem.variables.size());
        assertEquals(3, constraints.size());
        assertEquals(0, constraints.get(0).size());
        assertEquals(1, constraints.get(1).size());
        assertEquals(Set.of(ab), new HashSet<>(constraints.get(1)));
        assertEquals(3, constraints.get(2).size());
        assertEquals(Set.of(ac, bc, abc), new HashSet<>(constraints.get(2)));
        Set<Map<Variable, Integer>> actual = Collections.synchronizedSet(new HashSet<>());
        assertEquals(2, solver.solve(problem, (control, map) -> actual.add(Map.copyOf(map))));
        Set<Map<Variable, Integer>> expected = Set.of(
            Map.of(a, 1, b, 2, c, 3),
            Map.of(a, 2, b, 1, c, 3)
        );
        assertEquals(expected, actual);
    }
}
