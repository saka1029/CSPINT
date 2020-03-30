<<<<<<< HEAD:src/test/java/test/jp/saka1029/cspint/sequential/puzzle/TestNQueens.java
package test.jp.saka1029.cspint.sequential.puzzle;
=======
package test.saka1029.cspint.sequential.puzzle;
>>>>>>> 7e8a2dd51adace096e2a91229a2601ffd7966e3e:src/test/java/test/saka1029/cspint/sequential/puzzle/TestNQueens.java
import static org.junit.jupiter.api.Assertions.*;

import java.util.logging.Logger;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import jp.saka1029.cspint.sequential.Domain;
import jp.saka1029.cspint.sequential.Problem;
import jp.saka1029.cspint.sequential.Solver;
import jp.saka1029.cspint.sequential.Variable;

class TestNQueens {

    static Logger logger = Logger.getLogger(TestNQueens.class.getName());

    static int nQueens(final int n) {
        long start = System.currentTimeMillis();
        Problem problem = new Problem();
        Domain domain = Domain.range(0, n);
        Variable[] rows = IntStream.range(0, n)
            .mapToObj(i -> problem.variable("R" + i, domain))
            .toArray(Variable[]::new);
        IntStream.range(0,  n)
           .forEach(i -> IntStream.range(i + 1, n)
               .forEach(j -> problem.constraint(
                   (x, y) -> x != y && Math.abs(x - y) != j - i, rows[i], rows[j])));
        Solver solver = new Solver();
        int answers = solver.solve(problem, m -> {});
//        int[] count = {0};
//        int answers = solver.solve(problem, m -> {
//            logger.info("** answer " + (++count[0]));
//            for (int i = 0; i < n; ++i) {
//                int c = m.get(rows[i]);
//                logger.info(".".repeat(c) + "X" + ".".repeat(n - c -1));
//            }
//        });
        logger.info("n=" + n + " : answers=" + answers
            + " : elapse=" + (System.currentTimeMillis() - start) + "ms.");
        return answers;
    }

    @Test
    void test() {
        assertEquals(1, nQueens(1));
        assertEquals(0, nQueens(2));
        assertEquals(0, nQueens(3));
        assertEquals(2, nQueens(4));
        assertEquals(10, nQueens(5));
        assertEquals(4, nQueens(6));
        assertEquals(40, nQueens(7));
        assertEquals(92, nQueens(8));
        assertEquals(352, nQueens(9));
        assertEquals(724, nQueens(10));
    }

}