package test.jp.saka1029.cspint.puzzle;

import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import test.jp.saka1029.cspint.Common;

class Test包絡分析法 {

    static final Logger logger = Common.getLogger(Test包絡分析法.class);
    /**
     * 例題
     *
     * 　ある会社は，以下の6店舗を抱えている．
     *
     *     店舗番号    1   2   3   4   5   6
     *     店員数      5   10  20  20  30  50
     *     稼働時間    24  12  12  24  12  12
     *     売上        2   6   10  12  12  20
     *
     * 　各店舗が，全店舗に対して相対的に効率的であるかどうかを判定せよ．
     *
     * https://www.msi.co.jp/nuopt/docs/v19/examples/html/02-04-00.html
     */
    @Test
    void test() {
        int[] 店員数 = {5, 10, 20, 20, 30, 50};
        int[] 稼働時間 = {24, 12, 12, 24, 12, 12};
        int[] 売上 = {2, 6, 10, 12, 12, 20};
        int SIZE = 店員数.length;
        for (int i = 0; i < SIZE; ++i)
            logger.info(String.format("店舗番号%d: %s", i + 1, 効率(店員数[i], 稼働時間[i], 売上[i])));
    }

    static double 効率(int 店員数, int 稼働時間, int 売上) {
        return (double)売上 / (店員数 * 稼働時間);
    }

}
