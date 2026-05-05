package com.example.chess.GameObjects;

import com.example.chess.Enums.Color;
import com.example.chess.Enums.MoveType;
import com.example.chess.Enums.PieceType;

public class Rook extends Piece {
    public Rook(Color color) {
        super(color, PieceType.ROOK);

        // Set valid move types for a rook
        this.validMoveTypes = new MoveType[] {
            MoveType.ORTHOGONAL
        };
    }

    @Override
    public String toString() {
        String colorInitial = (color == Color.WHITE) ? "W" : "B";
        return colorInitial + "R";
    }
}
