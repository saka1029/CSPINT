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
        int constraintSize = problem.constraints.size();
        if (bindingOrder.size() != variableSize)
            throw new IllegalArgumentException("invalid bindingOrder size");
        List<List<Constraint>> result = new ArrayList<>(variableSize);
        Set<Variable> set = new HashSet<>(variableSize);
        Set<Constraint> done = new HashSet<>(constraintSize);
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
        int variableSize = bindingOrder.size();
        List<List<Constraint>> constraintOrder = constraintOrder(problem, bindingOrder);
        AtomicInteger count = new AtomicInteger();
        SearchControl control = new SearchControl();
        class CoreSolver extends Thread {

            final boolean isMainThread;
            final int index;
            final Map<Variable, Integer> result;
            final int[] parameters;

            CoreSolver(boolean isMainThread, int index, Map<Variable, Integer> result) {
                this.isMainThread = isMainThread;
                this.index = index;
                this.result = result;
                this.parameters = new int[variableSize];
            }

            // for main thread
            CoreSolver() {
                this(true, 0, new LinkedHashMap<>(variableSize));
            }

            // for sub thread
            CoreSolver(int index, Map<Variable, Integer> result) {
                this(false, index, new LinkedHashMap<>(result));
            }

            boolean test(int i) {
                for (Constraint c : constraintOrder.get(i)) {
                    for (int p = 0, cmax = c.variables.size(); p < cmax; ++p)
                        parameters[p] = result.get(c.variables.get(p));
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
                Variable v = bindingOrder.get(i);
                Domain d = v.domain;
                int size = d.size();
                List<Thread> threads = new ArrayList<>(size);
                logger.info("bindParallel i=" + i + " サブスレッド数=" + size);
                for (int p = 0; p < size; ++p) {
                    if (control.isStopped()) break;
                    result.put(v, d.get(p));
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
                    if (control.isStopped()) break;
                    result.put(v, d.get(j));
                    if (test(i))
                        solve(i + 1);
                }
            }

            void solve(int i) {
                if (control.isStopped()) return;
                if (i >= variableSize) {
                    count.incrementAndGet();
                    answer.answer(control, result);
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
    }

    @Override
    public int solve(Problem problem, Answer answer) {
        return solve(problem, problem.variables, answer);
    }

}
