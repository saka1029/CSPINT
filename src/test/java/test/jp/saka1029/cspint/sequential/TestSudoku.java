package test.jp.saka1029.cspint.sequential;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import jp.saka1029.cspint.sequential.Constraint;
import jp.saka1029.cspint.sequential.Domain;
import jp.saka1029.cspint.sequential.Problem;
import jp.saka1029.cspint.sequential.Solver;
import jp.saka1029.cspint.sequential.Variable;

class TestSudoku {

	static Logger logger = Logger.getLogger(TestSudoku.class.getName());

	static final int ROWS = 9, COLS = 9;
	static final int SMALL_ROWS = 3, SMALL_COLS = 3;
	static final Domain DOMAIN = Domain.rangeClosed(1, 9);
	static String name(int row, int col) { return row + "@" + col; }

	static void defineVariables(Problem p, int[][] q) {
		for (int r = 0; r < ROWS; ++r)
			for (int c = 0; c < COLS; ++c) {
				int value = q[r][c];
				p.variable(name(r, c), value == 0 ? DOMAIN : Domain.of(value));
			}
	}

	static void defineConstraints(Problem p) {
		Variable[] variables = new Variable[COLS];
		for (int r = 0; r < ROWS; ++r) {
			for (int c = 0; c < COLS; ++c)
				variables[c] = p.variable(name(r, c));
			p.allDifferent(variables);
		}
		for (int c = 0; c < COLS; ++c) {
			for (int r = 0; r < ROWS; ++r)
				variables[r] = p.variable(name(r, c));
			p.allDifferent(variables);
		}
		for (int i = 0; i < ROWS; i += 3)
			for (int j = 0; j < COLS; j += 3) {
				for (int r = i, rmax = i + SMALL_ROWS, k = 0; r < rmax; ++r)
					for (int c = j, cmax = j + SMALL_COLS; c < cmax; ++c)
						variables[k++] = p.variable(name(r, c));
				p.allDifferent(variables);
			}
	}

	static void print(Map<Variable, Integer> result) {
		int[][] board = new int[ROWS][COLS];
		for (Entry<Variable, Integer> e : result.entrySet()) {
			String[] c = e.getKey().name.split("@");
			board[Integer.parseInt(c[0])][Integer.parseInt(c[1])] = e.getValue();
		}
		for (int[] row : board)
			logger.info(Arrays.toString(row));
	}

	static void solve(String name, int[][] question) {
		logger.info(name);
		Problem problem = new Problem();
		defineVariables(problem, question);
		defineConstraints(problem);
		Solver solver = new Solver();
		// ドメインの小さい順に束縛
		List<Variable> order = problem.variables.stream()
		    .sorted(Comparator.comparing(v -> v.domain.size()))
		    .collect(Collectors.toList());
		assertEquals(1, solver.solve(problem, order, map -> print(map)));
		logger.info("束縛回数: " + Arrays.toString(solver.bindCount));
		logger.info("合計束縛回数: " + Arrays.stream(solver.bindCount).sum());
	}

