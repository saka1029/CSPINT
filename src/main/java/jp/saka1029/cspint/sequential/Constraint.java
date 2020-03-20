package jp.saka1029.cspint.sequential;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Constraint {

    public final Predicate predicate;
    private final List<Variable> _variables = new ArrayList<>();
    public final List<Variable> variables = Collections.unmodifiableList(_variables);

    Constraint(Predicate predicate, Variable... variables) {
        this.predicate = predicate;
        for (Variable v : variables) {
            this._variables.add(v);
            v.add(this);
        }
    }

//    public static Constraint of(Predicate predicate, Variable... variables) {
//        return new Constraint(predicate, variables);
//    }

    @Override
    public String toString() {
        return "constraint for " + _variables;
    }

}
