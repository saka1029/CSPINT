package jp.saka1029.cspint.sequential;

import java.util.Arrays;

public class Solver {

    public static Constraint[][] constraints(Problem problem) {
        int variableSize = problem.variables.size();
        Constraint[][] result = new Constraint[variableSize][];
        for (Constraint c : problem.constraints) {
            int max = -1;
            for (Variable v : c.variables) {
                Variable found = null;
                for (int i = 0; i < variableSize; ++i) {
                    if (problem.variables.get(i) == v) {
                        found = v;
                        max = i;
                        break;
                    }
                }
                if (found == null)
                    throw new IllegalStateException("variable not found: " + v);
            }
            if (max == -1) continue;
            Constraint[] r = result[max];
            r = r == null ? new Constraint[1] : Arrays.copyOf(r, r.length + 1);
            r[r.length - 1] = c;
            result[max] = r;
        }


        return result;
    }
}
