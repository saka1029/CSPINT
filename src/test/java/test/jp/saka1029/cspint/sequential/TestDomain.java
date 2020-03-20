package test.jp.saka1029.cspint.sequential;

import static org.junit.Assert.*;

import org.junit.Test;

import jp.saka1029.cspint.sequential.Domain;

public class TestDomain {

    @Test
    public void testOf() {
        Domain d = Domain.of(1, 4, 5);
        assertEquals(3, d.size());
        assertEquals(1, d.get(0));
        assertEquals(4, d.get(1));
        assertEquals(5, d.get(2));
    }

    @Test
    public void testRange() {
        Domain d = Domain.range(2, 5);
        assertEquals(3, d.size());
        assertEquals(2, d.get(0));
        assertEquals(3, d.get(1));
        assertEquals(4, d.get(2));
    }


    @Test
    public void testToString() {
        Domain d = Domain.range(2, 5);
        assertEquals("[2, 3, 4]", d.toString());
    }

}
