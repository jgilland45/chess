package com.example.chess.Game;

import java.util.List;

public class UnimplementedLegalMoveGenerator implements LegalMoveGenerator {
    @Override
    public List<Move> generateLegalMoves(GameState state) {
        throw new UnsupportedOperationException(
            "Legal move generation is not implemented yet. Full SAN support requires move legality logic.");
    }
}
