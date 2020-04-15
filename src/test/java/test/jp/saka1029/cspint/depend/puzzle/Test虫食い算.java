package test.jp.saka1029.cspint.depend.puzzle;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import jp.saka1029.cspint.depend.Domain;
import jp.saka1029.cspint.depend.Problem;
import jp.saka1029.cspint.depend.Solver;
import jp.saka1029.cspint.depend.Variable;
import test.jp.saka1029.cspint.Common;

class Test虫食い算 {

    Logger logger = Logger.getLogger(Test虫食い算.class.getName());

    static int number(int... digits) {
        return IntStream.of(digits).reduce(0, (a, b) -> 10 * a + b);
    }

    /**
     * 12□ + 3□4 = □56
     * https://ja.wikipedia.org/wiki/%E8%99%AB%E9%A3%9F%E3%81%84%E7%AE%97
     */
    @Test
    void test加減算Wikipedia() {
        logger.info(Common.methodName());
        Problem p = new Problem();
        Variable A = p.variable("A", Domain.rangeClosed(0, 9));
        Variable B = p.variable("B", Domain.rangeClosed(0, 9));
        Variable C = p.variable("C", Domain.rangeClosed(1, 9));
        p.constraint((a, b, c) -> 120 + a + 304 + b * 10 == c * 100 + 56, A, B, C);
        Solver s = new Solver();
        assertEquals(1, s.solve(p, m -> logger.info("answer: " + m)));
        logger.info("束縛回数: " + Arrays.toString(s.bindCount));
    }

    /**
     * □ + 7□ + □□ + □□ = □1
     * A + 7B + CD + EF = G1
     * A + 7B + CD < 100
     * A + 7B + CD + EF < 100
     * https://ja.wikipedia.org/wiki/%E8%99%AB%E9%A3%9F%E3%81%84%E7%AE%97
     */
    @Test
    void test加減算Wikipedia2() {
        logger.info(Common.methodName());
        Problem p = new Problem();
        Domain FIRST = Domain.rangeClosed(1, 9);
        Domain REST = Domain.rangeClosed(0, 9);
        Variable A = p.variable("A", FIRST);
        Variable B = p.variable("B", REST);
        Variable C = p.variable("C", FIRST);
        Variable D = p.variable("D", REST);
        Variable E = p.variable("E", FIRST);
        Variable F = p.variable("F", REST);
        Variable G = p.variable("G", FIRST);
        p.constraint((a, b, c, d) -> a + number(7, b) + number(c, d) < 100, A, B, C, D);
        p.constraint((a, b, c, d, e, f) -> a + number(7, b) + number(c, d) + number(e, f) < 100, A, B, C, D, E, F);
        p.constraint((a, b, c, d, e, f, g) ->
            a + number(7, b) + number(c, d) + number(e, f) == number(g, 1),
            A, B, C, D, E, F, G);
        Solver s = new Solver();
        assertEquals(1, s.solve(p, m -> logger.info("answer: " + m)));
        logger.info("束縛回数: " + Arrays.toString(s.bindCount));
    }

    static int fact(int n) {
        if (n > 9)
            return Integer.MAX_VALUE;
        int r = 1;
        for (int i = 2; i <= n; ++i)
            r *= i;
        return r;
    }

    @Test
    public void testFact() {
        assertEquals(1, fact(0));
        assertEquals(1, fact(1));
        assertEquals(2, fact(2));
        assertEquals(6, fact(3));
        assertEquals(24, fact(4));
        assertEquals(120, fact(5));
        assertEquals(720, fact(6));
        assertEquals(5040, fact(7));
        assertEquals(40320, fact(8));
        assertEquals(362880, fact(9));
    }

    /**
     * √(□! + (□!)!) = □
     * √(A! + (B!)!) = C
     * https://ja.wikipedia.org/wiki/%E8%99%AB%E9%A3%9F%E3%81%84%E7%AE%97
     */
    @Test
    void test加減算Wikipedia3() {
        logger.info(Common.methodName());
        Problem p = new Problem();
        Domain DIGIT = Domain.rangeClosed(0, 9);
        Variable A = p.variable("A", DIGIT);
        Variable B = p.variable("B", DIGIT);
        Variable C = p.variable("C", DIGIT);
        p.constraint((a, b, c) -> fact(a) + fact(fact(b)) == c * c, A, B, C);
        Solver s = new Solver();
        assertEquals(3, s.solve(p, m -> logger.info("answer: " + m)));
        logger.info("束縛回数: " + Arrays.toString(s.bindCount));
    }

}
