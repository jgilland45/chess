package com.example.chess.Game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.example.chess.Enums.PieceType;

class SanParserTest {

    @Test
    void parsesSimplePawnMove() {
        SanMove move = SanParser.parse("e4");

        assertEquals(PieceType.PAWN, move.getPieceType());
        assertEquals(4, move.getTargetRow());
        assertEquals(4, move.getTargetCol());
        assertFalse(move.isCapture());
        assertFalse(move.isCheck());
        assertFalse(move.isCheckmate());
        assertNull(move.getPromotion());
        assertFalse(move.isCastleKingside());
        assertFalse(move.isCastleQueenside());
    }

    @Test
    void parsesPawnCaptureWithCheck() {
        SanMove move = SanParser.parse("exd5+");

        assertEquals(PieceType.PAWN, move.getPieceType());
        assertTrue(move.isCapture());
        assertTrue(move.isCheck());
        assertEquals(3, move.getTargetRow());
        assertEquals(3, move.getTargetCol());
        assertEquals(Integer.valueOf(4), move.getDisambiguationFile());
        assertNull(move.getDisambiguationRank());
    }

    @Test
    void parsesPieceMoveWithDisambiguation() {
        SanMove move = SanParser.parse("Nbd2");

        assertEquals(PieceType.KNIGHT, move.getPieceType());
        assertEquals(6, move.getTargetRow());
        assertEquals(3, move.getTargetCol());
        assertEquals(Integer.valueOf(1), move.getDisambiguationFile());
        assertNull(move.getDisambiguationRank());
    }

    @Test
    void parsesPromotionWithCaptureAndMate() {
        SanMove move = SanParser.parse("dxe8=Q#");

        assertEquals(PieceType.PAWN, move.getPieceType());
        assertTrue(move.isCapture());
        assertTrue(move.isCheckmate());
        assertEquals(PieceType.QUEEN, move.getPromotion());
        assertEquals(0, move.getTargetRow());
        assertEquals(4, move.getTargetCol());
    }

    @Test
    void parsesCastlingVariants() {
        SanMove kingside = SanParser.parse("O-O");
        SanMove queenside = SanParser.parse("0-0-0+");

        assertTrue(kingside.isCastleKingside());
        assertFalse(kingside.isCastleQueenside());
        assertFalse(kingside.isCheck());

        assertTrue(queenside.isCastleQueenside());
        assertFalse(queenside.isCastleKingside());
        assertTrue(queenside.isCheck());
    }

    @Test
    void rejectsInvalidNotations() {
        assertThrows(IllegalArgumentException.class, () -> SanParser.parse(" "));
        assertThrows(IllegalArgumentException.class, () -> SanParser.parse("e8=Z"));
        assertThrows(IllegalArgumentException.class, () -> SanParser.parse("xd5"));
        assertThrows(IllegalArgumentException.class, () -> SanParser.parse("e9"));
        assertThrows(IllegalArgumentException.class, () -> SanParser.parse("Nabc"));
    }
}
