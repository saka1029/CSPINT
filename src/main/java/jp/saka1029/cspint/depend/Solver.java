package jp.saka1029.cspint.depend;

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

    static List<Variable> variableOrder(Collection<BaseVariable> bindingOrder, List<Dependent> dependents) {
        Set<Variable> variables = new LinkedHashSet<>();
        for (BaseVariable b : bindingOrder) {
            variables.add(b);
            for (Dependent d : dependents) {
                if (!(d instanceof DerivedVariable)) continue;
                DerivedVariable v = (DerivedVariable)d;
                if (!variables.contains(v) && variables.containsAll(v.variables()))
                    variables.add(v);
            }
        }
        return new ArrayList<>(variables);
    }

    static List<List<Constraint>> constraintOrder(List<Variable> variableOrder, List<Dependent> dependents) {
        Set<Variable> variableSet = new LinkedHashSet<>();
        Set<Constraint> constraintSet = new HashSet<>();
        List<List<Constraint>> constraintOrder = new ArrayList<>(variableOrder.size());
        for (Variable v : variableOrder) {
            variableSet.add(v);
            List<Constraint> list = new ArrayList<>();
            constraintOrder.add(list);
            for (Dependent d : dependents) {
                if (!(d instanceof Constraint)) continue;
                Constraint c = (Constraint)d;
                if (!constraintSet.contains(c) && variableSet.containsAll(c.variables())) {
                    list.add(c);
                    constraintSet.add(c);
                }
            }
        }
        return constraintOrder;
    }

    static String string(int[] array, int n) {
        StringBuilder sb = new StringBuilder("[");
        if (n > 0)
            sb.append(array[0]);
        for (int i = 1; i < n; ++i)
            sb.append(", ").append(array[i]);
        sb.append("]");
        return sb.toString();
    }

    public int[] bindCount;

    public int solve(Problem problem, List<BaseVariable> baseOrder, Answer receiver) {
        int variableSize = problem.variables().size();
        Map<Variable, Integer> bind = new LinkedHashMap<>();
        Map<Variable, Integer> lockedBind = Collections.unmodifiableMap(bind);
        int[] args = new int[variableSize];
        List<Variable> variableOrder = variableOrder(baseOrder, problem.dependents());
        List<List<Constraint>> constraintOrder = constraintOrder(variableOrder, problem.dependents());
        bindCount = new int[variableSize];
        int[] count = {0};
        new Object() {

            void makeArgs(List<Variable> variables) {
                int i = 0;
                for (Variable v : variables)
                    args[i++] = bind.get(v);
            }

            boolean test(Constraint d) {
                makeArgs(d.variables());
                boolean t = d.predicate().test(args);
                logger.fine("test:" + d + ":" + string(args, d.variables().size()) + ":" + t);
                return t;
            }

            int apply(DerivedVariable v) {
                makeArgs(v.variables());
                int r = v.derivation().apply(args);
                logger.fine("apply:" + v + ":" + string(args, v.variables().size()) + ":" + r);
                return r;
            }

            void solve(int i) {
                if (i > 0) ++bindCount[i - 1];
                if (i >= variableSize) {
                    ++count[0];
                    receiver.answer(lockedBind);
                } else {
                    Variable v = variableOrder.get(i);
                    if (v instanceof BaseVariable) {
                        BaseVariable b = (BaseVariable)v;
                        Domain domain = b.domain();
                        L: for (int j = 0, size = domain.size(); j < size; ++j) {
                            bind.put(v, domain.get(j));
                            for (Constraint d : constraintOrder.get(i))
                                if (!test(d)) continue L;
                            solve(i + 1);
                        }
                    } else /* if (v instanceof DerivedVariable) */ {
                        bind.put(v, apply((DerivedVariable)v));
                        for (Constraint d : constraintOrder.get(i))
                            if (!test(d)) return;
                        solve(i + 1);
                    }
                }
            }
        }.solve(0);
        return count[0];
    }

    public int solve(Problem problem, Answer receiver) {
        return solve(problem, problem.baseVariables(), receiver);
    }

}
