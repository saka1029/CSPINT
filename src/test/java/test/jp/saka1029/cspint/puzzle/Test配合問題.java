package test.jp.saka1029.cspint.puzzle;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import jp.saka1029.cspint.Domain;
import jp.saka1029.cspint.Function0;
import jp.saka1029.cspint.Problem;
import jp.saka1029.cspint.Solver;
import jp.saka1029.cspint.Variable;

class Test配合問題 {

    static int sum(int n, int[] k, int[] x) {
        return IntStream.range(0, n).map(i -> k[i] * x[i]).sum();
    }

    /**
     * 例題
     *
     * 　鉛，亜鉛，スズの構成比率が，それぞれ30%，30%，40%となるような合金を，
     *   市販の合金を混ぜ合わせ，できるだけ安いコストで生成することを考えます．
     *   現在手に入れることができる市販の合金は9種類で，それらの構成比率と
     *   単位量あたりのコストは以下の通りです．
     *
     *     市販の合金   1   2   3   4   5   6   7   8   9
     *     鉛（%）    20  50  30  30  30  60  40  10  10
     *     亜鉛（%）   30  40  20  40  30  30  50  30  10
     *     スズ（%）   50  10  50  30  40  10  10  60  80
     *     コスト（$/lb）   7.3 6.9 7.3 7.5 7.6 6.0 5.8 4.3 4.1
     *
     * 　所望の組成を持つ合金をコストを一番安く生成するには，市販の合金を
     *   どのように混ぜ合わせれば良いでしょうか．
     *   
     *   http://www.msi.co.jp/nuopt/docs/v20/examples/html/02-01-00.html
     */
    @Test
    void test() {
        int SIZE = 9;
        Problem problem = new Problem();
        Domain domain = Domain.of(IntStream.range(0, 10).map(i -> 10 * i).toArray());
        Variable[] vars = IntStream.range(0, SIZE)
            .mapToObj(i -> problem.variable("x" + i, domain)).toArray(Variable[]::new);
        int[] ONES = {1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] 鉛 = {20, 50, 30, 30, 30, 60, 40, 10, 10};
        int[] 亜鉛 = {30, 40, 20, 40, 30, 30, 50, 30, 10};
        int[] スズ = {50, 10, 50, 30, 40, 10, 10, 60, 80};
        int[] コスト = {73, 69, 73, 75, 76, 60, 58, 43, 41};
        // 目的関数
        Function0 maximize = x -> -sum(SIZE, コスト, x);
        // 混合比率の制約
        problem.constraint(x -> sum(SIZE, ONES, x) == 100, vars);
        problem.constraint(x -> sum(SIZE, 鉛, x) == 3000, vars);
        problem.constraint(x -> sum(SIZE, 亜鉛, x) == 3000, vars);
        problem.constraint(x -> sum(SIZE, スズ, x) == 4000, vars);
        // 部分制約
        IntStream.range(1, SIZE).forEach(i -> {
            Variable[] subset = Arrays.copyOf(vars, i);
            problem.constraint(x -> sum(i, ONES, x) <= 100, subset);
            problem.constraint(x -> sum(i, 鉛, x) <= 3000, subset);
            problem.constraint(x -> sum(i, 亜鉛, x) <= 3000, subset);
            problem.constraint(x -> sum(i, スズ, x) <= 4000, subset);
        });
        // 求解
        Solver solver = new Solver();
        Solver.printConstraintOrder(problem);
        Map<Variable, Integer> result = solver.maximize(problem, maximize, vars);
        System.out.println(result);
        int[] 配合比率 = IntStream.range(0, SIZE)
            .map(i -> result.get(problem.variable("x" + i)))
            .toArray();
        assertEquals(4980, sum(SIZE, コスト, 配合比率));
        assertEquals(3000, sum(SIZE, 鉛, 配合比率));
        assertEquals(3000, sum(SIZE, 亜鉛, 配合比率));
        assertEquals(4000, sum(SIZE, スズ, 配合比率));
    }

}
