package test.jp.saka1029.cspint.puzzle;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.*;

import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class TestParameter {

    @ParameterizedTest
    @MethodSource("parameters")
    public void test(int expected, int actual) {
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("parameters")
    public void test2(int expected, int actual) {
        assertEquals(expected, actual);
    }

    static List<Arguments> parameters() {
        return List.of(
            arguments(1, 1),
            arguments(2, 2),
            arguments(3, 3)
        );
    }

}
