package com.example.chess.Factories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.example.chess.Enums.Color;
import com.example.chess.Enums.PieceType;
import com.example.chess.GameObjects.Bishop;
import com.example.chess.GameObjects.King;
import com.example.chess.GameObjects.Knight;
import com.example.chess.GameObjects.Pawn;
import com.example.chess.GameObjects.Piece;
import com.example.chess.GameObjects.Queen;
import com.example.chess.GameObjects.Rook;

class PieceFactoryTest {

    @Test
    void createsExpectedPieceTypes() {
        assertPiece(PieceType.PAWN, Pawn.class);
        assertPiece(PieceType.ROOK, Rook.class);
        assertPiece(PieceType.KNIGHT, Knight.class);
        assertPiece(PieceType.BISHOP, Bishop.class);
        assertPiece(PieceType.QUEEN, Queen.class);
        assertPiece(PieceType.KING, King.class);
    }

    private void assertPiece(PieceType type, Class<? extends Piece> expectedClass) {
        Piece piece = PieceFactory.create(type, Color.WHITE);
        assertTrue(expectedClass.isInstance(piece));
        assertEquals(Color.WHITE, piece.getColor());
        assertEquals(type, piece.getType());
    }
}
