package com.example.chess.GameObjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.example.chess.Enums.Color;

class PieceStateTest {

    @Test
    void tracksMovementState() {
        Queen queen = new Queen(Color.WHITE);
        assertFalse(queen.hasMoved());

        queen.setHasMoved(true);
        assertTrue(queen.hasMoved());
    }

    @Test
    void storesPosition() {
        Bishop bishop = new Bishop(Color.BLACK);
        Position position = new Position(2, 6);

        bishop.setPosition(position);
        assertEquals(position, bishop.getPosition());
    }
}
