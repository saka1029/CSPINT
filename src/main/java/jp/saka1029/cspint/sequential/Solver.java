package jp.saka1029.cspint.sequential;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class Solver {

    static Logger logger = Logger.getLogger(Solver.class.getName());

    public int[] bindCount;

    /**
     * (例)
     * problem.variables = {A, B, C}
     * problem.constraints = {制約[A, B], 制約[B, C], 制約[A, C]}
     * bindingOrder = {C, B, A}
     * とすると
     * このメソッドは制約順序として以下のリストを返します。
     * {{}, {制約[B, C]}, {制約[A, B], 制約[A, C]}}
     * ３つのリストはそれぞれbindigOrder内の変数C, B, Aに対応します。
     * @param problem
     * @param bindingOrder 変数を束縛する順序をリストで指定します。
     *        problemで定義されたすべての変数を1回ずつ含んでいる必要があります。
     * @return
     */
    public static List<List<Constraint>> constraintOrder(Problem problem, Collection<Variable> bindingOrder) {
        int variableSize = problem.variables.size();
        if (bindingOrder.size() != variableSize)
            throw new IllegalArgumentException("invalid bindingOrder size");
        List<List<Constraint>> result = new ArrayList<>(variableSize);
        Set<Variable> set = new HashSet<>();
        Set<Constraint> done = new HashSet<>();
        for (Variable v : bindingOrder) {
            set.add(v);
            List<Constraint> list = new ArrayList<>();
            result.add(list);
            for (Constraint c : problem.constraints)
                if (!done.contains(c) && set.containsAll(c.variables)) {
                    list.add(c);
                    done.add(c);
                }
        }
//        for (int i = 0; i < variableSize; ++i)
//            logger.info(bindingOrder.get(i) + ":" + result.get(i));
        return result;
    }

    public static List<Variable> bindingOrder(Collection<Constraint> constraintOrder) {
        Set<Variable> set = new LinkedHashSet<>();
        for (Constraint c : constraintOrder)
            set.addAll(c.variables);
        return new ArrayList<>(set);
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
                variable.domain.stream()
                    .forEach(value -> {
                        result.put(variable, value);
                        if (constraints.get(i).stream().allMatch(c -> test(c)))
                            solve(i + 1);
                    });
//                Domain domain = variable.domain;
//                for (int j = 0, size = domain.size(); j < size; ++j) {
//                    result.put(variable, domain.get(j));
//                    if (constraints.get(i).stream().allMatch(c -> test(c)))
//                        solve(i + 1);
//                }
            }
        }.solve(0);
        return count[0];
    }

    public int solve(Problem problem, Answer answer) {
        return solve(problem, problem.variables, answer);
    }

    public static void printConstraintOrder(Problem problem, List<Variable> bindingOrder) {
    	List<List<Constraint>> constraints = constraintOrder(problem, bindingOrder);
    	for (int i = 0, size = bindingOrder.size(); i < size; ++i)
    		logger.info(String.format("%4d %s : %s", i, bindingOrder.get(i), constraints.get(i)));
    }

    public static void printConstraintOrder(Problem problem) {
    	printConstraintOrder(problem, problem.variables);
    }
}
