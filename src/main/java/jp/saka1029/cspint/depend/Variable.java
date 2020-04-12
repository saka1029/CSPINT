package jp.saka1029.cspint.depend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Variable {

    private final String name;
    public String name() { return name; }

    private final List<Dependent> _dependents = new ArrayList<>();
    private final List<Dependent> dependents = Collections.unmodifiableList(_dependents);
    public List<Dependent> dependents() { return dependents; }
    void add(Dependent dependent) { _dependents.add(dependent); }

    Variable(String name) {
        Objects.requireNonNull(name, "name");
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
