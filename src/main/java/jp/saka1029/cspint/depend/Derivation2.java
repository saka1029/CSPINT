package jp.saka1029.cspint.depend;

@FunctionalInterface
public interface Derivation2 extends Derivation {

    int apply(int a, int b);

    default int apply(int... a) {
        return apply(a[0], a[1]);
    }

}
