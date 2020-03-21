package jp.saka1029.cspint.sequential;

public interface Predicate3 extends Predicate0 {

    boolean test(int a0, int a1, int a2);

    default boolean test(int... a) {
        return test(a[0], a[1], a[2]);
    }

}
