package jp.saka1029.cspint.problem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Constraint {

    private final List<Variable> _variables = new ArrayList<>();
    public final List<Variable> variables = Collections.unmodifiableList(_variables);

    protected Constraint(Variable... variables) {
        for (Variable v : variables) {
            this._variables.add(v);
            v.constraint(this);
        }
    }

    @Override
    public String toString() {
        return "constraint for " + _variables;
    }

}
