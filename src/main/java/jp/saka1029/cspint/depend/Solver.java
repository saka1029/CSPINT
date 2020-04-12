package jp.saka1029.cspint.depend;

import java.util.LinkedHashMap;
import java.util.Map;

public class Solver {
    
    void solve(Problem p, Answer receiver) {
        BaseVariable[] baseVariables = p.variables().stream()
            .filter(v -> v instanceof BaseVariable)
            .toArray(BaseVariable[]::new);
        int baseVariableSize = baseVariables.length;
        int[] binding = new int[baseVariableSize];
        Map<Variable, Integer> map = new LinkedHashMap<>();
        new Object() {
            
            void solver(int i) {
                if (i >= baseVariableSize)
                    receiver.answer(map);
                else {
                    BaseVariable v = baseVariables[i];
                    for (int j = 0, size = v.domain().size(); j < size; ++j) {
                        
                    }
                }
            }

        }.solver(0);
    }

}
