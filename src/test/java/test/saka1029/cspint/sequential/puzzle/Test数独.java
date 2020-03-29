package test.saka1029.cspint.sequential.puzzle;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import jp.saka1029.cspint.sequential.Domain;
import jp.saka1029.cspint.sequential.Problem;
import jp.saka1029.cspint.sequential.Solver;
import jp.saka1029.cspint.sequential.Variable;

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
		// 行の変数を集める。
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
		// 値が確定している変数を追加します。
		for (Variable[] a : variables)
			for (Variable v : a)
				if (v.domain.size() == 1)
					bindingOrder.add(v);
		// 各制約セットをDomainサイズの小さいもの順にソートします。
		differentVariablesSet.sort(Comparator.comparing(
            a -> Arrays.stream(a).mapToInt(v -> v.domain.size()).sum()));
		/// 各制約セット内の変数を追加します。
		for (Variable[] a : differentVariablesSet)
			for (Variable v : a)
				bindingOrder.add(v);
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

	public static void 数独(int[][] question) {
		Objects.requireNonNull(question, "board is null");
		if (question.length != SIZE)
			throw new IllegalArgumentException("invalid board row size");
		if (!Arrays.stream(question).allMatch(a -> a.length == SIZE))
			throw new IllegalArgumentException("invalid board column size");
		Problem problem = new Problem();
		Variable[][] variables = defineVariables(problem, question);
		List<Variable[]> differentVariablesSet = defineConstraints(problem, variables);
		List<Variable> bindingOrder = defineBindingOrder(variables, differentVariablesSet);
		Solver solver = new Solver();
		solver.solve(problem, bindingOrder, m -> printResult(variables, m));
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
		logger.info(Common.methodName());
		数独(question);
	}

}
