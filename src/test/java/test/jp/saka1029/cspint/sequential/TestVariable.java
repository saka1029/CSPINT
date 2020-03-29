package test.jp.saka1029.cspint.sequential;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import jp.saka1029.cspint.sequential.Constraint;
import jp.saka1029.cspint.sequential.Domain;
import jp.saka1029.cspint.sequential.Problem;
import jp.saka1029.cspint.sequential.Variable;

class TestVariable {

    static final Logger logger = Logger.getLogger(TestVariable.class.getName());

    @Test
    void testVariable() {
        Problem p = new Problem();
        Domain d = Domain.of(1, 2);
        Variable v = p.variable("v", d);
        Constraint c1 = p.constraint(x -> x == 1, v);
        Constraint c2 = p.constraint(x -> x == 2, v);
        assertEquals("v", v.name);
        assertEquals(d, v.domain);
        assertEquals(List.of(c1, c2), v.constraints);
        assertEquals(List.of(c1, c2), p.constraints);
    }

    @Test
    void testVariable2() {
        Problem p = new Problem();
        Domain d1 = Domain.of(1, 2);
        Domain d2 = Domain.of(3, 4);
        Variable v1 = p.variable("v1", d1);
        Variable v2 = p.variable("v2", d2);
        Constraint c = p.constraint((x, y) -> x == y, v1, v2);
        assertEquals("v1", v1.name);
        assertEquals(d1, v1.domain);
        assertEquals(List.of(c), v1.constraints);
        assertEquals("v2", v2.name);
        assertEquals(d2, v2.domain);
        assertEquals(List.of(c), v2.constraints);
        assertEquals(List.of(c), p.constraints);
    }

    @Test
    public void testConstraints() {
        Problem p = new Problem();
        Domain d = Domain.of(1, 2);
        Variable v = p.variable("v", d);
        p.constraint(x -> x == 1, v);
        try {
            v.constraints.set(0, null);
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

}
