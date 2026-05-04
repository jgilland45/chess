package com.example.chess.GameObjects;

import com.example.chess.Enums.Color;
import com.example.chess.Enums.MoveType;
import com.example.chess.Enums.PieceType;

public class Pawn extends Piece {
    public Pawn(Color color) {
        super(color, PieceType.PAWN);

        // Set valid move types for a pawn
        this.validMoveTypes = new MoveType[] {
            MoveType.PAWN_DOUBLE_STEP,
            MoveType.PAWN_SINGLE_STEP
        };
    }

    public void removeDoubleStepMove() {
        // Remove the double step move type after the pawn has moved
        this.validMoveTypes = new MoveType[] {
            MoveType.PAWN_SINGLE_STEP
        };
    }

    public void addPromotionMove() {
        // Add promotion move type when the pawn reaches the last rank
        this.validMoveTypes = new MoveType[] {
            MoveType.PAWN_SINGLE_STEP,
            MoveType.PAWN_PROMOTION
        };
    }

    @Override
    public String toString() {
        String colorInitial = (color == Color.WHITE) ? "W" : "B";
        return colorInitial + "P";
    }
}
