package test.jp.saka1029.cspint.puzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import jp.saka1029.cspint.Constraint;
import jp.saka1029.cspint.Domain;
import jp.saka1029.cspint.Predicate0;
import jp.saka1029.cspint.Problem;
import jp.saka1029.cspint.Solver;
import jp.saka1029.cspint.Variable;
import test.jp.saka1029.cspint.Common;

class Testナンバーリンク {

    static final Logger logger = Common.getLogger(Testナンバーリンク.class);

    static int[] numberLink(int[][] matrix) {
        Problem problem = new Problem();
        int rows = matrix.length, cols = matrix[0].length;
        Variable[][] variables = new Variable[rows][cols];
        Domain numbers = Domain.of(Arrays.stream(matrix)
            .flatMapToInt(row -> IntStream.of(row))
            .filter(n -> n != 0)
            .distinct()
            .toArray());
        Solver solver = new Solver();

        new Object() {
            List<Variable> bindingOrder;

            String name(int r, int c) {
                return String.format("v%d_%d", r, c);
            }

            void defineVariables() {
                for (int r = 0; r < rows; ++r)
                    for (int c = 0; c < cols; ++c) {
                        int n = matrix[r][c];
                        variables[r][c] = problem.variable(name(r, c),
                            n == 0 ? numbers : Domain.of(n));
                    }
            }

            void addNeighbors(List<Variable> list, int r, int c) {
                if (r > 0) list.add(variables[r - 1][c]);
                if (r < rows - 1) list.add(variables[r + 1][c]);
                if (c > 0) list.add(variables[r][c - 1]);
                if (c < cols - 1) list.add(variables[r][c + 1]);
            }

            Predicate0 endPoint = a -> IntStream.of(a)
                    .filter(n -> n == a[0]).count() == 2;
            Predicate0 passingPoint = a -> IntStream.of(a)
                    .filter(n -> n == a[0]).count() == 3;

            void defineConstraint() {
                for (int r = 0; r < rows; ++r)
                    for (int c = 0; c < cols; ++c) {
                        List<Variable> neighbors = new ArrayList<>();
                        neighbors.add(variables[r][c]);
                        addNeighbors(neighbors, r, c);
                        problem.constraint(matrix[r][c] != 0 ? endPoint : passingPoint,
                            neighbors.toArray(Variable[]::new));
                    }
            }

            void print(Map<Variable, Integer> map) {
                StringBuilder sb = new StringBuilder();
                for (int r = 0; r < rows; ++r) {
                    sb.setLength(0);
                    for (int c = 0; c < cols; ++c)
                        sb.append(map.get(variables[r][c])).append(" ");
                    logger.info(sb.toString());
                }
            }

            void debug() {
                List<List<Constraint>> co = Solver.constraintOrder(problem, bindingOrder);
                for (int i = 0, max = bindingOrder.size(); i < max; ++i)
                    logger.info(String.format("%s %d %s", bindingOrder.get(i), solver.bindCount[i], co.get(i)));
            }

            int complicated(int r, int c) {
                if (matrix[r][c] != 0) return 0;
                int count = 0;
                if (r > 0 && matrix[r - 1][c] == 0) ++count;
                if (r < rows - 1 && matrix[r + 1][c] == 0) ++count;
                if (c > 0 && matrix[r][c - 1] == 0) ++count;
                if (c < cols - 1 && matrix[r][c + 1] == 0) ++count;
                return count;
            }

            void solve() {
                defineVariables();
                defineConstraint();
                List<Entry<Integer, Variable>> vc = new ArrayList<>();
                for (int r = 0; r < rows; ++r)
                    for (int c = 0; c < cols; ++c)
                        vc.add(Map.entry(complicated(r, c), variables[r][c]));
                bindingOrder = vc.stream()
                    .sorted(Comparator.comparing(Entry::getKey))
                    .map(Entry::getValue)
                    .toList();
//                bindingOrder = problem.variables.stream()
//                    .sorted(Comparator.comparing(v -> v.domain.size()))
//                    .toList();
//                Solver.printConstraintOrder(problem, bindingOrder);
                solver.solve(problem, bindingOrder, (c, map) -> print(map));
                debug();
            }
        }.solve();
        return solver.bindCount;
    }

    @Test
    void testSample() {
        logger.info(Common.methodName());
        // ナンバーリンクの遊び方、ルール、解き方 | WEBニコリ
        // https://www.nikoli.co.jp/ja/puzzles/numberlink/
        int[][] question = {
            {0, 0, 0, 0, 3, 2, 1},
            {0, 0, 0, 0, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 2, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 3, 5, 0, 0, 4, 0},
            {4, 0, 0, 0, 0, 0, 5},
        };
        int[] c = numberLink(question);
    }

    @Test
    void testWikipedia() {
        logger.info(Common.methodName());
        int[][] question = {
            {3, 0, 0, 0, 1},
            {0, 0, 0, 0, 0},
            {0, 2, 0, 3, 0},
            {0, 0, 0, 0, 0},
            {1, 0, 0, 0, 2},
        };
        int[] c = numberLink(question);
    }

    @Test
    void testTECH_PROjin() {
        logger.info(Common.methodName());
        // 【C++】ナンバーリンク【前編】 | TECH PROjin
        // https://tech.pjin.jp/blog/2020/09/01/numberlink-1/#numberlink_02
        int[][] question = {
            {1, 2, 3, 1},
            {0, 0, 0, 0},
            {0, 2, 3, 0},
            {0, 0, 0, 0},
        };
        int[] c = numberLink(question);
    }

    @Test
    void testSimple3x3() {
        logger.info(Common.methodName());
        int[][] question = {
            {1, 2, 1},
            {0, 2, 0},
            {0, 0, 0},
        };
        int[] c = numberLink(question);
    }

}
