package jp.saka1029.cspint.depend;

@FunctionalInterface
public interface Derivation9 extends Derivation {

    int apply(int a, int b, int c, int d, int e, int f, int g, int h, int i);

    default int apply(int... a) {
        return apply(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8]);
    }

}