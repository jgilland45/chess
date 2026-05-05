package com.example.chess.Game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.chess.Enums.PieceType;

class SanMoveTest {

    @Test
    void findsCastleMoveWhenRequested() {
        SanMove sanMove = new SanMove(PieceType.KING, -1, -1, false, false, false, null,
            true, false, null, null);
        Move normal = new Move(7, 4, 7, 5, null, false, false, false, false, false);
        Move castle = new Move(7, 4, 7, 6, null, false, true, false, false, false);

        Move match = sanMove.findMatch(List.of(normal, castle), GameState.initial());

        assertEquals(castle, match);
    }

    @Test
    void respectsPromotionWhenMatching() {
        SanMove sanMove = new SanMove(PieceType.PAWN, 0, 0, false, false, false, PieceType.QUEEN,
            false, false, null, null);
        Move rookPromo = new Move(6, 0, 0, 0, PieceType.ROOK, false, false, false, false, false);
        Move queenPromo = new Move(6, 0, 0, 0, PieceType.QUEEN, false, false, false, false, false);

        Move match = sanMove.findMatch(List.of(rookPromo, queenPromo), GameState.initial());

        assertEquals(queenPromo, match);
    }

    @Test
    void returnsNullWhenNoTargetMatches() {
        SanMove sanMove = new SanMove(PieceType.BISHOP, 4, 4, false, false, false, null,
            false, false, null, null);
        Move move = new Move(7, 2, 5, 2, null, false, false, false, false, false);

        Move match = sanMove.findMatch(List.of(move), GameState.initial());

        assertNull(match);
    }
}
