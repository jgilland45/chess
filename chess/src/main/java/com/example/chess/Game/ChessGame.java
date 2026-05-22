package com.example.chess.Game;

import java.util.List;

import com.example.chess.GameObjects.Board;

public class ChessGame {
    private GameState state;
    private final LegalMoveGenerator moveGenerator;

    public ChessGame() {
        this(new MoveGenerator());
    }

    ChessGame(LegalMoveGenerator moveGenerator) {
        this.state = GameState.initial();
        this.moveGenerator = moveGenerator;
    }

    public Board getBoard() {
        return state.getBoard();
    }

    public void printGameState() {
        System.out.println(state);
    }

    public GameState getState() {
        return state;
    }

    public void applySan(String notation) {
        SanMove sanMove = SanParser.parse(notation);
        System.out.println("Calling from applySan for SAN move: " + sanMove);
        List<Move> legalMoves = moveGenerator.generateLegalMoves(state, true);
        System.out.println("Legal moves: " + legalMoves);
        Move matchingMove = sanMove.findMatch(legalMoves, state);
        if (matchingMove == null) {
            throw new IllegalArgumentException("Illegal move: " + notation);
        }
        System.out.println("Applying move: " + sanMove);
        state.applyMove(matchingMove);
    }
}
