package jp.saka1029.cspint;

public interface Predicate10 extends Predicate0 {

    boolean test(int a0, int a1, int a2, int a3, int a4, int a5, int a6, int a7, int a8, int a9);

    default boolean test(int... a) {
        return test(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8], a[9]);
    }

}
