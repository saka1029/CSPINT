package test.jp.saka1029.cspint.depend;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import jp.saka1029.cspint.depend.BaseVariable;
import jp.saka1029.cspint.depend.Constraint;
import jp.saka1029.cspint.depend.Derivation2;
import jp.saka1029.cspint.depend.DerivedVariable;
import jp.saka1029.cspint.depend.Domain;
import jp.saka1029.cspint.depend.Predicate2;
import jp.saka1029.cspint.depend.Problem;
import jp.saka1029.cspint.depend.Variable;

class TestProblem {

    static Logger logger = Logger.getLogger(TestProblem.class.getName());

    /**
     * 変数: A, B, C, D : {1, 2, 3}
     * 導出: C = A + B
     * 制約: C < D
     */
    @Test
    public void test参照() {
        Problem p = new Problem();
        Domain d = Domain.of(1, 2, 3);
        BaseVariable A = p.variable("A", d);
        BaseVariable B = p.variable("B", d);
        Derivation2 E = (a, b) -> a + b;
        DerivedVariable C = p.variable("C", E, A, B);
        BaseVariable D = p.variable("D", d);
        Predicate2 P = (a, b) -> a < b;
        Constraint T = p.constraint(P, C, D);
        assertEquals("A", A.name());
        assertEquals(d, A.domain());
        assertEquals(List.of(C), A.dependents());
        assertEquals(List.of(C), B.dependents());
        assertEquals("C", C.name());
        assertEquals(E, C.derivation());
        assertEquals(List.of(A, B), C.variables());
        assertEquals(List.of(T), C.dependents());
        assertEquals(List.of(T), D.dependents());
        assertEquals(List.of(A, B), C.variables());
        assertEquals(P, T.predicate());
        assertEquals(List.of(C, D), T.variables());
        assertEquals(A, p.variable("A"));
        assertEquals(List.of(A, B, C, D), p.variables());
        assertEquals(List.of(C, T), p.dependents());
        assertEquals(3, p.baseVariables().size());
        logger.info(p.toString());
    }

    @Test
    public void testVariableError() {
        Problem p = new Problem();
        Domain d = Domain.of(1);
        BaseVariable V = p.variable("V", d);
        try {
            BaseVariable W = p.variable("V", d);
            fail();
        } catch (IllegalArgumentException e) {}
        try {
            DerivedVariable W = p.variable("V", a -> a + 1, V);
            fail();
        } catch (IllegalArgumentException e) {}
        try {
            DerivedVariable W = p.variable("V", a -> a + 1, V);
            fail();
        } catch (IllegalArgumentException e) {}
        try {
            DerivedVariable W = p.variable("W", a -> a[0] + 1);
            fail();
        } catch (IllegalArgumentException e) {}
    }

    @Test
    public void testConstraintError() {
        Problem p = new Problem();
        Domain d = Domain.of(1);
        Variable V = p.variable("V", d);
        try {
            Constraint W = p.constraint(a -> a[0] == 1);
            fail();
        } catch (IllegalArgumentException e) {}
    }

}
