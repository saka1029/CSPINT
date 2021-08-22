package test.jp.saka1029.cspint;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import jp.saka1029.cspint.Constraint;
import jp.saka1029.cspint.Domain;
import jp.saka1029.cspint.Predicate1;
import jp.saka1029.cspint.Predicate10;
import jp.saka1029.cspint.Predicate2;
import jp.saka1029.cspint.Predicate3;
import jp.saka1029.cspint.Predicate4;
import jp.saka1029.cspint.Predicate5;
import jp.saka1029.cspint.Predicate6;
import jp.saka1029.cspint.Predicate7;
import jp.saka1029.cspint.Predicate8;
import jp.saka1029.cspint.Predicate9;
import jp.saka1029.cspint.Problem;
import jp.saka1029.cspint.Variable;

class TestProblem {

    static final Logger logger = Common.getLogger(TestProblem.class);

    @Test
    void testVariable() {
        Problem p = new Problem();
        Variable A = p.variable("A", Domain.range(1, 4));
        assertEquals(1, p.variables.size());
        assertEquals(A, p.variables.get(0));
        assertEquals(Domain.of(1, 2, 3), p.variables.get(0).domain);
        assertEquals(0, p.variables.get(0).constraints.size());
        Variable B = p.variable("B", Domain.range(1, 3));
        assertEquals(2, p.variables.size());
        assertEquals(List.of(A, B), p.variables);
        assertEquals(Domain.of(1, 2), p.variables.get(1).domain);
        assertEquals(0, p.variables.get(1).constraints.size());
        Constraint c = p.constraint((x, y) -> x != y, A, B);
        assertEquals(List.of(c), p.constraints);
        assertEquals(B, p.variable("B"));
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
        assertTrue(CA.predicate.test(1));
        Predicate2 PB = (a, b) -> a == 1 && b == 2;
        Constraint CB = problem.constraint(PB, A, B);
        assertEquals(PB, CB.predicate);
        assertEquals(List.of(CA, CB), problem.constraints);
        assertTrue(CB.predicate.test(1, 2));
        Predicate3 PC = (a, b, c) -> a == 1 && b == 2 && c == 3;
        Constraint CC = problem.constraint(PC, A, B, C);
        assertEquals(PC, CC.predicate);
        assertEquals(List.of(CA, CB, CC), problem.constraints);
        assertTrue(CC.predicate.test(1, 2, 3));
        Predicate4 PD = (a, b, c, d) -> a == 1 && b == 2 && c == 3 && d == 4;
        Constraint CD = problem.constraint(PD, A, B, C, D);
        assertEquals(PD, CD.predicate);
        assertEquals(List.of(CA, CB, CC, CD), problem.constraints);
        assertTrue(CD.predicate.test(1, 2, 3, 4));
        Predicate5 PE = (a, b, c, d, e) -> a == 1 && b == 2 && c == 3 && d == 4 && e == 5;
        Constraint CE = problem.constraint(PE, A, B, C, D, E);
        assertEquals(PE, CE.predicate);
        assertEquals(List.of(CA, CB, CC, CD, CE), problem.constraints);
        assertTrue(CE.predicate.test(1, 2, 3, 4, 5));
        Predicate6 PF = (a, b, c, d, e, f) -> a == 1 && b == 2 && c == 3 && d == 4 && e == 5 && f == 6;
        Constraint CF = problem.constraint(PF, A, B, C, D, E, F);
        assertEquals(PF, CF.predicate);
        assertEquals(List.of(CA, CB, CC, CD, CE, CF), problem.constraints);
        assertTrue(CF.predicate.test(1, 2, 3, 4, 5, 6));
        Predicate7 PG = (a, b, c, d, e, f, g) -> a == 1 && b == 2 && c == 3 && d == 4 && e == 5 && f == 6 && g == 7;
        Constraint CG = problem.constraint(PG, A, B, C, D, E, F, G);
        assertEquals(PG, CG.predicate);
        assertEquals(List.of(CA, CB, CC, CD, CE, CF, CG), problem.constraints);
        assertTrue(CG.predicate.test(1, 2, 3, 4, 5, 6, 7));
        Predicate8 PH = (a, b, c, d, e, f, g, h) -> a == 1 && b == 2 && c == 3 && d == 4 && e == 5 && f == 6 && g == 7 && h == 8;
        Constraint CH = problem.constraint(PH, A, B, C, D, E, F, G, H);
        assertEquals(PH, CH.predicate);
        assertEquals(List.of(CA, CB, CC, CD, CE, CF, CG, CH), problem.constraints);
        assertTrue(CH.predicate.test(1, 2, 3, 4, 5, 6, 7, 8));
        Predicate9 PI = (a, b, c, d, e, f, g, h, i) -> a == 1 && b == 2 && c == 3 && d == 4 && e == 5 && f == 6 && g == 7 && h == 8 && i == 9;
        Constraint CI = problem.constraint(PI, A, B, C, D, E, F, G, H, I);
        assertEquals(PI, CI.predicate);
        assertEquals(List.of(CA, CB, CC, CD, CE, CF, CG, CH, CI), problem.constraints);
        assertTrue(CI.predicate.test(1, 2, 3, 4, 5, 6, 7, 8, 9));
        Predicate10 PJ = (a, b, c, d, e, f, g, h, i, j) -> a == 1 && b == 2 && c == 3 && d == 4 && e == 5 && f == 6 && g == 7 && h == 8 && i == 9 && j == 10;
        Constraint CJ = problem.constraint(PJ, A, B, C, D, E, F, G, H, I, J);
        assertEquals(PJ, CJ.predicate);
        assertEquals(List.of(CA, CB, CC, CD, CE, CF, CG, CH, CI, CJ), problem.constraints);
        assertTrue(CJ.predicate.test(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
    }

    @Test
    public void testConstraint2D() {
    	Problem p = new Problem();
        Domain d = Domain.range(0, 2);
        Variable A = p.variable("A", d);
        Variable B = p.variable("B", d);
        Variable C = p.variable("C", d);
        Variable D = p.variable("D", d);
        Variable[][] V = { {A, B}, {C, D} };
        Constraint c = p.constraint(v -> v[0][0] + v[0][1] == v[1][0] + v[1][1], V);
        assertEquals(List.of(A, B, C, D), c.variables);
        assertTrue(c.predicate.test(0, 2, 1, 1));
    }


    @Test
    public void testAllDifferentEachRows() {
    	Problem p = new Problem();
        Domain d = Domain.range(0, 2);
        Variable A = p.variable("A", d);
        Variable B = p.variable("B", d);
        Variable C = p.variable("C", d);
        Variable D = p.variable("D", d);
        Variable[][] V = { {A, B}, {C, D} };
        p.allDifferentEachRows(V);
        assertEquals(2, p.constraints.size());
        assertEquals(Set.of(List.of(A, B), List.of(C, D)),
        	p.constraints.stream().map(c -> c.variables).collect(Collectors.toSet()));
    }

    @Test
    public void testAllDifferentEachColumns() {
    	Problem p = new Problem();
        Domain d = Domain.range(0, 2);
        Variable A = p.variable("A", d);
        Variable B = p.variable("B", d);
        Variable C = p.variable("C", d);
        Variable D = p.variable("D", d);
        Variable[][] V = { {A, B}, {C, D} };
        p.allDifferentEachColumns(V);
        assertEquals(2, p.constraints.size());
        assertEquals(Set.of(List.of(A, C), List.of(B, D)),
        	p.constraints.stream().map(c -> c.variables).collect(Collectors.toSet()));
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
