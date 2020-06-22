package test.jp.saka1029.cspint.puzzle;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import jp.saka1029.cspint.Constraint;
import jp.saka1029.cspint.Domain;
import jp.saka1029.cspint.Problem;
import jp.saka1029.cspint.Solver;
import jp.saka1029.cspint.Variable;
import test.jp.saka1029.cspint.Common;

class Test整数問題 {

	static final Logger logger = Logger.getLogger(Test整数問題.class.getName());

	static int number(int... d) {
		return IntStream.of(d).reduce(0, (a, b) -> a * 10 + b);
	}

    /**
     * A, B, C, D, E, F, G, H, I:{1..9}
     * A < D < G
     * (A / BC) + (D / EF) + (G / HI) = 1
     */
    @Test
    public void test分数の和() {
		logger.info(Common.methodName());
        Problem p = new Problem();
        Domain digit = Domain.rangeClosed(1, 9);
        Variable A = p.variable("A", digit);
        Variable B = p.variable("B", digit);
        Variable C = p.variable("C", digit);
        Variable D = p.variable("D", digit);
        Variable E = p.variable("E", digit);
        Variable F = p.variable("F", digit);
        Variable G = p.variable("G", digit);
        Variable H = p.variable("H", digit);
        Variable I = p.variable("I", digit);
        p.allDifferent(A, B, C, D, E, F, G, H, I);
        p.constraint((a, b, c, d, e, f, g, h, i) -> {
            int bc = number(b, c), ef = number(e, f), hi = number(h, i);
            return a * ef * hi + d * bc * hi + g * bc * ef == bc * ef * hi;
        }, A, B, C, D, E, F, G, H, I);
        p.constraint((a, d) -> a < d, A, D);
        p.constraint((d, g) -> d < g, D, G);
        Solver s = new Solver();
        s.solve(p, (c, a) -> logger.info("answer: " + a));
        List<List<Constraint>> order = Solver.constraintOrder(p, p.variables);
        for (int i = 0; i < order.size(); ++i)
            logger.info(p.variables.get(i) + ":" + order.get(i));
        logger.info("束縛回数: " + Arrays.toString(s.bindCount));
    }

    @Test
    public void test分数の和2() {
		logger.info(Common.methodName());
        Problem p = new Problem();
        Domain digit = Domain.rangeClosed(1, 9);
        String[] names = {"A", "B", "C", "D", "E", "F", "G", "H", "I"};
        Variable[] vars = Stream.of(names).map(n -> p.variable(n, digit)).toArray(Variable[]::new);
        p.allDifferent(vars);
        p.constraint(v -> {
            int bc = number(v[1], v[2]), ef = number(v[4], v[5]), hi = number(v[7], v[8]);
            return v[0] * ef * hi + v[3] * bc * hi + v[6] * bc * ef == bc * ef * hi;
        }, vars);
        p.constraint((a, d, g) -> a < d && d < g, vars[0], vars[3], vars[6]);
        Solver s = new Solver();
        s.solve(p, (c, a) -> logger.info("answer: " + a));
        List<List<Constraint>> order = Solver.constraintOrder(p, p.variables);
        for (int i = 0; i < order.size(); ++i)
            logger.info(p.variables.get(i) + ":" + order.get(i));
        logger.info("束縛回数: " + Arrays.toString(s.bindCount));
    }
	/**
     * https://www.youtube.com/watch?v=Z6oF_MWwsD4
     * 1/x + 1/2y + 1/3z = 4/3
     * 6yz + 3xz + 2xy = 8xyz
	 */
    @Test
    public void test整数マスター() {
		logger.info(Common.methodName());
        Problem p = new Problem();
        Domain d = Domain.range(1, 100);
        Variable X = p.variable("X", d);
        Variable Y = p.variable("Y", d);
        Variable Z = p.variable("Z", d);
        p.constraint((x, y, z) -> 6*y*z + 3*x*z + 2*x*y == 8*x*y*z, X, Y, Z);
        Solver s = new Solver();
        assertEquals(3, s.solve(p, (c, a) -> logger.info("answer: " + a)));
        logger.info(Arrays.toString(s.bindCount));
    }

