package jp.saka1029.cspint.sequential;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

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

    public void solve(Problem problem, int parallelIndex, Answer answer) {
        List<Variable> bindingOrder = problem.variables;
        int size = bindingOrder.size();
        List<List<Constraint>> constraintOrder = constraintOrder(problem, bindingOrder);
        class CoreSolver extends Thread {

            final int index;
            final Map<Variable, Integer> result;

            CoreSolver(int index, Map<Variable, Integer> result) {
                this.index = index;
                this.result = new LinkedHashMap<>(result);
            }

            boolean test(Constraint c) {
                return c.predicate.test(c.variables.stream()
                    .mapToInt(v -> result.get(v))
                    .toArray());
            }

            boolean test(int i) {
                return constraintOrder.get(i).stream()
                    .allMatch(c -> test(c));
            }

//            void bind(int i) {
//                List<Thread> threads = null;
//                if (i == parallelIndex) threads = new ArrayList<>();
//                Variable v = bindingOrder.get(i);
//                Domain d = v.domain;
//                for (int j = 0, size = d.size(); j < size; ++j) {
//                    int value = d.get(j);
//                    result.put(v, value);
//                    if (test(i))
//                        if (i == parallelIndex) {
//                            Thread t = new CoreSolver(i + 1, result);
//                            threads.add(t);
//                            t.start();
//                        } else
//                            solve(i);
//                }
//                if (i == parallelIndex) join(threads);
//            }

            void join(List<Thread> threads) {
                try {
                    for (Thread t : threads)
                        t.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            void solveParallel(int i) {
                List<Thread> threads = new ArrayList<>();
                Variable v = bindingOrder.get(i);
                Domain d = v.domain;
                for (int j = 0, size = d.size(); j < size; ++j) {
                    int value = d.get(j);
                    result.put(v, value);
                    if (test(i)) {
                        Thread t = new CoreSolver(i + 1, result);
                        threads.add(t);
                        t.start();
                    }
                }
                join(threads);
            }

            void solveSequential(int i) {
                Variable v = bindingOrder.get(i);
                Domain d = v.domain;
                for (int j = 0, size = d.size(); j < size; ++j) {
                    int value = d.get(j);
                    result.put(v, value);
                    if (test(i))
                        solve(i + 1);
                }
            }

            void solve(int i) {
                if (i >= size)
                    answer.answer(result);
                else if (i == parallelIndex)
                    solveParallel(i);
                else
                    solveSequential(i);
            }

            @Override
            public void run() {
                solve(index);
            }
        }
        new CoreSolver(0, Map.of()).run();
    }

}
