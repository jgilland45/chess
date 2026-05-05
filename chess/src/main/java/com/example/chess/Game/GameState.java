package com.example.chess.Game;

import com.example.chess.Enums.Color;
import com.example.chess.GameObjects.Board;
import com.example.chess.GameObjects.Position;

public class GameState {
    private final Board board;
    private Color sideToMove;
    private boolean whiteCastleKingside;
    private boolean whiteCastleQueenside;
    private boolean blackCastleKingside;
    private boolean blackCastleQueenside;
    private Position enPassantTarget;
    private int halfmoveClock;
    private int fullmoveNumber;

    private GameState(Board board) {
        this.board = board;
        this.sideToMove = Color.WHITE;
        this.whiteCastleKingside = true;
        this.whiteCastleQueenside = true;
        this.blackCastleKingside = true;
        this.blackCastleQueenside = true;
        this.enPassantTarget = null;
        this.halfmoveClock = 0;
        this.fullmoveNumber = 1;
    }

    public static GameState initial() {
        return new GameState(new Board());
    }

    public Board getBoard() {
        return board;
    }

    public Color getSideToMove() {
        return sideToMove;
    }

    public boolean canWhiteCastleKingside() {
        return whiteCastleKingside;
    }

    public boolean canWhiteCastleQueenside() {
        return whiteCastleQueenside;
    }

    public boolean canBlackCastleKingside() {
        return blackCastleKingside;
    }

    public boolean canBlackCastleQueenside() {
        return blackCastleQueenside;
    }

    public Position getEnPassantTarget() {
        return enPassantTarget;
    }

    public int getHalfmoveClock() {
        return halfmoveClock;
    }

    public int getFullmoveNumber() {
        return fullmoveNumber;
    }

    public void applyMove(Move move) {
        board.movePiece(move.getFromRow(), move.getFromCol(), move.getToRow(), move.getToCol());
        // TODO: update castling rights, en passant, clocks, and side to move.
    }
}
