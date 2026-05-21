package com.example.chess.GameObjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.Test;

import com.example.chess.Enums.Color;
import com.example.chess.GameObjects.Position;

class BoardTest {

    @Test
    void constructorSetsUpBoard() {
        Board board = new Board();
        assertEquals(8, board.getNumRows());
        assertEquals(8, board.getNumCols());

        Piece[][] pieces = board.getPieces();
        assertEquals(8, pieces.length);
        assertEquals(8, pieces[0].length);

        assertTrue(board.getPieceAt(0, 0) instanceof Rook);
        assertTrue(board.getPieceAt(0, 1) instanceof Knight);
        assertTrue(board.getPieceAt(0, 2) instanceof Bishop);
        assertTrue(board.getPieceAt(0, 3) instanceof Queen);
        assertTrue(board.getPieceAt(0, 4) instanceof King);

        assertEquals(Color.BLACK, board.getPieceAt(0, 4).getColor());
        assertEquals(Color.WHITE, board.getPieceAt(7, 4).getColor());

        int count = countPieces(pieces);
        assertEquals(32, count);
    }

    @Test
    void setupPiecesRestoresMissingPiece() {
        Board board = new Board();
        Piece[][] pieces = board.getPieces();
        pieces[0][0] = null;

        board.setupPieces();

        assertNotNull(board.getPieceAt(0, 0));
        assertTrue(board.getPieceAt(0, 0) instanceof Rook);
        assertEquals(Color.BLACK, board.getPieceAt(0, 0).getColor());
    }

    @Test
    void getPieceAtOutOfBoundsReturnsNull() {
        Board board = new Board();
        assertNull(board.getPieceAt(-1, 0));
        assertNull(board.getPieceAt(0, -1));
        assertNull(board.getPieceAt(8, 0));
        assertNull(board.getPieceAt(0, 8));
    }

    @Test
    void setPiecesRejectsWrongSizeWhenAssertionsEnabled() {
        assumeTrue(Board.class.desiredAssertionStatus());
        Board board = new Board();
        Piece[][] wrong = new Piece[7][8];

        assertThrows(AssertionError.class, () -> board.setPieces(wrong));
    }

    @Test
    void copyCreatesIndependentBoard() {
        Board board = new Board();
        Piece original = board.getPieceAt(6, 0);
        original.setHasMoved(true);
        original.setPosition(new Position(0, 0));

        Board copy = board.copy();

        assertNotSame(board, copy);
        Piece copied = copy.getPieceAt(6, 0);
        assertNotSame(original, copied);
        assertEquals(original.getType(), copied.getType());
        assertEquals(original.getColor(), copied.getColor());
        assertTrue(copied.hasMoved());
        assertNotSame(original.getPosition(), copied.getPosition());
        assertEquals(original.getPosition().getRow(), copied.getPosition().getRow());
        assertEquals(original.getPosition().getCol(), copied.getPosition().getCol());

        original.setHasMoved(false);
        assertTrue(copied.hasMoved());
    }

    private int countPieces(Piece[][] pieces) {
        int count = 0;
        for (int row = 0; row < pieces.length; row++) {
            for (int col = 0; col < pieces[row].length; col++) {
                if (pieces[row][col] != null) {
                    count++;
                }
            }
        }
        return count;
    }
}
