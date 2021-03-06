package jp.saka1029.cspint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Constraint {

    public final Predicate0 predicate;
    private final List<Variable> _variables = new ArrayList<>();
    public final List<Variable> variables = Collections.unmodifiableList(_variables);

    Constraint(Predicate0 predicate, Variable... variables) {
        this.predicate = predicate;
        for (Variable v : variables) {
            this._variables.add(v);
            v.add(this);
        }
    }
    
    public boolean isAllDifferent() {
        return predicate == Problem.DIFFERENT;
    }

    @Override
    public String toString() {
        return "制約" + _variables;
    }

}