    /**
     * https://www.youtube.com/watch?v=rNDqZOnzFLo
     * (n/m - n/2 + 1)l = 2  (n, m, l は3以上の整数)
     * (2n - mn + 2m)l = 4m
     */
    @Test
    public void test整数マスター25() {
		logger.info(Common.methodName());
        Problem p = new Problem();
        Domain d = Domain.range(3, 100);
        Variable L = p.variable("L", d);
        Variable M = p.variable("M", d);
        Variable N = p.variable("N", d);
        p.constraint((l, m, n) -> (2*n - m*n + 2*m)*l == 4*m, L, M, N);
        Solver s = new Solver();
        assertEquals(5, s.solve(p, (c, a) -> logger.info("answer: " + a)));
        logger.info(Arrays.toString(s.bindCount));
    }

	/**
	 * https://www.youtube.com/watch?v=b-OmPH4T4Vg
	 * a, b, cは自然数で a < b < c
	 * (a - 1)(b - 1)(c - 1) が abc - 1の約数
	 */
	@Test
	void test国際数オリ1992年の整数問題() {
		logger.info(Common.methodName());
		Problem p = new Problem();
		Domain N = Domain.range(2, 200);
		Variable A = p.variable("A", N);
		Variable B = p.variable("B", N);
		Variable C = p.variable("C", N);
		p.constraint((a, b) -> a < b, A, B);
		p.constraint((b, c) -> b < c, B, C);
		p.constraint((a, b, c) -> (a * b * c - 1) % ((a - 1) * (b - 1) * (c - 1)) == 0, A, B, C);
		Solver s = new Solver();
		s.solve(p, (c, a) -> logger.info("answer: " + a));
	}

	/**
	 * https://youtu.be/0tvF39-zpSg
	 * a, b はすべて自然数
	 * a² + b² = 2020
	 */
	@Test
	public void test整数問題2020() {
		logger.info(Common.methodName());
		final int RIGHT = 2020;
		Problem p = new Problem();
		Domain d = Domain.range(1, RIGHT);
		Variable A = p.variable("A", d);
		Variable B = p.variable("B", d);
		p.constraint((a, b) -> a * a + b * b == RIGHT, A, B);
		Solver s = new Solver();
		s.solve(p, (c, a) -> logger.info("answer: " + a));
	}

	/**
	 * https://www.youtube.com/watch?v=ZTH7m5IiJv8
	 * n, m は自然数
	 * n² + 785 = 3^m
	 */
	@Test
	public void test整数問題nm() {
		logger.info(Common.methodName());
		final int MAX = 1000;
		Problem p = new Problem();
		Domain d = Domain.range(1, MAX);
		Variable N = p.variable("N", d);
		Variable M = p.variable("M", d);
		p.constraint((n, m) -> n * n + 785 == (int)Math.pow(3, m), N, M);
		Solver s = new Solver();
		s.solve(p, (c, a) -> logger.info("answer: " + a));
	}

	/**
	 * The Viral Balloon Puzzle - The REAL Answer Explained (Using Ph.D. Level Math)
	 * Channel: MindYourDecisions
	 * URL    : https://youtu.be/DNmWtINlkhw
	 */
	@Test
	public void testTheViralBalloonPuzzle() {
	    Problem p = new Problem();
	    Domain d = Domain.range(0, 100);
	    Variable R = p.variable("R", d);
	    Variable G = p.variable("G", d);
	    Variable B = p.variable("B", d);
	    p.constraint(r -> 3 * r == 30, R);
	    p.constraint((r, g) -> r + 2 * g + 2 * g == 18, R, G);
	    p.constraint((g, b) -> 2 * g - 2 * b == 2, G, B);
	    Solver s = new Solver();
	    s.solveParallel(p, (c, m) -> logger.info("B + R * G = " + (m.get(B) + m.get(R) * m.get(G))));
	}

