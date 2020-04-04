package test.jp.saka1029.cspint.sequential.puzzle;

import java.util.Arrays;
import java.util.logging.Logger;

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

    enum 名前 { セツオ, イクミ, カナコ, シンイチ, ツキコ };
    static final int 人数 = 名前.values().length;
    static final int セツオ行 = 名前.セツオ.ordinal();
    static final int イクミ行 = 名前.イクミ.ordinal();
    static final int カナコ行 = 名前.カナコ.ordinal();
    static final int シンイチ行 = 名前.シンイチ.ordinal();
    static final int ツキコ行 = 名前.ツキコ.ordinal();

    enum 新生活 { 国内転勤, 海外転勤, 転職, 結婚, ペットを飼う }
    static final int[] 年齢 = { 25, 26, 27, 29, 30 };

    enum 属性 { 年齢, 新生活 };
    static final int 列数 = 属性.values().length;
    static final int 年齢列 = 属性.年齢.ordinal();
    static final int 新生活列 = 属性.新生活.ordinal();

//    static int o(Enum e) { return e.ordinal(); }

    @Test
    void test() {
        Domain 新生活Domain = Domain.of(Arrays.stream(新生活.values()).mapToInt(新生活::ordinal).toArray());
        Domain 年齢Domain = Domain.of(年齢);
        Problem problem = new Problem();
        Variable[][] v = new Variable[人数][列数];
        for (int i = 0; i < 人数; ++i) {
            v[i][年齢列] = problem.variable(名前.values()[i] + ".年齢", 年齢Domain);
            v[i][新生活列] = problem.variable(名前.values()[i] + ".新生活", 新生活Domain);
        }
        // 年齢、新生活はそれぞれ異なる。
        problem.allDifferentEachColumns(v);
        // セツオ：　　オレはこの春、結婚することになったんだ。
        problem.constraint(x -> x == 新生活.結婚.ordinal(), v[セツオ行][新生活列]);
        // イクミ：　　私はセツオより一つ年上よ。
        problem.constraint((x, y) -> x == y + 1, v[イクミ行][年齢列], v[セツオ行][年齢列]);
        // ２５才で思い切って転職する人がいるのね。
        problem.constraint(a -> Arrays.stream(a)
        	.anyMatch(r -> r[年齢列] == 25 && r[新生活列] == 新生活.転職.ordinal()), v);
        // カナコ：　　私は海外転勤になっちゃった。
        problem.constraint(x -> x == 新生活.海外転勤.ordinal(), v[カナコ行][新生活列]);
        //             私はツキコとは１才違いで、
        problem.constraint((x, y) -> Math.abs(x - y) == 1, v[カナコ行][年齢列], v[ツキコ行][年齢列]);
        //             セツオとは３才違いよ。
        problem.constraint((x, y) -> Math.abs(x - y) == 3, v[カナコ行][年齢列], v[セツオ行][年齢列]);
        // シンイチ：　カナコ姉さんはしっかりしていますよね。
        problem.constraint((x, y) -> x < y, v[シンイチ行][年齢列], v[カナコ行][年齢列]);
        // 　　　　　　ところで、３０才の記念にペットを飼い始めたのは誰だっけ？
        problem.constraint(a -> Arrays.stream(a)
			.anyMatch(r -> r[年齢列] == 30 && r[新生活列] == 新生活.ペットを飼う.ordinal()), v) ;
        // ツキコ：　　私もイクミ姉さんのように仕事頑張らないとね。
        problem.constraint((x, y) -> x < y, v[ツキコ行][年齢列], v[イクミ行][年齢列]);
        Solver solver = new Solver();
        solver.solve(problem, m -> {
            for (名前 name : 名前.values())
                logger.info(String.format("%s : %s才 %s",
                    name, m.get(problem.variable(name + ".年齢")),
                    新生活.values()[m.get(problem.variable(name + ".新生活"))]));
        });
    }

}
