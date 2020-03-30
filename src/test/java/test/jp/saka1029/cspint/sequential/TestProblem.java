package test.jp.saka1029.cspint.sequential;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import jp.saka1029.cspint.sequential.Constraint;
import jp.saka1029.cspint.sequential.Domain;
import jp.saka1029.cspint.sequential.Predicate1;
import jp.saka1029.cspint.sequential.Predicate10;
import jp.saka1029.cspint.sequential.Predicate2;
import jp.saka1029.cspint.sequential.Predicate3;
import jp.saka1029.cspint.sequential.Predicate4;
import jp.saka1029.cspint.sequential.Predicate5;
import jp.saka1029.cspint.sequential.Predicate6;
import jp.saka1029.cspint.sequential.Predicate7;
import jp.saka1029.cspint.sequential.Predicate8;
import jp.saka1029.cspint.sequential.Predicate9;
import jp.saka1029.cspint.sequential.Problem;
import jp.saka1029.cspint.sequential.Variable;

class TestProblem {

    static final Logger logger = Logger.getLogger(TestProblem.class.toString());

    @Test
    void testVariable() {
        Problem p = new Problem();
        Variable A = p.variable("a", Domain.range(1, 4));
        assertEquals(1, p.variables.size());
        assertEquals(A, p.variables.get(0));
        assertEquals(Domain.of(1, 2, 3), p.variables.get(0).domain);
        assertEquals(0, p.variables.get(0).constraints.size());
        Variable B = p.variable("b", Domain.range(1, 3));
        assertEquals(2, p.variables.size());
        assertEquals(List.of(A, B), p.variables);
        assertEquals(Domain.of(1, 2), p.variables.get(1).domain);
        assertEquals(0, p.variables.get(1).constraints.size());
        Constraint c = p.constraint((x, y) -> x != y, A, B);
        assertEquals(List.of(c), p.constraints);
    }

