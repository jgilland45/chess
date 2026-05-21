package com.example.chess.GameObjects;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.example.chess.Enums.Color;
import com.example.chess.Enums.MoveType;

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

    @Test
    void copyCreatesIndependentPiece() {
        Queen queen = new Queen(Color.WHITE);
        queen.setHasMoved(true);
        queen.setPosition(new Position(4, 4));
        queen.setValidMoveTypes(new MoveType[] { MoveType.DIAGONAL });

        Piece copy = queen.copy();

        assertNotSame(queen, copy);
        assertEquals(queen.getType(), copy.getType());
        assertEquals(queen.getColor(), copy.getColor());
        assertTrue(copy.hasMoved());
        assertNotSame(queen.getPosition(), copy.getPosition());
        assertEquals(queen.getPosition().getRow(), copy.getPosition().getRow());
        assertEquals(queen.getPosition().getCol(), copy.getPosition().getCol());
        assertArrayEquals(queen.getValidMoveTypes(), copy.getValidMoveTypes());
        assertNotSame(queen.getValidMoveTypes(), copy.getValidMoveTypes());

        queen.setHasMoved(false);
        assertTrue(copy.hasMoved());
    }
}
