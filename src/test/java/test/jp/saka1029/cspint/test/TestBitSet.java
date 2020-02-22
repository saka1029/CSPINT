package test.jp.saka1029.cspint.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.BitSet;
import java.util.Iterator;

import org.junit.Test;

public class TestBitSet {

    static class BitSetIterator implements Iterator<Integer> {

        private final BitSet bs;
        private int index = 0;
        public BitSetIterator(BitSet bs) {
            this.bs = bs;
        }

        public boolean hasNext() {
            return bs.nextSetBit(index) >= 0;
        }

        public Integer next() {
            int next = bs.nextSetBit(index);
            index = next + 1;
            return next;
        }
    }

    @Test
    public void testBitSet() {
        BitSet bs = new BitSet(4);
        assertEquals(64, bs.size());
        assertEquals(0, bs.length());
        assertEquals(0, bs.cardinality());
        bs.set(3);
        assertEquals(64, bs.size());
        assertEquals(4, bs.length());
        assertEquals(1, bs.cardinality());
        bs.set(4);
        assertEquals(64, bs.size());
        assertEquals(5, bs.length());
        assertEquals(2, bs.cardinality());
    }
    
    @Test
    public void testBitSetIterator() {
        BitSet bs = new BitSet(4);
        bs.set(3);
        bs.set(4);
        Iterator<Integer> it =  new BitSetIterator(bs);
        assertTrue(it.hasNext());
        assertEquals(3, it.next());
        assertTrue(it.hasNext());
        assertEquals(4, it.next());
        assertFalse(it.hasNext());
    }

}
