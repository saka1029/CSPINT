package jp.saka1029.cspint.depend;

public interface Predicate4 extends Predicate {

    boolean test(int a, int b, int c, int d);

    default boolean test(int... a) {
        return test(a[0], a[1], a[2], a[3]);
    }

}
