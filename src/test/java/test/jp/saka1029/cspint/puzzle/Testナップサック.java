package test.jp.saka1029.cspint.puzzle;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import jp.saka1029.cspint.Domain;
import jp.saka1029.cspint.Function0;
import jp.saka1029.cspint.Problem;
import jp.saka1029.cspint.Solver;
import jp.saka1029.cspint.Variable;

class Testナップサック {

    static int[] ナップサック(int maxSize, int[] sizes, int[] values) {
        int size = sizes.length;
        Problem p = new Problem();
        Variable[] variables = IntStream.range(0, size)
            .mapToObj(i -> p.variable("v" + i, Domain.rangeClosed(0, maxSize / sizes[i])))
            .toArray(Variable[]::new);
        // 全体の単一制約
        // p.constraint(q -> IntStream.range(0, size).map(i -> q[i] *
        // sizes[i]).sum() <= maxSize, variables);
        // 分割した制約
        IntStream.range(0, size)
            .forEach(i -> p.constraint(
                q -> IntStream.rangeClosed(0, i).map(j -> q[j] * sizes[j]).sum() <= maxSize,
                Arrays.copyOf(variables, i + 1)));
        System.out.println(p.constraints);
        Function0 maximize = q -> IntStream.range(0, size).map(i -> q[i] * values[i]).sum();
        Solver s = new Solver();
        Map<Variable, Integer> map = s.maximize(p, maximize, variables);
        return IntStream.range(0, size)
            .map(i -> map.get(variables[i]))
            .toArray();
    }

    /**
     * 2.5 ナップサック問題
     * https://www.msi.co.jp/nuopt/docs/v19/examples/html/02-05-00.html
     *
     * 例題
     * 容量65のナップサックに次の表にある品物を詰め込むことにします．
     * この時，詰め込んだ品物の総価値を最大にするためには何をいくつ詰め込むと良いでしょうか．
     * ただし，同じ品物を何個詰め込んでも良いものとします．
     *
     * <pre>
     * 品物 1個あたりの価値 1個あたりのサイズ
     * 缶コーヒー 120 10
     * 水入りペットボトル 130 12
     * バナナ 80 7 りんご 100 9
     * おにぎり 250 21
     * パン 185 16
     * </pre>
     *
     */
    @Test
    void test() {
        int maxSize = 65;
        String[] names = {"coffee", "water", "banana", "apple", "rice", "bread"};
        int[] sizes = {10, 12, 7, 9, 21, 16};
        int[] values = {120, 130, 80, 100, 250, 185};
        int[] results = ナップサック(maxSize, sizes, values);
        System.out.println(Arrays.toString(results));
        assertArrayEquals(new int[] {3, 0, 2, 0, 1, 0}, results);
        assertEquals(770, IntStream.range(0, sizes.length).map(i -> results[i] * values[i]).sum());
    }

}
