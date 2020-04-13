package jp.saka1029.cspint.depend;

public interface Predicate2 extends Predicate {

    boolean test(int a, int b);

    default boolean test(int... a) {
        return test(a[0], a[1]);
    }

}
