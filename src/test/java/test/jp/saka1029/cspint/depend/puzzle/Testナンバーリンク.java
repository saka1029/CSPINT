package test.jp.saka1029.cspint.depend.puzzle;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import jp.saka1029.cspint.depend.Variable;

class Testナンバーリンク {

    static final Logger logger = Logger.getLogger(Testナンバーリンク.class.getName());

    static Variable[] neighbors(Variable[][] variables, int r, int c) {
        int rmax = variables.length;
        int cmax = variables[0].length;
        List<Variable> list = new ArrayList<>();
        new Object() {
            void add(int r, int c) {
                if (r < 0 || r >= rmax) return;
                if (c < 0 || c >= cmax) return;
                list.add(variables[r][c]);
            }

            void add() {
                add(r, c);
                add(r - 1, c);
                add(r + 1, c);
                add(r, c - 1);
                add(r, c + 1);

            }
        }.add();
        return list.toArray(Variable[]::new);
    }

    @Test
    void test() {
    }

}
