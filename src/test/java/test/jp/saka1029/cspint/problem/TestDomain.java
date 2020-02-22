package test.jp.saka1029.cspint.problem;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

import jp.saka1029.cspint.problem.Domain;

public class TestDomain {

    @Test
    public void testOf() {
        Domain d = Domain.of(1, 4, 5);
        Iterator<Integer> i = d.iterator();
        assertEquals(3, d.size());
        assertTrue(i.hasNext());
        assertEquals(1, (int)i.next());
        assertTrue(i.hasNext());
        assertEquals(4, (int)i.next());
        assertTrue(i.hasNext());
        assertEquals(5, (int)i.next());
        assertFalse(i.hasNext());
    }

    @Test
    public void testRange() {
        Domain d = Domain.range(2, 5);
        Iterator<Integer> i = d.iterator();
        assertEquals(3, d.size());
        assertTrue(i.hasNext());
        assertEquals(2, (int)i.next());
        assertTrue(i.hasNext());
        assertEquals(3, (int)i.next());
        assertTrue(i.hasNext());
        assertEquals(4, (int)i.next());
        assertFalse(i.hasNext());
    }


    @Test
    public void testToString() {
        Domain d = Domain.range(2, 5);
        assertEquals("[2, 3, 4]", d.toString());
    }

}
