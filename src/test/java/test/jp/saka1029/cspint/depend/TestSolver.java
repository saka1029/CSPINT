package test.jp.saka1029.cspint.depend;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import jp.saka1029.cspint.depend.BaseVariable;
import jp.saka1029.cspint.depend.DerivedVariable;
import jp.saka1029.cspint.depend.Domain;
import jp.saka1029.cspint.depend.Problem;
import jp.saka1029.cspint.depend.Solver;
import test.jp.saka1029.cspint.Common;

class TestSolver {

    static final Logger logger = Logger.getLogger(TestSolver.class.getName());

    /**
     * 変数: A, B, C : {1, 2, 3}
     * 制約: A + B = C, A < B
     */
    @Test
    void testBaseVariable() {
        logger.info(Common.methodName());
        Problem p = new Problem();
        Domain d = Domain.of(1, 2, 3);
        BaseVariable A = p.variable("A", d);
        BaseVariable B = p.variable("B", d);
        BaseVariable C = p.variable("C", d);
        p.constraint(a -> a[0] + a[1] == a[2], A, B, C);
        p.constraint(a -> a[0] < a[1], A, B);
        Solver s = new Solver();
        assertEquals(1, s.solve(p, m -> logger.info("*** answer: " + m)));
        logger.info("bind count: " + Arrays.toString(s.bindCount));
    }

    /**
     * 変数: A, B : {1, 2, 3}
     * 導出: C = A + B
     * 制約: A < B, C in {1, 2, 3}
     */
    @Test
    void testDerivedVariable() {
        logger.info(Common.methodName());
        Problem p = new Problem();
        Domain d = Domain.of(1, 2, 3);
        BaseVariable A = p.variable("A", d);
        BaseVariable B = p.variable("B", d);
        DerivedVariable C = p.variable("C", a -> a[0] + a[1], A, B);
        p.constraint(a -> a[0] < a[1], A, B);
        p.constraint(a -> d.contains(a[0]), C);
        Solver s = new Solver();
        assertEquals(1, s.solve(p, m -> logger.info("*** answer: " + m)));
        logger.info("bind count: " + Arrays.toString(s.bindCount));
    }

    /**
     * 変数: A, B, D : {1, 2, 3}
     * 導出: C = A + B
     * 制約: A < B, C == D
     */
    @Test
    void testDerivedVariable2() {
        logger.info(Common.methodName());
        Problem p = new Problem();
        Domain d = Domain.of(1, 2, 3);
        BaseVariable A = p.variable("A", d);
        BaseVariable B = p.variable("B", d);
        BaseVariable D = p.variable("D", d);
        DerivedVariable C = p.variable("C", a -> a[0] + a[1], A, B);
        p.constraint(a -> a[0] < a[1], A, B);
        p.constraint(a -> a[0] == a[1], C, D);
        Solver s = new Solver();
        assertEquals(1, s.solve(p, m -> logger.info("*** answer: " + m)));
        logger.info("bind count: " + Arrays.toString(s.bindCount));
    }
}
