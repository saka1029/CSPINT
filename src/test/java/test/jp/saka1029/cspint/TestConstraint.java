package test.jp.saka1029.cspint;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import jp.saka1029.cspint.Constraint;
import jp.saka1029.cspint.Domain;
import jp.saka1029.cspint.Problem;
import jp.saka1029.cspint.Variable;

class TestConstraint {

    static final Logger logger = Common.getLogger(TestConstraint.class);

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
    public void testIsDifferent() {
        Problem p = new Problem();
        Variable V = p.variable("V", Domain.of(1, 2));
        Variable W = p.variable("W", Domain.of(1, 2));
        Constraint c = p.constraint((v, w) -> v != w, V, W);
        p.allDifferent(V, W);
        assertFalse(c.isAllDifferent());
        assertTrue(p.constraints.get(1).isAllDifferent());
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
