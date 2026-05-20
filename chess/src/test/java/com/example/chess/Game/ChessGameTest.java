package com.example.chess.Game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.chess.Enums.PieceType;
import com.example.chess.GameObjects.Piece;

class ChessGameTest {

    @Test
    void applySanMovesMatchingMove() {
        LegalMoveGenerator generator = (state, validateKingSafety) -> List.of(new Move(6, 4, 4, 4, null, false, false, false, false, false));
        ChessGame game = new ChessGame(generator);
        Piece piece = game.getBoard().getPieceAt(6, 4);

        game.applySan("e4");

        assertNull(game.getBoard().getPieceAt(6, 4));
        assertEquals(piece, game.getBoard().getPieceAt(4, 4));
    }

    @Test
    void applySanThrowsWhenNoLegalMoveMatches() {
        LegalMoveGenerator generator = (state, validateKingSafety) -> List.of();
        ChessGame game = new ChessGame(generator);

        assertThrows(IllegalArgumentException.class, () -> game.applySan("e4"));
    }

    @Test
    void applySanHonorsPromotionMatch() {
        Move rookPromo = new Move(6, 0, 0, 0, PieceType.ROOK, false, false,false, false, false);
        Move queenPromo = new Move(6, 0, 0, 0, PieceType.QUEEN, false, false, false, false, false);
        LegalMoveGenerator generator = (state, validateKingSafety) -> List.of(rookPromo, queenPromo);
        ChessGame game = new ChessGame(generator);

        game.applySan("a8=Q");

        assertNull(game.getBoard().getPieceAt(6, 0));
        assertNotNull(game.getBoard().getPieceAt(0, 0));
    }

    @Test
    void applySanCastleMovesKing() {
        Move castleMove = new Move(7, 4, 7, 6, null, false, true, false, false, false);
        LegalMoveGenerator generator = (state, validateKingSafety) -> List.of(castleMove);
        ChessGame game = new ChessGame(generator);
        Piece king = game.getBoard().getPieceAt(7, 4);

        game.applySan("O-O");

        assertNull(game.getBoard().getPieceAt(7, 4));
        assertEquals(king, game.getBoard().getPieceAt(7, 6));
    }
}
