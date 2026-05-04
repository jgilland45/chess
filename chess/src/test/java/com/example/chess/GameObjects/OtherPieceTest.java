package com.example.chess.GameObjects;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.example.chess.Enums.Color;
import com.example.chess.Enums.MoveType;

class OtherPieceTest {

    @Test
    void bishopUsesDiagonalMoves() {
        Bishop bishop = new Bishop(Color.WHITE);

        assertArrayEquals(new MoveType[] { MoveType.DIAGONAL }, bishop.getValidMoveTypes());
        assertEquals("WB", bishop.toString());
    }

    @Test
    void knightUsesLShapeMoves() {
        Knight knight = new Knight(Color.BLACK);

        assertArrayEquals(new MoveType[] { MoveType.L_SHAPE }, knight.getValidMoveTypes());
        assertEquals("BN", knight.toString());
    }

    @Test
    void queenUsesDiagonalAndOrthogonalMoves() {
        Queen queen = new Queen(Color.WHITE);

        assertArrayEquals(new MoveType[] { MoveType.ORTHOGONAL, MoveType.DIAGONAL }, queen.getValidMoveTypes());
        assertEquals("WQ", queen.toString());
    }
}
