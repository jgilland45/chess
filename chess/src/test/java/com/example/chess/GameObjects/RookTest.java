package com.example.chess.GameObjects;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.example.chess.Enums.Color;
import com.example.chess.Enums.MoveType;

class RookTest {

    @Test
    void startsWithCastleMoves() {
        Rook rook = new Rook(Color.WHITE);

        assertArrayEquals(new MoveType[] { MoveType.ORTHOGONAL },
            rook.getValidMoveTypes());
    }

    @Test
    void toStringUsesColorInitial() {
        Rook whiteRook = new Rook(Color.WHITE);
        Rook blackRook = new Rook(Color.BLACK);

        assertEquals("WR", whiteRook.toString());
        assertEquals("BR", blackRook.toString());
    }
}
