package com.example.chess.GameObjects;

import com.example.chess.Enums.Color;
import com.example.chess.Enums.MoveType;
import com.example.chess.Enums.PieceType;

public class Knight extends Piece {
    public Knight(Color color) {
        super(color, PieceType.KNIGHT);

        // Set valid move types for a knight
        this.validMoveTypes = new MoveType[] {
            MoveType.L_SHAPE
        };
    }

    @Override
    public Piece copy() {
        Knight copy = new Knight(color);
        copyCommonFields(copy);
        return copy;
    }

    @Override
    public String toString() {
        String colorInitial = (color == Color.WHITE) ? "W" : "B";
        return colorInitial + "N";
    }
}
