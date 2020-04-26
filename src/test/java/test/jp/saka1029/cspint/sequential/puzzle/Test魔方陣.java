package test.jp.saka1029.cspint.sequential.puzzle;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runners.Parameterized.Parameters;

import jp.saka1029.cspint.sequential.Domain;
import jp.saka1029.cspint.sequential.ParallelSolver;
import jp.saka1029.cspint.sequential.Predicate0;
import jp.saka1029.cspint.sequential.Problem;
import jp.saka1029.cspint.sequential.SequentialSolver;
import jp.saka1029.cspint.sequential.Solver;
import jp.saka1029.cspint.sequential.Variable;
import test.jp.saka1029.cspint.Common;

class Test魔方陣 {

    static Logger logger = Logger.getLogger(Test魔方陣.class.getName());

    static void 魔方陣(final int n, Solver solver) {
        Problem problem = new Problem();
        int max = n * n;                // セルの総数
        int sum = n * (n * n + 1) / 2;  // 各行、列の合計
        Domain number = Domain.rangeClosed(1, max);
        // 変数定義
        Variable[][] cells = IntStream.range(0, n)
            .mapToObj(r -> IntStream.range(0, n)
                .mapToObj(c -> problem.variable(r + "@" + c, number))
                .toArray(Variable[]::new))
            .toArray(Variable[][]::new);
        // すべてのセルが異なる制約
        problem.allDifferent(Arrays.stream(cells)
            .flatMap(a -> Arrays.stream(a))
            .toArray(Variable[]::new));
        // 合計の制約を表すラムダ式
        Predicate0 checkSum = intArray -> Arrays.stream(intArray).sum() == sum;
        // 行ごとの合計の制約
        IntStream.range(0, n)
            .forEach(r -> problem.constraint(checkSum,
                IntStream.range(0, n)
                    .mapToObj(c -> cells[r][c])
                    .toArray(Variable[]::new)));
        // 列ごとの合計の制約
        IntStream.range(0, n)
            .forEach(c -> problem.constraint(checkSum,
                IntStream.range(0, n)
                    .mapToObj(r -> cells[r][c])
                    .toArray(Variable[]::new)));
        // 右下がり斜めの合計の制約
        problem.constraint(checkSum,
            IntStream.range(0, n)
                .mapToObj(r -> cells[r][r])
                .toArray(Variable[]::new));
        // 左下がり斜めの合計の制約
        problem.constraint(checkSum,
            IntStream.range(0, n)
                .mapToObj(r -> cells[r][n - r - 1])
                .toArray(Variable[]::new));
//        Solver solver = new SequentialSolver();
        solver.solve(problem, (control, result) -> {
            for (int r = 0; r < n; ++r) {
                StringBuilder sb = new StringBuilder();
                for (int c = 0; c < n; ++c)
                    sb.append(String.format("%3d", result.get(cells[r][c])));
                logger.info(sb.toString());
            }
            control.stop();
        });
    }

    @Parameters
    static List<Solver> parameters() {
        return List.of(new SequentialSolver(), new ParallelSolver());
    }

	@ParameterizedTest @MethodSource("parameters")
    public void test魔方陣3(Solver solver) {
	    logger.info(Common.methodName());
        魔方陣(3, solver);
    }

	@ParameterizedTest @MethodSource("parameters")
    public void test魔方陣4(Solver solver) {
	    logger.info(Common.methodName());
        魔方陣(4, solver);
    }

}
