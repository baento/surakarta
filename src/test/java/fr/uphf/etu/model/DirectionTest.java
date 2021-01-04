package fr.uphf.etu.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class DirectionTest {
    @Test
    public void testSuivant() {
        assertEquals(Direction.NONE.next(), Direction.NONE);
        assertEquals(Direction.NORTH.next(), Direction.NORTH_EAST);
        assertEquals(Direction.NORTH_EAST.next(), Direction.EAST);
        assertEquals(Direction.EAST.next(), Direction.SOUTH_EAST);
        assertEquals(Direction.SOUTH_EAST.next(), Direction.SOUTH);
        assertEquals(Direction.SOUTH.next(), Direction.SOUTH_WEST);
        assertEquals(Direction.SOUTH_WEST.next(), Direction.WEST);
        assertEquals(Direction.WEST.next(), Direction.NORTH_WEST);
        assertEquals(Direction.NORTH_WEST.next(), Direction.NORTH);
    }

    @Test
    public void testSuivantCardinal() {
        assertEquals(Direction.NONE.nextCardinal(), Direction.NONE);
        assertEquals(Direction.NORTH.nextCardinal(), Direction.EAST);
        assertEquals(Direction.NORTH_EAST.nextCardinal(), Direction.EAST);
        assertEquals(Direction.EAST.nextCardinal(), Direction.SOUTH);
        assertEquals(Direction.SOUTH_EAST.nextCardinal(), Direction.SOUTH);
        assertEquals(Direction.SOUTH.nextCardinal(), Direction.WEST);
        assertEquals(Direction.SOUTH_WEST.nextCardinal(), Direction.WEST);
        assertEquals(Direction.WEST.nextCardinal(), Direction.NORTH);
        assertEquals(Direction.NORTH_WEST.nextCardinal(), Direction.NORTH);
    }

    @Test
    public void testPrecedent() {
        assertEquals(Direction.NONE.previous(), Direction.NONE);
        assertEquals(Direction.NORTH.previous(), Direction.NORTH_WEST);
        assertEquals(Direction.NORTH_WEST.previous(), Direction.WEST);
        assertEquals(Direction.WEST.previous(), Direction.SOUTH_WEST);
        assertEquals(Direction.SOUTH_WEST.previous(), Direction.SOUTH);
        assertEquals(Direction.SOUTH.previous(), Direction.SOUTH_EAST);
        assertEquals(Direction.SOUTH_EAST.previous(), Direction.EAST);
        assertEquals(Direction.EAST.previous(), Direction.NORTH_EAST);
        assertEquals(Direction.NORTH_EAST.previous(), Direction.NORTH);
    }

    @Test
    public void testPrecedentCardinal() {
        assertEquals(Direction.NONE.previousCardinal(), Direction.NONE);
        assertEquals(Direction.NORTH.previousCardinal(), Direction.WEST);
        assertEquals(Direction.NORTH_WEST.previousCardinal(), Direction.WEST);
        assertEquals(Direction.WEST.previousCardinal(), Direction.SOUTH);
        assertEquals(Direction.SOUTH_WEST.previousCardinal(), Direction.SOUTH);
        assertEquals(Direction.SOUTH.previousCardinal(), Direction.EAST);
        assertEquals(Direction.SOUTH_EAST.previousCardinal(), Direction.EAST);
        assertEquals(Direction.EAST.previousCardinal(), Direction.NORTH);
        assertEquals(Direction.NORTH_EAST.previousCardinal(), Direction.NORTH);
    }

    @Test
    public void testOppose() {
        assertEquals(Direction.NONE.opposite(), Direction.NONE);
        assertEquals(Direction.NORTH.opposite(), Direction.SOUTH);
        assertEquals(Direction.NORTH_EAST.opposite(), Direction.SOUTH_WEST);
        assertEquals(Direction.EAST.opposite(), Direction.WEST);
        assertEquals(Direction.SOUTH_EAST.opposite(), Direction.NORTH_WEST);
        assertEquals(Direction.SOUTH.opposite(), Direction.NORTH);
        assertEquals(Direction.SOUTH_WEST.opposite(), Direction.NORTH_EAST);
        assertEquals(Direction.WEST.opposite(), Direction.EAST);
        assertEquals(Direction.NORTH_WEST.opposite(), Direction.SOUTH_EAST);
    }

    @Test
    public void testCardinal() {
        assertTrue(Direction.NORTH.isCardinal());
        assertTrue(Direction.EAST.isCardinal());
        assertTrue(Direction.SOUTH.isCardinal());
        assertTrue(Direction.WEST.isCardinal());

        assertFalse(Direction.NORTH_EAST.isCardinal());
        assertFalse(Direction.SOUTH_EAST.isCardinal());
        assertFalse(Direction.SOUTH_WEST.isCardinal());
        assertFalse(Direction.NORTH_WEST.isCardinal());
    }
}