	/**
	 * mn - 3m - 2n + 3 = 0 (m, nは整数)
	 *
	 * 【整数問題が面白いほどわかる】不定方程式① / 因数分解型 #13
	 * https://www.youtube.com/watch?v=pNAGOeTlcc8
	 */
	@Test
	public void test不定方程式1() {
	    Problem p = new Problem();
	    Domain d = Domain.rangeClosed(-10, 10);
	    Variable M = p.variable("m", d);
	    Variable N = p.variable("n", d);
	    p.constraint((m, n) -> m * n - 3 * m - 2 * n + 3 == 0, M, N);
	    new Solver().solve(p, (c, m) -> logger.info("answer: " + m));
	}

	/**
	 * √(c² + 72)が整数となるような正の整数cをすべて求めよ
	 *
	 * √(c² + 72) = m (mは整数)
	 * c² + 72 = m²
	 *
	 * 【整数問題が面白いほどわかる】不定方程式① / 因数分解型 #13
	 * https://www.youtube.com/watch?v=pNAGOeTlcc8
	 */
	@Test
	public void test不定方程式2() {
	    Problem p = new Problem();
	    Variable C = p.variable("c", Domain.range(1, 1000));
	    Variable M = p.variable("m", Domain.range(1, 1000));
	    p.constraint((c, m) -> c * c + 72 == m * m, C, M);
	    new Solver().solve(p, (c, m) -> logger.info(
	        "answer: " + m + " √(c² + 72) = " + Math.sqrt(m.get(C) * m.get(C) + 72)));
	}

	/**
	 * 重複組合せ［入試基礎　ワンポイント演習３］ - YouTube
	 * https://www.youtube.com/watch?v=ln-c5VEYTCQ
	 */
	@Test
	public void test重複組み合わせ1() {
	    Common.methodName();
	    Problem p = new Problem();
	    Domain domain = Domain.rangeClosed(1, 8);
	    Variable A = p.variable("a", domain);
	    Variable B = p.variable("b", domain);
	    Variable C = p.variable("c", domain);
	    Variable D = p.variable("d", domain);
	    p.constraint((a, b) -> a < b, A, B);
	    p.constraint((b, c) -> b < c, B, C);
	    p.constraint((c, d) -> c < d, C, D);
	    int count = new Solver().solve(p, (c, m) -> logger.info("answer: " + m));
	    logger.info("count=" + count);
	}

	@Test
	public void test重複組み合わせ3() {
	    Common.methodName();
	    Problem p = new Problem();
	    Domain domain = Domain.rangeClosed(1, 8);
	    Variable A = p.variable("a", domain);
	    Variable B = p.variable("b", domain);
	    Variable C = p.variable("c", domain);
	    Variable D = p.variable("d", domain);
	    p.constraint((a, b) -> a < b, A, B);
	    p.constraint((b, c) -> b <= c, B, C);
	    p.constraint((c, d) -> c <= d, C, D);
	    int count = new Solver().solve(p, (c, m) -> logger.info("answer: " + m));
	    logger.info("count=" + count);
	}

	@Test
	public void test重複組み合わせ2() {
	    Common.methodName();
	    Problem p = new Problem();
	    Domain domain = Domain.rangeClosed(1, 8);
	    Variable A = p.variable("a", domain);
	    Variable B = p.variable("b", domain);
	    Variable C = p.variable("c", domain);
	    Variable D = p.variable("d", domain);
	    p.constraint((a, b) -> a <= b, A, B);
	    p.constraint((b, c) -> b <= c, B, C);
	    p.constraint((c, d) -> c <= d, C, D);
	    int count = new Solver().solve(p, (c, m) -> logger.info("answer: " + m));
	    logger.info("count=" + count);
	}
}
