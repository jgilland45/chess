package com.example.chess.Game;

import java.util.ArrayList;
import java.util.List;

import com.example.chess.Enums.Color;
import com.example.chess.Enums.PieceType;
import com.example.chess.GameObjects.Board;
import com.example.chess.GameObjects.King;
import com.example.chess.GameObjects.Pawn;
import com.example.chess.GameObjects.Piece;

public class PermissiveLegalMoveGenerator implements LegalMoveGenerator {
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
                    moves.add(new Move(fromRow, fromCol, toRow, toCol, null, capture, false, false));
                }
            }
        }
    }

    private void addPromotionMoves(List<Move> moves, int fromRow, int fromCol, int toRow, int toCol, boolean capture) {
        moves.add(new Move(fromRow, fromCol, toRow, toCol, PieceType.QUEEN, capture, false, false));
        moves.add(new Move(fromRow, fromCol, toRow, toCol, PieceType.ROOK, capture, false, false));
        moves.add(new Move(fromRow, fromCol, toRow, toCol, PieceType.BISHOP, capture, false, false));
        moves.add(new Move(fromRow, fromCol, toRow, toCol, PieceType.KNIGHT, capture, false, false));
    }

    private boolean isPromotionRank(Color color, int toRow) {
        return (color == Color.WHITE && toRow == 0) || (color == Color.BLACK && toRow == 7);
    }

    private void addCastleMoves(List<Move> moves, int fromRow, int fromCol) {
        moves.add(new Move(fromRow, fromCol, fromRow, 6, null, false, true, false));
        moves.add(new Move(fromRow, fromCol, fromRow, 2, null, false, false, true));
    }
}
