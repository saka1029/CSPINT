package jp.saka1029.cspint;

public interface Function0 {

    int apply(int... a);

    public default Function0 negate() {
        return a -> -apply(a);
    }

}
