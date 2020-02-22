package test.jp.saka1029.cspint.problem;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import jp.saka1029.cspint.problem.Domain;
import jp.saka1029.cspint.problem.Problem;
import jp.saka1029.cspint.problem.Variable;

class TestProblem {

    @Test
    void testVariable() {
        Problem p = new Problem();
        Variable v = p.variable("a", Domain.range(1, 4));
        assertEquals(1, p.variables.size());
        assertEquals(v, p.variables.get(0));
        assertEquals(Domain.of(1, 2, 3), p.variables.get(0).domain);
        assertEquals(0, p.variables.get(0).constraints.size());
    }

}
