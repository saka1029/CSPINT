package jp.saka1029.cspint.depend;

public interface Predicate1 extends Predicate {

    boolean test(int a);

    default boolean test(int... a) {
        return test(a[0]);
    }

}
