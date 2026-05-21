package com.example.chess.GameObjects;

import com.example.chess.Enums.Color;
import com.example.chess.Enums.MoveType;
import com.example.chess.Enums.PieceType;

public class Bishop extends Piece {
    public Bishop(Color color) {
        super(color, PieceType.BISHOP);

        // Set valid move types for a bishop
        this.validMoveTypes = new MoveType[] {
            MoveType.DIAGONAL
        };
    }

    @Override
    public Piece copy() {
        Bishop copy = new Bishop(color);
        copyCommonFields(copy);
        return copy;
    }

    @Override
    public String toString() {
        String colorInitial = (color == Color.WHITE) ? "W" : "B";
        return colorInitial + "B";
    }
}
