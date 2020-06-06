package test.jp.saka1029.cspint.sequential.puzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

import org.junit.Test;

import jp.saka1029.cspint.sequential.Domain;
import jp.saka1029.cspint.sequential.Problem;
import jp.saka1029.cspint.sequential.Solver;
import jp.saka1029.cspint.sequential.Variable;
import test.jp.saka1029.cspint.Common;

class Test数独 {

	static final Logger logger = Logger.getLogger(Test数独.class.getName());

	static final int SIZE = 9;
	static final int SSIZE = 3;
	static final Domain DIGITS = Domain.rangeClosed(1, 9);
	static String name(int row, int col) { return row + "@" + col; }

	static Variable[][] defineVariables(Problem problem, int[][] question) {
		Variable[][] variables = new Variable[SIZE][SIZE];
		for (int row = 0; row < SIZE; ++row)
            for (int col = 0; col < SIZE; ++col) {
            	int digit = question[row][col];
            	variables[row][col] = problem.variable(name(row, col),
            		digit > 0 ? Domain.of(digit) : DIGITS);
            }
		return variables;
	}

	static List<Variable[]> defineConstraints(Problem problem, Variable[][] variables) {
		List<Variable[]> differentVariablesSet = new ArrayList<>();
		// 行の変数を集めます。
		for (int row = 0; row < SIZE; ++row)
			differentVariablesSet.add(Arrays.copyOf(variables[row], SIZE));
		// 列の変数を集めます。
		for (int col = 0; col < SIZE; ++col) {
			Variable[] column = new Variable[SIZE];
            for (int row = 0; row < SIZE; ++row)
            	column[row] = variables[row][col];
            differentVariablesSet.add(column);
		}
		// 小四角形の変数を集めます。
		for (int i = 0; i < SIZE; i += SSIZE)
            for (int j = 0; j < SIZE; j += SSIZE) {
            	Variable[] small = new Variable[SSIZE * SSIZE];
            	int p = 0;
            	for  (int row = i, rowMax = i + SSIZE; row < rowMax; ++row)
                    for  (int col = j, colMax = j + SSIZE; col < colMax; ++col)
                    	small[p++] = variables[row][col];
            	differentVariablesSet.add(small);
            }
		// すべての制約を定義します。
		for (Variable[] a : differentVariablesSet)
			problem.allDifferent(a);
		return differentVariablesSet;
	}

	static List<Variable> defineBindingOrder(Variable[][] variables, List<Variable[]> differentVariablesSet) {
		Set<Variable> bindingOrder = new LinkedHashSet<>();
		// 値が確定している変数を結果に追加します。
		Arrays.stream(variables)
		    .forEach(a -> Arrays.stream(a)
		        .filter(v -> v.domain.size() == 1)
		        .forEach(v -> bindingOrder.add(v)));
		differentVariablesSet.stream()
            // 各制約セットをDomainサイズの小さいもの順にソートします。
		    .sorted(Comparator.comparing(a -> Arrays.stream(a).mapToInt(v -> v.domain.size()).sum()))
            /// 各制約セット内の変数を追加します。
		    .forEach(a -> Arrays.stream(a)
		        .forEach(v -> bindingOrder.add(v)));
		return new ArrayList<>(bindingOrder);
	}

	static void printResult(Variable[][] variables, Map<Variable, Integer> answer) {
        StringBuilder sb = new StringBuilder();
		for (int row = 0; row < SIZE; ++row) {
			sb.setLength(0);
			for (int col = 0; col < SIZE; ++col)
				sb.append("  ").append(answer.get(variables[row][col]));
			logger.info(sb.toString());
		}
	}

	public static void 数独(int[][] question, Solver solver) {
		Objects.requireNonNull(question, "board is null");
		if (question.length != SIZE)
			throw new IllegalArgumentException("invalid board row size");
		if (!Arrays.stream(question).allMatch(a -> a.length == SIZE))
			throw new IllegalArgumentException("invalid board column size");
		Problem problem = new Problem();
		Variable[][] variables = defineVariables(problem, question);
		List<Variable[]> differentVariablesSet = defineConstraints(problem, variables);
		List<Variable> bindingOrder = defineBindingOrder(variables, differentVariablesSet);
//		Solver solver = new SequentialSolver();
//		Solver solver = new ParallelSolver();
		solver.solve(problem, bindingOrder, (c, m) -> printResult(variables, m));
	}

    @Test
	public void testWikipedia() {
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
		logger.info(Common.methodName());
		数独(question, new Solver());
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
		logger.info(Common.methodName());
		数独(question, new Solver());
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
		logger.info(Common.methodName());
		数独(question, new Solver());
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
		logger.info(Common.methodName());
		数独(question, new Solver());
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
		logger.info(Common.methodName());
		数独(question, new Solver());
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
		logger.info(Common.methodName());
		数独(question, new Solver());
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
		logger.info(Common.methodName());
		数独(question, new Solver());
	}

	@Test
	void testGood_at_Sudoku_Heres_some_youll_never_complete() {
		// http://theconversation.com/good-at-sudoku-heres-some-youll-never-complete-5234
		int[][] question = {
			{ 0, 0, 0, 7, 0, 0, 0, 0, 0 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 4, 3, 0, 2, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 6 },
			{ 0, 0, 0, 5, 0, 9, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 4, 1, 8 },
			{ 0, 0, 0, 0, 8, 1, 0, 0, 0 },
			{ 0, 0, 2, 0, 0, 0, 0, 5, 0 },
			{ 0, 4, 0, 0, 0, 0, 3, 0, 0 },
		};
		logger.info(Common.methodName());
		数独(question, new Solver());
	}
}
