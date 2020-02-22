package jp.saka1029.cspint.problem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Problem {

    private final List<Variable> _variables = new ArrayList<>();
    public final List<Variable> variables = Collections.unmodifiableList(_variables);

    public Variable variable(String name, Domain domain) {
        Variable v = new Variable(_variables.size(), name, domain);
        _variables.add(v);
        return v;
    }

}
