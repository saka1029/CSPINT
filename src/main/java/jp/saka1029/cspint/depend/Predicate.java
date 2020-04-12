package jp.saka1029.cspint.depend;

@FunctionalInterface
public interface Predicate {

    boolean test(int... args);

}
