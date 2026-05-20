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
    public List<Move> generateLegalMoves(GameState state, boolean validateKingSafety) {
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
                    case PAWN -> addValidPawnMoves(moves, piece, fromRow, fromCol, state, validateKingSafety);
                    case KING -> addValidKingMoves(moves, piece, fromRow, fromCol, state, validateKingSafety);
                    case ROOK -> addValidRookMoves(moves, piece, fromRow, fromCol, state, validateKingSafety);
                    case BISHOP -> addValidBishopMoves(moves, piece, fromRow, fromCol, state, validateKingSafety);
                    case KNIGHT -> addValidKnightMoves(moves, piece, fromRow, fromCol, state, validateKingSafety);
                    case QUEEN -> addValidQueenMoves(moves, piece, fromRow, fromCol, state, validateKingSafety);
                    default -> throw new IllegalStateException("Unexpected piece type: " + piece.getType());
                }
            }
        }

        return moves;
    }

    private void addMoveToMoves(List<Move> moves, Move move) {
        // Assumes move is already validated for legality (e.g. does not expose own king to check, is not obstructed, etc.)
        if (move != null) {
            moves.add(move);
        }
    }

    private void addToMoves(List<Move> moves, List<Move> newMoves) {
        for (Move move : newMoves) {
            addMoveToMoves(moves, move);
        }
    }

    private List<Move> validateMoves(List<Move> moves, GameState state, boolean validateKingSafety) {
        List<Move> validMoves = new ArrayList<>(moves);
        validMoves = validateNotExposeKing(validMoves, state, validateKingSafety);
        validMoves = validateNotObstructed(validMoves, state);
        validMoves = validatePromotion(validMoves, state, validateKingSafety);
        // TODO: add rest of validations

        // System.out.println("Valid moves after validation: " + validMoves);

        return validMoves;
    }

    private void addAndValidateMove(List<Move> moves, Move move, GameState state, boolean validateKingSafety) {
        List<Move> movelist = new ArrayList<>();
        movelist.add(move);

        movelist = validateMoves(movelist, state, validateKingSafety);

        if (movelist != null) {
            addToMoves(moves, movelist);
        }
    }

    private void addValidPawnMoves(List<Move> moves, Piece piece, int fromRow, int fromCol, GameState state, boolean validateKingSafety) {
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
                Move newMove = new Move(fromRow, fromCol, targetRow, fromCol, null, false, false, false, false, false);
                addAndValidateMove(moves, newMove, state, validateKingSafety);
                // TODO: check for obstructions, promotion, captures (incl en pessant), checks, and checkmates
            }
            // Handle single move
            int targetRow = fromRow - 1;
            Move newMove = new Move(fromRow, fromCol, targetRow, fromCol, null, false, false, false, false, false);
            addAndValidateMove(moves, newMove, state, validateKingSafety);
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
                Move newMove = new Move(fromRow, fromCol, targetRow, fromCol, null, false, false, false, false, false);
                addAndValidateMove(moves, newMove, state, validateKingSafety);
                // TODO: check for obstructions, promotion, captures (incl en pessant), checks, and checkmates
            }
            // Handle single move
            int targetRow = fromRow + 1;
            Move newMove = new Move(fromRow, fromCol, targetRow, fromCol, null, false, false, false, false, false);
            addAndValidateMove(moves, newMove, state, validateKingSafety);
            // TODO: check for obstructions, promotion, captures (incl en pessant), checks, and checkmates
        }
    }

    private void addValidKingMoves(List<Move> moves, Piece piece, int fromRow, int fromCol, GameState state, boolean validateKingSafety) {
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
                Move newMove = new Move(fromRow, fromCol, toRow, toCol, null, false, false, false, false, false);
                addAndValidateMove(moves, newMove, state, validateKingSafety);
                // TODO: check for obstructions, captures, checks, and checkmates
            }
        }
        
        // queenside castling
        if (canCastleQueenside(state, piece.getColor())) {
            System.out.println("Can castle queenside");
            Move newMove = new Move(fromRow, fromCol, fromRow, fromCol - 2, null, false, false, false, false, false);
            addAndValidateMove(moves, newMove, state, validateKingSafety);
        }
        // kingside castling
        if (canCastleKingside(state, piece.getColor())) {
            System.out.println("Can castle kingside");
            Move newMove = new Move(fromRow, fromCol, fromRow, fromCol + 2, null, false, false, false, false, false);
            addAndValidateMove(moves, newMove, state, validateKingSafety);
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

    private void addValidRookMoves(List<Move> moves, Piece piece, int fromRow, int fromCol, GameState state, boolean validateKingSafety) {
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
            Move newMove = new Move(fromRow, fromCol, toRow, fromCol, null, false, false, false, false, false);
            addAndValidateMove(moves, newMove, null, validateKingSafety);
        }
        for (int toCol = 0; toCol < 8; toCol++) {
            if (toCol == fromCol) {
                continue;
            }
            Move newMove = new Move(fromRow, fromCol, fromRow, toCol, null, false, false, false, false, false);
            addAndValidateMove(moves, newMove, null, validateKingSafety);
        }
        // TODO: check for obstructions, captures, checks, and checkmates
    }
    
    private void addValidBishopMoves(List<Move> moves, Piece piece, int fromRow, int fromCol, GameState state, boolean validateKingSafety) {
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
                        Move newMove = new Move(fromRow, fromCol, toRow, toCol, null, false, false, false, false, false);
                        addAndValidateMove(moves, newMove, null, validateKingSafety);
                    }
                }
            }
        }
        // TODO: check for obstructions, captures, checks, and checkmates
    }

    private void addValidKnightMoves(List<Move> moves, Piece piece, int fromRow, int fromCol, GameState state, boolean validateKingSafety) {
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
                Move newMove = new Move(fromRow, fromCol, toRow, toCol, null, false, false, false, false, false);
                addAndValidateMove(moves, newMove, null, validateKingSafety);
            }
        }
        // TODO: check for obstructions, captures, checks, and checkmates
    }

    private void addValidQueenMoves(List<Move> moves, Piece piece, int fromRow, int fromCol, GameState state, boolean validateKingSafety) {
        addValidRookMoves(moves, piece, fromRow, fromCol, state, validateKingSafety);
        addValidBishopMoves(moves, piece, fromRow, fromCol, state, validateKingSafety);
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

    private List<Move> validateNotExposeKing(List<Move> moves, GameState state, boolean validateKingSafety) {
        /*
            Checks if the move would expose the player's own king to check. If it does, returns null. Otherwise, returns the move.
        */
       if (moves == null || moves.isEmpty()) {
           return null;
       }
       if (!validateKingSafety) {
           return new ArrayList<>(moves);
       }

       List<Move> validMoves = new ArrayList<>();

       for (Move move : moves) {
            if (move == null) {
                continue;
            }
            if (state == null) {
                continue;
            }

            // TODO: fix copy to be a deep copy to avoid mutating the original state when applying the move
            GameState copyState = state.copy();
            copyState.applyMove(move);
            if (!copyState.isInCheck(state.getSideToMove())) {
                validMoves.add(move);
            }
       } 
       return validMoves;
    }

    private List<Move> validateNotObstructed(List<Move> moves, GameState state) {
        /*
            Checks if the move is obstructed by other pieces. If it is, returns null. Otherwise, returns the move.
        */
        if (moves == null || moves.isEmpty()) {
            return null;
        }

        List<Move> validMoves = new ArrayList<>();

        for (Move move : moves) {
            if (move == null) {
                continue;
            }

            if (state == null) {
                continue;
            }

            Piece[][] pieces = state.getBoard().getPieces();
            int fromRow = move.getFromRow();
            int fromCol = move.getFromCol();
            int toRow = move.getToRow();
            int toCol = move.getToCol();

            Piece piece = pieces[fromRow][fromCol];
            if (piece == null) {
                continue; // No piece to move
            }

            // Check for obstructions along the path of the move (for sliding pieces)
            if (piece.getType() == PieceType.BISHOP || piece.getType() == PieceType.ROOK || piece.getType() == PieceType.QUEEN) {
                int rowDirection = Integer.compare(toRow, fromRow);
                int colDirection = Integer.compare(toCol, fromCol);
                int currentRow = fromRow + rowDirection;
                int currentCol = fromCol + colDirection;
                while (currentRow != toRow || currentCol != toCol) {
                    if (pieces[currentRow][currentCol] != null) {
                        break; // Path is obstructed
                    }
                    currentRow += rowDirection;
                    currentCol += colDirection;
                }
            }

            // Check for capture on the target square
            Piece targetPiece = pieces[toRow][toCol];
            if (targetPiece != null && targetPiece.getColor() == piece.getColor()) {
                continue; // Can't capture own piece
            } else if (targetPiece != null && targetPiece.getColor() != piece.getColor()) { // Capture enemy piece
                move = new Move(
                    move.getFromRow(), move.getFromCol(), move.getToRow(), move.getToCol(),
                    move.getPromotion(), true, move.isCheck(), move.isCheckmate(), move.isCastleKingside(), move.isCastleQueenside()
                );
            }

            validMoves.add(move);
        }
        return validMoves;
    }

    private List<Move> validatePromotion(List<Move> moves, GameState state, boolean validateKingSafety) {
        /*
            If the move is a pawn move to the promotion rank, adds valid promotion options. Otherwise, returns the move.
        */
        if (moves == null || moves.isEmpty()) {
            return null;
        }

        List<Move> validMoves = new ArrayList<>();

        for (Move move : moves) {
            if (move == null) {
                continue;
            }

            if (state == null) {
                continue;
            }

            Piece[][] pieces = state.getBoard().getPieces();
            int fromRow = move.getFromRow();
            int fromCol = move.getFromCol();
            int toRow = move.getToRow();
            int toCol = move.getToCol();

            Piece piece = pieces[fromRow][fromCol];
            if (piece == null || piece.getType() != PieceType.PAWN) {
                validMoves.add(move); // Not a pawn move
                continue;
            }

            if (isPromotionRank(piece.getColor(), toRow)) {
                // For simplicity, we'll just default to queen promotion for now. In a full implementation, we would want to allow the player to choose.
                Move promoteToQueenMove = new Move(fromRow, fromCol, toRow, toCol, PieceType.QUEEN, move.isCapture(), move.isCheck(), move.isCheckmate(), move.isCastleKingside(), move.isCastleQueenside());
                Move promoteToRookMove = new Move(fromRow, fromCol, toRow, toCol, PieceType.ROOK, move.isCapture(), move.isCheck(), move.isCheckmate(), move.isCastleKingside(), move.isCastleQueenside());
                Move promoteToBishopMove = new Move(fromRow, fromCol, toRow, toCol, PieceType.BISHOP, move.isCapture(), move.isCheck(), move.isCheckmate(), move.isCastleKingside(), move.isCastleQueenside());
                Move promoteToKnightMove = new Move(fromRow, fromCol, toRow, toCol, PieceType.KNIGHT, move.isCapture(), move.isCheck(), move.isCheckmate(), move.isCastleKingside(), move.isCastleQueenside());

                // Validate each promotion option and add valid ones to the list
                List<Move> promotionMoves = List.of(promoteToQueenMove, promoteToRookMove, promoteToBishopMove, promoteToKnightMove);

                promotionMoves = validateMoves(promotionMoves, state, validateKingSafety);

                validMoves.addAll(promotionMoves.stream().filter(m -> m != null).toList());
                continue;
            }

            validMoves.add(move);
        }
        return validMoves;
    }
}