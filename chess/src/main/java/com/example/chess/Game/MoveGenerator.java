package com.example.chess.Game;

import java.util.ArrayList;
import java.util.List;

import com.example.chess.Enums.Color;
import com.example.chess.Enums.PieceType;
import com.example.chess.GameObjects.Board;
import com.example.chess.GameObjects.King;
import com.example.chess.GameObjects.Pawn;
import com.example.chess.GameObjects.Piece;

public class MoveGenerator implements LegalMoveGenerator {
    @Override
    public List<Move> generateLegalMoves(GameState state) {
        Board board = state.getBoard();
        Piece[][] pieces = board.getPieces();
        List<Move> moves = new ArrayList<>();

        for (int fromRow = 0; fromRow < pieces.length; fromRow++) {
            for (int fromCol = 0; fromCol < pieces[fromRow].length; fromCol++) {
                Piece piece = pieces[fromRow][fromCol];
                if (piece == null) {
                    continue;
                }

                addAllTargets(moves, pieces, piece, fromRow, fromCol);
                if (piece instanceof King) {
                    addCastleMoves(moves, fromRow, fromCol);
                }
            }
        }

        return moves;
    }

    private void addAllTargets(List<Move> moves, Piece[][] pieces, Piece piece, int fromRow, int fromCol) {
        for (int toRow = 0; toRow < pieces.length; toRow++) {
            for (int toCol = 0; toCol < pieces[toRow].length; toCol++) {
                if (toRow == fromRow && toCol == fromCol) {
                    continue;
                }
                boolean capture = pieces[toRow][toCol] != null;
                if (piece instanceof Pawn && isPromotionRank(piece.getColor(), toRow)) {
                    addPromotionMoves(moves, fromRow, fromCol, toRow, toCol, capture);
                } else {
                    moves.add(new Move(fromRow, fromCol, toRow, toCol, null, capture, false, false, false, false));
                }
            }
        }
    }

    private void addValidPawnMoves(List<Move> moves, Piece piece, int fromRow, int fromCol) {
        if (piece.getType() != PieceType.PAWN) {
            return;
        }
        Color color = piece.getColor();
        if (color == Color.WHITE) {
            if (fromCol < 0 || fromCol >= 8) {
                return; // Invalid column
            }
            if (fromRow < 1 || fromRow >= 7) {
                return; // Invalid row for a white pawn (can't move from the last rank)
            }
            // Handle double move
            if (!piece.hasMoved() && fromRow == 1) {
                int targetRow = fromRow + 2;
                // TODO: check for obstructions, promotion, captures (incl en pessant), checks, and checkmates
            }
            // Handle single move
            int targetRow = fromRow + 1;
            // TODO: check for obstructions, promotion, captures (incl en pessant), checks, and checkmates
        } else {
            if (fromCol < 0 || fromCol >= 8) {
                return; // Invalid column
            }
            if (fromRow <= 1 || fromRow > 7) {
                return; // Invalid row for a black pawn (can't move from the first rank)
            }
            // Handle double move
            if (!piece.hasMoved() && fromRow == 6) {
                int targetRow = fromRow - 2;
                // TODO: check for obstructions, promotion, captures (incl en pessant), checks, and checkmates
            }
            // Handle single move
            int targetRow = fromRow - 1;
            // TODO: check for obstructions, promotion, captures (incl en pessant), checks, and checkmates
        }
    }

    private boolean isPromotionRank(Color color, int toRow) {
        return (color == Color.WHITE && toRow == 7) || (color == Color.BLACK && toRow == 0);
    }

    private void addCastleMoves(List<Move> moves, int fromRow, int fromCol) {
        moves.add(new Move(fromRow, fromCol, fromRow, 6, null, false, false, false, true, false));
        moves.add(new Move(fromRow, fromCol, fromRow, 2, null, false, false, false, false, true));
    }

    // TODO: add checks and checkmates

    // TODO: filter out moves that would leave the king in check

    // TODO: add en passant

    // TODO: filter out obstructed moves for pieces that can't jump

    // TODO: implement legal move filtering based on piece type and movement rules
}