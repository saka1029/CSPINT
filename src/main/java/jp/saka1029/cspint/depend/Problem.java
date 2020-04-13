package jp.saka1029.cspint.depend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Problem {

    private final List<Variable> _variables = new ArrayList<>();
    private final List<Variable> variables = Collections.unmodifiableList(_variables);
    public List<Variable> variables() { return variables; }

    private final List<BaseVariable> _baseVariables = new ArrayList<>();
    private final List<BaseVariable> baseVariables = Collections.unmodifiableList(_baseVariables);
    public List<BaseVariable> baseVariables() { return baseVariables; }

    private final List<Dependent> _dependents = new ArrayList<>();
    private final List<Dependent> dependents = Collections.unmodifiableList(_dependents);
    public List<Dependent> dependents() { return dependents; }

    private final Map<String, Variable> variableMap = new LinkedHashMap<>();

    public BaseVariable variable(String name, Domain domain) {
        if (variableMap.containsKey(name))
            throw new IllegalArgumentException("変数名(" + name + ")が重複しています");
        BaseVariable result = new BaseVariable(name, domain);
        _variables.add(result);
        _baseVariables.add(result);
        variableMap.put(name, result);
        return result;
    }

    public DerivedVariable variable(String name, Derivation derivation, Variable... variables) {
        if (variableMap.containsKey(name))
            throw new IllegalArgumentException("変数名(" + name + ")が重複しています");
        DerivedVariable result = new DerivedVariable(name, derivation, variables);
        _variables.add(result);
        _dependents.add(result);
        variableMap.put(name, result);
        return result;
    }

    public DerivedVariable variable(String name, Derivation1 derivation, Variable a) {
        return variable(name, (Derivation)derivation, a);
    }

    public DerivedVariable variable(String name, Derivation2 derivation, Variable a, Variable b) {
        return variable(name, (Derivation)derivation, a, b);
    }

    public DerivedVariable variable(String name, Derivation3 derivation, Variable a, Variable b, Variable c) {
        return variable(name, (Derivation)derivation, a, b, c);
    }

    public DerivedVariable variable(String name, Derivation4 derivation, Variable a, Variable b, Variable c,
        Variable d) {
        return variable(name, (Derivation)derivation, a, b, c, d);
    }

    public DerivedVariable variable(String name, Derivation5 derivation, Variable a, Variable b, Variable c,
        Variable d, Variable e) {
        return variable(name, (Derivation)derivation, a, b, c, d, e);
    }

    public DerivedVariable variable(String name, Derivation6 derivation, Variable a, Variable b, Variable c,
        Variable d, Variable e, Variable f) {
        return variable(name, (Derivation)derivation, a, b, c, d, e, f);
    }

    public DerivedVariable variable(String name, Derivation7 derivation, Variable a, Variable b, Variable c,
        Variable d, Variable e, Variable f, Variable g) {
        return variable(name, (Derivation)derivation, a, b, c, d, e, f, g);
    }

    public DerivedVariable variable(String name, Derivation8 derivation, Variable a, Variable b, Variable c,
        Variable d, Variable e, Variable f, Variable g, Variable h) {
        return variable(name, (Derivation)derivation, a, b, c, d, e, f, g, h);
    }

    public DerivedVariable variable(String name, Derivation9 derivation, Variable a, Variable b, Variable c,
        Variable d, Variable e, Variable f, Variable g, Variable h, Variable i) {
        return variable(name, (Derivation)derivation, a, b, c, d, e, f, g, h, i);
    }

    public DerivedVariable variable(String name, Derivation10 derivation, Variable a, Variable b, Variable c,
        Variable d, Variable e, Variable f, Variable g, Variable h, Variable i, Variable j) {
        return variable(name, (Derivation)derivation, a, b, c, d, e, f, g, h, i, j);
    }

    public Variable variable(String name) {
        Objects.requireNonNull(name, "name");
        return variableMap.get(name);
    }

    public Constraint constraint(Predicate predicate, Variable... variables) {
        Constraint result = new Constraint(predicate, variables);
        _dependents.add(result);
        return result;
    }

    public Constraint constraint(Predicate1 predicate, Variable a) {
        return constraint((Predicate)predicate, a);
    }

    public Constraint constraint(Predicate2 predicate, Variable a, Variable b) {
        return constraint((Predicate)predicate, a, b);
    }

    public Constraint constraint(Predicate3 predicate, Variable a, Variable b, Variable c) {
        return constraint((Predicate)predicate, a, b, c);
    }

    public Constraint constraint(Predicate4 predicate, Variable a, Variable b, Variable c, Variable d) {
        return constraint((Predicate)predicate, a, b, c, d);
    }

    public Constraint constraint(Predicate5 predicate, Variable a, Variable b, Variable c, Variable d, Variable e) {
        return constraint((Predicate)predicate, a, b, c, d, e);
    }

    public Constraint constraint(Predicate6 predicate, Variable a, Variable b, Variable c, Variable d, Variable e,
        Variable f) {
        return constraint((Predicate)predicate, a, b, c, d, e, f);
    }

    public Constraint constraint(Predicate7 predicate, Variable a, Variable b, Variable c, Variable d, Variable e,
        Variable f, Variable g) {
        return constraint((Predicate)predicate, a, b, c, d, e, f, g);
    }

    public Constraint constraint(Predicate8 predicate, Variable a, Variable b, Variable c, Variable d, Variable e,
        Variable f, Variable g, Variable h) {
        return constraint((Predicate)predicate, a, b, c, d, e, f, g, h);
    }

    public Constraint constraint(Predicate9 predicate, Variable a, Variable b, Variable c, Variable d, Variable e,
        Variable f, Variable g, Variable h, Variable i) {
        return constraint((Predicate)predicate, a, b, c, d, e, f, g, h, i);
    }

    public Constraint constraint(Predicate10 predicate, Variable a, Variable b, Variable c, Variable d, Variable e,
        Variable f, Variable g, Variable h, Variable i, Variable j) {
        return constraint((Predicate)predicate, a, b, c, d, e, f, g, h, i, j);
    }

    public static Predicate DIFFERENT = a -> a[0] != a[1];

    public void allDifferent(Variable... variables) {
        for (int i = 0, size = variables.length; i < size; ++i)
            for (int j = i + 1; j < size; ++j)
                constraint(DIFFERENT, variables[i], variables[j]);
    }

    @Override
    public String toString() {
        return "問題(変数=" + _variables + ", 導出=" + _dependents + ")";
    }

}
