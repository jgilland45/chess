package com.example.chess.Factories;

import com.example.chess.Enums.Color;
import com.example.chess.Enums.PieceType;

import com.example.chess.GameObjects.Piece;
import com.example.chess.GameObjects.Pawn;
import com.example.chess.GameObjects.Rook;
import com.example.chess.GameObjects.Knight;
import com.example.chess.GameObjects.Bishop;
import com.example.chess.GameObjects.Queen;
import com.example.chess.GameObjects.King;

public class PieceFactory {

    private PieceFactory() {
        // Private constructor to prevent instantiation
    }

    public static Piece create(PieceType pieceType, Color color) {
        switch (pieceType) {
            case PAWN:
                return new Pawn(color);
            case ROOK:
                return new Rook(color);
            case KNIGHT:
                return new Knight(color);
            case BISHOP:
                return new Bishop(color);
            case QUEEN:
                return new Queen(color);
            case KING:
                return new King(color);
            default:
                throw new IllegalArgumentException("Invalid piece type: " + pieceType);
        }
    }
}
