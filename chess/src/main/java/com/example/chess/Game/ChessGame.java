package com.example.chess.Game;

import java.util.List;

import com.example.chess.GameObjects.Board;

public class ChessGame {
    private GameState state;
    private final LegalMoveGenerator moveGenerator;

    public ChessGame() {
        this(new PermissiveLegalMoveGenerator());
    }

    ChessGame(LegalMoveGenerator moveGenerator) {
        this.state = GameState.initial();
        this.moveGenerator = moveGenerator;
    }

    public Board getBoard() {
        return state.getBoard();
    }

    public void applySan(String notation) {
        SanMove sanMove = SanParser.parse(notation);
        List<Move> legalMoves = moveGenerator.generateLegalMoves(state);
        Move matchingMove = sanMove.findMatch(legalMoves, state);
        if (matchingMove == null) {
            throw new IllegalArgumentException("Illegal move: " + notation);
        }
        state.applyMove(matchingMove);
    }
}
