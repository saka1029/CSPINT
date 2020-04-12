package jp.saka1029.cspint.depend;

import java.util.Map;

@FunctionalInterface
public interface Answer {
    
    void answer(Map<Variable, Integer> answer);

}
