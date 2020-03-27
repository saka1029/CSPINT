package test.jp.saka1029.cspint.sequential;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import jp.saka1029.cspint.sequential.Domain;
import jp.saka1029.cspint.sequential.Predicate3;
import jp.saka1029.cspint.sequential.Problem;
import jp.saka1029.cspint.sequential.Solver;
import jp.saka1029.cspint.sequential.Variable;

class Test魔法陣 {

    static Logger logger = Logger.getLogger(Test魔法陣.class.getName());

    @Test
    void test() {
        Problem p = new Problem();
        Domain digits = Domain.rangeClosed(1, 9);
        Variable A = p.variable("A", digits);
        Variable B = p.variable("B", digits);
        Variable C = p.variable("C", digits);
        Variable D = p.variable("D", digits);
        Variable E = p.variable("E", digits);
        Variable F = p.variable("F", digits);
        Variable G = p.variable("G", digits);
        Variable H = p.variable("H", digits);
        Variable I = p.variable("I", digits);
        p.allDifferent(A, B, C, D, E, F, G, H, I);
        Predicate3 predicate = (x, y, z) -> x + y + z == 15;
        p.constraint(predicate, A, B, C);
        p.constraint(predicate, D, E, F);
        p.constraint(predicate, G, H, I);
        p.constraint(predicate, A, D, G);
        p.constraint(predicate, B, E, H);
        p.constraint(predicate, C, F, I);
        p.constraint(predicate, A, E, I);
        p.constraint(predicate, C, E, G);
        Solver solver = new Solver();
        assertEquals(8, solver.solve(p, m -> logger.info("answer: " + m)));
        logger.info("束縛回数: " + Arrays.toString(solver.bindCount));
    }

}
