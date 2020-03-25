package test.jp.saka1029.cspint.sequential;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

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
		logger.info("start");
		Problem problem = new Problem();
		defineVariables(problem, question);
		defineConstraints(problem);
		assertEquals(81, problem.variables.size());
		assertEquals(1, problem.variables.get(0).domain.size());
		assertEquals(5, problem.variables.get(0).domain.get(0));
		assertEquals(1, problem.variables.get(1).domain.size());
		assertEquals(9, problem.variables.get(2).domain.size());
		assertEquals(36 * 27, problem.constraints.size());
		Solver solver = new Solver();
		List<Variable> order = new ArrayList<>(problem.variables);
		// ドメインの小さい順に束縛
		order.sort(Comparator.comparing(v -> v.domain.size()));
		solver.solve(problem, order, map -> print(map));
		logger.info("束縛回数: " + Arrays.toString(solver.bindCount));
	}

}
