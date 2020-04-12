package test.jp.saka1029.cspint.depend;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import jp.saka1029.cspint.depend.BaseVariable;
import jp.saka1029.cspint.depend.Constraint;
import jp.saka1029.cspint.depend.Derivation;
import jp.saka1029.cspint.depend.DerivedVariable;
import jp.saka1029.cspint.depend.Domain;
import jp.saka1029.cspint.depend.Predicate;
import jp.saka1029.cspint.depend.Problem;

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
        Derivation E = a -> a[0] + a[1];
        DerivedVariable C = p.variable("C", E, A, B);
        BaseVariable D = p.variable("D", d);
        Predicate P = a -> a[0] < a[1];
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
        logger.info(p.toString());
    }

}
