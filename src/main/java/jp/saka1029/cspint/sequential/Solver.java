package jp.saka1029.cspint.sequential;

import java.util.List;

public interface Solver {

    int solve(Problem problem, Answer answer);

    int solve(Problem problem, List<Variable> bindingOrder, Answer answer);

}
