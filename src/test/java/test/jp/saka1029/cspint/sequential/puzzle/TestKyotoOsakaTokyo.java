package test.jp.saka1029.cspint.sequential.puzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import jp.saka1029.cspint.sequential.Constraint;
import jp.saka1029.cspint.sequential.Domain;
import jp.saka1029.cspint.sequential.Predicate5;
import jp.saka1029.cspint.sequential.Problem;
import jp.saka1029.cspint.sequential.SequentialSolver;
import jp.saka1029.cspint.sequential.Variable;

class TestKyotoOsakaTokyo {

    static Logger logger = Logger.getLogger(TestKyotoOsakaTokyo.class.getName());

    static int number(int... digits) {
        return IntStream.of(digits).reduce(0, (a, b) -> a * 10 + b);
    }

    @Test
    public void testKyotoOsakaTokyo() {
        Problem problem = new Problem();
        Domain first = Domain.rangeClosed(1, 9);
        Domain digits = Domain.rangeClosed(0, 9);
        Domain carry = Domain.rangeClosed(0, 1);
        Domain zero = Domain.rangeClosed(0, 0);
        Variable K = problem.variable("K", first);
        Variable Y = problem.variable("Y", digits);
        Variable O = problem.variable("O", first);
        Variable T = problem.variable("T", first);
        Variable S = problem.variable("S", digits);
        Variable A = problem.variable("A", digits);
        Variable Z = problem.variable("Z", zero);
        Variable C1 = problem.variable("C1", carry);
        Variable C2 = problem.variable("C2", carry);
        Variable C3 = problem.variable("C3", carry);
        Variable C4 = problem.variable("C4", carry);

        // 束縛回数: [1, 2, 4, 8, 16, 144, 68, 480, 3360, 20160, 5160, 1400, 192, 8]
//        problem.constraint((k, o, t, y, s, a) ->
//            number(k, y, o, t, o) + number(o, s, a, k, a) == number(t, o, k, y, o),
//            K, O, T, Y, S, A);

        //   C4 C3 C2 C1
        //   K  Y  O  T  O
        // + O  S  A  K  A
        // ---------------
        //   T  O  K  Y  O
        // 束縛回数: [1, 2, 18, 144, 56, 112, 43, 86, 35, 70, 350, 64, 192, 8]
        Predicate5 addDigit = (c0, a, b, c, c1) -> c0 + a + b == c + c1 * 10;
        problem.constraint(addDigit,  Z, O, A, O, C1);
        problem.constraint(addDigit, C1, T, K, Y, C2);
        problem.constraint(addDigit, C2, O, A, K, C3);
        problem.constraint(addDigit, C3, Y, S, O, C4);
        problem.constraint(addDigit, C4, K, O, T, Z);
        problem.allDifferent(K, O, T, Y, S, A);
        SequentialSolver solver = new SequentialSolver();
        solver.solve(problem, (c, m) -> {
            logger.info("answer: " + m);
            logger.info(" " + number(m.get(K), m.get(Y), m.get(O), m.get(T), m.get(O)));
            logger.info("+" + number(m.get(O), m.get(S), m.get(A), m.get(K), m.get(A)));
            logger.info("=" + number(m.get(T), m.get(O), m.get(K), m.get(Y), m.get(O)));
        });
        logger.info("束縛回数: " + Arrays.toString(solver.bindCount));
    }

    static List<Variable> constraintOrder(List<Constraint> constraints) {
        Set<Variable> vars = constraints.stream()
           .flatMap(c -> c.variables.stream()
               .sorted(Comparator.comparing(v -> v.domain.size())))
           .collect(Collectors.toCollection(LinkedHashSet::new));
        return new ArrayList<>(vars);
    }

    @Test
    public void testKyotoOsakaTokyoConstraintOrder() {
        Problem problem = new Problem();
        Domain first = Domain.rangeClosed(1, 9);
        Domain digits = Domain.rangeClosed(0, 9);
        Domain carry = Domain.rangeClosed(0, 1);
        Domain zero = Domain.rangeClosed(0, 0);
        Variable Z = problem.variable("Z", zero);
        Variable C4 = problem.variable("C4", carry);
        Variable K = problem.variable("K", first);
        Variable O = problem.variable("O", first);
        Variable T = problem.variable("T", first);
        Variable C3 = problem.variable("C3", carry);
        Variable Y = problem.variable("Y", digits);
        Variable S = problem.variable("S", digits);
        Variable C2 = problem.variable("C2", carry);
        Variable A = problem.variable("A", digits);
        Variable C1 = problem.variable("C1", carry);
        //   C4 C3 C2 C1
        //   K  Y  O  T  O
        // + O  S  A  K  A
        // ---------------
        //   T  O  K  Y  O
        // 束縛回数: [1, 2, 18, 144, 56, 112, 784, 228, 456, 132, 1]
        // 制約の順番を逆にしても束縛回数は同じ。
        Predicate5 addDigit = (c0, a, b, c, c1) -> c0 + a + b == c + c1 * 10;
        problem.constraint(addDigit, C4, K, O, T, Z);
        problem.constraint(addDigit, C3, Y, S, O, C4);
        problem.constraint(addDigit, C2, O, A, K, C3);
        problem.constraint(addDigit, C1, T, K, Y, C2);
        problem.constraint(addDigit,  Z, O, A, O, C1);
        problem.allDifferent(K, O, T, Y, S, A);
        SequentialSolver solver = new SequentialSolver();
        List<Variable> bindOrder = constraintOrder(problem.constraints);
        logger.info("bind order: " + bindOrder);
        solver.solve(problem, (c, m) -> {
            logger.info("answer: " + m);
            logger.info(" " + number(m.get(K), m.get(Y), m.get(O), m.get(T), m.get(O)));
            logger.info("+" + number(m.get(O), m.get(S), m.get(A), m.get(K), m.get(A)));
            logger.info("=" + number(m.get(T), m.get(O), m.get(K), m.get(Y), m.get(O)));
        });
        logger.info("束縛回数: " + Arrays.toString(solver.bindCount));
    }

}

