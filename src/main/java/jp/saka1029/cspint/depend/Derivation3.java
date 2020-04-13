package jp.saka1029.cspint.depend;

@FunctionalInterface
public interface Derivation3 extends Derivation {

    int apply(int a, int b, int c);

    default int apply(int... a) {
        return apply(a[0], a[1], a[2]);
    }

}
