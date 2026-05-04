package com.example.chess.GameObjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.Test;

class PositionTest {

    @Test
    void storesCoordinates() {
        Position position = new Position(3, 5);
        assertEquals(3, position.x);
        assertEquals(5, position.y);
    }

    @Test
    void rejectsInvalidCoordinatesWhenAssertionsEnabled() {
        assumeTrue(Position.class.desiredAssertionStatus());

        assertThrows(AssertionError.class, () -> new Position(-1, 0));
        assertThrows(AssertionError.class, () -> new Position(0, -1));
        assertThrows(AssertionError.class, () -> new Position(8, 0));
        assertThrows(AssertionError.class, () -> new Position(0, 8));
    }
}
