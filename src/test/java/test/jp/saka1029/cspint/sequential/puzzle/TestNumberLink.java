package test.jp.saka1029.cspint.sequential.puzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import org.junit.Test;

import jp.saka1029.cspint.sequential.Domain;
import jp.saka1029.cspint.sequential.Predicate3;
import jp.saka1029.cspint.sequential.Predicate4;
import jp.saka1029.cspint.sequential.Predicate5;
import jp.saka1029.cspint.sequential.Problem;
import jp.saka1029.cspint.sequential.Solver;
import jp.saka1029.cspint.sequential.Variable;

public class TestNumberLink {
    
    static final Logger logger = Logger.getLogger(TestNumberLink.class.toString());

    static String name(int r, int c) {
        return r + "@" + c;
    }
    
    static Domain defineDomain(int[][] board) {
        Set<Integer> numbers = Arrays.stream(board)
            .flatMapToInt(r -> Arrays.stream(r))
            .filter(n -> n > 0)
            .collect(TreeSet::new, (set, n) -> set.add(n), (a, b) -> a.addAll(b));
        return Domain.of(numbers.stream().mapToInt(n -> n).toArray());
    }
    
    static Variable[][] defineVariables(Problem problem, int[][] board, Domain domain) {
        int rows = board.length;
        int cols = board[0].length;
        Variable[][] variables = new Variable[rows][cols];
        for (int r = 0; r < rows; ++r)
            for (int c = 0; c < rows; ++c) {
                int number = board[r][c];
                variables[r][c] = problem.variable(name(r, c),
                    number == 0 ? domain : Domain.of(number));
            }
        return variables;
    }
    
    static Predicate5 C52 = (v, a, b, c, d) ->
        v == a && v == b && v != c && v != d ||
        v == a && v != b && v == c && v != d ||
        v == a && v != b && v != c && v == d ||
        v != a && v == b && v == c && v != d ||
        v != a && v == b && v != c && v == d ||
        v != a && v != b && v == c && v == d;
        
    static Predicate4 C42 = (v, a, b, c) ->
        v == a && v == b && v != c ||
        v == a && v != b && v == c ||
        v != a && v == b && v == c;
        
    static Predicate3 C32 = (v, a, b) ->
        v == a && v == b;
    
    static Predicate5 C51 = (v, a, b, c, d) ->
        v == a && v != b && v != c && v != d ||
        v != a && v == b && v != c && v != d ||
        v != a && v != b && v == c && v != d ||
        v != a && v != b && v != c && v == d;
        
    static Predicate4 C41 = (v, a, b, c) ->
        v == a && v != b && v != c ||
        v != a && v == b && v != c ||
        v != a && v != b && v == c;
        
    static Predicate3 C31 = (v, a, b) ->
        v == a && v != b ||
        v != a && v == b;

    static Variable[] neighbors(Variable[][] variables, int r, int c) {
        int rows = variables.length;
        int cols = variables[0].length;
        List<Variable> result = new ArrayList<>();
        new Object() {
            void addNeighbors(int r, int c) {
                if (r >= 0 && c >= 0 && r < rows && c < cols)
                    result.add(variables[r][c]);
            }

            void run() {
                addNeighbors(r, c);
                addNeighbors(r - 1, c);
                addNeighbors(r + 1, c);
                addNeighbors(r, c - 1);
                addNeighbors(r, c + 1);
            }
        }.run();
        return result.toArray(Variable[]::new);
    }

    static void defineConstraint(Problem problem, Variable[][] variables) {
        int rows = variables.length;
        int cols = variables[0].length;
        for (int r = 0; r < rows; ++r)
            for (int c = 0; c < cols; ++c) {
                Variable[] n = neighbors(variables, r, c);
                Variable center = n[0];
                boolean p = center.domain.size() == 1;
                switch (n.length) {
                case 5: problem.constraint(p ? C51 : C52, n[0], n[1], n[2], n[3], n[4]); break;
                case 4: problem.constraint(p ? C41 : C42, n[0], n[1], n[2], n[3]); break;
                case 3: problem.constraint(p ? C31 : C32, n[0], n[1], n[2]); break;
                }
            }
    }
    
    static class IntVariable {
        final int n; final Variable v;
        IntVariable(int n, Variable v) { this.n = n; this.v = v; }
    }

    static List<Variable> defineBindingOrder(Problem problem, Variable[][] variables) {
        Set<Variable> bindingOrder = new LinkedHashSet<>();
        int rows = variables.length;
        int cols = variables[0].length;
        for (Variable v : problem.variables)
            if (v.domain.size() == 1)
                bindingOrder.add(v);
        List<IntVariable> intVars = new ArrayList<>();
        new Object() {
            int count(int r, int c) {
                if (r >= 0 && c >= 0 && r < rows && c < cols)
                    return variables[r][c].domain.size() == 1 ? 1 : 0;
                else
                    return 0;
            }

            void gather() {
                for (int r = 0; r < rows; ++r)
                    for (int c = 0; c < cols; ++c)
                        intVars.add(new IntVariable(
                            count(r - 1, c) + count(r + 1, c)
                            + count(r, c - 1) + count(r, c + 1),
                            variables[r][c]));
            }
        }.gather();
        intVars.stream()
            .sorted(Comparator.comparing(x -> -x.n))
            .forEach(x -> bindingOrder.add(x.v));
        return new ArrayList<>(bindingOrder);
    }
    
    static void answer(Variable[][] variables, Map<Variable, Integer> m) {
        logger.info("answer:");
        int rows = variables.length;
        int cols = variables[0].length;
        for (int r = 0; r < rows; ++r) {
            StringBuilder sb = new StringBuilder();
            for (int c = 0; c < cols; ++c)
                sb.append(" ").append(m.get(variables[r][c]));
            logger.info(sb.toString());
        }
    }

    static void solveNumberLink(int[][] board) {
        Problem problem = new Problem();
        Domain domain = defineDomain(board);
        Variable[][] variables = defineVariables(problem, board, domain);
        defineConstraint(problem, variables);
        List<Variable> bindingOrder = defineBindingOrder(problem, variables);
        logger.info("binding order: " + bindingOrder);
        Solver solver = new Solver();
        solver.solve(problem, bindingOrder, m -> answer(variables, m));
        logger.info("binding count: " + Arrays.toString(solver.bindCount));
    }
    
    @Test
    public void test() {
        int[][] board = {
            {0, 0, 0, 0, 3, 2, 1},
            {0, 0, 0, 0, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 2, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 3, 5, 0, 0, 4, 0},
            {4, 0, 0, 0, 0, 0, 5},
        };
        solveNumberLink(board);
    }

}
