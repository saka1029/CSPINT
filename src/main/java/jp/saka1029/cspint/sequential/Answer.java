package jp.saka1029.cspint.sequential;

import java.util.Map;

public interface Answer {

    void answer(SearchControl control, Map<Variable, Integer> result);

}