    @Test
    void testConstraint() {
        Problem problem = new Problem();
        Variable A = problem.variable("A", Domain.range(1, 4));
        Variable B = problem.variable("B", Domain.range(1, 4));
        Variable C = problem.variable("C", Domain.range(1, 4));
        Variable D = problem.variable("D", Domain.range(1, 4));
        Variable E = problem.variable("E", Domain.range(1, 4));
        Variable F = problem.variable("F", Domain.range(1, 4));
        Variable G = problem.variable("G", Domain.range(1, 4));
        Variable H = problem.variable("H", Domain.range(1, 4));
        Variable I = problem.variable("I", Domain.range(1, 4));
        Variable J = problem.variable("J", Domain.range(1, 4));
        assertEquals(List.of(), problem.constraints);
        Predicate1 PA = a -> a == 1;
        Constraint CA = problem.constraint(PA, A);
        assertEquals(PA, CA.predicate);
        assertEquals(List.of(CA), problem.constraints);
        Predicate2 PB = (a, b) -> a == 1 && a == b;
        Constraint CB = problem.constraint(PB, A, B);
        assertEquals(PB, CB.predicate);
        assertEquals(List.of(CA, CB), problem.constraints);
        Predicate3 PC = (a, b, c) -> a == 1 && a == b && b == c;
        Constraint CC = problem.constraint(PC, A, B, C);
        assertEquals(PC, CC.predicate);
        assertEquals(List.of(CA, CB, CC), problem.constraints);
        Predicate4 PD = (a, b, c, d) -> a == 1 && a == b && b == c && c == d;
        Constraint CD = problem.constraint(PD, A, B, C, D);
        assertEquals(PD, CD.predicate);
        assertEquals(List.of(CA, CB, CC, CD), problem.constraints);
        Predicate5 PE = (a, b, c, d, e) -> a == 1 && a == b && b == c && c == d && d == e;
        Constraint CE = problem.constraint(PE, A, B, C, D, E);
        assertEquals(PE, CE.predicate);
        assertEquals(List.of(CA, CB, CC, CD, CE), problem.constraints);
        Predicate6 PF = (a, b, c, d, e, f) -> a == 1 && a == b && b == c && c == d && d == e && e == f;
        Constraint CF = problem.constraint(PF, A, B, C, D, E, F);
        assertEquals(PF, CF.predicate);
        assertEquals(List.of(CA, CB, CC, CD, CE, CF), problem.constraints);
        Predicate7 PG = (a, b, c, d, e, f, g) -> a == 1 && a == b && b == c && c == d && d == e && e == f && f == g;
        Constraint CG = problem.constraint(PG, A, B, C, D, E, F, G);
        assertEquals(PG, CG.predicate);
        assertEquals(List.of(CA, CB, CC, CD, CE, CF, CG), problem.constraints);
        Predicate8 PH = (a, b, c, d, e, f, g, h) -> a == 1 && a == b && b == c && c == d && d == e && e == f && f == g && g == h;
        Constraint CH = problem.constraint(PH, A, B, C, D, E, F, G, H);
        assertEquals(PH, CH.predicate);
        assertEquals(List.of(CA, CB, CC, CD, CE, CF, CG, CH), problem.constraints);
        Predicate9 PI = (a, b, c, d, e, f, g, h, i) -> a == 1 && a == b && b == c && c == d && d == e && e == f && f == g && g == h && h == i;
        Constraint CI = problem.constraint(PI, A, B, C, D, E, F, G, H, I);
        assertEquals(PI, CI.predicate);
        assertEquals(List.of(CA, CB, CC, CD, CE, CF, CG, CH, CI), problem.constraints);
        Predicate10 PJ = (a, b, c, d, e, f, g, h, i, j) -> a == 1 && a == b && b == c && c == d && d == e && e == f && f == g && g == h && h == i && i == j;
        Constraint CJ = problem.constraint(PJ, A, B, C, D, E, F, G, H, I, J);
        assertEquals(PJ, CJ.predicate);
        assertEquals(List.of(CA, CB, CC, CD, CE, CF, CG, CH, CI, CJ), problem.constraints);
        assertTrue(CJ.predicate.test(1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    }

    @Test
    public void testAllDifferent() {
        Problem p = new Problem();
        Domain d = Domain.range(0, 4);
        Variable A = p.variable("A", d);
        Variable B = p.variable("B", d);
        Variable C = p.variable("C", d);
        Variable D = p.variable("D", d);
        p.allDifferent(A, B, C, D);
        assertEquals(6, p.constraints.size());
        for (Constraint c : p.constraints) {
            assertTrue(c.predicate.test(1, 2));
            assertFalse(c.predicate.test(2, 2));
        }
    }

    @Test
    public void testAllDifferentMatrix() {
        Problem p = new Problem();
        Domain d = Domain.range(0, 2);
        Variable[][] v = {
            {p.variable("A", d), p.variable("B", d)},
            {p.variable("C", d), p.variable("D", d)},
        };
        p.allDifferent(v);
        assertEquals(6, p.constraints.size());
        Set<List<Variable>> expected = Set.of(
            List.of(v[0][0], v[0][1]), List.of(v[0][0], v[1][0]), List.of(v[0][0], v[1][1]),
            List.of(v[0][1], v[1][0]), List.of(v[0][1], v[1][1]),
            List.of(v[1][0], v[1][1])
        );
        Set<List<Variable>> actual = p.constraints.stream()
            .map(c -> c.variables)
            .collect(Collectors.toSet());
        assertEquals(expected, actual);
        for (Constraint c : p.constraints) {
            assertTrue(c.predicate.test(1, 2));
            assertFalse(c.predicate.test(2, 2));
        }
    }

    @Test
    public void testVariableNameInvalid() {
        Problem p = new Problem();
        Variable v = p.variable("v", Domain.of(1, 2));
        try {
            p.variable(null, Domain.of(1));
            fail();
        } catch (NullPointerException e) {
        }
        try {
            p.variable("v", Domain.of(1));
            fail();
        } catch (IllegalArgumentException e) {
        }

    }

    @Test
    public void testVariableUnmodifiable() {
        Problem p = new Problem();
        Variable v = p.variable("v", Domain.of(1, 2));
        p.constraint(x -> x == 1, v);
        try {
            p.variables.set(0, null);
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    @Test
    public void testConstraintsUnmodifiable() {
        Problem p = new Problem();
        Variable v = p.variable("v", Domain.of(1, 2));
        p.constraint(x -> x == 1, v);
        try {
            p.constraints.set(0, null);
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

}
