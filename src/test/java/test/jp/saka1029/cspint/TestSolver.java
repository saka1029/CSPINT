package test.jp.saka1029.cspint;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import jp.saka1029.cspint.Constraint;
import jp.saka1029.cspint.Domain;
import jp.saka1029.cspint.Predicate2;
import jp.saka1029.cspint.Predicate3;
import jp.saka1029.cspint.Problem;
import jp.saka1029.cspint.Solver;
import jp.saka1029.cspint.Variable;

class TestSolver {

    static Logger logger = Common.getLogger(TestSolver.class);

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
        List<List<Constraint>> constraints = Solver.constraintOrder(problem, problem.variables);
        assertEquals(3, problem.variables.size());
        assertEquals(3, constraints.size());
        assertEquals(0, constraints.get(0).size());
        assertEquals(1, constraints.get(1).size());
        assertEquals(Set.of(ab), new HashSet<>(constraints.get(1)));
        assertEquals(3, constraints.get(2).size());
        assertEquals(Set.of(ac, bc, abc), new HashSet<>(constraints.get(2)));
        List<Map<Variable, Integer>> actual = new ArrayList<>();
        solver.solve(problem, (control, map) -> actual.add(Map.copyOf(map)));
        List<Map<Variable, Integer>> expected = List.of(
            Map.of(a, 1, b, 2, c, 3),
            Map.of(a, 2, b, 1, c, 3)
        );
        assertEquals(expected, actual);
        Solver.printConstraintOrder(problem);
        Solver.printConstraintOrder(problem);
    }

    @Test
    public void testConstraintOrder() {
        Problem p = new Problem();
        Domain d = Domain.range(0, 100);
        Variable A = p.variable("A", d);
        Variable B = p.variable("B", d);
        Constraint C1 = p.constraint(a -> a < 2, A);
        Constraint C2 = p.constraint(b -> b < 2, B);
        assertEquals(List.of(B, A), Solver.bindingOrder(List.of(C2, C1)));
    }

    @Test
    public void testInvalidBindingOrder() {
        Problem p = new Problem();
        Domain d = Domain.range(0, 100);
        Variable A = p.variable("A", d);
        Variable B = p.variable("B", d);
        Solver s = new Solver();
        try {
        	Solver.constraintOrder(p, List.of(B));
        	fail();
        } catch (IllegalArgumentException e) {
        }
        try {
        	s.solve(p, List.of(A), (c, m) ->{});
        	fail();
        } catch (IllegalArgumentException e) {
        }

    }

}
