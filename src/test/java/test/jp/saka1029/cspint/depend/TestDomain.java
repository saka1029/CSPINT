package test.jp.saka1029.cspint.depend;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import jp.saka1029.cspint.depend.Domain;

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
    public void testRangeClosed() {
        Domain d = Domain.rangeClosed(2, 5);
        assertEquals(4, d.size());
        assertEquals(2, d.get(0));
        assertEquals(3, d.get(1));
        assertEquals(4, d.get(2));
        assertEquals(5, d.get(3));
    }

    @Test
    public void testHashCodeEquals() {
        assertEquals(Domain.of(1, 2), Domain.range(1, 3));
        assertEquals(Domain.of(1, 2).hashCode(), Domain.range(1, 3).hashCode());
        assertNotEquals(Domain.of(0), Domain.of(1));
        assertNotEquals(Domain.of(1), "abc");
    }

    @Test
    public void testToString() {
        Domain d = Domain.range(2, 5);
        assertEquals("[2, 3, 4]", d.toString());
    }

    @Test
    public void testRangeException() {
        try {
            Domain.range(9, 3);
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

}
