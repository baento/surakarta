package fr.uphf.etu.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {

    @Test
    public void testSuivant() {
        assertEquals(Player.NONE.next(), Player.P1);
        assertEquals(Player.P1.next(), Player.P2);
        assertEquals(Player.P2.next(), Player.P1);
    }
}