	@Disabled
	@Test
	void testTemplate() {
		// http
		int[][] question = {
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0 },
		};
		solve("Template", question);
	}

	@Test
	void testWikipedia() {
		// Wikipedia 数独 の例題
		// https://ja.wikipedia.org/wiki/%E6%95%B0%E7%8B%AC
		int[][] question = {
			{ 5, 3, 0, 0, 7, 0, 0, 0, 0 },
			{ 6, 0, 0, 1, 9, 5, 0, 0, 0 },
			{ 0, 9, 8, 0, 0, 0, 0, 6, 0 },
			{ 8, 0, 0, 0, 6, 0, 0, 0, 3 },
			{ 4, 0, 0, 8, 0, 3, 0, 0, 1 },
			{ 7, 0, 0, 0, 2, 0, 0, 0, 6 },
			{ 0, 6, 0, 0, 0, 0, 2, 8, 0 },
			{ 0, 0, 0, 4, 1, 9, 0, 0, 5 },
			{ 0, 0, 0, 0, 8, 0, 0, 7, 9 },
		};
		solve("Wikipedia", question);
	}

	@Test
	void test難問SUDOKU() {
		// 難問SUDOKU の例題
		// https://www.danboko.net/
		int[][] question = {
			{ 2, 0, 0, 4, 0, 6, 0, 0, 9 },
			{ 0, 3, 1, 0, 5, 0, 6, 8, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 6, 0, 0, 9, 0, 5, 0, 0, 4 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 8, 0, 6, 0, 7, 0, 9, 0 },
			{ 5, 0, 0, 0, 0, 0, 0, 0, 2 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 4, 9, 5, 0, 1, 8, 3, 0 },
		};
		solve("難問SUDOKU", question);
	}

	@Test
	void testナンプレ問題10() {
		// https://si-coding.net/sudoku10.html
		int[][] question = {
			{ 0, 0, 1, 0, 9, 0, 0, 0, 0 },
			{ 0, 5, 0, 4, 0, 0, 0, 0, 2 },
			{ 8, 0, 3, 0, 1, 0, 5, 0, 0 },
			{ 0, 0, 6, 0, 0, 0, 0, 2, 0 },
			{ 0, 0, 0, 0, 6, 0, 0, 0, 8 },
			{ 2, 0, 0, 8, 0, 3, 0, 6, 5 },
			{ 0, 0, 0, 0, 0, 6, 0, 0, 4 },
			{ 0, 0, 0, 0, 0, 4, 0, 7, 0 },
			{ 0, 9, 2, 0, 0, 0, 0, 0, 3 },
		};
		solve("ナンプレ問題10", question);
	}

	@Test
	void test問題22001難問() {
		// https://number-place-puzzle.net/22001.html#content
		int[][] question = {
			{ 0, 5, 0, 7, 0, 0, 6, 0, 0 },
			{ 7, 1, 0, 0, 2, 0, 0, 4, 0 },
			{ 0, 8, 0, 0, 0, 5, 0, 0, 0 },
			{ 0, 0, 0, 8, 0, 0, 4, 7, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 5 },
			{ 1, 2, 0, 3, 0, 0, 0, 6, 0 },
			{ 0, 0, 0, 0, 0, 6, 2, 0, 8 },
			{ 0, 0, 0, 0, 0, 7, 0, 3, 0 },
			{ 0, 0, 3, 5, 0, 0, 0, 0, 0 },
		};
		solve("問題22001難問", question);
	}

	@Test
	void testナンプレNo601010() {
		// https://numpre7.com/np601010
		int[][] question = {
			{ 0, 0, 1, 0, 6, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 1, 5, 0, 0 },
			{ 0, 8, 0, 3, 0, 0, 0, 0, 9 },
			{ 0, 7, 0, 4, 0, 9, 8, 0, 0 },
			{ 2, 0, 0, 0, 0, 0, 0, 0, 4 },
			{ 0, 0, 6, 1, 0, 2, 0, 5, 0 },
			{ 4, 0, 0, 0, 0, 5, 0, 7, 0 },
			{ 0, 0, 9, 6, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 7, 0, 6, 0, 0 },
		};
		solve("ナンプレNo601010", question);
	}

	@Test
	void testOurHardestSudokuAndHowToSolveIt() {
	    // YouTube
		// https://youtu.be/-ZZFEgCQsvA
		int[][] question = {
			{ 0, 0, 1, 0, 6, 0, 0, 5, 9 },
			{ 0, 0, 0, 0, 0, 3, 0, 2, 0 },
			{ 0, 6, 0, 0, 8, 0, 0, 0, 0 },
			{ 4, 0, 0, 0, 0, 0, 5, 0, 0 },
			{ 0, 2, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 7, 0, 2, 0, 0, 4, 8, 0 },
			{ 8, 0, 0, 0, 0, 0, 9, 0, 5 },
			{ 7, 0, 0, 6, 0, 9, 0, 3, 0 },
			{ 0, 0, 5, 0, 0, 0, 0, 4, 0 },
		};
		solve("Our Hardest Sudoku And How To Solve It", question);
	}

	static int domainSize(Constraint c) {
	    return c.variables.stream()
	        .mapToInt(v -> v.domain.size())
	        .sum();
	}

	static Comparator<Constraint> variableSize = Comparator.comparing(c -> -c.variables.size());
	static Comparator<Constraint> variableSizeAndDomainSize = variableSize.thenComparing(c -> domainSize(c));

	// 制約が持つ変数のすべてのドメイン数の和が小さいもの順に束縛する。
	static List<Variable> optimize(Problem problem) {
	    Set<Variable> set = new LinkedHashSet<>();
//	    problem.variables.stream()
//	        .filter(v -> v.domain.size() == 1)
//	        .forEach(v -> set.add(v));
	    problem.constraints.stream()
//	        .sorted(variableSizeAndDomainSize)
	        .sorted(Comparator.comparing(c -> domainSize(c)))
	        .flatMap(c -> c.variables.stream()
	            .sorted(Comparator.comparing(v -> v.domain.size())))
	        .forEach(v -> set.add(v));
	    return new ArrayList<>(set);
	}

	@Test
	void testEvil_sudoku_with_17_initial_values() {
		// https://www.free-sudoku.com/sudoku.php?dchoix=evil
		int[][] question = {
			{ 1, 0, 0, 7, 0, 0, 0, 0, 6 },
			{ 0, 8, 0, 0, 0, 0, 0, 9, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 6, 0, 0, 4, 0, 0, 2, 0, 0 },
			{ 4, 0, 0, 0, 8, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 9, 0, 0, 5, 0 },
			{ 0, 5, 3, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 2, 0, 0, 4, 0, 0 },
			{ 0, 9, 0, 0, 0, 0, 0, 0, 0 },
		};
		logger.info("Evil sudoku with 17 initial values");
        Problem problem = new Problem();
        defineVariables(problem, question);
        defineConstraints(problem);
        Solver solver = new Solver();
        // ドメインの小さい順に束縛
        List<Variable> order = optimize(problem);
        assertEquals(1, solver.solve(problem, order, map -> print(map)));
        logger.info("束縛回数: " + Arrays.toString(solver.bindCount));
		logger.info("合計束縛回数: " + Arrays.stream(solver.bindCount).sum());
	}

}
