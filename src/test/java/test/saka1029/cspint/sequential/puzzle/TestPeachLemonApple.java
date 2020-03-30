<<<<<<< HEAD:src/test/java/test/jp/saka1029/cspint/sequential/puzzle/TestPeachLemonApple.java
package test.jp.saka1029.cspint.sequential.puzzle;
=======
package test.saka1029.cspint.sequential.puzzle;
>>>>>>> 7e8a2dd51adace096e2a91229a2601ffd7966e3e:src/test/java/test/saka1029/cspint/sequential/puzzle/TestPeachLemonApple.java

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import jp.saka1029.cspint.sequential.Constraint;
import jp.saka1029.cspint.sequential.Domain;
import jp.saka1029.cspint.sequential.Predicate5;
import jp.saka1029.cspint.sequential.Problem;
import jp.saka1029.cspint.sequential.Solver;
import jp.saka1029.cspint.sequential.Variable;

class TestPeachLemonApple {

    static Logger logger = Logger.getLogger(TestPeachLemonApple.class.getName());

    static int number(int... digits) {
        return IntStream.of(digits).reduce(0, (a, b) -> a * 10 + b);
    }

    @Test
    public void testPeachLemonApple() {
        Problem problem = new Problem();
        Domain first = Domain.rangeClosed(1, 9);
        Domain digits = Domain.rangeClosed(0, 9);
        Domain carry = Domain.rangeClosed(0, 1);
        Domain zero = Domain.rangeClosed(0, 0);
        Variable Z = problem.variable("Z", zero);
        Variable C4 = problem.variable("C4", carry);
        Variable P = problem.variable("P", first);
        Variable L = problem.variable("L", first);
        Variable A = problem.variable("A", first);
        Variable C3 = problem.variable("C3", carry);
        Variable E = problem.variable("E", digits);
        Variable C2 = problem.variable("C2", carry);
        Variable M = problem.variable("M", digits);
        Variable C1 = problem.variable("C1", carry);
        Variable C = problem.variable("C", digits);
        Variable O = problem.variable("O", digits);
        Variable H = problem.variable("H", digits);
        Variable N = problem.variable("N", digits);
        problem.allDifferent(P, E, A, C, H, L, M, O, N);
        Predicate5 addDigit = (c0, a, b, c, c1) -> c0 + a + b == c + c1 * 10;
        //   C4 C3 C2 C1
        //   P  E  A  C  H
        // + L  E  M  O  N
        // ---------------
        //   A  P  P  L  E

        // 束縛回数: [1, 2, 4, 8, 16, 144, 68, 480, 3360, 20160, 5160, 1400, 192, 8]
        //        problem.constraint((p, e, a, c, h, l, m, o, n) ->
        //            number(p, e, a, c, h) + number(l, e, m, o, n) == number(a, p, p, l, e),
        //            P, E, A, C, H, L, M, O, N);

        // 束縛回数: [1, 2, 18, 144, 56, 112, 43, 86, 35, 70, 350, 64, 192, 8]
        problem.constraint(addDigit,  Z, H, N, E, C1);
        problem.constraint(addDigit, C1, C, O, L, C2);
        problem.constraint(addDigit, C2, A, M, P, C3);
        problem.constraint(addDigit, C3, E, E, P, C4);
        problem.constraint(addDigit, C4, P, L, A, Z);
        Solver solver = new Solver();
        solver.solve(problem, m -> {
            logger.info("answer: " + m);
            logger.info(" " + number(m.get(P), m.get(E), m.get(A), m.get(C), m.get(H)));
            logger.info("+" + number(m.get(L), m.get(E), m.get(M), m.get(O), m.get(N)));
            logger.info("=" + number(m.get(A), m.get(P), m.get(P), m.get(L), m.get(E)));
        });
        logger.info("束縛回数: " + Arrays.toString(solver.bindCount));
    }

    static List<Variable> constraintOrder(List<Constraint> constraints) {
        Set<Variable> vars = new LinkedHashSet<>();
        for (Constraint c : constraints)
            for (Variable v : c.variables)
                vars.add(v);
        return new ArrayList<>(vars);
    }

    @Test
    public void testPeachLemonAppleConstraintOrder() {
        Problem problem = new Problem();
        Domain first = Domain.rangeClosed(1, 9);
        Domain digits = Domain.rangeClosed(0, 9);
        Domain carry = Domain.rangeClosed(0, 1);
        Domain zero = Domain.rangeClosed(0, 0);
        Variable P = problem.variable("P", first);
        Variable E = problem.variable("E", digits);
        Variable A = problem.variable("A", first);
        Variable C = problem.variable("C", digits);
        Variable H = problem.variable("H", digits);
        Variable L = problem.variable("L", first);
        Variable M = problem.variable("M", digits);
        Variable O = problem.variable("O", digits);
        Variable N = problem.variable("N", digits);
        Variable Z = problem.variable("Z", zero);
        Variable C3 = problem.variable("C3", carry);
        Variable C1 = problem.variable("C1", carry);
        Variable C2 = problem.variable("C2", carry);
        Variable C4 = problem.variable("C4", carry);
        Predicate5 addDigit = (c0, c1, a, b, c) -> c0 + a + b == c + c1 * 10;
        //   C4 C3 C2 C1
        //   P  E  A  C  H
        // + L  E  M  O  N
        // ---------------
        //   A  P  P  L  E

        // 束縛回数: [1, 2, 4, 8, 16, 144, 68, 480, 3360, 20160, 5160, 1400, 192, 8]
        //        problem.constraint((p, e, a, c, h, l, m, o, n) ->
        //            number(p, e, a, c, h) + number(l, e, m, o, n) == number(a, p, p, l, e),
        //            P, E, A, C, H, L, M, O, N);

        // 束縛回数: [1, 2, 18, 144, 56, 112, 43, 86, 35, 70, 350, 64, 192, 8]
        problem.constraint(addDigit, C4,  Z, P, L, A);
        problem.constraint(addDigit, C3, C4, E, E, P);
        problem.constraint(addDigit, C2, C3, A, M, P);
        problem.constraint(addDigit, C1, C2, C, O, L);
        problem.constraint(addDigit,  Z, C1, H, N, E);
        problem.allDifferent(P, E, A, C, H, L, M, O, N);
        Solver solver = new Solver();
        List<Variable> bindOrder = constraintOrder(problem.constraints);
        logger.info("bind order: " + bindOrder);
        solver.solve(problem, bindOrder, m -> {
            logger.info("answer: " + m);
            logger.info(" " + number(m.get(P), m.get(E), m.get(A), m.get(C), m.get(H)));
            logger.info("+" + number(m.get(L), m.get(E), m.get(M), m.get(O), m.get(N)));
            logger.info("=" + number(m.get(A), m.get(P), m.get(P), m.get(L), m.get(E)));
        });
        logger.info("束縛回数: " + Arrays.toString(solver.bindCount));
    }

}