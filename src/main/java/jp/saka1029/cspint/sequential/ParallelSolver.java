package jp.saka1029.cspint.sequential;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class ParallelSolver {

    static Logger logger = Logger.getLogger(ParallelSolver.class.getName());


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
        return result;
    }

    public void solve(Problem problem, Answer answer) {
        List<Variable> bindingOrder = problem.variables;
        int size = bindingOrder.size();
        List<List<Constraint>> constraintOrder = constraintOrder(problem, bindingOrder);
        class CoreSolver implements Runnable {

            Map<Variable, Integer> result;

            boolean test(Constraint c) {
                return c.predicate.test(c.variables.stream()
                    .mapToInt(v -> result.get(v))
                    .toArray());
            }

            boolean test(int i) {
                return constraintOrder.get(i).stream()
                    .allMatch(c -> test(c));
            }

            void solve(int i, boolean parallel) {
                if (i >= size) {
                    answer.answer(result);
                    return;
                }
                Variable v = bindingOrder.get(i);
                IntStream valueStream = v.domain.stream();
                if (parallel) valueStream = valueStream.parallel();
                valueStream
                    .forEach(value -> {
                        result.put(v, value);
                        if (test(i))
                            solve(i + 1, parallel);
                    });
            }

            @Override
            public void run() {
            }
        }
        new CoreSolver().solve(0, false);
    }

}
