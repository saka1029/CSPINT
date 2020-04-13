package jp.saka1029.cspint.depend;

@FunctionalInterface
public interface Derivation1 extends Derivation {

    int apply(int a);

    default int apply(int... a) {
        return apply(a[0]);
    }

}
