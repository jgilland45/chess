package com.example.chess.Enums;

public enum PieceType {
    PAWN,
    ROOK,
    KNIGHT,
    BISHOP,
    QUEEN,
    KING;

    public String getSanSymbol() {
        switch (this) {
            case PAWN:
                return "";
            case ROOK:
                return "R";
            case KNIGHT:
                return "N";
            case BISHOP:
                return "B";
            case QUEEN:
                return "Q";
            case KING:
                return "K";
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
    }
}
