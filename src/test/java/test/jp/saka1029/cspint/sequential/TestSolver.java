package test.jp.saka1029.cspint.sequential;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.Test;

import jp.saka1029.cspint.sequential.Constraint;
import jp.saka1029.cspint.sequential.Domain;
import jp.saka1029.cspint.sequential.Predicate;
import jp.saka1029.cspint.sequential.Problem;
import jp.saka1029.cspint.sequential.Solver;
import jp.saka1029.cspint.sequential.Variable;

class TestSolver {

    @Test
    void test() {
        Domain domain = Domain.range(1, 4);
        Problem problem = new Problem();
        Variable a = problem.variable("a", domain);
        Variable b = problem.variable("b", domain);
        Variable c = problem.variable("c", domain);
        Predicate diff = args -> args[0] != args[1];
        Predicate equation = args -> args[0] + args[1] == args[2];
        Constraint ab = problem.constraint(diff, a, b);
        Constraint ac = problem.constraint(diff, a, c);
        Constraint bc = problem.constraint(diff, b, c);
        Constraint abc = problem.constraint(equation, a, b, c);
        Solver solver = new Solver();
        Constraint[][] constraints = solver.constraints(problem);
        assertEquals(3, problem.variables.size());
        assertEquals(3, constraints.length);
        assertNull(constraints[0]);
        assertNotNull(constraints[1]);
        assertEquals(Set.of(ab), Set.of(constraints[1]));
        assertNotNull(constraints[2]);
        assertEquals(Set.of(ac, bc, abc), Set.of(constraints[2]));
    }

}
