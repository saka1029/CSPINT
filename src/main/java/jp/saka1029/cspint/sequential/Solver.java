package jp.saka1029.cspint.sequential;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Solver {

    static Logger logger = Logger.getLogger(Solver.class.getName());

    public int[] bindCount;

    /**
     * (例)
     * problem.variables = {A, B, C}
     * problem.constraintOrder = {制約[A, B], 制約[B, C], 制約[A, C]}
     * bindingOrder = {C, B, A}
     * とすると
     * このメソッドは制約順序として以下のリストを返します。
     * {{}, {制約[B, C]}, {制約[A, B], 制約[A, C]}}
     * ３つのリストはそれぞれbindigOrder内の変数C, B, Aに対応します。
     * @param problem
     * @param bindingOrder
     * @return
     */
    public static List<List<Constraint>> constraintOrder(Problem problem, Collection<Variable> bindingOrder) {
        int variableSize = problem.variables.size();
        if (bindingOrder.size() != variableSize)
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

    public int solve(Problem problem, List<Variable> bindingOrder, Answer answer) {
        int variableSize = problem.variables.size();
        if (bindingOrder.size() != variableSize)
            throw new IllegalArgumentException("invalid bindingOrder size");
        bindCount = new int[variableSize];
        Map<Variable, Integer> result = new LinkedHashMap<>();
        Map<Variable, Integer> protectedResult = Collections.unmodifiableMap(result);
        List<List<Constraint>> constraints = constraintOrder(problem, bindingOrder);
        int[] testArgs = new int[variableSize];
        int[] count = {0};
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
                	++count[0];
                    answer.answer(protectedResult);
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
        return count[0];
    }

    public int solve(Problem problem, Answer answer) {
        return solve(problem, problem.variables, answer);
    }
}
