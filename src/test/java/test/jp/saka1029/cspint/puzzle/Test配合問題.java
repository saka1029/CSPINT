package test.jp.saka1029.cspint.puzzle;

import java.util.Map;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import jp.saka1029.cspint.Domain;
import jp.saka1029.cspint.Function0;
import jp.saka1029.cspint.Problem;
import jp.saka1029.cspint.Solver;
import jp.saka1029.cspint.Variable;

class Test配合問題 {

    /**
     * 例題
     *
     * 　鉛，亜鉛，スズの構成比率が，それぞれ30%，30%，40%となるような合金を，市販の合金を混ぜ合わせ，できるだけ安いコストで生成することを考えます．現在手に入れることができる市販の合金は9種類で，それらの構成比率と単位量あたりのコストは以下の通りです．
     *
     * 市販の合金   1   2   3   4   5   6   7   8   9
     * 鉛（%）    20  50  30  30  30  60  40  10  10
     * 亜鉛（%）   30  40  20  40  30  30  50  30  10
     * スズ（%）   50  10  50  30  40  10  10  60  80
     * コスト（$/lb）   7.3 6.9 7.3 7.5 7.6 6.0 5.8 4.3 4.1
     *
     * 　所望の組成を持つ合金をコストを一番安く生成するには，市販の合金をどのように混ぜ合わせれば良いでしょうか．
     */
    @Test
    void test() {
        Problem problem = new Problem();
        Domain domain = Domain.of(0, 10, 20, 30, 40, 50, 60, 70, 80, 90);
        Variable[] vars = IntStream.range(0, 10)
            .mapToObj(i -> problem.variable("x" + i, domain)).toArray(Variable[]::new);
        // 目的関数
        Function0 maximize = x -> -(73 * x[0] + 69 * x[1] + 73 * x[2] + 75 * x[3] + 76 * x[4]
            + 60 * x[5] + 58 * x[6] + 43 * x[7] + 41 * x[8]);
        // 混合比率の制約
        problem.constraint(x -> x[0] + x[1] + x[2] + x[3] + x[4] + x[5] + x[6] + x[7] + x[8] == 100, vars);
        problem.constraint(x -> 20 * x[0] + 50 * x[1] + 30 * x[2] + 30 * x[3] + 30 * x[4]
            + 60 * x[5] + 40 * x[6] + 10 * x[7] + 10 * x[8] == 30, vars);
        problem.constraint(x -> 30 * x[0] + 40 * x[1] + 20 * x[2] + 40 * x[3] + 30 * x[4]
            + 30 * x[5] + 50 * x[6] + 30 * x[7] + 10 * x[8] == 30, vars);
        problem.constraint(x -> 50 * x[0] + 10 * x[2] + 50 * x[2] + 30 * x[3] + 40 * x[4]
            + 10 * x[5] + 10 * x[6] + 60 * x[7] + 80 * x[8] == 40, vars);
        // 求解
        Solver solver = new Solver();
        Map<Variable, Integer> result = solver.maximize(problem, maximize, vars);
        System.out.println(result);
    }

}
