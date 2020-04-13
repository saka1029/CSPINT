package jp.saka1029.cspint.depend;

public interface Predicate6 extends Predicate {

    boolean test(int a, int b, int c, int d, int e, int f);

    default boolean test(int... a) {
        return test(a[0], a[1], a[2], a[3], a[4], a[5]);
    }

}
