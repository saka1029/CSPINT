package jp.saka1029.cspint.sequential;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class ParallelSolver implements Solver {

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

    @Override
    public int solve(Problem problem, List<Variable> bindingOrder, Answer answer) {
        int size = bindingOrder.size();
        List<List<Constraint>> constraintOrder = constraintOrder(problem, bindingOrder);
        AtomicInteger count = new AtomicInteger();
//        int[] count = {0};
        class CoreSolver extends Thread {

            final boolean isMainThread;
            final int index;
            final Map<Variable, Integer> result;
            final int[] parameters;

            CoreSolver(boolean isMainThread, int index, Map<Variable, Integer> result) {
                this.isMainThread = isMainThread;
                this.index = index;
                this.result = result;
                this.parameters = new int[size];
            }

            CoreSolver() {
                this(true, 0, new LinkedHashMap<>());
            }

            CoreSolver(int index, Map<Variable, Integer> result) {
                this(false, index, new LinkedHashMap<>(result));
            }

            boolean test(int i) {
                for (Constraint c : constraintOrder.get(i)) {
                    int p = 0;
                    for (Variable v : c.variables)
                        parameters[p++] = result.get(v);
                    if (!c.predicate.test(parameters))
                        return false;
                }
                return true;
            }

            void join(List<Thread> threads) {
                try {
                    for (Thread t : threads)
                        t.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            void bindParallel(int i) {
//                logger.info(Thread.currentThread().getName() + ":parallel@" + i);
                List<Thread> threads = new ArrayList<>();
                Variable v = bindingOrder.get(i);
                Domain d = v.domain;
                logger.info("bindParallel i=" + i + " サブスレッド数=" + d.size());
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

            void bindSequential(int i) {
//                logger.info(Thread.currentThread().getName() + ":sequential@" + i);
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
                if (i >= size) {
                    count.incrementAndGet();
//                    synchronized (ParallelSolver.this) {
//                        ++count[0];
//                    }
                    answer.answer(result);
                } else if (isMainThread && bindingOrder.get(i).domain.size() > 1)
                    bindParallel(i);
                else
                    bindSequential(i);
            }

            @Override
            public void run() {
                solve(index);
            }
        }
        new CoreSolver().run();
        return count.get();
//        return count[0];
    }

    @Override
    public int solve(Problem problem, Answer answer) {
        return solve(problem, problem.variables, answer);
    }

}
