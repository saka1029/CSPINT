package test.jp.saka1029.cspint.sequential;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import jp.saka1029.cspint.sequential.Constraint;
import jp.saka1029.cspint.sequential.Domain;
import jp.saka1029.cspint.sequential.Problem;
import jp.saka1029.cspint.sequential.Variable;

class TestProblem {

    @Test
    void testVariable() {
        Problem p = new Problem();
        Variable A = p.variable("a", Domain.range(1, 4));
        assertEquals(1, p.variables.size());
        assertEquals(A, p.variables.get(0));
        assertEquals(Domain.of(1, 2, 3), p.variables.get(0).domain);
        assertEquals(0, p.variables.get(0).constraints.size());
        Variable B = p.variable("b", Domain.range(1, 3));
        assertEquals(2, p.variables.size());
        assertEquals(A, p.variables.get(0));
        assertEquals(B, p.variables.get(1));
        assertEquals(Domain.of(1, 2), p.variables.get(1).domain);
        assertEquals(0, p.variables.get(1).constraints.size());
        Constraint c = p.constraint((x, y) -> x != y, A, B);
    }

}
