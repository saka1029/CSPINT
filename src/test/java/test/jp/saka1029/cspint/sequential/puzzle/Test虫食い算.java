package test.jp.saka1029.cspint.sequential.puzzle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import jp.saka1029.cspint.sequential.Constraint;
import jp.saka1029.cspint.sequential.Domain;
import jp.saka1029.cspint.sequential.Predicate5;
import jp.saka1029.cspint.sequential.Problem;
import jp.saka1029.cspint.sequential.Solver;
import jp.saka1029.cspint.sequential.Variable;

class Test虫食い算 {

    static final Logger logger = Logger.getLogger(Test虫食い算.class.getName());

    static int number(int... digits) {
        return IntStream.of(digits).reduce(0, (n, d) -> 10 * n + d);
    }

    /**
     * Wikipedia 虫食い算
     * https://ja.wikipedia.org/wiki/%E8%99%AB%E9%A3%9F%E3%81%84%E7%AE%97
     * 　 □□□
     * ×    □□
     * ---------
     * 　 □□□
     *  □□□
     * ---------
     *  □□□□
     *
     *     A  B  C
     *  x     D  E
     *  ----------
     *     F  G  H
     *  I  J  K
     *  ----------
     *  L  M  N  O
     *
     */
//    @Test
    void test完全虫食い算() {
        Problem p = new Problem();
        Domain FIRST = Domain.rangeClosed(1, 9);
        Domain REST = Domain.rangeClosed(0, 9);
        Variable A = p.variable("A", FIRST);
        Variable B = p.variable("B", REST);
        Variable C = p.variable("C", REST);
        Variable D = p.variable("D", FIRST);
        Variable E = p.variable("E", REST);
        Variable F = p.variable("F", FIRST);
        Variable G = p.variable("G", REST);
        Variable H = p.variable("H", REST);
        Variable I = p.variable("I", FIRST);
        Variable J = p.variable("J", REST);
        Variable K = p.variable("K", REST);
        Variable L = p.variable("L", FIRST);
        Variable M = p.variable("M", REST);
        Variable N = p.variable("N", REST);
        Variable O = p.variable("O", REST);
        p.constraint((a, b, c, e, f, g, h) ->
            number(a, b, c) * e == number(f, g, h),
            A, B, C, E, F, G, H);
        p.constraint((a, b, c, d, i, j, k) ->
            number(a, b, c) * d == number(i, j, k),
            A, B, C, D, I, J, K);
        p.constraint((f, g, h, i, j, k, l, m, n, o) ->
            number(f, g, h) + number(i, j, k) * 10 == number(l, m, n, o),
            F, G, H, I, J, K, L, M, N, O);
        Solver s = new Solver();
        int[] count = {0};
        s.solve(p, (c, m) -> ++count[0]);
        logger.info("解の数:" + count[0]);
    }

    static List<Variable> bindingOrder(Problem p) {
        Set<Variable> set = new LinkedHashSet<>();
        p.variables.stream()
            .filter(v -> v.domain.size() < 4)
            .sorted(Comparator.comparing(v -> v.domain.size()))
            .forEach(v -> set.add(v));
        for (Constraint c : p.constraints)
            set.addAll(c.variables);
        return new ArrayList<>(set);
    }

//    @Test
    void test完全虫食い算桁ごと() {
        Problem p = new Problem();
        Domain FIRST = Domain.rangeClosed(1, 9);
        Domain REST = Domain.rangeClosed(0, 9);
        Domain ADD_CARRY = Domain.rangeClosed(0, 1);
        Domain MULT_CARRY = Domain.rangeClosed(0, 8);
        Domain ZERO = Domain.of(0);
        Variable A = p.variable("A", FIRST);
        Variable B = p.variable("B", REST);
        Variable C = p.variable("C", REST);
        Variable D = p.variable("D", FIRST);
        Variable E = p.variable("E", REST);
        Variable F = p.variable("F", FIRST);
        Variable G = p.variable("G", REST);
        Variable H = p.variable("H", REST);
        Variable I = p.variable("I", FIRST);
        Variable J = p.variable("J", REST);
        Variable K = p.variable("K", REST);
        Variable L = p.variable("L", FIRST);
        Variable M = p.variable("M", REST);
        Variable N = p.variable("N", REST);
        Variable O = p.variable("O", REST);
        Variable Z = p.variable("Z", ZERO);
        Variable C1 = p.variable("C1", MULT_CARRY);
        Variable C2 = p.variable("C2", MULT_CARRY);
        Variable D1 = p.variable("D1", MULT_CARRY);
        Variable D2 = p.variable("D2", MULT_CARRY);
        Variable E2 = p.variable("E2", ADD_CARRY);
        Variable E3 = p.variable("E3", ADD_CARRY);
        Predicate5 MULT = (a, b, c, d, e) -> a * b + c == d + e * 10;
        Predicate5 ADD = (a, b, c, d, e) -> a + b + c == d + e * 10;
        p.constraint(ADD, I, Z, E3, L, Z);
        p.constraint(ADD, F, J, E2, M, E3);
        p.constraint(ADD, G, K, Z, N, E2);
        p.constraint(ADD, H, Z, Z, O, Z);
        p.constraint(MULT, A, D, D2, I, Z);
        p.constraint(MULT, B, D, D1, J, D2);
        p.constraint(MULT, C, D, Z, K, D1);
        p.constraint(MULT, A, E, C2, F, Z);
        p.constraint(MULT, B, E, C1, G, C2);
        p.constraint(MULT, C, E, Z, H, C1);
        Solver s = new Solver();
        List<Variable> bindingOrder = bindingOrder(p);
        int[] count = {0};
        s.solve(p, bindingOrder, (c, m) -> ++count[0]);
//        s.solve(p, bindingOrder, m -> logger.info("ans: " + m));
        logger.info("解の数:" + count[0]);
    }

}
