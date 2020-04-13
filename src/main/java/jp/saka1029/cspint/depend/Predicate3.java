package jp.saka1029.cspint.depend;

public interface Predicate3 extends Predicate {

    boolean test(int a, int b, int c);

    default boolean test(int... a) {
        return test(a[0], a[1], a[2]);
    }

}
