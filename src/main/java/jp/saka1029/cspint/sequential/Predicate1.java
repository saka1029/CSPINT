package jp.saka1029.cspint.sequential;

public interface Predicate1 extends Predicate0 {

    boolean test(int a0);

    default boolean test(int... a) {
        return test(a[0]);
    }

}
