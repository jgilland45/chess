package com.example.chess.Game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.example.chess.Enums.Color;
import com.example.chess.GameObjects.Piece;

class GameStateTest {

    @Test
    void initialStateHasExpectedDefaults() {
        GameState state = GameState.initial();

        assertNotNull(state.getBoard());
        assertEquals(Color.WHITE, state.getSideToMove());
        assertEquals(true, state.canWhiteCastleKingside());
        assertEquals(true, state.canWhiteCastleQueenside());
        assertEquals(true, state.canBlackCastleKingside());
        assertEquals(true, state.canBlackCastleQueenside());
        assertNull(state.getEnPassantTargets());
        assertEquals(0, state.getHalfmoveClock());
        assertEquals(1, state.getFullmoveNumber());
    }

    @Test
    void applyMoveRelocatesPieceOnBoard() {
        GameState state = GameState.initial();
        Piece piece = state.getBoard().getPieceAt(6, 4);

        state.applyMove(new Move(6, 4, 4, 4, null, false, false, false, false, false));

        assertNull(state.getBoard().getPieceAt(6, 4));
        assertEquals(piece, state.getBoard().getPieceAt(4, 4));
    }

    @Test
    void applyMoveIgnoresOutOfBoundsTarget() {
        GameState state = GameState.initial();
        Piece piece = state.getBoard().getPieceAt(6, 4);

        state.applyMove(new Move(6, 4, 8, 4, null, false, false, false, false, false));

        assertEquals(piece, state.getBoard().getPieceAt(6, 4));
        assertNull(state.getBoard().getPieceAt(8, 4));
    }
}
