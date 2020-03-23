package jp.saka1029.cspint.sequential;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Solver {

    static Logger logger = Logger.getLogger(Solver.class.getName());

    public int[] bindCount;

    public static List<List<Constraint>> constraintLists(Problem problem, List<Variable> bindingOrder) {
        int variableSize = problem.variables.size();
        if (new HashSet<>(bindingOrder).size() != variableSize)
            throw new IllegalArgumentException("invalid bindingOrder size");
        Map<Variable, Integer> variableIndexes = new HashMap<>();
        List<List<Constraint>> result = new ArrayList<>(variableSize);
        int p = 0;
        for (Variable v : bindingOrder) {
            variableIndexes.put(v, p++);
            result.add(new ArrayList<>());
        }
        for (Constraint c : problem.constraints) {
            int max = c.variables.stream()
                .mapToInt(v -> variableIndexes.get(v))
                .max().getAsInt();
            result.get(max).add(c);
        }
//        for (int i = 0; i < variableSize; ++i)
//            logger.info(bindingOrder.get(i) + ":" + result.get(i));
        return result;
    }

    public void solve(Problem problem, List<Variable> bindingOrder, Answer answer) {
        int variableSize = problem.variables.size();
        if (new HashSet<>(bindingOrder).size() != variableSize)
            throw new IllegalArgumentException("invalid bindingOrder size");
        bindCount = new int[variableSize];
        Map<Variable, Integer> result = new LinkedHashMap<>();
        List<List<Constraint>> constraints = constraintLists(problem, bindingOrder);
        int[] testArgs = new int[variableSize];
        new Object() {

            boolean test(Constraint constraint) {
                int i = 0;
                for (Variable v : constraint.variables)
                    testArgs[i++] = result.get(v);
                return constraint.predicate.test(testArgs);
            }

            void solve(int i) {
                if (i > 0) ++bindCount[i - 1];
                if (i >= variableSize) {
                    answer.answer(result);
                    return;
                }
                Variable variable = bindingOrder.get(i);
                Domain domain = variable.domain;
                L: for (int j = 0, size = domain.size(); j < size; ++j) {
                    result.put(variable, domain.get(j));
                    for (Constraint constraint : constraints.get(i))
                        if (!test(constraint))
                            continue L;
                    solve(i + 1);
                }
            }
        }.solve(0);
    }

    public void solve(Problem problem, Answer answer) {
        solve(problem, problem.variables, answer);
    }
}
