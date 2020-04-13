package jp.saka1029.cspint.depend;

@FunctionalInterface
public interface Derivation6 extends Derivation {

    int apply(int a, int b, int c, int d, int e, int f);

    default int apply(int... a) {
        return apply(a[0], a[1], a[2], a[3], a[4], a[5]);
    }

}
