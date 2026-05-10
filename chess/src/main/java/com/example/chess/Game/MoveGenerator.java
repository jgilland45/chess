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

                switch (piece.getType()) {
                    case PAWN -> addValidPawnMoves(moves, piece, fromRow, fromCol);
                    case KING -> addValidKingMoves(moves, piece, fromRow, fromCol, state);
                    case ROOK -> addValidRookMoves(moves, piece, fromRow, fromCol);
                    case BISHOP -> addValidBishopMoves(moves, piece, fromRow, fromCol);
                    case KNIGHT -> addValidKnightMoves(moves, piece, fromRow, fromCol);
                    case QUEEN -> addValidQueenMoves(moves, piece, fromRow, fromCol);
                    default -> throw new IllegalStateException("Unexpected piece type: " + piece.getType());
                }
            }
        }

        return moves;
    }

    private void addValidPawnMoves(List<Move> moves, Piece piece, int fromRow, int fromCol) {
        Color color = piece.getColor();
        if (color == Color.WHITE) {
            if (fromCol < 0 || fromCol >= 8) {
                return; // Invalid column
            }
            if (fromRow < 1 || fromRow > 6) {
                return; // Invalid row for a white pawn (can't move from the last rank)
            }
            // Handle double move
            if (!piece.hasMoved() && fromRow == 6) {
                int targetRow = fromRow - 2;
                moves.add(new Move(fromRow, fromCol, targetRow, fromCol, null, false, false, false, false, false));
                // TODO: check for obstructions, promotion, captures (incl en pessant), checks, and checkmates
            }
            // Handle single move
            int targetRow = fromRow - 1;
            moves.add(new Move(fromRow, fromCol, targetRow, fromCol, null, false, false, false, false, false));
            // TODO: check for obstructions, promotion, captures (incl en pessant), checks, and checkmates
        } else {
            if (fromCol < 0 || fromCol >= 8) {
                return; // Invalid column
            }
            if (fromRow < 1 || fromRow > 7) {
                return; // Invalid row for a black pawn (can't move from the first rank)
            }
            // Handle double move
            if (!piece.hasMoved() && fromRow == 1) {
                int targetRow = fromRow + 2;
                moves.add(new Move(fromRow, fromCol, targetRow, fromCol, null, false, false, false, false, false));
                // TODO: check for obstructions, promotion, captures (incl en pessant), checks, and checkmates
            }
            // Handle single move
            int targetRow = fromRow + 1;
            moves.add(new Move(fromRow, fromCol, targetRow, fromCol, null, false, false, false, false, false));
            // TODO: check for obstructions, promotion, captures (incl en pessant), checks, and checkmates
        }
    }

    private void addValidKingMoves(List<Move> moves, Piece piece, int fromRow, int fromCol, GameState state) {
        if (fromCol < 0 || fromCol >= 8) {
            return; // Invalid column
        }
        if (fromRow < 0 || fromRow >= 8) {
            return; // Invalid row
        }

        for (int toRow = fromRow - 1; toRow <= fromRow + 1; toRow++) {
            for (int toCol = fromCol - 1; toCol <= fromCol + 1; toCol++) {
                // Skip the square the king is currently on
                if (toRow == fromRow && toCol == fromCol) {
                    continue;
                }
                // Skip squares that are off the board
                if (toRow < 0 || toRow >= 8 || toCol < 0 || toCol >= 8) {
                    continue;
                }
                moves.add(new Move(fromRow, fromCol, toRow, toCol, null, false, false, false, false, false));
                // TODO: check for obstructions, captures, checks, and checkmates
            }
        }
        
        // queenside castling
        if (canCastleQueenside(state, piece.getColor())) {
            System.out.println("Can castle queenside");
            moves.add(new Move(fromRow, fromCol, fromRow, fromCol - 2, null, false, false, false, false, false));
        }
        // kingside castling
        if (canCastleKingside(state, piece.getColor())) {
            System.out.println("Can castle kingside");
            moves.add(new Move(fromRow, fromCol, fromRow, fromCol + 2, null, false, false, false, false, false));
        }
    }

    private boolean canCastleKingside(GameState state, Color color) {
        if (color == Color.WHITE) {
            return state.canWhiteCastleKingside();
        } else {
            return state.canBlackCastleKingside();
        }
    }

    private boolean canCastleQueenside(GameState state, Color color) {
        if (color == Color.WHITE) {
            return state.canWhiteCastleQueenside();
        } else {
            return state.canBlackCastleQueenside();
        }
    }

    private void addValidRookMoves(List<Move> moves, Piece piece, int fromRow, int fromCol) {
        if (fromCol < 0 || fromCol >= 8) {
            return; // Invalid column
        }
        if (fromRow < 0 || fromRow >= 8) {
            return; // Invalid row
        }

        for (int toRow = 0; toRow < 8; toRow++) {
            if (toRow == fromRow) {
                continue;
            }
            moves.add(new Move(fromRow, fromCol, toRow, fromCol, null, false, false, false, false, false));
        }
        for (int toCol = 0; toCol < 8; toCol++) {
            if (toCol == fromCol) {
                continue;
            }
            moves.add(new Move(fromRow, fromCol, fromRow, toCol, null, false, false, false, false, false));
        }
        // TODO: check for obstructions, captures, checks, and checkmates
    }
    
    private void addValidBishopMoves(List<Move> moves, Piece piece, int fromRow, int fromCol) {
        if (fromCol < 0 || fromCol >= 8) {
            return; // Invalid column
        }
        if (fromRow < 0 || fromRow >= 8) {
            return; // Invalid row
        }

        for (int offset = -7; offset <= 7; offset++) {
            if (offset == 0) {
                continue;
            }
            for (int rowSign = -1; rowSign <= 1; rowSign += 2) {
                for (int colSign = -1; colSign <= 1; colSign += 2) {
                    int toRow = fromRow + offset * rowSign;
                    int toCol = fromCol + offset * colSign;
                    if (toRow >= 0 && toRow < 8 && toCol >= 0 && toCol < 8) {
                        moves.add(new Move(fromRow, fromCol, toRow, toCol, null, false, false, false, false, false));
                    }
                }
            }
        }
        // TODO: check for obstructions, captures, checks, and checkmates
    }

    private void addValidKnightMoves(List<Move> moves, Piece piece, int fromRow, int fromCol) {
        if (fromCol < 0 || fromCol >= 8) {
            return; // Invalid column
        }
        if (fromRow < 0 || fromRow >= 8) {
            return; // Invalid row
        }

        int[][] knightOffsets = {
            {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},
            {1, -2}, {1, 2}, {2, -1}, {2, 1}
        };
        for (int[] offset : knightOffsets) {
            int toRow = fromRow + offset[0];
            int toCol = fromCol + offset[1];
            if (toRow >= 0 && toRow < 8 && toCol >= 0 && toCol < 8) {
                moves.add(new Move(fromRow, fromCol, toRow, toCol, null, false, false, false, false, false));
            }
        }
        // TODO: check for obstructions, captures, checks, and checkmates
    }

    private void addValidQueenMoves(List<Move> moves, Piece piece, int fromRow, int fromCol) {
        addValidRookMoves(moves, piece, fromRow, fromCol);
        addValidBishopMoves(moves, piece, fromRow, fromCol);
        // TODO: check for obstructions, captures, checks, and checkmates
    }

    private boolean isPromotionRank(Color color, int toRow) {
        return (color == Color.WHITE && toRow == 0) || (color == Color.BLACK && toRow == 7);
    }

    // Order in which we should check move validity:
    // 1. Ensure move does not put own king in check
    // 2. Ensure move is not obstructed by other pieces
    // 2a. If only obstruction is on the target square, ensure it's an opponent's piece (capture)
    // 2b. If only obstruction is on the target square and it is not an opponent's piece, the move is invalid
    // 3. If piece is pawn:
    // 3a. If promoting, add all valid promotion options
    // 3b. If en passant, ensure the target square is the en passant target
    // 4. If piece is king and move is castling, ensure castling rights and that the squares the king passes through are not under attack
    // 5. If move results in check, ensure the opponent's king is attacked in the resulting position
    // 6. If move results in checkmate, ensure the opponent's king is attacked and has no legal moves in the resulting position

    private Move validateNotExposeKing(Move move, GameState state) {
        /*
            Checks if the move would expose the player's own king to check. If it does, returns null. Otherwise, returns the move.
        */
       if (move == null) {
           return null;
       }

       GameState copyState = state.copy();
       copyState.applyMove(move);
        if (copyState.isInCheck(state.getSideToMove())) {
            return null;
        }
        return move;
    }

    private Move validateNotObstructed(Move move, GameState state) {
        /*
            Checks if the move is obstructed by other pieces. If it is, returns null. Otherwise, returns the move.
        */
        if (move == null) {
            return null;
        }

        Piece[][] pieces = state.getBoard().getPieces();
        int fromRow = move.getFromRow();
        int fromCol = move.getFromCol();
        int toRow = move.getToRow();
        int toCol = move.getToCol();

        Piece piece = pieces[fromRow][fromCol];
        if (piece == null) {
            return null; // No piece to move
        }

        // Check for obstructions along the path of the move (for sliding pieces)
        if (piece.getType() == PieceType.BISHOP || piece.getType() == PieceType.ROOK || piece.getType() == PieceType.QUEEN) {
            int rowDirection = Integer.compare(toRow, fromRow);
            int colDirection = Integer.compare(toCol, fromCol);
            int currentRow = fromRow + rowDirection;
            int currentCol = fromCol + colDirection;
            while (currentRow != toRow || currentCol != toCol) {
                if (pieces[currentRow][currentCol] != null) {
                    return null; // Path is obstructed
                }
                currentRow += rowDirection;
                currentCol += colDirection;
            }
        }

        // Check for capture on the target square
        Piece targetPiece = pieces[toRow][toCol];
        if (targetPiece != null && targetPiece.getColor() == piece.getColor()) {
            return null; // Can't capture own piece
        } else if (targetPiece != null && targetPiece.getColor() != piece.getColor()) { // Capture enemy piece
            move = new Move(move.getFromRow(), move.getFromCol(), move.getToRow(), move.getToCol(),
                move.getPromotion(), true, move.isCheck(), move.isCheckmate(), move.isCastleKingside(), move.isCastleQueenside());
        }

        return move;
    }

    private Move validatePromotion(Move move, GameState state) {
        /*
            If the move is a pawn move to the promotion rank, adds valid promotion options. Otherwise, returns the move.
        */
        if (move == null) {
            return null;
        }

        Piece[][] pieces = state.getBoard().getPieces();
        int fromRow = move.getFromRow();
        int fromCol = move.getFromCol();
        int toRow = move.getToRow();
        int toCol = move.getToCol();

        Piece piece = pieces[fromRow][fromCol];
        if (piece == null || piece.getType() != PieceType.PAWN) {
            return move; // Not a pawn move
        }

        if (isPromotionRank(piece.getColor(), toRow)) {
            // For simplicity, we'll just default to queen promotion for now. In a full implementation, we would want to allow the player to choose.
            return new Move(fromRow, fromCol, toRow, toCol, PieceType.QUEEN, move.isCapture(), move.isCheck(), move.isCheckmate(), move.isCastleKingside(), move.isCastleQueenside());

            // TODO: add options for other promotion types and handle user choice
        }

        return move;
    }
}