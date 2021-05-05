package test.jp.saka1029.cspint.puzzle;

import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import jp.saka1029.cspint.Domain;
import jp.saka1029.cspint.Problem;
import jp.saka1029.cspint.Solver;
import jp.saka1029.cspint.Variable;
import test.jp.saka1029.cspint.Common;

/**
 * The "Impossible" Ages of Sisters Puzzle - YouTube
 * MindYourDecisions
 *
 * https://www.youtube.com/watch?v=AKH85K_rcDg
 */
public class TestTheImpossibleAgesofSistersPuzzle {

    static final Logger logger = Common.getLogger(TestTheImpossibleAgesofSistersPuzzle.class);

    static int number(int... digits) {
        return IntStream.of(digits).reduce(0, (a, b) -> a * 10 + b);
    }

    static boolean isPerfectSquare(int n) {
        int s = (int) Math.sqrt(n);
        return s * s == n;
    }

    @Test
    void test() {
        Domain zero = Domain.rangeClosed(0, 9);
        Domain one = Domain.rangeClosed(1, 9);
        Problem p = new Problem();
        Variable A = p.variable("a", one);
        Variable B = p.variable("b", zero);
        Variable C = p.variable("c", one);
        Variable D = p.variable("d", zero);
        Variable E = p.variable("e", one);
        Variable F = p.variable("f", zero);
        Variable G = p.variable("g", one);
        Variable H = p.variable("h", zero);
        p.constraint((a, b, c, d) -> isPerfectSquare(number(a, b, c, d)), A, B, C, D);
        p.constraint((e, f, g, h) -> isPerfectSquare(number(e, f, g, h)), E, F, G, H);
        p.constraint((a, b, e, f) -> number(a, b) + 11 == number(e, f), A, B, E, F);
        p.constraint((c, d, g, h) -> number(c, d) + 11 == number(g, h), C, D, G, H);
        Solver solver = new Solver();
        solver.solve(p, (c, r) -> logger.info(r.toString()));
        logger.info(Arrays.toString(solver.bindCount));
    }
}
