package jp.saka1029.cspint.depend;

@FunctionalInterface
public interface Derivation5 extends Derivation {

    int apply(int a, int b, int c, int d, int e);

    default int apply(int... a) {
        return apply(a[0], a[1], a[2], a[3], a[4]);
    }

}
