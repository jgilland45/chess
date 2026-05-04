package com.example.chess.GameObjects;

import com.example.chess.Enums.Color;
import com.example.chess.Enums.MoveType;
import com.example.chess.Enums.PieceType;

public class King extends Piece {
    public King(Color color) {
        super(color, PieceType.KING);

        // Set valid move types for a king
        this.validMoveTypes = new MoveType[] {
            MoveType.KING,
            MoveType.CASTLE_KINGSIDE,
            MoveType.CASTLE_QUEENSIDE
        };
    }

    public void removeCastleMoves() {
        // Remove castling move types after the rook has moved
        this.validMoveTypes = new MoveType[] {
            MoveType.KING
        };
    }

    public void removeQueensideCastleMove() {
        // Remove queenside castling move type after the rook has moved
        this.validMoveTypes = new MoveType[] {
            MoveType.KING,
            MoveType.CASTLE_KINGSIDE
        };
    }

    public void removeKingsideCastleMove() {
        // Remove kingside castling move type after the rook has moved
        this.validMoveTypes = new MoveType[] {
            MoveType.KING,
            MoveType.CASTLE_QUEENSIDE
        };
    }

    @Override
    public String toString() {
        String colorInitial = (color == Color.WHITE) ? "W" : "B";
        return colorInitial + "K";
    }
}
