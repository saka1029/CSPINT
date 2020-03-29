package test.jp.saka1029.cspint.sequential;

import java.util.Arrays;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import jp.saka1029.cspint.sequential.Domain;
import jp.saka1029.cspint.sequential.Problem;
import jp.saka1029.cspint.sequential.Solver;
import jp.saka1029.cspint.sequential.Variable;

/**
 * この春から新生活を始めることになった５人兄弟。
 * 彼らの話から、それぞれの年齢とどんな新生活が
 * 始まるのかを当ててください。
 * なお、５人は年齢の上下に関係なく、
 * 名前を呼び捨てにすることはありますが、
 * 「○○姉さん」と言っているときは○○さんは話してより年上です。
 * また、自分のことを他人のように言っている人はいません。
 *
 * 年齢:       { 25, 26, 27, 29, 30 }
 * 新生活:     { 国内転勤, 海外転勤, 転職, 結婚, ペットを飼う }
 *
 * セツオ：　　オレはこの春、結婚することになったんだ。
 * イクミ：　　私はセツオより一つ年上よ。２５才で思い切って転職する人がいるのね。
 * カナコ：　　私は海外転勤になっちゃった。
 * 　　　　　　私はツキコとは１才違いで、セツオとは３才違いよ。
 * シンイチ：　カナコ姉さんはしっかりしていますよね。
 * 　　　　　　ところで、３０才の記念にペットを飼い始めたのは誰だっけ？
 * ツキコ：　　私もイクミ姉さんのように仕事頑張らないとね。
 */

class Test春からみんな新生活 {

    static final Logger logger = Logger.getLogger(Test春からみんな新生活.class.toString());

    static final int セツオ = 0, イクミ = 1, カナコ = 2, シンイチ = 3, ツキコ = 4, 人数 = 5;
    static final String[] 名前 = {"セツオ", "イクミ", "カナコ", "シンイチ", "ツキコ"};
    static final int 国内転勤 = 0, 海外転勤 = 1, 転職 = 2, 結婚 = 3, ペットを飼う = 4;
    static final String[] 新生活名 = {"国内転勤", "海外転勤", "転職", "結婚", "ペットを飼う"};
    static final int[] 年齢 = { 25, 26, 27, 29, 30 };
    static final int 年齢列 = 0, 新生活列 = 1, 列数 = 2;

    @Test
    void test() {
        Domain 新生活Domain = Domain.of(国内転勤, 海外転勤, 転職, 結婚, ペットを飼う);
        Domain 年齢Domain = Domain.of(年齢);
        Problem problem = new Problem();
        Variable[][] v = new Variable[人数][列数];
        for (int i = 0; i < 人数; ++i) {
            v[i][年齢列] = problem.variable(名前[i] + ".年齢", 年齢Domain);
            v[i][新生活列] = problem.variable(名前[i] + ".新生活", 新生活Domain);
        }
        Variable[] flat = Arrays.stream(v).flatMap(a -> Arrays.stream(a)).toArray(Variable[]::new);
        problem.allDifferent(IntStream.range(0, 人数).mapToObj(i -> v[i][年齢列]).toArray(Variable[]::new));
        problem.allDifferent(IntStream.range(0, 人数).mapToObj(i -> v[i][新生活列]).toArray(Variable[]::new));
        // セツオ：　　オレはこの春、結婚することになったんだ。
        problem.constraint(x -> x == 結婚, v[セツオ][新生活列]);
        problem.constraint((x, y) -> x == y + 1, v[イクミ][年齢列], v[セツオ][年齢列]);
        // 25才で転職する人がいる
        problem.constraint(a -> {
            boolean result = false;
            for (int i = 0, size = a.length; i < size; i += 列数)
                result |= a[i + 年齢列] == 25 && a[i + 新生活列] == 転職;
            return result;
        }, flat) ;
        problem.constraint(x -> x == 海外転勤, v[カナコ][新生活列]);
        problem.constraint((x, y) -> Math.abs(x - y) == 1, v[カナコ][年齢列], v[ツキコ][年齢列]);
        problem.constraint((x, y) -> Math.abs(x - y) == 3, v[カナコ][年齢列], v[セツオ][年齢列]);
        problem.constraint((x, y) -> x < y, v[シンイチ][年齢列], v[カナコ][年齢列]);
        // 30才でペット
        problem.constraint(a -> {
            boolean result = false;
            for (int i = 0, size = a.length; i < size; i += 列数)
                result |= a[i + 年齢列] == 30 && a[i + 新生活列] == ペットを飼う;
            return result;
        }, flat) ;
        problem.constraint((x, y) -> x < y, v[ツキコ][年齢列], v[イクミ][年齢列]);
        Solver solver = new Solver();
        solver.solve(problem, m -> {
            for (Entry<Variable, Integer> e : m.entrySet())
                if (e.getKey().name.endsWith("新生活"))
                    logger.info(e.getKey() + "=" + 新生活名[e.getValue()]);
                else
                    logger.info(e.getKey() + "=" + e.getValue());
        });
    }

}
