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

    public Variable variable(String name) {
        Objects.requireNonNull(name, "name");
        return variableMap.get(name);
    }

    public Constraint constraint(Predicate predicate, Variable... variables) {
        Constraint result = new Constraint(predicate, variables);
        _dependents.add(result);
        return result;
    }

    @Override
    public String toString() {
        return "問題(変数=" + _variables + ", 導出=" + _dependents + ")";
    }

}
