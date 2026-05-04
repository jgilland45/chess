package com.example.chess.GameObjects;

import com.example.chess.Enums.Color;
import com.example.chess.Enums.MoveType;
import com.example.chess.Enums.PieceType;

public class Queen extends Piece {
    public Queen(Color color) {
        super(color, PieceType.QUEEN);

        // Set valid move types for a queen
        this.validMoveTypes = new MoveType[] {
            MoveType.ORTHOGONAL,
            MoveType.DIAGONAL
        };
    }

    @Override
    public String toString() {
        String colorInitial = (color == Color.WHITE) ? "W" : "B";
        return colorInitial + "Q";
    }
}
