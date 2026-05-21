package com.example.chess.Game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.example.chess.Enums.Color;
import com.example.chess.GameObjects.Piece;
import com.example.chess.GameObjects.Position;

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
        assertNotNull(state.getEnPassantTargets());
        assertEquals(0, state.getEnPassantTargets().size());
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

    @Test
    void copyCreatesIndependentGameState() {
        GameState state = GameState.initial();
        state.getEnPassantTargets().add(new Position(3, 3));

        GameState copy = state.copy();

        assertNotSame(state.getBoard(), copy.getBoard());

        Piece originalPawn = state.getBoard().getPieceAt(6, 0);
        Piece copiedPawn = copy.getBoard().getPieceAt(6, 0);
        assertNotSame(originalPawn, copiedPawn);

        state.getEnPassantTargets().add(new Position(4, 4));
        assertEquals(1, copy.getEnPassantTargets().size());

        originalPawn.setHasMoved(true);
        assertFalse(copiedPawn.hasMoved());
    }
}
