package jp.saka1029.cspint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class Solver {

    static Logger logger = Logger.getLogger(Solver.class.getName());

    public static void printConstraintOrder(Problem problem, List<Variable> bindingOrder) {
    	List<List<Constraint>> constraints = constraintOrder(problem, bindingOrder);
    	for (int i = 0, size = bindingOrder.size(); i < size; ++i)
    		logger.info(String.format("%4d %s : %s", i, bindingOrder.get(i), constraints.get(i)));
    }

    public static void printConstraintOrder(Problem problem) {
    	printConstraintOrder(problem, problem.variables);
    }

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
        SearchControl control = new SearchControl();
        int[] testArgs = new int[variableSize];
        int[] count = {0};
        new Object() {

            boolean test(Constraint constraint) {
                int i = 0;
                for (Variable v : constraint.variables)
                    testArgs[i++] = result.get(v);
                return constraint.predicate.test(testArgs);
            }

            boolean test(int i) {
                for (Constraint c : constraints.get(i))
                    if (!test(c))
                        return false;
                return true;
            }

            void solve(int i) {
                if (control.isStopped()) return;
                if (i > 0) ++bindCount[i - 1];
                if (i >= variableSize) {
                	++count[0];
                    answer.answer(control, protectedResult);
                    return;
                }
                Variable variable = bindingOrder.get(i);
                Domain domain = variable.domain;
                for (int j = 0, size = domain.size(); j < size; ++j) {
                    if (control.isStopped()) break;
                    result.put(variable, domain.get(j));
                        if (test(i))
                            solve(i + 1);
                }
            }
        }.solve(0);
        return count[0];
    }

    public int solve(Problem problem, Answer answer) {
        return solve(problem, problem.variables, answer);
    }

    public int solveParallel(Problem problem, List<Variable> bindingOrder, Answer answer) {
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

    public int solveParallel(Problem problem, Answer answer) {
        return solve(problem, problem.variables, answer);
    }

    public Map<Variable, Integer> maximize(Problem problem, List<Variable> bindingOrder,
            Function0 maximize, Variable... variables) {
        int variableSize = problem.variables.size();
        if (bindingOrder.size() != variableSize)
            throw new IllegalArgumentException("invalid bindingOrder size");
        bindCount = new int[variableSize];
        Map<Variable, Integer> result = new LinkedHashMap<>();
        List<List<Constraint>> constraints = constraintOrder(problem, bindingOrder);
        SearchControl control = new SearchControl();
        int[] maxValue = {Integer.MIN_VALUE};
        Map<Variable, Integer> maxResult = new LinkedHashMap<>();
        int[] testArgs = new int[variableSize];
        int[] count = {0};
        new Object() {

            boolean test(Constraint constraint) {
                int i = 0;
                for (Variable v : constraint.variables)
                    testArgs[i++] = result.get(v);
                return constraint.predicate.test(testArgs);
            }

            boolean test(int i) {
                for (Constraint c : constraints.get(i))
                    if (!test(c))
                        return false;
                return true;
            }

            void testMax() {
                int i = 0;
                for (Variable v : variables)
                    testArgs[i++] = result.get(v);
                int value = maximize.apply(testArgs);
                if (value > maxValue[0]) {
                    maxValue[0] = value;
                    maxResult.putAll(result);
                }
            }

            void solve(int i) {
                if (control.isStopped()) return;
                if (i > 0) ++bindCount[i - 1];
                if (i >= variableSize) {
                    testMax();
                	++count[0];
                    return;
                }
                Variable variable = bindingOrder.get(i);
                Domain domain = variable.domain;
                for (int j = 0, size = domain.size(); j < size; ++j) {
                    if (control.isStopped()) break;
                    result.put(variable, domain.get(j));
                        if (test(i))
                            solve(i + 1);
                }
            }
        }.solve(0);
        return maxResult;
    }

    public Map<Variable, Integer> maximize(Problem problem, Function0 maximize, Variable... variables) {
        return maximize(problem, problem.variables, maximize, variables);
    }

}
