package jp.saka1029.cspint.depend;

import java.util.List;
import java.util.Objects;

public class Constraint implements Dependent {

    private final Predicate predicate;
    public Predicate predicate() { return predicate; }

    private final List<Variable> variables;
    @Override public List<Variable> variables() { return variables; }

    Constraint(Predicate predicate, Variable... variables) {
        Objects.requireNonNull(predicate, "predicate");
        Objects.requireNonNull(variables, "variables");
        if (variables.length < 0)
            throw new IllegalArgumentException("variables");
        this.predicate = predicate;
        this.variables = List.of(variables);
        for (Variable v : variables)
            v.add(this);
    }

    @Override
    public String toString() {
        return "制約" + variables;
    }

}
