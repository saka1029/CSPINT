package test.jp.saka1029.cspint.sequential;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import jp.saka1029.cspint.sequential.Constraint;
import jp.saka1029.cspint.sequential.Domain;
import jp.saka1029.cspint.sequential.Problem;
import jp.saka1029.cspint.sequential.Variable;

class TestConstraint {

    @Test
    void testVariables() {
        Problem p = new Problem();
        Variable v = p.variable("v", Domain.of(1, 2));
        Constraint c = p.constraint(x -> x == 1, v);
        assertEquals(List.of(v), c.variables);
    }

    @Test
    void testPredicate() {
        Problem p = new Problem();
        Variable v = p.variable("v", Domain.of(1, 2));
        Constraint c = p.constraint(x -> x == 1, v);
        assertEquals(true, c.predicate.test(1));
    }

    @Test
    void testVariablesUnmodifiable() {
        Problem p = new Problem();
        Variable v = p.variable("v", Domain.of(1, 2));
        Constraint c = p.constraint(x -> x == 1, v);
        try {
            c.variables.set(0, null);
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    @Test
    void testToString() {
        Problem p = new Problem();
        Variable v = p.variable("v", Domain.of(1, 2));
        Constraint c = p.constraint(x -> x == 1, v);
        assertEquals("制約[v]", c.toString());
    }

}
