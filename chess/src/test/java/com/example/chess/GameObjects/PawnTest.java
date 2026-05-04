package com.example.chess.GameObjects;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.example.chess.Enums.Color;
import com.example.chess.Enums.MoveType;

class PawnTest {

    @Test
    void startsWithSingleAndDoubleStepMoves() {
        Pawn pawn = new Pawn(Color.WHITE);

        assertArrayEquals(new MoveType[] { MoveType.PAWN_DOUBLE_STEP, MoveType.PAWN_SINGLE_STEP },
            pawn.getValidMoveTypes());
    }

    @Test
    void removeDoubleStepMoveLeavesSingleStepOnly() {
        Pawn pawn = new Pawn(Color.BLACK);
        pawn.removeDoubleStepMove();

        assertArrayEquals(new MoveType[] { MoveType.PAWN_SINGLE_STEP }, pawn.getValidMoveTypes());
    }

    @Test
    void addPromotionMoveAddsPromotionToSingleStep() {
        Pawn pawn = new Pawn(Color.WHITE);
        pawn.addPromotionMove();

        assertArrayEquals(new MoveType[] { MoveType.PAWN_SINGLE_STEP, MoveType.PAWN_PROMOTION },
            pawn.getValidMoveTypes());
    }

    @Test
    void toStringUsesColorInitial() {
        Pawn whitePawn = new Pawn(Color.WHITE);
        Pawn blackPawn = new Pawn(Color.BLACK);

        assertEquals("WP", whitePawn.toString());
        assertEquals("BP", blackPawn.toString());
    }
}
