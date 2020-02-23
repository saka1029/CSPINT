package jp.saka1029.cspint.problem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Variable {

    public final int id;
    public final String name;
    public final Domain domain;
    private final List<Constraint> _constraints = new ArrayList<>();
    public final List<Constraint> constraints = Collections.unmodifiableList(_constraints);

    Variable(int id, String name, Domain domain) {
        this.id = id;
        this.name = name;
        this.domain = domain;
    }

    void constraint(Constraint c) {
        this._constraints.add(c);
    }

    @Override
    public String toString() {
        return String.format("variable(%s %s:%s)", id, name, domain);
    }

}
