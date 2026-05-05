package com.example.chess.Game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.example.chess.Enums.PieceType;

class MoveTest {

    @Test
    void exposesConstructorValues() {
        Move move = new Move(6, 4, 4, 4, PieceType.QUEEN, true, true, false);

        assertEquals(6, move.getFromRow());
        assertEquals(4, move.getFromCol());
        assertEquals(4, move.getToRow());
        assertEquals(4, move.getToCol());
        assertEquals(PieceType.QUEEN, move.getPromotion());
        assertTrue(move.isCapture());
        assertTrue(move.isCastleKingside());
        assertFalse(move.isCastleQueenside());
    }
}
