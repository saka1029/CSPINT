package jp.saka1029.cspint.depend;

public interface Predicate5 extends Predicate {

    boolean test(int a, int b, int c, int d, int e);

    default boolean test(int... a) {
        return test(a[0], a[1], a[2], a[3], a[4]);
    }

}
