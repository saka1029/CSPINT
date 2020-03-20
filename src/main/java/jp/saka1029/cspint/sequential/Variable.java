package jp.saka1029.cspint.sequential;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Variable {

    public final String name;
    public final Domain domain;
    private final List<Constraint> _constraints = new ArrayList<>();
    public final List<Constraint> constraints = Collections.unmodifiableList(_constraints);

    Variable(String name, Domain domain) {
        this.name = name;
        this.domain = domain;
    }

    void add(Constraint c) {
        this._constraints.add(c);
    }

    @Override
    public String toString() {
        return String.format("%s:%s", name, domain);
    }

}
