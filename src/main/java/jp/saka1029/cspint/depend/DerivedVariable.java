package jp.saka1029.cspint.depend;

import java.util.List;
import java.util.Objects;

public class DerivedVariable extends Variable implements Dependent {

    private final Derivation derivation;
    public Derivation derivation() { return derivation; }

    private final List<Variable> variables;
    @Override public List<Variable> variables() { return variables; }

    DerivedVariable(String name, Derivation derivation, Variable... variables) {
        super(name);
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(derivation, "derivation");
        Objects.requireNonNull(variables, "variables");
        if (variables.length <= 0)
            throw new IllegalArgumentException("variables");
        this.derivation = derivation;
        this.variables = List.of(variables);
        for (Variable v : variables)
            v.add(this);
    }

}