package jp.saka1029.cspint.sequential;

@FunctionalInterface
public interface Predicate {

    boolean test(int... args);

}
