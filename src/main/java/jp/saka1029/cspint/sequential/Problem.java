package jp.saka1029.cspint.sequential;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Problem {

    private final Set<String> variableNames = new HashSet<>();
    private final List<Constraint> _constraints = new ArrayList<>();
    public final List<Constraint> constraints = Collections.unmodifiableList(_constraints);
    private final List<Variable> _variables = new ArrayList<>();
    public final List<Variable> variables = Collections.unmodifiableList(_variables);

    public Variable variable(String name, Domain domain) {
        if (variableNames.contains(name))
            throw new IllegalArgumentException("duplicated variable name: " + name);
        variableNames.add(name);
        Variable v = new Variable(name, domain);
        _variables.add(v);
        return v;
    }

    public Constraint constraint(Predicate predicate, Variable... variables) {
        Constraint c = new Constraint(predicate, variables);
        _constraints.add(c);
        for (Variable v : variables)
            v.add(c);
        return c;
    }

}
