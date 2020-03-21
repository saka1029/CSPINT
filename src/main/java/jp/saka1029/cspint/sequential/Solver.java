package jp.saka1029.cspint.sequential;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Solver {

    static Logger logger = Logger.getLogger(Solver.class.getName());

    public static Constraint[][] constraintArrays(Problem problem) {
        int variableSize = problem.variables.size();
        Map<Variable, Integer> variableIndexes = new HashMap<>();
        int p = 0;
        for (Variable v : problem.variables)
            variableIndexes.put(v, p++);
        Constraint[][] result = new Constraint[variableSize][];
        for (Constraint c : problem.constraints) {
            int max = c.variables.stream()
                .mapToInt(v -> variableIndexes.get(v))
                .max().getAsInt();
            Constraint[] cs = result[max];
            result[max] = cs = cs == null ? new Constraint[1] : Arrays.copyOf(cs, cs.length + 1);
            result[max][cs.length - 1] = c;
        }
        for (int i = 0; i < variableSize; ++i)
            logger.info(problem.variables.get(i) + ":" + Arrays.toString(result[i]));
        return result;
    }

    public static List<List<Constraint>> constraintLists(Problem problem) {
        int variableSize = problem.variables.size();
        Map<Variable, Integer> variableIndexes = new HashMap<>();
        List<List<Constraint>> result = new ArrayList<>(variableSize);
        int p = 0;
        for (Variable v : problem.variables) {
            variableIndexes.put(v, p++);
            result.add(new ArrayList<>());
        }
        for (Constraint c : problem.constraints) {
            int max = c.variables.stream()
                .mapToInt(v -> variableIndexes.get(v))
                .max().getAsInt();
            result.get(max).add(c);
        }
        for (int i = 0; i < variableSize; ++i)
            logger.info(problem.variables.get(i) + ":" + result.get(i));
        return result;
    }

    public void solve(Problem problem, Answer answer) {
        int variableSize = problem.variables.size();
        Map<Variable, Integer> result = new LinkedHashMap<>();
        List<List<Constraint>> constraints = constraintLists(problem);
        int[] testArgs = new int[variableSize];
        new Object() {

            boolean test(Constraint constraint) {
                int i = 0;
                for (Variable v : constraint.variables)
                    testArgs[i++] = result.get(v);
                return constraint.predicate.test(testArgs);
            }

            void solve(int i) {
                if (i >= variableSize) {
                    answer.answer(result);
                    return;
                }
                Variable variable = problem.variables.get(i);
                Domain domain = variable.domain;
                L: for (int j = 0, size = domain.size(); j < size; ++j) {
                    int value = domain.get(j);
                    result.put(variable, value);
                    for (Constraint constraint : constraints.get(i))
                        if (!test(constraint))
                            continue L;
                    solve(i + 1);
                }
            }
        }.solve(0);
    }
}
