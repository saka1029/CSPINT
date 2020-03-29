package jp.saka1029.cspint.sequential;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Problem {

    static Logger logger = Logger.getLogger(Problem.class.getName());

    private final Map<String, Variable> variableNames = new HashMap<>();
    private final List<Constraint> _constraints = new ArrayList<>();
    public final List<Constraint> constraints = Collections.unmodifiableList(_constraints);
    private final List<Variable> _variables = new ArrayList<>();
    public final List<Variable> variables = Collections.unmodifiableList(_variables);

    public Variable variable(String name, Domain domain) {
        if (name == null)
            throw new NullPointerException("name");
        if (variableNames.containsKey(name))
            throw new IllegalArgumentException("duplicated variable name: " + name);
        Variable v = new Variable(name, domain);
        variableNames.put(name, v);
        _variables.add(v);
        return v;
    }

    public Variable variable(String name) {
        return variableNames.get(name);
    }

    public Constraint constraint(Predicate0 predicate, Variable... variables) {
        Constraint c = new Constraint(predicate, variables);
        _constraints.add(c);
        for (Variable v : variables)
            v.add(c);
        return c;
    }

    public Constraint constraint(Predicate1 predicate, Variable v0) {
        return constraint((Predicate0)predicate, v0);
    }

    public Constraint constraint(Predicate2 predicate, Variable v0, Variable v1) {
        return constraint((Predicate0)predicate, v0, v1);
    }

    public Constraint constraint(Predicate3 predicate, Variable v0, Variable v1, Variable v2) {
        return constraint((Predicate0)predicate, v0, v1, v2);
    }

    public Constraint constraint(Predicate4 predicate, Variable v0, Variable v1, Variable v2,
        Variable v3) {
        return constraint((Predicate0)predicate, v0, v1, v2, v3);
    }

    public Constraint constraint(Predicate5 predicate, Variable v0, Variable v1, Variable v2,
        Variable v3, Variable v4) {
        return constraint((Predicate0)predicate, v0, v1, v2, v3, v4);
    }

    public Constraint constraint(Predicate6 predicate, Variable v0, Variable v1, Variable v2,
        Variable v3, Variable v4, Variable v5) {
        return constraint((Predicate0)predicate, v0, v1, v2, v3, v4, v5);
    }

    public Constraint constraint(Predicate7 predicate, Variable v0, Variable v1, Variable v2,
        Variable v3, Variable v4, Variable v5, Variable v6) {
        return constraint((Predicate0)predicate, v0, v1, v2, v3, v4, v5, v6);
    }

    public Constraint constraint(Predicate8 predicate, Variable v0, Variable v1, Variable v2,
        Variable v3, Variable v4, Variable v5, Variable v6, Variable v7) {
        return constraint((Predicate0)predicate, v0, v1, v2, v3, v4, v5, v6, v7);
    }

    public Constraint constraint(Predicate9 predicate, Variable v0, Variable v1, Variable v2,
        Variable v3, Variable v4, Variable v5, Variable v6, Variable v7, Variable v8) {
        return constraint((Predicate0)predicate, v0, v1, v2, v3, v4, v5, v6, v7, v8);
    }

    public Constraint constraint(Predicate10 predicate, Variable v0, Variable v1, Variable v2,
        Variable v3, Variable v4, Variable v5, Variable v6, Variable v7, Variable v8, Variable v9) {
        return constraint((Predicate0)predicate, v0, v1, v2, v3, v4, v5, v6, v7, v8, v9);
    }
    
    static class PredicateD2Support implements Predicate0 {

    	final PredicateD2 predicate;
    	final int rows, cols;

    	PredicateD2Support(PredicateD2 predicate, int rows, int cols) {
    		this.predicate = predicate;
    		this.rows = rows;
    		this.cols = cols;
    	}

		@Override
		public boolean test(int... args) {
			int[][] matrix = new int[rows][cols];
			for (int r = 0, s = 0; r < rows; ++r, s += cols)
                System.arraycopy(args, s, matrix[r], 0, cols);
			return predicate.test(matrix);
		}
    }

    public static Variable[] flat(Variable[][] variables) {
    	return Arrays.stream(variables).flatMap(a -> Arrays.stream(a)).toArray(Variable[]::new);
    }

    public Constraint constraint(PredicateD2 predicate, Variable[][] variables) {
    	return constraint(new PredicateD2Support(predicate, variables.length, variables[0].length), flat(variables));
    }

    public void allDifferent(Variable... variables) {
        int size = variables.length;
        for (int i = 0; i < size; ++i)
            for (int j = i + 1; j < size; ++j)
                constraint((x, y) -> x != y, variables[i], variables[j]);
    }

    public void allDifferent(Variable[][] variables) {
    	allDifferent(flat(variables));
    }
    
    public void allDifferentRows(Variable[][] variables) {
    	for (Variable[] row : variables)
    		allDifferent(row);
    }
    
    public void allDifferentCols(Variable[][] variables) {
    	int rowSize = variables.length;
    	int colSize = variables[0].length;
    	Variable[] cols = new Variable[rowSize];
    	for (int c = 0; c < colSize; ++c) {
            for (int r = 0; r < rowSize; ++r)
            	cols[r] = variables[r][c];
            allDifferent(cols);
    	}
    }

}
