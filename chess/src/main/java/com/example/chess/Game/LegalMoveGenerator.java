package com.example.chess.Game;

import java.util.List;

public interface LegalMoveGenerator {
    List<Move> generateLegalMoves(GameState state, boolean validateKingSafety);
}
