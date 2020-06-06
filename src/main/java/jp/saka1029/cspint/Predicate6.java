package jp.saka1029.cspint;

public interface Predicate6 extends Predicate0 {

    boolean test(int a0, int a1, int a2, int a3, int a4, int a5);

    default boolean test(int... a) {
        return test(a[0], a[1], a[2], a[3], a[4], a[5]);
    }

}
