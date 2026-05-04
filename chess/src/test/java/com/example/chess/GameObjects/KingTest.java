package com.example.chess.GameObjects;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.example.chess.Enums.Color;
import com.example.chess.Enums.MoveType;

class KingTest {

    @Test
    void startsWithCastleMoves() {
        King king = new King(Color.WHITE);

        assertArrayEquals(new MoveType[] { MoveType.KING, MoveType.CASTLE_KINGSIDE, MoveType.CASTLE_QUEENSIDE },
            king.getValidMoveTypes());
    }

    @Test
    void removeCastleMovesLeavesOnlyKingMoves() {
        King king = new King(Color.BLACK);
        king.removeCastleMoves();

        assertArrayEquals(new MoveType[] { MoveType.KING }, king.getValidMoveTypes());
    }

    @Test
    void removeQueensideCastleMoveLeavesKingsideCastle() {
        King king = new King(Color.WHITE);
        king.removeQueensideCastleMove();

        assertArrayEquals(new MoveType[] { MoveType.KING, MoveType.CASTLE_KINGSIDE }, king.getValidMoveTypes());
    }

    @Test
    void removeKingsideCastleMoveLeavesQueensideCastle() {
        King king = new King(Color.BLACK);
        king.removeKingsideCastleMove();

        assertArrayEquals(new MoveType[] { MoveType.KING, MoveType.CASTLE_QUEENSIDE }, king.getValidMoveTypes());
    }

    @Test
    void toStringUsesColorInitial() {
        King whiteKing = new King(Color.WHITE);
        King blackKing = new King(Color.BLACK);

        assertEquals("WK", whiteKing.toString());
        assertEquals("BK", blackKing.toString());
    }
}
